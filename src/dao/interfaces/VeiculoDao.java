package dao.interfaces;

import java.util.List;
import java.util.UUID;

import entidades.Veiculo;

public interface VeiculoDao {
    
    // Cadastra um Veiculo
    void cadastro(Veiculo veiculo);

    // Busca um Veiculo pelo id
    Veiculo buscarVeiculoPeloId(UUID id);

    // Busca um Veiculo pelo id do Gestor
    List<Veiculo> buscarVeiculosPorGestorId(UUID gestorId);

    // Busca todos os veiculos cadastrados
    List<Veiculo> buscarVeiculos();

    // Atualiza um Veiculo
    void atualizar(Veiculo veiculo);

    // Apaga um Veiculo do sistema
    void excluir(Veiculo veiculo);

    // Atualiza a quantidade em estoque
    void atualizarEstoque(Veiculo veiculo, Integer unidades);

}
