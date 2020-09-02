package com.matheuscordeiro.myfinancesapi.services;

import com.matheuscordeiro.myfinancesapi.model.entities.Entries;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesStatus;
import com.matheuscordeiro.myfinancesapi.model.repositories.EntriesRepository;
import com.matheuscordeiro.myfinancesapi.services.interfaces.IEntriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntriesService implements IEntriesService {
    @Autowired
    private EntriesRepository entriesRepository;

    @Override
    public Entries saveEntries(Entries entries) {
        return null;
    }

    @Override
    public Entries updateEntries(Entries entries) {
        return null;
    }

    @Override
    public void deleteEntries(Entries entries) {

    }

    @Override
    public List<Entries> getEntries(Entries entries) {
        return null;
    }

    @Override
    public void updateStatus(Entries entries, EntriesStatus entriesStatus) {

    }
}
