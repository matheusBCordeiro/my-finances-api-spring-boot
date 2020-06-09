package com.matheuscordeiro.myfinancesapi.model.repository;

import com.matheuscordeiro.myfinancesapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
