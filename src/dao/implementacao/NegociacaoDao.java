package dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import dao.DaoGenerico;
import entidades.Cliente;
import entidades.Negociacao;
import entidades.Veiculo;
import entidades.Vendedor;

public class NegociacaoDao extends DaoGenerico<Negociacao, UUID> {

    public NegociacaoDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSqlInserir() {
        return "INSERT INTO tabela_negociacao (id, data, cliente_id, vendedor_id, veiculo_id) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSqlBuscarPorId() {
        return "SELECT * FROM tabela_negociacao WHERE id = ?";
    }

    @Override
    protected String getSqlBuscarTodos() {
        return "SELECT * FROM tabela_negociacao";
    }

    @Override
    protected String getSqlAtualizar() {
        throw new UnsupportedOperationException("Negociações não podem ser alteradas.");
    }

    @Override
    protected String getSqlExcluir() {
        throw new UnsupportedOperationException("Negociações não podem ser excluídas.");
    }

    @Override
    protected void preencherStatementInserir(PreparedStatement st, Negociacao n) throws SQLException {
        if (n.getId() == null) n.setId(UUID.randomUUID());
        st.setString(1, n.getId().toString());
        st.setObject(2, n.getData());
        st.setString(3, n.getCliente().getId().toString());
        st.setString(4, n.getVendedor().getId().toString());
        st.setString(5, n.getVeiculo().getId().toString());
    }

    @Override
    protected void preencherStatementAtualizar(PreparedStatement st, Negociacao n) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void preencherStatementExcluir(PreparedStatement st, Negociacao n) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Negociacao mapearResultSet(ResultSet rs) throws SQLException {
        Negociacao n = new Negociacao();
        n.setId(UUID.fromString(rs.getString("id")));
        n.setData(rs.getObject("data", LocalDateTime.class));

        Cliente c = new Cliente();
        c.setId(UUID.fromString(rs.getString("cliente_id")));
        n.setCliente(c);

        Vendedor v = new Vendedor();
        v.setId(UUID.fromString(rs.getString("vendedor_id")));
        n.setVendedor(v);

        Veiculo veic = new Veiculo();
        veic.setId(UUID.fromString(rs.getString("veiculo_id")));
        n.setVeiculo(veic);
        return n;
    }

    // Consultas específicas de NegociacaoDao
    public List<Negociacao> buscarPorClienteId(UUID clienteId) {
        return executarConsulta("SELECT * FROM tabela_negociacao WHERE cliente_id = ?",
                st -> st.setString(1, clienteId.toString()));
    }

    public List<Negociacao> buscarPorVendedorId(UUID vendedorId) {
        return executarConsulta("SELECT * FROM tabela_negociacao WHERE vendedor_id = ?",
                st -> st.setString(1, vendedorId.toString()));
    }
}