package com.matheuscordeiro.myfinancesapi.api.controllers;

import com.matheuscordeiro.myfinancesapi.api.dto.UserAuthDTO;
import com.matheuscordeiro.myfinancesapi.api.dto.UserDTO;
import com.matheuscordeiro.myfinancesapi.exceptions.BusinessException;
import com.matheuscordeiro.myfinancesapi.model.entities.User;
import com.matheuscordeiro.myfinancesapi.services.interfaces.IEntriesService;
import com.matheuscordeiro.myfinancesapi.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    public IUserService userService;

    @Autowired
    public IEntriesService entriesService;

    @PostMapping("/auth")
    public ResponseEntity authentication(@RequestBody UserAuthDTO userAuthDto) {
        try {
            User userAuthenticate = userService.authenticate(userAuthDto.getEmail(), userAuthDto.getPassword());
            return ResponseEntity.ok(userAuthenticate);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity saveUser(@RequestBody  UserDTO userDto) {
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword()).build();

        try {
            User userSave = userService.saveUser(user);
            return new ResponseEntity(userSave, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}/balance")
    public ResponseEntity getBalance(@PathVariable Long id) {
        Optional<User> user = userService.getById(id);

        if(!user.isPresent()) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        BigDecimal balance = entriesService.getBalanceByUser(id);
        return ResponseEntity.ok(balance);
    }
}
