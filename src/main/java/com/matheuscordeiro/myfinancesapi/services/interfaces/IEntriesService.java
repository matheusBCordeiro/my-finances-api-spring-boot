package com.matheuscordeiro.myfinancesapi.services.interfaces;

import com.matheuscordeiro.myfinancesapi.model.entities.Entries;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesStatus;

import java.util.List;

public interface IEntriesService {
    Entries saveEntries(Entries entries);

    Entries updateEntries(Entries entries);

    void deleteEntries(Entries entries);

    List<Entries> getEntries(Entries entries);

    void updateStatus(Entries entries, EntriesStatus entriesStatus);

    void validateEntries(Entries entries);
}
