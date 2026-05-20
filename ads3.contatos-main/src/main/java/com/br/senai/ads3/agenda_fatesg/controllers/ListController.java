package com.br.senai.ads3.agenda_fatesg.controllers;

import com.br.senai.ads3.agenda_fatesg.exceptions.BusinessException;
import com.br.senai.ads3.agenda_fatesg.model.Contato;
import java.util.List;

/**
 * Contrato da camada Controller para tela de listagem.
 */
public interface ListController {
    List<Contato> listAll();
    boolean markInactiveByName(String name) throws BusinessException;
    List<Contato> findByName(String name);
}
