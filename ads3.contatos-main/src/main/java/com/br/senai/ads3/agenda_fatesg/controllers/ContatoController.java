package com.br.senai.ads3.agenda_fatesg.controllers;

import com.br.senai.ads3.agenda_fatesg.exceptions.BusinessException;
import com.br.senai.ads3.agenda_fatesg.exceptions.ValidationException;
import com.br.senai.ads3.agenda_fatesg.model.Contato;
import com.br.senai.ads3.agenda_fatesg.repository.ContatoRepository;
import com.br.senai.ads3.agenda_fatesg.service.ContatoService;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Camada Controller — orquestra chamadas entre a View e o Service.
 * Não contém regras de negócio nem acesso direto ao arquivo.
 * Implementa FormController e ListController para desacoplar as Views.
 */
public class ContatoController implements FormController, ListController {

    private final ContatoService service;

    public ContatoController(ContatoService service) {
        this.service = service;
    }

    public ContatoController(Path storagePath) {
        this(new ContatoService(new ContatoRepository(storagePath)));
    }

    public ContatoController(String storageFilePath) {
        this(Paths.get(storageFilePath));
    }

    @Override
    public void validate(Contato dto) throws ValidationException {
        service.validate(dto);
    }

    @Override
    public Contato create(Contato dto) throws ValidationException, BusinessException {
        return service.create(dto);
    }

    @Override
    public Contato update(String originalName, Contato dto) throws ValidationException, BusinessException {
        return service.update(originalName, dto);
    }

    @Override
    public List<Contato> listAll() {
        return service.listAll();
    }

    @Override
    public boolean markInactiveByName(String name) throws BusinessException {
        return service.markInactiveByName(name);
    }

    @Override
    public List<Contato> findByName(String name) {
        return service.findByName(name);
    }
}
