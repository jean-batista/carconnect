package dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import dao.DaoGenerico;
import entidades.Veiculo;

public class VeiculoDao extends DaoGenerico<Veiculo, UUID> {

    public VeiculoDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSqlInserir() {
        return "INSERT INTO tabela_veiculo (id, marca, modelo, ano, combustivel, condicao, preco, quantidade_estoque) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSqlBuscarPorId() {
        return "SELECT * FROM tabela_veiculo WHERE id = ?";
    }

    @Override
    protected String getSqlBuscarTodos() {
        return "SELECT * FROM tabela_veiculo";
    }

    @Override
    protected String getSqlAtualizar() {
        return "UPDATE tabela_veiculo SET marca = ?, modelo = ?, ano = ?, combustivel = ?, condicao = ?, preco = ?, quantidade_estoque = ? WHERE id = ?";
    }

    @Override
    protected String getSqlExcluir() {
        return "DELETE FROM tabela_veiculo WHERE id = ?";
    }

    @Override
    protected void preencherStatementInserir(PreparedStatement st, Veiculo v) throws SQLException {
        if (v.getId() == null) v.setId(UUID.randomUUID());
        st.setString(1, v.getId().toString());
        preencherDadosComuns(st, v, 2);
    }

    @Override
    protected void preencherStatementAtualizar(PreparedStatement st, Veiculo v) throws SQLException {
        preencherDadosComuns(st, v, 1);
        st.setString(8, v.getId().toString());
    }

    private void preencherDadosComuns(PreparedStatement st, Veiculo v, int inicio) throws SQLException {
        st.setString(inicio, v.getMarca());
        st.setString(inicio + 1, v.getModelo());
        st.setInt(inicio + 2, v.getAno());
        st.setString(inicio + 3, v.getCombustivel());
        st.setString(inicio + 4, v.getCondicao());
        st.setDouble(inicio + 5, v.getPreco());
        st.setInt(inicio + 6, v.getQuantidadeEstoque());
    }

    @Override
    protected void preencherStatementExcluir(PreparedStatement st, Veiculo v) throws SQLException {
        st.setString(1, v.getId().toString());
    }

    @Override
    protected Veiculo mapearResultSet(ResultSet rs) throws SQLException {
        Veiculo v = new Veiculo();
        v.setId(UUID.fromString(rs.getString("id")));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));
        v.setAno(rs.getInt("ano"));
        v.setCombustivel(rs.getString("combustivel"));
        v.setCondicao(rs.getString("condicao"));
        v.setPreco(rs.getDouble("preco"));
        v.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
        return v;
    }

    // Operação específica de VeiculoDao
    public void atualizarEstoque(Veiculo veiculo, Integer unidades) {
        String sql = "UPDATE tabela_veiculo SET quantidade_estoque = ? WHERE id = ?";
        executarUpdate(sql, st -> {
            st.setInt(1, unidades);
            st.setString(2, veiculo.getId().toString());
        }, "Não foi possível alterar o estoque");
        veiculo.setQuantidadeEstoque(unidades);
    }
}