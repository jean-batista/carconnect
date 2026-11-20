package dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import dao.DaoGenerico;
import entidades.Agendamento;
import entidades.Cliente;
import entidades.Vendedor;

public class AgendamentoDao extends DaoGenerico<Agendamento, UUID> {

    public AgendamentoDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSqlInserir() {
        return "INSERT INTO tabela_agendamento (id, agendado_em, agendado_para, cliente_id, vendedor_id) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSqlBuscarPorId() {
        return "SELECT * FROM tabela_agendamento WHERE id = ?";
    }

    @Override
    protected String getSqlBuscarTodos() {
        return "SELECT * FROM tabela_agendamento";
    }

    @Override
    protected String getSqlAtualizar() {
        return "UPDATE tabela_agendamento SET agendado_para = ?, cliente_id = ?, vendedor_id = ? WHERE id = ?";
    }

    @Override
    protected String getSqlExcluir() {
        return "DELETE FROM tabela_agendamento WHERE id = ?";
    }

    @Override
    protected void preencherStatementInserir(PreparedStatement st, Agendamento a) throws SQLException {
        if (a.getId() == null) a.setId(UUID.randomUUID());
        st.setString(1, a.getId().toString());
        st.setObject(2, a.getAgendadoEm());
        st.setObject(3, a.getAgendadoPara());
        st.setString(4, a.getCliente().getId().toString());
        st.setString(5, a.getVendedor().getId().toString());
    }

    @Override
    protected void preencherStatementAtualizar(PreparedStatement st, Agendamento a) throws SQLException {
        st.setObject(1, a.getAgendadoPara());
        st.setString(2, a.getCliente().getId().toString());
        st.setString(3, a.getVendedor().getId().toString());
        st.setString(4, a.getId().toString());
    }

    @Override
    protected void preencherStatementExcluir(PreparedStatement st, Agendamento a) throws SQLException {
        st.setString(1, a.getId().toString());
    }

    @Override
    protected Agendamento mapearResultSet(ResultSet rs) throws SQLException {
        Agendamento a = new Agendamento();
        a.setId(UUID.fromString(rs.getString("id")));
        a.setAgendadoEm(rs.getObject("agendado_em", LocalDateTime.class));
        a.setAgendadoPara(rs.getObject("agendado_para", LocalDateTime.class));

        Cliente c = new Cliente();
        c.setId(UUID.fromString(rs.getString("cliente_id")));
        a.setCliente(c);

        Vendedor v = new Vendedor();
        v.setId(UUID.fromString(rs.getString("vendedor_id")));
        a.setVendedor(v);
        return a;
    }

    // Consultas específicas de AgendamentoDao
    public List<Agendamento> buscarPorClienteId(UUID clienteId) {
        return executarConsulta("SELECT * FROM tabela_agendamento WHERE cliente_id = ?",
                st -> st.setString(1, clienteId.toString()));
    }

    public List<Agendamento> buscarPorVendedorId(UUID vendedorId) {
        return executarConsulta("SELECT * FROM tabela_agendamento WHERE vendedor_id = ?",
                st -> st.setString(1, vendedorId.toString()));
    }
}