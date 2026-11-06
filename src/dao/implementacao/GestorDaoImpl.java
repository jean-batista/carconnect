package dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import banco.BancoDeDados;
import banco.BancoException;
import dao.interfaces.GestorDao;
import entidades.Gestor;

public class GestorDaoImpl implements GestorDao {

    // Conexao do Banco de Dados
    private Connection connection = null;

    // Recebe a conexao do Banco de Dados via construtor
    public GestorDaoImpl(Connection connection) {
        this.connection = connection; // Define a conexao do banco no atributo da classe
    }

    @Override
    public void cadastrar(Gestor gestor) {
        String sql = "INSERT INTO tabela_gestor (id) VALUES (?)";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, gestor.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            // Ocorre um erro caso nao houver linhas alteradas
            if (linhasAlteradas == 0) {
                throw new BancoException("Não foi possível cadastrar o gestor");
            }
        } catch (SQLException e) {
            throw new BancoException("Erro ao cadastrar gestor: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public Gestor buscarGestor(UUID id) {
        // Faz o JOIN com a tabela_pessoa para trazer todos os atributos cadastrais em uma única consulta
        String sql = "SELECT p.id, p.nome, p.sobrenome, p.email, p.telefone, p.cpf, p.senha " +
                     "FROM tabela_gestor g " +
                     "INNER JOIN tabela_pessoa p ON g.id = p.id " +
                     "WHERE g.id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();

            if (rs.next()) {
                Gestor gestor = new Gestor();
                gestor.setId(UUID.fromString(rs.getString("id")));
                gestor.setNome(rs.getString("nome"));
                gestor.setSobrenome(rs.getString("sobrenome"));
                gestor.setEmail(rs.getString("email"));
                gestor.setTelefone(rs.getString("telefone"));
                gestor.setCpf(rs.getString("cpf"));
                gestor.setSenha(rs.getString("senha"));

                return gestor;
            }
            return null;
        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar gestor por ID: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Gestor> listarGestores() {
        // Traz todas as pessoas que são gestoras através do INNER JOIN
        String sql = "SELECT p.id, p.nome, p.sobrenome, p.email, p.telefone, p.cpf, p.senha " +
                     "FROM tabela_gestor g " +
                     "INNER JOIN tabela_pessoa p ON g.id = p.id";
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Gestor> gestores = new ArrayList<>();

        try {
            st = connection.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                Gestor gestor = new Gestor();
                gestor.setId(UUID.fromString(rs.getString("id")));
                gestor.setNome(rs.getString("nome"));
                gestor.setSobrenome(rs.getString("sobrenome"));
                gestor.setEmail(rs.getString("email"));
                gestor.setTelefone(rs.getString("telefone"));
                gestor.setCpf(rs.getString("cpf"));
                gestor.setSenha(rs.getString("senha"));

                gestores.add(gestor);
            }
            return gestores;
        } catch (SQLException e) {
            throw new BancoException("Erro ao listar gestores cadastrados: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void excluirGestor(Gestor gestor) {
        String sql = "DELETE FROM tabela_gestor WHERE id = ?";
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, gestor.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            if (linhasAlteradas == 0) {
                throw new BancoException("O gestor não pode ser excluído");
            }
        } catch (SQLException e) {
            throw new BancoException("Erro ao excluir gestor: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }
}