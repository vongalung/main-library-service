package com.test.library.main.controller;

import com.test.library.main.dto.request.LoginRequestDto;
import com.test.library.main.dto.request.NewUserDto;
import com.test.library.main.dto.response.CommonResponseDto;
import com.test.library.main.dto.response.LoginResponseDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.service.UserManagementControllerService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user/management")
@RequiredArgsConstructor
@Validated
@Log4j2
public class UserManagementController {
    final UserManagementControllerService userManagementControllerService;

    @PostMapping
    public CompletableFuture<Void> registerNewUser(@RequestBody @NotNull @Valid NewUserDto request)
            throws BaseApplicationException {
        return userManagementControllerService.registerNewUser(request)
                .thenAccept(v -> log.info(
                        "User={} is temporarily added, waiting for verification.", request))
                .exceptionally(e -> {
                    log.error("Failed user registration for request={}: {}",
                            request, e.getMessage(), e);
                    return null;
                });
    }

    @GetMapping("/verify")
    @Transactional
    public CommonResponseDto verifyRegistration(@RequestParam @NotNull UUID verificationKey) {
        log.debug("INCOMING REQUEST to UsrMgmt.verifyRegistration : {}", verificationKey);
        userManagementControllerService.verifyUserRegistration(verificationKey);
        return CommonResponseDto.generateWithMessage("User registration is successfully verified.");
    }

    @PostMapping("/login")
    @Transactional
    public LoginResponseDto login(@RequestBody @NotNull @Valid LoginRequestDto request)
            throws BaseApplicationException {
        return userManagementControllerService.login(request);
    }

    @PostMapping("/logout")
    @Transactional
    public CommonResponseDto logout(@RequestBody @NotNull @Valid LoginRequestDto request)
            throws BaseApplicationException{
        userManagementControllerService.logout();
        return CommonResponseDto.generateWithMessage("User logged out.");
    }
}
