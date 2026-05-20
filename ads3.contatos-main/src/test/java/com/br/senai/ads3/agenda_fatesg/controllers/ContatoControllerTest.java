package com.br.senai.ads3.agenda_fatesg.controllers;

import com.br.senai.ads3.agenda_fatesg.exceptions.BusinessException;
import com.br.senai.ads3.agenda_fatesg.exceptions.ValidationException;
import com.br.senai.ads3.agenda_fatesg.model.Contato;
import com.br.senai.ads3.agenda_fatesg.repository.ContatoRepository;
import com.br.senai.ads3.agenda_fatesg.service.ContatoService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.gen5.api.Assertions.assertThrows;
import static org.junit.gen5.api.Assertions.assertTrue;
import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Testes do ContatoController com arquitetura em 5 camadas.
 * Estratégia: isolar I/O usando arquivo temporário injetado via Path.
 * O Controller delega ao Service que delega ao Repository.
 */
public class ContatoControllerTest {

    ContatoController controller;
    Path storage;

    /**
     * Setup antes de cada teste.
     * Cria instâncias reais de Repository, Service e Controller com arquivo temporário.
     */
    @BeforeEach
    void setup(@TempDir Path temp) {
        storage = temp.resolve("agenda.txt");
        ContatoRepository repository = new ContatoRepository(storage);
        ContatoService service = new ContatoService(repository);
        controller = new ContatoController(service);
    }

    /**
     * Teste: nome em branco deve lançar ValidationException (regra do Service).
     */
    @Test
    void create_whenNameMissing_thenValidationException() {
        Contato dto = new Contato("", "a@b.com", "123");
        assertThrows(ValidationException.class, () -> controller.create(dto));
    }

    /**
     * Teste: nome duplicado ativo deve lançar BusinessException (regra do Service).
     */
    @Test
    void create_whenDuplicate_thenBusinessException() throws Exception {
        Files.writeString(storage, "Joao;j@x.com;111;ativo\n");
        Contato dto = new Contato("Joao", "j@x.com", "111");
        assertThrows(BusinessException.class, () -> controller.create(dto));
    }

    /**
     * Teste: contato válido deve ser persistido pelo Repository.
     */
    @Test
    void create_whenValid_thenSaved() throws Exception {
        Contato dto = new Contato("Maria", "m@x.com", "222");
        controller.create(dto);
        List<String> lines = Files.readAllLines(storage);
        assertTrue(lines.stream().anyMatch(l -> l.startsWith("Maria;")));
    }

    /**
     * Teste: listAll deve retornar apenas contatos com status 'ativo'.
     */
    @Test
    void listAll_returnsOnlyActive() throws Exception {
        Files.writeString(storage, "A;a@a.com;111;inativo\nB;b@b.com;222;ativo\n");
        List<Contato> list = controller.listAll();
        assertEquals(1, list.size());
        assertEquals("B", list.get(0).getNome());
    }
}
