package com.test.library.main.service;

import static com.test.library.main.common.EncryptionUtils.verifyEncryptedMatches;

import com.test.library.main.dto.request.LoginRequestDto;
import com.test.library.main.dto.request.NewUserDto;
import com.test.library.main.dto.response.LoginResponseDto;
import com.test.library.main.exception.BadRequestException;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.exception.UserNotFoundException;
import com.test.library.main.exception.WrongPasswordException;
import com.test.library.main.model.User;
import com.test.library.main.model.UserSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserManagementControllerService {
    final NewUserService newUserService;
    final EmailService emailService;
    final UserService userService;
    final UserSessionService userSessionService;
    final EmailTemplatingService emailTemplatingService;

    @Transactional
    public void verifyUserRegistration(UUID verificationKey) {
        try {
            User user = newUserService.proceedRegisterNewUser(verificationKey);
            log.debug("Registration complete for user: {}", user.getId());
        } catch (Exception e) {
            log.error("Error during user registration verification for verificationKey={}: {}",
                    verificationKey, e.getMessage(), e);
            throw e;
        }
    }

    public CompletableFuture<Void> registerNewUser(NewUserDto newUser) throws BaseApplicationException  {
        try {
            UUID key = newUserService.registerTempNewUser(newUser);
            return sendVerificationEmail(newUser.email(), key);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    CompletableFuture<Void> sendVerificationEmail(String email, UUID key) {
        return emailService.sendEmail(email, "User registration verification",
                emailTemplatingService.createRegistrationVerificationEmail(key));
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto request) throws BaseApplicationException {
        User user = userService.findByEmail(request.email()).orElseThrow(UserNotFoundException::new);
        verifyLoginCredentials(user, request);
        UserSession session = userSessionService.createUserSession(user);
        return new LoginResponseDto(session.getId(), session.getExpiresAt());
    }

    @Transactional
    public void logout() throws BaseApplicationException {
        userSessionService.removeUserSession();
    }

    void verifyLoginCredentials(User user, LoginRequestDto request) throws BaseApplicationException {
        if(verifyEncryptedMatches(user.getEncryptedPassword(), request.password())) {
            return;
        }
        throw new WrongPasswordException();
    }
}
