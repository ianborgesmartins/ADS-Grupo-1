package com.br.senai.ads3.agenda_fatesg;

import com.br.senai.ads3.agenda_fatesg.view.Form_Listagem;

/**
 * Ponto de entrada da aplicação Agenda Swing.
 * Inicia a interface gráfica abrindo a tela de listagem de contatos.
 */
public class Agenda_Fatesg {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new Form_Listagem().setVisible(true);
        });
    }
}
