package dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Supplier;

import dao.DaoGenerico;
import entidades.Pessoa;

/**
 * Classe abstrata intermediária que elimina a duplicação de SQL e mapeamento
 * entre ClienteDao, VendedorDao e GestorDao.
 */
public abstract class PessoaSubclasseDao<T extends Pessoa> extends DaoGenerico<T, UUID> {

    private final String nomeTabela;
    private final Supplier<T> construtor;

    protected PessoaSubclasseDao(Connection connection, String nomeTabela, Supplier<T> construtor) {
        super(connection);
        this.nomeTabela = nomeTabela;
        this.construtor = construtor;
    }

    @Override
    protected String getSqlInserir() {
        return "INSERT INTO " + nomeTabela + " (id) VALUES (?)";
    }

    @Override
    protected String getSqlBuscarPorId() {
        return "SELECT p.* FROM " + nomeTabela + " sub INNER JOIN tabela_pessoa p ON sub.id = p.id WHERE sub.id = ?";
    }

    @Override
    protected String getSqlBuscarTodos() {
        return "SELECT p.* FROM " + nomeTabela + " sub INNER JOIN tabela_pessoa p ON sub.id = p.id";
    }

    @Override
    protected String getSqlAtualizar() {
        throw new UnsupportedOperationException("Dados cadastrais devem ser atualizados via PessoaDao.");
    }

    @Override
    protected String getSqlExcluir() {
        return "DELETE FROM " + nomeTabela + " WHERE id = ?";
    }

    @Override
    protected void preencherStatementInserir(PreparedStatement st, T entidade) throws SQLException {
        st.setString(1, entidade.getId().toString());
    }

    @Override
    protected void preencherStatementAtualizar(PreparedStatement st, T entidade) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void preencherStatementExcluir(PreparedStatement st, T entidade) throws SQLException {
        st.setString(1, entidade.getId().toString());
    }

    @Override
    protected T mapearResultSet(ResultSet rs) throws SQLException {
        T entidade = construtor.get();
        PessoaDao.preencherCamposPessoa(rs, entidade);
        return entidade;
    }
}