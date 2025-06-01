package com.test.library.main.service;

import static com.test.library.main.common.EncryptionUtils.encrypt;

import com.test.library.main.dto.request.NewUserDto;
import com.test.library.main.dto.response.NewUserEncryptedDto;
import com.test.library.main.exception.BadPasswordException;
import com.test.library.main.exception.BadRequestException;
import com.test.library.main.exception.UserAlreadyExistsException;
import com.test.library.main.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewUserService {
    final UserService userService;

    @Transactional
    public User registerNewUser(NewUserDto newUser) throws BadRequestException {
        verifyUserNotRegistered(newUser);
        return userService.saveNewUser(encryptData(newUser));
    }

    void verifyUserNotRegistered(NewUserDto newUserDto) throws BadRequestException {
        if(userService.findByEmail(newUserDto.email()).isEmpty()) {
            return;
        }
        throw new UserAlreadyExistsException();
    }

    NewUserEncryptedDto encryptData(NewUserDto origin) throws BadRequestException {
        try {
            byte[] password = encrypt(origin.password());
            return new NewUserEncryptedDto(
                    origin.email(),
                    origin.fullName(),
                    password);
        } catch (Exception e) {
            BadPasswordException exception = new BadPasswordException();
            exception.initCause(e);
            throw exception;
        }
    }
}
