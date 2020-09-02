package com.matheuscordeiro.myfinancesapi.services;

import com.matheuscordeiro.myfinancesapi.exceptions.AuthenticationErrorException;
import com.matheuscordeiro.myfinancesapi.exceptions.BusinessException;
import com.matheuscordeiro.myfinancesapi.model.entities.User;
import com.matheuscordeiro.myfinancesapi.model.repositories.UserRepository;
import com.matheuscordeiro.myfinancesapi.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if(!user.isPresent())
            throw new AuthenticationErrorException("Usuário não encontrado para o email informado.");
        if(!user.get().getPassword().equals(password))
            throw new AuthenticationErrorException("Senha inválida.");

        return user.get();
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        validateEmail(user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void validateEmail(String email) {
        boolean exist = userRepository.existsByEmail(email);
        if (exist)
            throw new BusinessException("Já existe um usuário cadastrado com este email");
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }
}
