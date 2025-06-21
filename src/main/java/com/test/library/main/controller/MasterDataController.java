package com.test.library.main.controller;

import com.test.library.main.dto.response.ReturnStatusDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.model.UserRole;
import com.test.library.main.service.MasterDataControllerService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/master")
@RequiredArgsConstructor
@Validated
public class MasterDataController {
    final MasterDataControllerService masterDataControllerService;

    @GetMapping("/returnStatus")
    @Transactional
    public List<ReturnStatusDto> listReturnStatuses() throws BaseApplicationException {
        return masterDataControllerService.listReturnStatuses();
    }

    @GetMapping("/userRole")
    public List<UserRole> listUserRole() throws BaseApplicationException {
        return masterDataControllerService.listUserRole();
    }

    @GetMapping("/titles")
    public Page<String> findUniqueTitles(@RequestParam @NotBlank String title,
                                         @RequestParam(required = false) @PositiveOrZero Integer page,
                                         @RequestParam(required = false) @Positive Integer pagesize)
            throws BaseApplicationException {
        return masterDataControllerService.findUniqueTitles(title, page, pagesize);
    }

    @GetMapping("/author")
    public Page<String> findUniqueAuthors(@RequestParam @NotBlank String author,
                                          @RequestParam(required = false) @PositiveOrZero Integer page,
                                          @RequestParam(required = false) @Positive Integer pagesize)
            throws BaseApplicationException {
        return masterDataControllerService.findUniqueAuthors(author, page, pagesize);
    }
}
