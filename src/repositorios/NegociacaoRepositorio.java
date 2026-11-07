package repositorios;

import java.util.List;
import java.util.UUID;

import dao.interfaces.ClienteDao;
import dao.interfaces.NegociacaoDao;
import dao.interfaces.VeiculoDao;
import dao.interfaces.VendedorDao;
import entidades.Cliente;
import entidades.Negociacao;
import entidades.Veiculo;
import entidades.Vendedor;
import repositorios.interfaces.Repositorio;

public class NegociacaoRepositorio implements Repositorio<Negociacao, UUID> {

    private final NegociacaoDao dao;
    private final ClienteDao clienteDao;
    private final VendedorDao vendedorDao;
    private final VeiculoDao veiculoDao;

    // Construtor desacoplado com injeção de dependências
    public NegociacaoRepositorio(NegociacaoDao dao, ClienteDao clienteDao, VendedorDao vendedorDao, VeiculoDao veiculoDao) {
        this.dao = dao;
        this.clienteDao = clienteDao;
        this.vendedorDao = vendedorDao;
        this.veiculoDao = veiculoDao;
    }

    @Override
    public Negociacao cadastrar(Negociacao negociacao) {
        dao.cadastrar(negociacao);
        return negociacao;
    }

    @Override
    public Negociacao buscarPorId(UUID id) {
        Negociacao negociacao = dao.buscarNegociacaoPorId(id);
        if (negociacao != null) {
            carregarRelacionamentos(negociacao);
        }
        return negociacao;
    }

    @Override
    public List<Negociacao> buscarTodos() {
        List<Negociacao> negociacoes = dao.buscarNegociacoes();
        for (Negociacao negociacao : negociacoes) {
            carregarRelacionamentos(negociacao);
        }
        return negociacoes;
    }

    public List<Negociacao> buscarPorClienteId(UUID clienteId) {
        List<Negociacao> negociacoes = dao.buscarNegociacoesPorCliente(clienteId);
        for (Negociacao negociacao : negociacoes) {
            carregarRelacionamentos(negociacao);
        }
        return negociacoes;
    }

    public List<Negociacao> buscarPorVendedorId(UUID vendedorId) {
        List<Negociacao> negociacoes = dao.buscarNegociacoesPorVendedor(vendedorId);
        for (Negociacao negociacao : negociacoes) {
            carregarRelacionamentos(negociacao);
        }
        return negociacoes;
    }

    @Override
    public void atualizar(Negociacao negociacao) {
        throw new UnsupportedOperationException("Negociações não podem ser alteradas após concluídas.");
    }

    @Override
    public void excluir(Negociacao negociacao) {
        throw new UnsupportedOperationException("Negociações não podem ser excluídas para manter o histórico financeiro.");
    }

    private void carregarRelacionamentos(Negociacao negociacao) {
        if (negociacao.getCliente() != null && negociacao.getCliente().getId() != null) {
            Cliente clienteCompleto = clienteDao.buscarClientePorId(negociacao.getCliente().getId());
            if (clienteCompleto != null) {
                negociacao.setCliente(clienteCompleto);
            }
        }
        if (negociacao.getVendedor() != null && negociacao.getVendedor().getId() != null) {
            Vendedor vendedorCompleto = vendedorDao.buscarVendedorPorId(negociacao.getVendedor().getId());
            if (vendedorCompleto != null) {
                negociacao.setVendedor(vendedorCompleto);
            }
        }
        if (negociacao.getVeiculo() != null && negociacao.getVeiculo().getId() != null) {
            Veiculo veiculoCompleto = veiculoDao.buscarVeiculoPeloId(negociacao.getVeiculo().getId());
            if (veiculoCompleto != null) {
                negociacao.setVeiculo(veiculoCompleto);
            }
        }
    }
}