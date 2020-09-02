package com.matheuscordeiro.myfinancesapi.services.interfaces;

import com.matheuscordeiro.myfinancesapi.model.entities.Entries;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IEntriesService {
    Entries saveEntries(Entries entries);

    Entries updateEntries(Entries entries);

    void deleteEntries(Entries entries);

    List<Entries> getEntries(Entries entries);

    void updateStatus(Entries entries, EntriesStatus entriesStatus);

    void validateEntries(Entries entries);

    Optional<Entries> getById(Long id);

    BigDecimal getBalanceByUser(Long userId);
}
