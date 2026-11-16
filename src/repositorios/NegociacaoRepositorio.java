package repositorios;

import java.util.List;
import java.util.UUID;

import dao.implementacao.ClienteDao;
import dao.implementacao.NegociacaoDao;
import dao.implementacao.VeiculoDao;
import dao.implementacao.VendedorDao;
import entidades.Negociacao;
import repositorios.interfaces.Repositorio;

public class NegociacaoRepositorio implements Repositorio<Negociacao, UUID> {

    private final NegociacaoDao dao;
    private final ClienteDao clienteDao;
    private final VendedorDao vendedorDao;
    private final VeiculoDao veiculoDao;

    public NegociacaoRepositorio(NegociacaoDao dao, ClienteDao clienteDao, VendedorDao vendedorDao, VeiculoDao veiculoDao) {
        this.dao = dao;
        this.clienteDao = clienteDao;
        this.vendedorDao = vendedorDao;
        this.veiculoDao = veiculoDao;
    }

    @Override
    public Negociacao cadastrar(Negociacao negociacao) {
        return dao.inserir(negociacao);
    }

    @Override
    public Negociacao buscarPorId(UUID id) {
        Negociacao negociacao = dao.buscarPorId(id);
        if (negociacao != null) carregarRelacionamentos(negociacao);
        return negociacao;
    }

    @Override
    public List<Negociacao> buscarTodos() {
        return preencherLista(dao.buscarTodos());
    }

    public List<Negociacao> buscarPorClienteId(UUID clienteId) {
        return preencherLista(dao.buscarPorClienteId(clienteId));
    }

    public List<Negociacao> buscarPorVendedorId(UUID vendedorId) {
        return preencherLista(dao.buscarPorVendedorId(vendedorId));
    }

    @Override
    public void atualizar(Negociacao negociacao) {
        dao.atualizar(negociacao);
    }

    @Override
    public void excluir(Negociacao negociacao) {
        dao.excluir(negociacao);
    }

    private List<Negociacao> preencherLista(List<Negociacao> lista) {
        for (Negociacao n : lista) carregarRelacionamentos(n);
        return lista;
    }

    private void carregarRelacionamentos(Negociacao n) {
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
}