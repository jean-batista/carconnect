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
import dao.interfaces.ClienteDao;
import entidades.Cliente;

/*  =================================
    == Implementacao de ClienteDao ==
    =================================
*/

public class ClienteDaoImpl implements ClienteDao {

    // Conexao do Banco de Dados
    private Connection connection = null;

    // Recebe a conexao do Banco de Dados via construtor
    public ClienteDaoImpl(Connection connection) {
        this.connection = connection; // Define a conexao do banco no atributo da classe
    }

    @Override
    public Cliente cadastro(Cliente cliente) {
        String sql = "INSERT INTO tabela_cliente (id) VALUES (?)";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, cliente.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            // Ocorre um erro caso nao houver linhas alteradas
            if(linhasAlteradas == 0) throw new BancoException("Não foi possível cadastrar o cliente");
            return cliente;
        } catch (Exception e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    public Cliente buscarClientePorId(UUID id) {
        // Faz o JOIN entre a tabela cliente (c) e a tabela pessoa (p) usando o ID.
        // Assim buscamos todos os atributos herdados e específicos em uma ÚNICA consulta!
        String sql = "SELECT p.id, p.nome, p.sobrenome, p.email, p.telefone, p.cpf, p.senha " +
                     "FROM tabela_cliente c " +
                     "INNER JOIN tabela_pessoa p ON c.id = p.id " +
                     "WHERE c.id = ?";

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();

            if (rs.next()) {
                // Instancia o objeto Cliente já preenchendo todos os dados vindos do JOIN
                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(rs.getString("id")));
                cliente.setNome(rs.getString("nome"));
                cliente.setSobrenome(rs.getString("sobrenome"));
                cliente.setEmail(rs.getString("email"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setSenha(rs.getString("senha"));

                return cliente;
            }

            // Retorna null caso não encontre nenhum cliente com o ID especificado
            return null;

        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar cliente por ID: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override 
    public List<Cliente> buscarClientesCadastrados() {
        // Realiza o JOIN trazendo todas as pessoas que estão cadastradas como clientes
        String sql = "SELECT p.id, p.nome, p.sobrenome, p.email, p.telefone, p.cpf, p.senha " +
                     "FROM tabela_cliente c " +
                     "INNER JOIN tabela_pessoa p ON c.id = p.id";

        PreparedStatement st = null;
        ResultSet rs = null;
        List<Cliente> clientes = new ArrayList<>();

        try {
            st = connection.prepareStatement(sql);
            rs = st.executeQuery();

            // Usamos o while para percorrer todas as linhas retornadas pelo banco de dados
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(rs.getString("id")));
                cliente.setNome(rs.getString("nome"));
                cliente.setSobrenome(rs.getString("sobrenome"));
                cliente.setEmail(rs.getString("email"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setSenha(rs.getString("senha"));

                clientes.add(cliente);
            }

            return clientes;

        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar clientes cadastrados: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void excluirCadastro(Cliente cliente) {
        String sql = "DELETE FROM tabela_cliente WHERE id = ?";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, cliente.getId().toString());
            int linhasAlteradas = st.executeUpdate();
            if(linhasAlteradas == 0) throw new BancoException("O cliente não pode ser excluído");
        } catch(SQLException e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }
    
}
