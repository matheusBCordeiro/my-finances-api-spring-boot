package com.matheuscordeiro.myfinancesapi.api.controllers;

import com.matheuscordeiro.myfinancesapi.api.dto.EntriesDTO;
import com.matheuscordeiro.myfinancesapi.api.dto.UpdateStatusDTO;
import com.matheuscordeiro.myfinancesapi.exceptions.BusinessException;
import com.matheuscordeiro.myfinancesapi.model.entities.Entries;
import com.matheuscordeiro.myfinancesapi.model.entities.User;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesStatus;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesType;
import com.matheuscordeiro.myfinancesapi.services.interfaces.IEntriesService;
import com.matheuscordeiro.myfinancesapi.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/entries")
@RequiredArgsConstructor
public class EntriesController {
    @Autowired
    private IEntriesService entriesService;

    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity getAll(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam("user") Long userId
    ) {
        Entries entriesFilter = new Entries();
        entriesFilter.setDescription(description);
        entriesFilter.setMonth(month);
        entriesFilter.setYear(year);
        Optional<User> user = userService.getById(userId);

        if(!user.isPresent()) {
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado.");
        }else {
            entriesFilter.setUser(user.get());
        }

        List<Entries> entries = entriesService.getEntries(entriesFilter);
        return ResponseEntity.ok(entries);
    }

    @PostMapping
    public ResponseEntity saveEntries(@RequestBody EntriesDTO entriesDTO) {
        try {
            Entries entries = convertToEntity(entriesDTO);
            entries = entriesService.saveEntries(entries);
            return new ResponseEntity(entries, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity updateEntries(@PathVariable Long id, @RequestBody EntriesDTO entriesDto) {
        return entriesService.getById(id).map( entity -> {
            try {
                Entries entries = convertToEntity(entriesDto);
                entries.setId(entity.getId());
                entriesService.updateEntries(entries);
                return ResponseEntity.ok(entries);
            } catch (BusinessException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } ).orElseGet( () -> new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

    @PutMapping("{id}/update-status")
    public ResponseEntity updateStatus(@PathVariable("id") Long id ,@RequestBody UpdateStatusDTO updateStatusDto) {
        return entriesService.getById(id).map( entity -> {
            EntriesStatus status = EntriesStatus.valueOf(updateStatusDto.getStatus());

            if(status == null) {
                return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido.");
            }

            try {
                entity.setStatus(status);
                entriesService.updateEntries(entity);
                return ResponseEntity.ok(entity);
            }catch (BusinessException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet( () ->
                new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteEntries(@PathVariable Long id) {
        return entriesService.getById(id).map( entidade -> {
            entriesService.deleteEntries(entidade);
            return new ResponseEntity( HttpStatus.NO_CONTENT );
        }).orElseGet( () ->
                new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

    public Entries convertToEntity(EntriesDTO entriesDto) {
        Entries entries = new Entries();
        entries.setId(entriesDto.getId());
        entries.setDescription(entriesDto.getDescription());
        entries.setMonth(entriesDto.getMonth());
        entries.setYear(entriesDto.getYear());
        entries.setValue(entriesDto.getValue());
        User user = userService.getById(entriesDto.getUser())
                .orElseThrow( () -> new BusinessException("Usuário não encontrado para o Id informado.") );

        entries.setUser(user);

        if(entriesDto.getType() != null) {
            entries.setType(EntriesType.valueOf(entriesDto.getType()));
        }

        if(entriesDto.getStatus() != null) {
            entries.setStatus(EntriesStatus.valueOf(entriesDto.getStatus()));
        }
        return entries;
    }
}
