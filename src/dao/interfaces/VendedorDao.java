package dao.interfaces;

import java.util.List;
import java.util.UUID;

import entidades.Vendedor;

public interface VendedorDao {
    
    // Cadastra um vendedor no sistema
    Vendedor cadastro(Vendedor vendedor);
    
    // Busca um cliente pelo id
    Vendedor buscarVendedorPorId(UUID id);

    // Lista os clientes cadastrados no sistema
    List<Vendedor> buscarVendedoresCadastrados();

    // Exclui um cliente cadastrado
    void excluirCadastro(Vendedor vendedor);

}
