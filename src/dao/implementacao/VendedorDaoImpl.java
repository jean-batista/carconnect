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
import dao.interfaces.VendedorDao;
import entidades.Vendedor;

public class VendedorDaoImpl implements VendedorDao {

    // Conexao do Banco de Dados
    private Connection connection = null;

    // Recebe a conexao do Banco de Dados via construtor
    public VendedorDaoImpl(Connection connection) {
        this.connection = connection; // Define a conexao do banco no atributo da classe
    }

    @Override
    public Vendedor cadastro(Vendedor vendedor) {
        String sql = "INSERT INTO tabela_vendedor (id) VALUES (?)";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, vendedor.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            // Ocorre um erro caso nao houver linhas alteradas
            if(linhasAlteradas == 0) throw new BancoException("Não foi possível cadastrar o vendedor");
            return vendedor;
        } catch (Exception e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public Vendedor buscarVendedorPorId(UUID id) {
        // Faz o JOIN com a tabela_pessoa para trazer todos os atributos cadastrais em uma única consulta
        String sql = "SELECT p.id, p.nome, p.sobrenome, p.email, p.telefone, p.cpf, p.senha " +
                     "FROM tabela_vendedor v " +
                     "INNER JOIN tabela_pessoa p ON v.id = p.id " +
                     "WHERE v.id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();

            if (rs.next()) {
                Vendedor vendedor = new Vendedor();
                vendedor.setId(UUID.fromString(rs.getString("id")));
                vendedor.setNome(rs.getString("nome"));
                vendedor.setSobrenome(rs.getString("sobrenome"));
                vendedor.setEmail(rs.getString("email"));
                vendedor.setTelefone(rs.getString("telefone"));
                vendedor.setCpf(rs.getString("cpf"));
                vendedor.setSenha(rs.getString("senha"));
                return vendedor;
            }
            return null;
        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar vendedor por ID: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Vendedor> buscarVendedoresCadastrados() {
        // Traz todas as pessoas que são vendedoras através do INNER JOIN
        String sql = "SELECT p.id, p.nome, p.sobrenome, p.email, p.telefone, p.cpf, p.senha " +
                     "FROM tabela_vendedor v " +
                     "INNER JOIN tabela_pessoa p ON v.id = p.id";
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Vendedor> vendedores = new ArrayList<>();
        try {
            st = connection.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                Vendedor vendedor = new Vendedor();
                vendedor.setId(UUID.fromString(rs.getString("id")));
                vendedor.setNome(rs.getString("nome"));
                vendedor.setSobrenome(rs.getString("sobrenome"));
                vendedor.setEmail(rs.getString("email"));
                vendedor.setTelefone(rs.getString("telefone"));
                vendedor.setCpf(rs.getString("cpf"));
                vendedor.setSenha(rs.getString("senha"));
                vendedores.add(vendedor);
            }
            return vendedores;
        } catch (SQLException e) {
            throw new BancoException("Erro ao listar vendedores cadastrados: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void excluirCadastro(Vendedor vendedor) {
        String sql = "DELETE FROM tabela_vendedor WHERE id = ?";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, vendedor.getId().toString());
            int linhasAlteradas = st.executeUpdate();
            if(linhasAlteradas == 0) throw new BancoException("O vendedor não pode ser excluído");
        } catch(SQLException e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }
}