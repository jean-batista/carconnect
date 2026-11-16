package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import banco.BancoDeDados;
import banco.BancoException;

/**
 * Classe abstrata genérica que centraliza o gerenciamento de conexões,
 * tratamento de exceções e execução de CRUD via Template Method.
 */
public abstract class DaoGenerico<T, ID> {

    protected final Connection connection;

    protected DaoGenerico(Connection connection) {
        this.connection = connection;
    }

    // Métodos abstratos que cada DAO concreto define (SQLs e mapeamentos)
    protected abstract String getSqlInserir();
    protected abstract String getSqlBuscarPorId();
    protected abstract String getSqlBuscarTodos();
    protected abstract String getSqlAtualizar();
    protected abstract String getSqlExcluir();

    protected abstract void preencherStatementInserir(PreparedStatement st, T entidade) throws SQLException;
    protected abstract void preencherStatementAtualizar(PreparedStatement st, T entidade) throws SQLException;
    protected abstract void preencherStatementExcluir(PreparedStatement st, T entidade) throws SQLException;
    protected abstract T mapearResultSet(ResultSet rs) throws SQLException;

    public T inserir(T entidade) {
        executarUpdate(getSqlInserir(), st -> preencherStatementInserir(st, entidade), "Erro ao inserir registro");
        return entidade;
    }

    public T buscarPorId(ID id) {
        List<T> resultados = executarConsulta(getSqlBuscarPorId(), st -> st.setString(1, id.toString()));
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public List<T> buscarTodos() {
        return executarConsulta(getSqlBuscarTodos(), null);
    }

    public void atualizar(T entidade) {
        executarUpdate(getSqlAtualizar(), st -> preencherStatementAtualizar(st, entidade), "Nenhum registro atualizado");
    }

    public void excluir(T entidade) {
        executarUpdate(getSqlExcluir(), st -> preencherStatementExcluir(st, entidade), "Nenhum registro excluído");
    }

    /**
     * Helper reutilizável para qualquer SELECT (inclusive consultas específicas nas subclasses).
     */
    protected List<T> executarConsulta(String sql, StatementBinder binder) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<T> lista = new ArrayList<>();
        try {
            st = connection.prepareStatement(sql);
            if (binder != null) {
                binder.bind(st);
            }
            rs = st.executeQuery();
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new BancoException("Erro na consulta ao banco: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    /**
     * Helper reutilizável para INSERT, UPDATE e DELETE.
     */
    protected void executarUpdate(String sql, StatementBinder binder, String msgErroZeroLinhas) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            if (binder != null) {
                binder.bind(st);
            }
            int linhas = st.executeUpdate();
            if (linhas == 0 && msgErroZeroLinhas != null) {
                throw new BancoException(msgErroZeroLinhas);
            }
        } catch (SQLException e) {
            throw new BancoException("Erro na operação de escrita: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @FunctionalInterface
    protected interface StatementBinder {
        void bind(PreparedStatement st) throws SQLException;
    }
}