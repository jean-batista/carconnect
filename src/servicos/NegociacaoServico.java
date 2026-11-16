package servicos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import config.AppConfig;
import entidades.Negociacao;
import entidades.Veiculo;
import repositorios.NegociacaoRepositorio;
import repositorios.VeiculoRepositorio;

public class NegociacaoServico extends ServicoBase<Negociacao, UUID> {

    private final VeiculoRepositorio veiculoRepositorio;

    public NegociacaoServico() {
        super(AppConfig.configurarNegociacaoRepositorio());
        this.veiculoRepositorio = AppConfig.configurarVeiculoRepositorio();
    }

    public NegociacaoServico(NegociacaoRepositorio repositorio, VeiculoRepositorio veiculoRepositorio) {
        super(repositorio);
        this.veiculoRepositorio = veiculoRepositorio;
    }

    @Override
    public Negociacao cadastrar(Negociacao negociacao) {
        if (negociacao != null && negociacao.getData() == null) {
            negociacao.setData(LocalDateTime.now());
        }
        validar(negociacao);

        Veiculo veiculo = veiculoRepositorio.buscarPorId(negociacao.getVeiculo().getId());
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo selecionado não encontrado.");
        }
        if (veiculo.getQuantidadeEstoque() == null || veiculo.getQuantidadeEstoque() <= 0) {
            throw new IllegalStateException("O veículo selecionado não possui unidades em estoque.");
        }

        Negociacao cadastrada = super.cadastrar(negociacao);
        veiculoRepositorio.atualizarEstoque(veiculo, veiculo.getQuantidadeEstoque() - 1);
        return cadastrada;
    }

    public List<Negociacao> buscarPorClienteId(UUID clienteId) {
        if (clienteId == null) throw new IllegalArgumentException("O ID do cliente não pode ser nulo.");
        return ((NegociacaoRepositorio) repositorio).buscarPorClienteId(clienteId);
    }

    public List<Negociacao> buscarPorVendedorId(UUID vendedorId) {
        if (vendedorId == null) throw new IllegalArgumentException("O ID do vendedor não pode ser nulo.");
        return ((NegociacaoRepositorio) repositorio).buscarPorVendedorId(vendedorId);
    }

    @Override
    protected void validar(Negociacao n) {
        super.validar(n);
        if (n.getCliente() == null || n.getCliente().getId() == null) {
            throw new IllegalArgumentException("O cliente associado à negociação é obrigatório.");
        }
        if (n.getVendedor() == null || n.getVendedor().getId() == null) {
            throw new IllegalArgumentException("O vendedor associado à negociação é obrigatório.");
        }
        if (n.getVeiculo() == null || n.getVeiculo().getId() == null) {
            throw new IllegalArgumentException("O veículo associado à negociação é obrigatório.");
        }
    }
}