package com.matheuscordeiro.myfinancesapi.model.repositories;

import com.matheuscordeiro.myfinancesapi.model.entities.Entries;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesStatus;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface EntriesRepository extends JpaRepository<Entries, Long> {
    @Query(value = " select sum(l.value) from Entries e join e.user u "
            + " where u.id = :userId and e.type =:type and e.status = :status group by u ")
    BigDecimal getBalanceByEntriesTypeAndUserAndStatus(
            @Param("userId")Long userId,
            @Param("type") EntriesType entriesType,
            @Param("status")EntriesStatus entriesStatus
    );
}
