package com.br.senai.ads3.agenda_fatesg.controllers;

import com.br.senai.ads3.agenda_fatesg.exceptions.BusinessException;
import com.br.senai.ads3.agenda_fatesg.exceptions.ValidationException;
import com.br.senai.ads3.agenda_fatesg.model.Contato;

/**
 * Contrato da camada Controller para telas de cadastro e edição.
 */
public interface FormController {
    Contato create(Contato dto) throws ValidationException, BusinessException;
    Contato update(String originalName, Contato dto) throws ValidationException, BusinessException;
    void validate(Contato dto) throws ValidationException;
}
