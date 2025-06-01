package com.test.library.main.service;

import static com.test.library.main.common.EncryptionUtils.encrypt;

import com.test.library.main.dto.request.NewUserDto;
import com.test.library.main.dto.response.NewUserTempDto;
import com.test.library.main.exception.BadPasswordException;
import com.test.library.main.exception.BadRequestException;
import com.test.library.main.exception.UserAlreadyExistsException;
import com.test.library.main.model.User;
import com.test.library.main.repository.redis.NewUserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewUserService {
    final NewUserRepo newUserRepo;
    final UserService userService;

    public UUID registerTempNewUser(NewUserDto newUser) throws BadRequestException {
        verifyUserNotRegistered(newUser);
        return newUserRepo.saveTempNewUser(convertToTemp(newUser));
    }

    void verifyUserNotRegistered(NewUserDto newUserDto) throws BadRequestException {
        if(userService.findByEmail(newUserDto.email()).isEmpty()) {
            return;
        }
        throw new UserAlreadyExistsException();
    }

    @Transactional
    public User proceedRegisterNewUser(UUID key) {
        NewUserTempDto newUser = newUserRepo.findTempNewUser(key);
        return userService.saveNewUser(newUser);
    }

    NewUserTempDto convertToTemp(NewUserDto origin) throws BadRequestException {
        try {
            byte[] password = encrypt(origin.password());
            return new NewUserTempDto(
                    origin.email(),
                    origin.fullName(),
                    origin.userRole(),
                    password);
        } catch (Exception e) {
            BadPasswordException exception = new BadPasswordException();
            exception.initCause(e);
            throw exception;
        }
    }
}
