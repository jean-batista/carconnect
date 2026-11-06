package dao.interfaces;

import java.util.List;
import java.util.UUID;

import entidades.Cliente;

public interface ClienteDao {

    // Cadastra um cliente no sistema
    void cadastro(Cliente cliente);
    
    // Busca um cliente pelo id
    Cliente buscarClientePorId(UUID id);

    // Lista os clientes cadastrados no sistema
    List<Cliente> buscarClientesCadastrados();

    // Atualiza o cadastro de um cliente
    void atualizarCadastro(Cliente cliente);

    // Exclui um cliente cadastrado
    void excluirCadastro(Cliente cliente);

}
