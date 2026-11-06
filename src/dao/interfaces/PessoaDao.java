package dao.interfaces;

import java.util.List;
import java.util.UUID;

import entidades.Pessoa;

public interface PessoaDao {
    
    // Insere uma Pessoa
    void inserir(Pessoa pessoa);

    // Busca uma Pessoa por id
    Pessoa buscarPorId(UUID id);

    // Busca uma lista de pessoas
    List<Pessoa> buscarPessoas();

    // Atualiza uma Pessoa
    void atualizar(Pessoa pessoa);

    // Exclui uma Pessoa
    void excluir(Pessoa pessoa);

}
