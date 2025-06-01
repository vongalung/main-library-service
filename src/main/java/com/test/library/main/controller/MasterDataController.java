package com.test.library.main.controller;

import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.model.ReturnStatus;
import com.test.library.main.model.UserRole;
import com.test.library.main.service.MasterDataControllerService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/master")
@RequiredArgsConstructor
@Validated
public class MasterDataController {
    final MasterDataControllerService masterDataControllerService;

    @GetMapping("/returnStatus")
    public List<ReturnStatus> listReturnStatuses() throws BaseApplicationException {
        return masterDataControllerService.listReturnStatuses();
    }

    @GetMapping("/userRole")
    public List<UserRole> listUserRole() throws BaseApplicationException {
        return masterDataControllerService.listUserRole();
    }
}
