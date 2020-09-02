package com.matheuscordeiro.myfinancesapi.services.interfaces;

import com.matheuscordeiro.myfinancesapi.model.entities.User;
import com.matheuscordeiro.myfinancesapi.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface IUserService {

    User authenticate(String email, String password);

    User saveUser(User user);

    void validateEmail(String email);

    Optional<User> getById(Long id);
}
