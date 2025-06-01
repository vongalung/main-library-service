package com.test.library.main.service;

import static com.test.library.main.common.DtoRemapper.remapUser;
import static com.test.library.main.common.EncryptionUtils.verifyEncryptedMatches;
import static com.test.library.main.common.VerifyingUtils.verifyAsAdmin;

import com.test.library.main.dto.request.LoginRequestDto;
import com.test.library.main.dto.request.NewUserDto;
import com.test.library.main.dto.response.LoginResponseDto;
import com.test.library.main.dto.response.UserDto;
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

@Service
@RequiredArgsConstructor
@Log4j2
public class UserManagementControllerService {
    final NewUserService newUserService;
    final UserService userService;
    final UserSessionService userSessionService;

    public UserDto findUserSelf() throws BaseApplicationException {
        User user = userSessionService.findUserFromSession();
        return getUserDetails(user.getId());
    }

    public UserDto findUser(UUID userId) throws BaseApplicationException {
        verifyAsAdmin(userSessionService.findUserFromSession());
        return getUserDetails(userId);
    }

    UserDto getUserDetails(UUID userId) throws BaseApplicationException {
        User user = userService.findByIdWithCheckOutsFilteredByUnreturnedStatus(userId)
                .orElseThrow(UserNotFoundException::new);
        return remapUser(user);
    }

    @Transactional
    public User registerNewUser(NewUserDto newUser) throws BaseApplicationException  {
        return newUserService.registerNewUser(newUser);
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
