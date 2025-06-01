package com.test.library.main.service;

import com.test.library.main.dto.response.NewUserEncryptedDto;
import com.test.library.main.model.MasterPerson;
import com.test.library.main.model.User;
import com.test.library.main.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    final UserRepo userRepo;

    public Optional<User> findByIdWithCheckOutsFilteredByUnreturnedStatus(UUID userId) {
        return userRepo.findByIdWithCheckOutsFilteredByUnreturnedStatus(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Transactional
    public User saveNewUser(NewUserEncryptedDto newUser) {
        User matchingUser = findByEmail(newUser.email()).orElse(null);
        if (matchingUser != null) {
            log.info("User with same \"email\" was found. Not registering current user: {}",
                    newUser);
            return matchingUser;
        }

        MasterPerson masterPerson = new MasterPerson();
        masterPerson.setFullName(newUser.fullName());
        masterPerson.setEmail(newUser.email());

        User user = new User();
        user.setEncryptedPassword(newUser.encryptedPassword());
        user.setMasterPerson(masterPerson);
        masterPerson.setUser(user);
        return userRepo.save(user);
    }
}
