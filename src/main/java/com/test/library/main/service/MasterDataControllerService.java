package com.test.library.main.service;

import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.model.ReturnStatus;
import com.test.library.main.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterDataControllerService {
    public List<ReturnStatus> listReturnStatuses() throws BaseApplicationException {
        return Arrays.asList(ReturnStatus.values());
    }

    public List<UserRole> listUserRole() throws BaseApplicationException {
        return Arrays.asList(UserRole.values());
    }
}
