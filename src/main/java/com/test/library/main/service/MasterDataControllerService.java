package com.test.library.main.service;

import com.test.library.main.common.DtoRemapper;
import com.test.library.main.dto.response.ReturnStatusDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.model.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterDataControllerService {
    final MasterBookService masterBookService;
    final ReturnStatusService returnStatusService;

    @Transactional
    public List<ReturnStatusDto> listReturnStatuses() throws BaseApplicationException {
        return returnStatusService.findAll().map(DtoRemapper::remapReturnStatus).toList();
    }

    public List<UserRole> listUserRole() throws BaseApplicationException {
        return Arrays.asList(UserRole.values());
    }

    public Page<String> findUniqueTitles(String title, Integer page, Integer pagesize) {
        return masterBookService.findUniqueTitles(title, page, pagesize);
    }

    public Page<String> findUniqueAuthors(String author, Integer page, Integer pagesize) {
        return masterBookService.findUniqueAuthors(author, page, pagesize);
    }
}
