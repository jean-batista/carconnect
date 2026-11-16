package dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import dao.DaoGenerico;
import entidades.Pessoa;

public class PessoaDao extends DaoGenerico<Pessoa, UUID> {

    public PessoaDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSqlInserir() {
        return "INSERT INTO tabela_pessoa (id, nome, sobrenome, email, telefone, cpf, senha) VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSqlBuscarPorId() {
        return "SELECT * FROM tabela_pessoa WHERE id = ?";
    }

    @Override
    protected String getSqlBuscarTodos() {
        return "SELECT * FROM tabela_pessoa";
    }

    @Override
    protected String getSqlAtualizar() {
        return "UPDATE tabela_pessoa SET nome = ?, sobrenome = ?, email = ?, telefone = ?, cpf = ?, senha = ? WHERE id = ?";
    }

    @Override
    protected String getSqlExcluir() {
        return "DELETE FROM tabela_pessoa WHERE id = ?";
    }

    @Override
    protected void preencherStatementInserir(PreparedStatement st, Pessoa p) throws SQLException {
        if (p.getId() == null) p.setId(UUID.randomUUID());
        st.setString(1, p.getId().toString());
        st.setString(2, p.getNome());
        st.setString(3, p.getSobrenome());
        st.setString(4, p.getEmail());
        st.setString(5, p.getTelefone());
        st.setString(6, p.getCpf());
        st.setString(7, p.getSenha());
    }

    @Override
    protected void preencherStatementAtualizar(PreparedStatement st, Pessoa p) throws SQLException {
        st.setString(1, p.getNome());
        st.setString(2, p.getSobrenome());
        st.setString(3, p.getEmail());
        st.setString(4, p.getTelefone());
        st.setString(5, p.getCpf());
        st.setString(6, p.getSenha());
        st.setString(7, p.getId().toString());
    }

    @Override
    protected void preencherStatementExcluir(PreparedStatement st, Pessoa p) throws SQLException {
        st.setString(1, p.getId().toString());
    }

    @Override
    protected Pessoa mapearResultSet(ResultSet rs) throws SQLException {
        Pessoa p = new Pessoa() {};
        preencherCamposPessoa(rs, p);
        return p;
    }

    public static void preencherCamposPessoa(ResultSet rs, Pessoa p) throws SQLException {
        p.setId(UUID.fromString(rs.getString("id")));
        p.setNome(rs.getString("nome"));
        p.setSobrenome(rs.getString("sobrenome"));
        p.setEmail(rs.getString("email"));
        p.setTelefone(rs.getString("telefone"));
        p.setCpf(rs.getString("cpf"));
        p.setSenha(rs.getString("senha"));
    }
}