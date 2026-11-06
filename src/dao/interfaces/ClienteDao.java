package dao.interfaces;

import java.util.List;
import java.util.UUID;

import entidades.Cliente;

public interface ClienteDao {

    // Cadastra um cliente no sistema
    Cliente cadastro(Cliente cliente);
    
    // Busca um cliente pelo id
    Cliente buscarClientePorId(UUID id);

    // Lista os clientes cadastrados no sistema
    List<Cliente> buscarClientesCadastrados();

    // Exclui um cliente cadastrado
    void excluirCadastro(Cliente cliente);

}
