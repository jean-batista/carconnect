package dao.interfaces;

import java.util.List;
import java.util.UUID;

import entidades.Cliente;
import entidades.Negociacao;
import entidades.Vendedor;

public interface VendedorDao {
    
    // Cadastra um vendedor no sistema
    void cadastro(Vendedor vendedor);

    // Efetua uma venda
    void efetuarVenda(Cliente cliente);

    // Lista o historico de vendas
    List<Negociacao> historicoDeVendas();
    
    // Busca um cliente pelo id
    Vendedor buscarPorId(UUID id);

    // Lista os clientes cadastrados no sistema
    List<Vendedor> listarVendedoresCadastrados();

    // Atualiza o cadastro de um cliente
    void atualizarCadastro(Vendedor vendedor);

    // Exclui um cliente cadastrado
    void excluirCadastro(Vendedor vendedor);

}
