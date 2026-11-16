package repositorios;

import java.util.List;
import java.util.UUID;

import dao.implementacao.AgendamentoDao;
import dao.implementacao.ClienteDao;
import dao.implementacao.VendedorDao;
import entidades.Agendamento;
import repositorios.interfaces.Repositorio;

public class AgendamentoRepositorio implements Repositorio<Agendamento, UUID> {

    private final AgendamentoDao dao;
    private final ClienteDao clienteDao;
    private final VendedorDao vendedorDao;

    public AgendamentoRepositorio(AgendamentoDao dao, ClienteDao clienteDao, VendedorDao vendedorDao) {
        this.dao = dao;
        this.clienteDao = clienteDao;
        this.vendedorDao = vendedorDao;
    }

    @Override
    public Agendamento cadastrar(Agendamento agendamento) {
        return dao.inserir(agendamento);
    }

    @Override
    public Agendamento buscarPorId(UUID id) {
        Agendamento agendamento = dao.buscarPorId(id);
        if (agendamento != null) carregarRelacionamentos(agendamento);
        return agendamento;
    }

    @Override
    public List<Agendamento> buscarTodos() {
        return preencherLista(dao.buscarTodos());
    }

    public List<Agendamento> buscarPorClienteId(UUID clienteId) {
        return preencherLista(dao.buscarPorClienteId(clienteId));
    }

    public List<Agendamento> buscarPorVendedorId(UUID vendedorId) {
        return preencherLista(dao.buscarPorVendedorId(vendedorId));
    }

    @Override
    public void atualizar(Agendamento agendamento) {
        Agendamento cadastrado = dao.buscarPorId(agendamento.getId());
        if (cadastrado != null) {
            if (agendamento.getAgendadoPara() != null) cadastrado.setAgendadoPara(agendamento.getAgendadoPara());
            if (agendamento.getCliente() != null) cadastrado.setCliente(agendamento.getCliente());
            if (agendamento.getVendedor() != null) cadastrado.setVendedor(agendamento.getVendedor());
            dao.atualizar(cadastrado);
        }
    }

    @Override
    public void excluir(Agendamento agendamento) {
        dao.excluir(agendamento);
    }

    private List<Agendamento> preencherLista(List<Agendamento> lista) {
        for (Agendamento a : lista) carregarRelacionamentos(a);
        return lista;
    }

    private void carregarRelacionamentos(Agendamento a) {
        if (a.getCliente() != null && a.getCliente().getId() != null) {
            a.setCliente(clienteDao.buscarPorId(a.getCliente().getId()));
        }
        if (a.getVendedor() != null && a.getVendedor().getId() != null) {
            a.setVendedor(vendedorDao.buscarPorId(a.getVendedor().getId()));
        }
    }
}