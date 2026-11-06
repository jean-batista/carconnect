package dao.interfaces;

import java.util.List;
import java.util.UUID;

import entidades.Agendamento;

public interface AgendamentoDao {
    
    // Faz um Agendamento
    void cadastro(Agendamento agendamento);

    // Busca um Agendamento pelo id
    Agendamento buscarAgendamentoPorId(UUID id);

    // Busca todos os agendamentos cadastrados no sistema
    List<Agendamento> buscarAgendamentos();

    // Busca todos os agendamentos de um Cliente
    List<Agendamento> buscarAgendamentosPorCliente(UUID clienteId);

    // Busca todos os agendamentos de um Vendedor
    List<Agendamento> buscarAgendamentosPorVendedor(UUID vendedorId);

    // Atualiza um Agendamento
    void atualizar(Agendamento agendamento);

    // Cancela um Agendamento (apaga do sistema)
    void excluir(Agendamento agendamento);

}
