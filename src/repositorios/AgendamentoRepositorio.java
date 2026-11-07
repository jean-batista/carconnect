package repositorios;

import java.util.List;
import java.util.UUID;

import dao.interfaces.AgendamentoDao;
import dao.interfaces.ClienteDao;
import dao.interfaces.VendedorDao;
import entidades.Agendamento;
import entidades.Cliente;
import entidades.Vendedor;

public class AgendamentoRepositorio implements Repositorio<Agendamento, UUID> {

    private final AgendamentoDao dao;
    private final ClienteDao clienteDao;
    private final VendedorDao vendedorDao;

    // Construtor desacoplado com injeção de dependências
    public AgendamentoRepositorio(AgendamentoDao dao, ClienteDao clienteDao, VendedorDao vendedorDao) {
        this.dao = dao;
        this.clienteDao = clienteDao;
        this.vendedorDao = vendedorDao;
    }

    @Override
    public Agendamento cadastrar(Agendamento agendamento) {
        dao.cadastro(agendamento);
        return agendamento;
    }

    @Override
    public Agendamento buscarPorId(UUID id) {
        Agendamento agendamento = dao.buscarAgendamentoPorId(id);
        if (agendamento != null) {
            carregarRelacionamentos(agendamento);
        }
        return agendamento;
    }

    @Override
    public List<Agendamento> buscarTodos() {
        List<Agendamento> agendamentos = dao.buscarAgendamentos();
        for (Agendamento agendamento : agendamentos) {
            carregarRelacionamentos(agendamento);
        }
        return agendamentos;
    }

    public List<Agendamento> buscarPorClienteId(UUID clienteId) {
        List<Agendamento> agendamentos = dao.buscarAgendamentosPorCliente(clienteId);
        for (Agendamento agendamento : agendamentos) {
            carregarRelacionamentos(agendamento);
        }
        return agendamentos;
    }

    public List<Agendamento> buscarPorVendedorId(UUID vendedorId) {
        List<Agendamento> agendamentos = dao.buscarAgendamentosPorVendedor(vendedorId);
        for (Agendamento agendamento : agendamentos) {
            carregarRelacionamentos(agendamento);
        }
        return agendamentos;
    }

    @Override
    public void atualizar(Agendamento agendamento) {
        Agendamento cadastrado = dao.buscarAgendamentoPorId(agendamento.getId());
        if (cadastrado != null) {
            atualizarCampos(cadastrado, agendamento);
            dao.atualizar(cadastrado);
        }
    }

    @Override
    public void excluir(Agendamento agendamento) {
        dao.excluir(agendamento);
    }

    private void carregarRelacionamentos(Agendamento agendamento) {
        if (agendamento.getCliente() != null && agendamento.getCliente().getId() != null) {
            Cliente clienteCompleto = clienteDao.buscarClientePorId(agendamento.getCliente().getId());
            if (clienteCompleto != null) {
                agendamento.setCliente(clienteCompleto);
            }
        }
        if (agendamento.getVendedor() != null && agendamento.getVendedor().getId() != null) {
            Vendedor vendedorCompleto = vendedorDao.buscarVendedorPorId(agendamento.getVendedor().getId());
            if (vendedorCompleto != null) {
                agendamento.setVendedor(vendedorCompleto);
            }
        }
    }

    private void atualizarCampos(Agendamento cadastrado, Agendamento dados) {
        if (dados.getAgendadoPara() != null) cadastrado.setAgendadoPara(dados.getAgendadoPara());
        if (dados.getCliente() != null) cadastrado.setCliente(dados.getCliente());
        if (dados.getVendedor() != null) cadastrado.setVendedor(dados.getVendedor());
    }
}