package com.br.senai.ads3.agenda_fatesg.service;

import com.br.senai.ads3.agenda_fatesg.exceptions.BusinessException;
import com.br.senai.ads3.agenda_fatesg.exceptions.ValidationException;
import com.br.senai.ads3.agenda_fatesg.model.Contato;
import com.br.senai.ads3.agenda_fatesg.repository.ContatoRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada Service — responsável por todas as regras de negócio da agenda.
 * Não acessa o arquivo diretamente; delega I/O ao ContatoRepository.
 */
public class ContatoService {

    private final ContatoRepository repository;

    public ContatoService(ContatoRepository repository) {
        this.repository = repository;
    }

    /**
     * Valida os dados de entrada do contato.
     * Regra: nome é obrigatório e não pode estar em branco.
     */
    public void validate(Contato dto) throws ValidationException {
        if (dto == null) {
            throw new ValidationException("Contato inválido");
        }
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new ValidationException("Nome é obrigatório");
        }
    }

    /**
     * Cria um novo contato.
     * Regras: validação de entrada + unicidade de nome entre contatos ativos.
     */
    public Contato create(Contato dto) throws ValidationException, BusinessException {
        validate(dto);
        try {
            List<String> lines = repository.readAllLines();
            for (String linha : lines) {
                String nomeSalvo = repository.getNome(linha);
                String status    = repository.getStatus(linha);
                if (nomeSalvo.equalsIgnoreCase(dto.getNome()) && "ativo".equalsIgnoreCase(status)) {
                    throw new BusinessException("Erro: O nome '" + dto.getNome() + "' já está cadastrado!");
                }
            }
            String line = repository.contatoToLine(dto, "ativo");
            repository.appendLine(line);
            return dto;
        } catch (IOException e) {
            throw new RuntimeException("Erro de I/O: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza um contato existente pelo nome original.
     * Regras: validação de entrada + contato deve existir.
     */
    public Contato update(String originalName, Contato dto) throws ValidationException, BusinessException {
        validate(dto);
        try {
            List<String> lines = repository.readAllLines();
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                if (repository.getNome(lines.get(i)).equalsIgnoreCase(originalName)) {
                    lines.set(i, repository.contatoToLine(dto, "ativo"));
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new BusinessException("Registro original não encontrado: " + originalName);
            }
            repository.writeAllLines(lines);
            return dto;
        } catch (IOException e) {
            throw new RuntimeException("Erro de I/O: " + e.getMessage(), e);
        }
    }

    /**
     * Retorna todos os contatos com status 'ativo'.
     */
    public List<Contato> listAll() {
        try {
            List<String> lines = repository.readAllLines();
            List<Contato> result = new ArrayList<>();
            for (String line : lines) {
                if ("ativo".equalsIgnoreCase(repository.getStatus(line))) {
                    result.add(repository.lineToContato(line));
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inativa um contato pelo nome (exclusão lógica — Soft Delete).
     * O registro permanece no arquivo com status 'inativo'.
     */
    public boolean markInactiveByName(String name) throws BusinessException {
        try {
            List<String> lines = repository.readAllLines();
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                if (repository.getNome(lines.get(i)).equalsIgnoreCase(name)) {
                    String[] r = lines.get(i).split(";");
                    String newLine = (r.length > 0 ? r[0] : "") + ";"
                                  + (r.length > 1 ? r[1] : "") + ";"
                                  + (r.length > 2 ? r[2] : "") + ";inativo";
                    lines.set(i, newLine);
                    found = true;
                    break;
                }
            }
            if (found) {
                repository.writeAllLines(lines);
            }
            return found;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Busca contatos ativos cujo nome contenha a string informada (case-insensitive).
     */
    public List<Contato> findByName(String name) {
        List<Contato> all = listAll();
        List<Contato> filtered = new ArrayList<>();
        for (Contato c : all) {
            if (c.getNome() != null && c.getNome().toLowerCase().contains(name.toLowerCase())) {
                filtered.add(c);
            }
        }
        return filtered;
    }
}
