package servicos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import config.AppConfig;
import dao.implementacao.AgendamentoDao;
import dao.implementacao.ClienteDao;
import dao.implementacao.VendedorDao;
import entidades.Agendamento;

public class AgendamentoServico extends ServicoBase<Agendamento, UUID> {

    private final ClienteDao clienteDao;
    private final VendedorDao vendedorDao;

    public AgendamentoServico() {
        this(
            AppConfig.criarAgendamentoDao(),
            AppConfig.criarClienteDao(),
            AppConfig.criarVendedorDao()
        );
    }

    public AgendamentoServico(AgendamentoDao dao, ClienteDao clienteDao, VendedorDao vendedorDao) {
        super(dao);
        this.clienteDao = clienteDao;
        this.vendedorDao = vendedorDao;
    }

    @Override
    public Agendamento cadastrar(Agendamento agendamento) {
        if (agendamento != null && agendamento.getAgendadoEm() == null) {
            agendamento.setAgendadoEm(LocalDateTime.now());
        }
        return super.cadastrar(agendamento);
    }

    @Override
    public void atualizar(Agendamento agendamento) {
        validar(agendamento);
        Agendamento cadastrado = dao.buscarPorId(agendamento.getId());
        if (cadastrado != null) {
            if (agendamento.getAgendadoPara() != null) cadastrado.setAgendadoPara(agendamento.getAgendadoPara());
            if (agendamento.getCliente() != null) cadastrado.setCliente(agendamento.getCliente());
            if (agendamento.getVendedor() != null) cadastrado.setVendedor(agendamento.getVendedor());
            dao.atualizar(cadastrado);
        }
    }

    public List<Agendamento> buscarPorClienteId(UUID clienteId) {
        if (clienteId == null) throw new IllegalArgumentException("O ID do cliente não pode ser nulo.");
        return preencherLista(((AgendamentoDao) dao).buscarPorClienteId(clienteId));
    }

    public List<Agendamento> buscarPorVendedorId(UUID vendedorId) {
        if (vendedorId == null) throw new IllegalArgumentException("O ID do vendedor não pode ser nulo.");
        return preencherLista(((AgendamentoDao) dao).buscarPorVendedorId(vendedorId));
    }

    @Override
    protected void carregarRelacionamentos(Agendamento a) {
        if (a.getCliente() != null && a.getCliente().getId() != null) {
            a.setCliente(clienteDao.buscarPorId(a.getCliente().getId()));
        }
        if (a.getVendedor() != null && a.getVendedor().getId() != null) {
            a.setVendedor(vendedorDao.buscarPorId(a.getVendedor().getId()));
        }
    }

    @Override
    protected void validar(Agendamento a) {
        super.validar(a);
        if (a.getCliente() == null || a.getCliente().getId() == null) {
            throw new IllegalArgumentException("O cliente associado ao agendamento é obrigatório.");
        }
        if (a.getVendedor() == null || a.getVendedor().getId() == null) {
            throw new IllegalArgumentException("O vendedor associado ao agendamento é obrigatório.");
        }
        if (a.getAgendadoPara() == null) {
            throw new IllegalArgumentException("A data/hora agendada é obrigatória.");
        }
        if (a.getAgendadoPara().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new IllegalArgumentException("A data do agendamento não pode ser no passado.");
        }
    }
}