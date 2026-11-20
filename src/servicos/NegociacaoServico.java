package servicos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import config.AppConfig;
import dao.implementacao.ClienteDao;
import dao.implementacao.NegociacaoDao;
import dao.implementacao.VeiculoDao;
import dao.implementacao.VendedorDao;
import entidades.Negociacao;
import entidades.Veiculo;

public class NegociacaoServico extends ServicoBase<Negociacao, UUID> {

    private final ClienteDao clienteDao;
    private final VendedorDao vendedorDao;
    private final VeiculoDao veiculoDao;

    public NegociacaoServico() {
        this(
            AppConfig.criarNegociacaoDao(),
            AppConfig.criarClienteDao(),
            AppConfig.criarVendedorDao(),
            AppConfig.criarVeiculoDao()
        );
    }

    public NegociacaoServico(NegociacaoDao dao, ClienteDao clienteDao, VendedorDao vendedorDao, VeiculoDao veiculoDao) {
        super(dao);
        this.clienteDao = clienteDao;
        this.vendedorDao = vendedorDao;
        this.veiculoDao = veiculoDao;
    }

    @Override
    public Negociacao cadastrar(Negociacao negociacao) {
        if (negociacao != null && negociacao.getData() == null) {
            negociacao.setData(LocalDateTime.now());
        }
        validar(negociacao);

        Veiculo veiculo = veiculoDao.buscarPorId(negociacao.getVeiculo().getId());
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo selecionado não encontrado.");
        }
        if (veiculo.getQuantidadeEstoque() == null || veiculo.getQuantidadeEstoque() <= 0) {
            throw new IllegalStateException("O veículo selecionado não possui unidades em estoque.");
        }

        Negociacao cadastrada = super.cadastrar(negociacao);
        veiculoDao.atualizarEstoque(veiculo, veiculo.getQuantidadeEstoque() - 1);
        return cadastrada;
    }

    public List<Negociacao> buscarPorClienteId(UUID clienteId) {
        if (clienteId == null) throw new IllegalArgumentException("O ID do cliente não pode ser nulo.");
        return preencherLista(((NegociacaoDao) dao).buscarPorClienteId(clienteId));
    }

    public List<Negociacao> buscarPorVendedorId(UUID vendedorId) {
        if (vendedorId == null) throw new IllegalArgumentException("O ID do vendedor não pode ser nulo.");
        return preencherLista(((NegociacaoDao) dao).buscarPorVendedorId(vendedorId));
    }

    @Override
    protected void carregarRelacionamentos(Negociacao n) {
        if (n.getCliente() != null && n.getCliente().getId() != null) {
            n.setCliente(clienteDao.buscarPorId(n.getCliente().getId()));
        }
        if (n.getVendedor() != null && n.getVendedor().getId() != null) {
            n.setVendedor(vendedorDao.buscarPorId(n.getVendedor().getId()));
        }
        if (n.getVeiculo() != null && n.getVeiculo().getId() != null) {
            n.setVeiculo(veiculoDao.buscarPorId(n.getVeiculo().getId()));
        }
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