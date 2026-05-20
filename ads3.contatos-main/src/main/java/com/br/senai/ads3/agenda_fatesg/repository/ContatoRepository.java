package com.br.senai.ads3.agenda_fatesg.repository;

import com.br.senai.ads3.agenda_fatesg.model.Contato;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Camada Repository — única responsável pelo acesso ao arquivo agenda.txt.
 * Nenhuma regra de negócio deve existir aqui; apenas operações de leitura e escrita.
 */
public class ContatoRepository {

    private final Path storagePath;

    public ContatoRepository(Path storagePath) {
        this.storagePath = storagePath;
    }

    public ContatoRepository(String storageFilePath) {
        this(Paths.get(storageFilePath));
    }

    /**
     * Retorna todas as linhas do arquivo como lista de Contato (ativos e inativos).
     */
    public List<String> readAllLines() throws IOException {
        ensureStorage();
        if (!Files.exists(storagePath)) {
            return Collections.emptyList();
        }
        return Files.readAllLines(storagePath, StandardCharsets.UTF_8);
    }

    /**
     * Adiciona uma nova linha ao final do arquivo.
     */
    public void appendLine(String line) throws IOException {
        ensureStorage();
        Files.write(storagePath, Collections.singleton(line),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    /**
     * Substitui todas as linhas do arquivo pela lista fornecida (usado em update e inativação).
     */
    public void writeAllLines(List<String> lines) throws IOException {
        ensureStorage();
        Files.write(storagePath, lines,
                StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE);
    }

    /**
     * Converte uma linha CSV para um objeto Contato.
     * Formato: nome;email;telefone;status
     */
    public Contato lineToContato(String line) {
        String[] d = line.split(";");
        String nome     = d.length > 0 ? d[0] : "";
        String email    = d.length > 1 ? d[1] : "";
        String telefone = d.length > 2 ? d[2] : "";
        return new Contato(nome, email, telefone);
    }

    /**
     * Converte um Contato para linha CSV com o status informado.
     * Formato: nome;email;telefone;status
     */
    public String contatoToLine(Contato c, String status) {
        String nome     = c.getNome()     == null ? "" : c.getNome();
        String email    = c.getEmail()    == null ? "" : c.getEmail();
        String telefone = c.getTelefone() == null ? "" : c.getTelefone();
        return nome + ";" + email + ";" + telefone + ";" + status + System.lineSeparator();
    }

    /**
     * Extrai o status (4ª coluna) de uma linha CSV.
     */
    public String getStatus(String line) {
        String[] r = line.split(";");
        return r.length > 3 ? r[3].trim() : "ativo";
    }

    /**
     * Extrai o nome (1ª coluna) de uma linha CSV.
     */
    public String getNome(String line) {
        String[] r = line.split(";");
        return r.length > 0 ? r[0] : "";
    }

    private void ensureStorage() throws IOException {
        if (!Files.exists(storagePath)) {
            Files.createFile(storagePath);
        }
    }

    public Path getStoragePath() {
        return storagePath;
    }
}
