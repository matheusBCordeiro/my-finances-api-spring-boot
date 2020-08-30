package com.matheuscordeiro.myfinancesapi.model.repositories;

import com.matheuscordeiro.myfinancesapi.model.entities.Entries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntriesRepository extends JpaRepository<Entries, Long> {
}
