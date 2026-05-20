package com.br.senai.ads3.agenda_fatesg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Camada Model — representa a entidade de domínio Contato.
 * Não contém lógica de negócio nem acesso a dados.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contato {
    private String nome;
    private String email;
    private String telefone;
}
