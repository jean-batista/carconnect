package servicos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import config.AppConfig;
import entidades.Agendamento;
import repositorios.AgendamentoRepositorio;

public class AgendamentoServico extends ServicoBase<Agendamento, UUID> {

    public AgendamentoServico() {
        super(AppConfig.configurarAgendamentoRepositorio());
    }

    public AgendamentoServico(AgendamentoRepositorio repositorio) {
        super(repositorio);
    }

    @Override
    public Agendamento cadastrar(Agendamento agendamento) {
        if (agendamento != null && agendamento.getAgendadoEm() == null) {
            agendamento.setAgendadoEm(LocalDateTime.now());
        }
        return super.cadastrar(agendamento);
    }

    public List<Agendamento> buscarPorClienteId(UUID clienteId) {
        if (clienteId == null) throw new IllegalArgumentException("O ID do cliente não pode ser nulo.");
        return ((AgendamentoRepositorio) repositorio).buscarPorClienteId(clienteId);
    }

    public List<Agendamento> buscarPorVendedorId(UUID vendedorId) {
        if (vendedorId == null) throw new IllegalArgumentException("O ID do vendedor não pode ser nulo.");
        return ((AgendamentoRepositorio) repositorio).buscarPorVendedorId(vendedorId);
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