package servicos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import config.AppConfig;
import entidades.Agendamento;
import repositorios.AgendamentoRepositorio;

/**
 * Camada de serviço para gerenciamento de Agendamentos.
 * Centraliza as regras de negócio e validações de datas e relacionamentos
 * antes de acionar a persistência via AgendamentoRepositorio.
 */
public class AgendamentoServico {

    private final AgendamentoRepositorio agendamentoRepositorio;

    /**
     * Construtor padrão que utiliza o AppConfig para resolver
     * e injetar automaticamente a instância configurada do AgendamentoRepositorio.
     */
    public AgendamentoServico() {
        this.agendamentoRepositorio = AppConfig.configurarAgendamentoRepositorio();
    }

    /**
     * Construtor sobrecarregado para permitir injeção manual (útil para testes unitários).
     * @param agendamentoRepositorio Instância do repositório de agendamentos.
     */
    public AgendamentoServico(AgendamentoRepositorio agendamentoRepositorio) {
        this.agendamentoRepositorio = agendamentoRepositorio;
    }

    /**
     * Cadastra um novo agendamento aplicando validações de data e vinculo.
     * @param agendamento Objeto de agendamento a ser cadastrado.
     * @return Agendamento cadastrado.
     */
    public Agendamento cadastrar(Agendamento agendamento) {
        if (agendamento != null && agendamento.getAgendadoEm() == null) {
            agendamento.setAgendadoEm(LocalDateTime.now());
        }
        validarAgendamento(agendamento);
        return agendamentoRepositorio.cadastrar(agendamento);
    }

    /**
     * Busca um agendamento por seu ID (UUID).
     * @param id Identificador do agendamento.
     * @return Objeto Agendamento ou null se não encontrado.
     */
    public Agendamento buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para busca não pode ser nulo.");
        }
        return agendamentoRepositorio.buscarPorId(id);
    }

    /**
     * Retorna a lista completa de agendamentos.
     * @return Lista de agendamentos.
     */
    public List<Agendamento> buscarTodos() {
        return agendamentoRepositorio.buscarTodos();
    }

    /**
     * Retorna a lista de agendamentos vinculados a um determinado cliente.
     * @param clienteId UUID do cliente.
     * @return Lista de agendamentos do cliente.
     */
    public List<Agendamento> buscarPorClienteId(UUID clienteId) {
        if (clienteId == null) {
            throw new IllegalArgumentException("O ID do cliente não pode ser nulo.");
        }
        return agendamentoRepositorio.buscarPorClienteId(clienteId);
    }

    /**
     * Retorna a lista de agendamentos vinculados a um determinado vendedor.
     * @param vendedorId UUID do vendedor.
     * @return Lista de agendamentos do vendedor.
     */
    public List<Agendamento> buscarPorVendedorId(UUID vendedorId) {
        if (vendedorId == null) {
            throw new IllegalArgumentException("O ID do vendedor não pode ser nulo.");
        }
        return agendamentoRepositorio.buscarPorVendedorId(vendedorId);
    }

    /**
     * Atualiza os dados de um agendamento (ex: remarcação).
     * @param agendamento Objeto contendo o ID e os novos dados.
     */
    public void atualizar(Agendamento agendamento) {
        if (agendamento == null || agendamento.getId() == null) {
            throw new IllegalArgumentException("O agendamento e seu ID são obrigatórios para atualização.");
        }
        validarAgendamento(agendamento);
        agendamentoRepositorio.atualizar(agendamento);
    }

    /**
     * Exclui ou cancela um agendamento.
     * @param agendamento Objeto do agendamento a ser removido.
     */
    public void excluir(Agendamento agendamento) {
        if (agendamento == null || agendamento.getId() == null) {
            throw new IllegalArgumentException("O agendamento e seu ID são obrigatórios para exclusão.");
        }
        agendamentoRepositorio.excluir(agendamento);
    }

    /**
     * Regras de validação de negócio para Agendamento.
     */
    private void validarAgendamento(Agendamento agendamento) {
        if (agendamento == null) {
            throw new IllegalArgumentException("O agendamento não pode ser nulo.");
        }
        if (agendamento.getCliente() == null || agendamento.getCliente().getId() == null) {
            throw new IllegalArgumentException("O cliente associado ao agendamento é obrigatório.");
        }
        if (agendamento.getVendedor() == null || agendamento.getVendedor().getId() == null) {
            throw new IllegalArgumentException("O vendedor associado ao agendamento é obrigatório.");
        }
        if (agendamento.getAgendadoPara() == null) {
            throw new IllegalArgumentException("A data/hora agendada (agendadoPara) é obrigatória.");
        }
        if (agendamento.getAgendadoPara().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new IllegalArgumentException("A data do agendamento não pode ser no passado.");
        }
    }
}