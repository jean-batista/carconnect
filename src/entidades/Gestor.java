package entidades;

import java.util.UUID;

public class Gestor extends Pessoa {

    public Gestor() {
    }

    public Gestor(UUID id, String nome, String sobrenome, String email, String telefone, String cpf, String senha) {
        super(id, nome, sobrenome, email, telefone, cpf, senha);
    }
}