package dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public void cadastro(Cliente cliente) {
        String sql = "INSERT INTO tabela_cliente (id) VALUES (?)";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, cliente.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            // Ocorre um erro caso nao houver linhas alteradas
            if(linhasAlteradas == 0) throw new BancoException("Não foi possível cadastrar o cliente");
        } catch (Exception e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public Cliente buscarClientePorId(UUID id) {
        String sql = "SELECT * FROM tabela_cliente WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        Cliente cliente = new Cliente(); // Cria um objeto Cliente vazio
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();
            while(rs.next()) {
                cliente.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Cliente
            }
            return cliente;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Cliente> buscarClientesCadastrados() {
        String sql = "SELECT * FROM tabela_cliente";
        Statement st = null;
        ResultSet rs = null;
        Cliente cliente = new Cliente(); // Cria um objeto Cliente vazio
        List<Cliente> clientes = new ArrayList<>(); // Cria uma lista de clientes vazia
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                cliente.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Pessoa
                clientes.add(cliente);
            }
            return clientes;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    // TODO implementar metodo caso tiver solucao
    @Override
    public void atualizarCadastro(Cliente cliente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarCadastro'");
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
