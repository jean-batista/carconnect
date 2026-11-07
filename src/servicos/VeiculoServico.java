package servicos;

import java.time.Year;
import java.util.List;
import java.util.UUID;

import config.AppConfig;
import entidades.Veiculo;
import repositorios.VeiculoRepositorio;

/**
 * Camada de serviço para gerenciamento de Veículos.
 * Centraliza as regras de negócio e validações (estoque, valores, ano) 
 * antes de acionar a persistência via VeiculoRepositorio.
 */
public class VeiculoServico {

    private final VeiculoRepositorio veiculoRepositorio;

    /**
     * Construtor padrão que utiliza o AppConfig para resolver
     * e injetar automaticamente a instância configurada do VeiculoRepositorio.
     */
    public VeiculoServico() {
        this.veiculoRepositorio = AppConfig.configurarVeiculoRepositorio();
    }

    /**
     * Construtor sobrecarregado para permitir injeção manual (útil para testes unitários).
     * @param veiculoRepositorio Instância do repositório de veículos.
     */
    public VeiculoServico(VeiculoRepositorio veiculoRepositorio) {
        this.veiculoRepositorio = veiculoRepositorio;
    }

    /**
     * Cadastra um novo veículo aplicando validações de preço, ano e marca/modelo.
     * @param veiculo Objeto do veículo a ser cadastrado.
     * @return Veículo cadastrado com ID populado.
     */
    public Veiculo cadastrar(Veiculo veiculo) {
        validarVeiculo(veiculo);
        return veiculoRepositorio.cadastrar(veiculo);
    }

    /**
     * Busca um veículo pelo seu ID único.
     * @param id UUID do veículo.
     * @return Objeto Veiculo encotrado ou null.
     */
    public Veiculo buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para busca não pode ser nulo.");
        }
        return veiculoRepositorio.buscarPorId(id);
    }

    /**
     * Retorna a lista completa de veículos do estoque.
     * @return Lista de veículos.
     */
    public List<Veiculo> buscarTodos() {
        return veiculoRepositorio.buscarTodos();
    }

    /**
     * Atualiza os dados de um veículo cadastrado.
     * @param veiculo Objeto contendo o ID e os novos dados.
     */
    public void atualizar(Veiculo veiculo) {
        if (veiculo == null || veiculo.getId() == null) {
            throw new IllegalArgumentException("O veículo e seu ID são obrigatórios para atualização.");
        }
        validarVeiculo(veiculo);
        veiculoRepositorio.atualizar(veiculo);
    }

    /**
     * Atualiza diretamente a quantidade de unidades em estoque do veículo.
     * @param veiculo Veículo a ter o estoque alterado.
     * @param novasUnidades Quantidade atualizada de estoque.
     */
    public void atualizarEstoque(Veiculo veiculo, Integer novasUnidades) {
        if (veiculo == null || veiculo.getId() == null) {
            throw new IllegalArgumentException("O veículo e seu ID são obrigatórios para atualizar estoque.");
        }
        if (novasUnidades == null || novasUnidades < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
        veiculoRepositorio.atualizarEstoque(veiculo, novasUnidades);
    }

    /**
     * Exclui um veículo do cadastro.
     * @param veiculo Objeto do veículo a ser removido.
     */
    public void excluir(Veiculo veiculo) {
        if (veiculo == null || veiculo.getId() == null) {
            throw new IllegalArgumentException("O veículo e seu ID são obrigatórios para exclusão.");
        }
        veiculoRepositorio.excluir(veiculo);
    }

    /**
     * Regras de validação de negócio para Veículo.
     */
    private void validarVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new IllegalArgumentException("O veículo não pode ser nulo.");
        }
        if (veiculo.getMarca() == null || veiculo.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("A marca do veículo é obrigatória.");
        }
        if (veiculo.getModelo() == null || veiculo.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("O modelo do veículo é obrigatório.");
        }
        if (veiculo.getPreco() == null || veiculo.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço do veículo deve ser maior que zero.");
        }
        if (veiculo.getQuantidadeEstoque() != null && veiculo.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("A quantidade inicial em estoque não pode ser negativa.");
        }
        int anoAtual = Year.now().getValue();
        if (veiculo.getAno() == null || veiculo.getAno() < 1900 || veiculo.getAno() > anoAtual + 1) {
            throw new IllegalArgumentException("Ano do veículo inválido.");
        }
    }
}