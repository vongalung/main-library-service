package com.test.library.main.controller;

import com.test.library.main.dto.request.LoginRequestDto;
import com.test.library.main.dto.request.NewUserDto;
import com.test.library.main.dto.response.CommonResponseDto;
import com.test.library.main.dto.response.LoginResponseDto;
import com.test.library.main.dto.response.UserDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.model.User;
import com.test.library.main.service.UserManagementControllerService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Log4j2
public class UserManagementController {
    final UserManagementControllerService userManagementControllerService;

    @GetMapping({"/self"})
    public UserDto findUserSelf() {
        return userManagementControllerService.findUserSelf();
    }

    @GetMapping({"/{userId}"})
    public UserDto findUser(@PathVariable @NotNull UUID userId) {
        return userManagementControllerService.findUser(userId);
    }

    @PostMapping
    @Transactional
    public CommonResponseDto registerNewUser(@RequestBody @NotNull @Valid NewUserDto request)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to UsrMgmt.registerNewUser : {}", request);
        User user = userManagementControllerService.registerNewUser(request);
        return CommonResponseDto.generateWithMessage(
                "User registration is successfully verified for user=" + user.getMasterPerson().getEmail());
    }

    @PostMapping("/login")
    @Transactional
    public LoginResponseDto login(@RequestBody @NotNull @Valid LoginRequestDto request)
            throws BaseApplicationException {
        return userManagementControllerService.login(request);
    }

    @PostMapping("/logout")
    @Transactional
    public CommonResponseDto logout()
            throws BaseApplicationException{
        userManagementControllerService.logout();
        return CommonResponseDto.generateWithMessage("User logged out.");
    }
}
