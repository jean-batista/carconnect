package servicos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import config.AppConfig;
import entidades.Negociacao;
import entidades.Veiculo;
import repositorios.NegociacaoRepositorio;
import repositorios.VeiculoRepositorio;

/**
 * Camada de serviço para gerenciamento de Negociações (vendas de veículos).
 * Centraliza as regras de negócio, validações de estoque e interações entre
 * NegociacaoRepositorio e VeiculoRepositorio.
 */
public class NegociacaoServico {

    private final NegociacaoRepositorio negociacaoRepositorio;
    private final VeiculoRepositorio veiculoRepositorio;

    /**
     * Construtor padrão que utiliza o AppConfig para resolver
     * e injetar automaticamente as instâncias configuradas dos repositórios.
     */
    public NegociacaoServico() {
        this.negociacaoRepositorio = AppConfig.configurarNegociacaoRepositorio();
        this.veiculoRepositorio = AppConfig.configurarVeiculoRepositorio();
    }

    /**
     * Construtor sobrecarregado para permitir injeção manual (útil para testes unitários).
     * @param negociacaoRepositorio Instância do repositório de negociações.
     * @param veiculoRepositorio Instância do repositório de veículos.
     */
    public NegociacaoServico(NegociacaoRepositorio negociacaoRepositorio, VeiculoRepositorio veiculoRepositorio) {
        this.negociacaoRepositorio = negociacaoRepositorio;
        this.veiculoRepositorio = veiculoRepositorio;
    }

    /**
     * Cadastra e conclui uma nova negociação de veículo.
     * Garante a validação dos dados, a verificação de disponibilidade no estoque
     * e a baixa automática de 1 unidade do veículo negociado.
     * 
     * @param negociacao Objeto da negociação a ser cadastrada.
     * @return Negociação cadastrada.
     */
    public Negociacao cadastrar(Negociacao negociacao) {
        if (negociacao != null && negociacao.getData() == null) {
            negociacao.setData(LocalDateTime.now());
        }

        validarNegociacao(negociacao);

        // Busca o veículo atualizado do banco para verificar estoque
        Veiculo veiculo = veiculoRepositorio.buscarPorId(negociacao.getVeiculo().getId());
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo selecionado para a negociação não foi encontrado.");
        }
        if (veiculo.getQuantidadeEstoque() == null || veiculo.getQuantidadeEstoque() <= 0) {
            throw new IllegalStateException("O veículo selecionado não possui unidades disponíveis em estoque.");
        }

        // Realiza o cadastro da negociação
        Negociacao cadastrada = negociacaoRepositorio.cadastrar(negociacao);

        // Atualiza a quantidade em estoque subtraindo 1 unidade
        veiculoRepositorio.atualizarEstoque(veiculo, veiculo.getQuantidadeEstoque() - 1);

        return cadastrada;
    }

    /**
     * Busca uma negociação pelo seu ID único.
     * @param id UUID da negociação.
     * @return Objeto Negociacao com dados completos ou null.
     */
    public Negociacao buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para busca não pode ser nulo.");
        }
        return negociacaoRepositorio.buscarPorId(id);
    }

    /**
     * Retorna a lista completa de negociações cadastradas.
     * @return Lista de negociações.
     */
    public List<Negociacao> buscarTodos() {
        return negociacaoRepositorio.buscarTodos();
    }

    /**
     * Retorna a lista de negociações associadas a um cliente.
     * @param clienteId UUID do cliente.
     * @return Lista de negociações do cliente.
     */
    public List<Negociacao> buscarPorClienteId(UUID clienteId) {
        if (clienteId == null) {
            throw new IllegalArgumentException("O ID do cliente não pode ser nulo.");
        }
        return negociacaoRepositorio.buscarPorClienteId(clienteId);
    }

    /**
     * Retorna a lista de negociações realizadas por um determinado vendedor.
     * @param vendedorId UUID do vendedor.
     * @return Lista de negociações do vendedor.
     */
    public List<Negociacao> buscarPorVendedorId(UUID vendedorId) {
        if (vendedorId == null) {
            throw new IllegalArgumentException("O ID do vendedor não pode ser nulo.");
        }
        return negociacaoRepositorio.buscarPorVendedorId(vendedorId);
    }

    /**
     * Regras de validação de negócio para a Negociação.
     */
    private void validarNegociacao(Negociacao negociacao) {
        if (negociacao == null) {
            throw new IllegalArgumentException("A negociação não pode ser nula.");
        }
        if (negociacao.getCliente() == null || negociacao.getCliente().getId() == null) {
            throw new IllegalArgumentException("O cliente associado à negociação é obrigatório.");
        }
        if (negociacao.getVendedor() == null || negociacao.getVendedor().getId() == null) {
            throw new IllegalArgumentException("O vendedor associado à negociação é obrigatório.");
        }
        if (negociacao.getVeiculo() == null || negociacao.getVeiculo().getId() == null) {
            throw new IllegalArgumentException("O veículo associado à negociação é obrigatório.");
        }
    }
}