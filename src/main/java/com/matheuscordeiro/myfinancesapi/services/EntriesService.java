package com.matheuscordeiro.myfinancesapi.services;

import com.matheuscordeiro.myfinancesapi.exceptions.BusinessException;
import com.matheuscordeiro.myfinancesapi.model.entities.Entries;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesStatus;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesType;
import com.matheuscordeiro.myfinancesapi.model.repositories.EntriesRepository;
import com.matheuscordeiro.myfinancesapi.services.interfaces.IEntriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EntriesService implements IEntriesService {
    @Autowired
    private EntriesRepository entriesRepository;

    @Override
    @Transactional
    public Entries saveEntries(Entries entries) {
        validateEntries(entries);
        entries.setStatus(EntriesStatus.PENDING);
        return entriesRepository.save(entries);
    }

    @Override
    @Transactional
    public Entries updateEntries(Entries entries) {
        Objects.requireNonNull(entries.getId());
        validateEntries(entries);
        return entriesRepository.save(entries);
    }

    @Override
    @Transactional
    public void deleteEntries(Entries entries) {
        Objects.requireNonNull(entries.getId());
        entriesRepository.delete(entries);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entries> getEntries(Entries entries) {
        Example example = Example.of( entries,
            ExampleMatcher.matching()
                    .withIgnoreCase()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) );
        return entriesRepository.findAll(example);
    }

    @Override
    public void updateStatus(Entries entries, EntriesStatus entriesStatus) {
        entries.setStatus(entriesStatus);
        updateEntries(entries);
    }

    @Override
    public void validateEntries(Entries entries) {
        if(entries.getDescription() == null || entries.getDescription().trim().equals("")) {
            throw new BusinessException("Informe uma Descrição válida.");
        }

        if(entries.getMonth() == null || entries.getMonth() < 1 || entries.getMonth() > 12) {
            throw new BusinessException("Informe um Mês válido.");
        }

        if(entries.getYear() == null || entries.getYear().toString().length() != 4 ) {
            throw new BusinessException("Informe um Ano válido.");
        }

        if(entries.getUser() == null || entries.getUser().getId() == null) {
            throw new BusinessException("Informe um Usuário.");
        }

        if(entries.getValue() == null || entries.getValue().compareTo(BigDecimal.ZERO) < 1 ) {
            throw new BusinessException("Informe um Valor válido.");
        }

        if(entries.getType() == null) {
            throw new BusinessException("Informe um tipo de Lançamento.");
        }
    }

    @Override
    public Optional<Entries> getById(Long id) {
        return entriesRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalanceByUser(Long userId) {
        BigDecimal revenues = entriesRepository.getBalanceByEntriesTypeAndUserAndStatus(userId, EntriesType.REVENUE , EntriesStatus.PAID);
        BigDecimal expenditures = entriesRepository.getBalanceByEntriesTypeAndUserAndStatus(userId, EntriesType.EXPENDITURE, EntriesStatus.PAID);

        if(revenues == null) {
            revenues = BigDecimal.ZERO;
        }

        if(expenditures == null) {
            expenditures = BigDecimal.ZERO;
        }

        return revenues.subtract(expenditures);
    }
}