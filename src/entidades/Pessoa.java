package entidades;

import java.util.UUID;

public class Pessoa {
    
    // Identificador unico da Pessoa
    private UUID id;

    // Nome da Pessoa
    private String nome;

    // Sobrenome da Pessoa
    private String sobrenome;

    // Email da Pessoa
    private String email;

    // Telefone da Pessoa
    private String telefone;

    // Cpf da Pessoa
    private String cpf;

    // Senha de acesso da Pessoa
    private String senha;
    
    // Construtor sem argumentos
    public Pessoa() {
    }

    // Construtor com argumentos
    public Pessoa(UUID id, String nome, String sobrenome, String email, String telefone, String cpf, String senha) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.senha = senha;
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    // Setters

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // Hashcode e equals

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pessoa other = (Pessoa) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Pessoa [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", email=" + email + ", telefone="
                + telefone + ", cpf=" + cpf + ", senha=" + senha + "]";
    }

    

}
