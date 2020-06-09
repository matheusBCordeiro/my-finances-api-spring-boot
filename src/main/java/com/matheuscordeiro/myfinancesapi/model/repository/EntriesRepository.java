package com.matheuscordeiro.myfinancesapi.model.repository;

import com.matheuscordeiro.myfinancesapi.model.entity.Entries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntriesRepository extends JpaRepository<Entries, Long> {
}
