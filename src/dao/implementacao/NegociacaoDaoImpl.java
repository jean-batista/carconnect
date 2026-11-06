package dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import banco.BancoDeDados;
import banco.BancoException;
import dao.interfaces.NegociacaoDao;
import entidades.Cliente;
import entidades.Negociacao;
import entidades.Veiculo;
import entidades.Vendedor;

public class NegociacaoDaoImpl implements NegociacaoDao {
    
    // Conexao do Banco de Dados
    private Connection connection = null;

    // Recebe a conexao do Banco de Dados via construtor
    public NegociacaoDaoImpl(Connection connection) {
        this.connection = connection; // Define a conexao do banco no atributo da classe
    }

    @Override
    public void cadastrar(Negociacao negociacao) {
        String sql = "INSERT INTO tabela_negociacao (id, data, cliente_id, vendedor_id, veiculo_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement st = null;
        try {
            negociacao.setId(UUID.randomUUID()); // Gera um UUID aleatoriamente

            st = connection.prepareStatement(sql);
            st.setString(1, negociacao.getId().toString()); // Define o id
            st.setObject(2, negociacao.getData()); // Define a data
            st.setString(3, negociacao.getCliente().getId().toString()); // Define id do Cliente
            st.setString(4, negociacao.getVendedor().getId().toString()); // Define o id da Negociacao
            st.setString(5, negociacao.getVeiculo().getId().toString()); // Define o id do Veiculo
            int linhasAlteradas = st.executeUpdate(); // Executa as mudancas
            
            // Ocorre um erro caso nao houver linhas alteradas
            if(linhasAlteradas == 0) throw new BancoException("Não foi possível cadastrar a negociacao");
        } catch (Exception e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            // Fecha a conexao com o banco de dados independentemente so `try` ou `catch`
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public Negociacao buscarNegociacaoPorId(UUID id) {
        String sql = "SELECT * FROM tabela_negociacao WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        Negociacao negociacao = new Negociacao(); // Cria um objeto Negociacao vazio
        Cliente cliente = new Cliente(); // Cria um objeto Cliente vazio
        Vendedor vendedor = new Vendedor(); // Cria um objeto Vendedor vazio
        Veiculo veiculo = new Veiculo(); // Cria um objeto Veiculo vazio
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();
            while(rs.next()) {
                negociacao.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Negociacao
                negociacao.setData(rs.getObject("data", LocalDateTime.class)); // Define a data
                cliente.setId(UUID.fromString(rs.getString("cliente_id"))); // Define o id do Cliente
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id"))); // Define o id do Vendedor
                veiculo.setId(UUID.fromString(rs.getString("veiculo_id"))); // Define o id do Veiculo
                negociacao.setCliente(cliente); // Define o Cliente da Negociacao
                negociacao.setVendedor(vendedor); // Define o Vendedor da Negociacao
                negociacao.setVeiculo(veiculo); // Define o Veiculo da Negociacao
            }
            return negociacao;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Negociacao> buscarNegociacoes() {
        String sql = "SELECT * FROM tabela_negociacao";
        Statement st = null;
        ResultSet rs = null;
        List<Negociacao> negociacoes = new ArrayList<>(); // Cria uma lista de Negociacao
        Negociacao negociacao = new Negociacao(); // Cria um objeto Negociacao vazio
        Cliente cliente = new Cliente(); // Cria um objeto Cliente vazio
        Vendedor vendedor = new Vendedor(); // Cria um objeto Vendedor vazio
        Veiculo veiculo = new Veiculo(); // Cria um objeto Veiculo vazio
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                negociacao.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Negociacao
                negociacao.setData(rs.getObject("data", LocalDateTime.class)); // Define a data
                cliente.setId(UUID.fromString(rs.getString("cliente_id"))); // Define o id do Cliente
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id"))); // Define o id do Vendedor
                veiculo.setId(UUID.fromString(rs.getString("veiculo_id"))); // Define o id do Veiculo
                negociacao.setCliente(cliente); // Define o Cliente da Negociacao
                negociacao.setVendedor(vendedor); // Define o Vendedor da Negociacao
                negociacao.setVeiculo(veiculo); // Define o Veiculo da Negociacao
                negociacoes.add(negociacao);
            }
            return negociacoes;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Negociacao> buscarNegociacoesPorCliente(UUID clienteId) {
        String sql = "SELECT * FROM tabela_negociacao WHERE cliente_id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Negociacao> negociacoes = new ArrayList<>(); // Cria uma lista de Negociacao
        Negociacao negociacao = new Negociacao(); // Cria um objeto Negociacao vazio
        Cliente cliente = new Cliente(); // Cria um objeto Cliente vazio
        Vendedor vendedor = new Vendedor(); // Cria um objeto Vendedor vazio
        Veiculo veiculo = new Veiculo(); // Cria um objeto Veiculo vazio
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, clienteId.toString());
            rs = st.executeQuery();
            while(rs.next()) {
                negociacao.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Negociacao
                negociacao.setData(rs.getObject("data", LocalDateTime.class)); // Define a data
                cliente.setId(UUID.fromString(rs.getString("cliente_id"))); // Define o id do Cliente
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id"))); // Define o id do Vendedor
                veiculo.setId(UUID.fromString(rs.getString("veiculo_id"))); // Define o id do Veiculo
                negociacao.setCliente(cliente); // Define o Cliente da Negociacao
                negociacao.setVendedor(vendedor); // Define o Vendedor da Negociacao
                negociacao.setVeiculo(veiculo); // Define o Veiculo da Negociacao
                negociacoes.add(negociacao);
            }
            return negociacoes;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Negociacao> buscarNegociacoesPorVendedor(UUID vendedorId) {
        String sql = "SELECT * FROM tabela_negociacao WHERE vendedor_id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Negociacao> negociacoes = new ArrayList<>(); // Cria uma lista de Negociacao
        Negociacao negociacao = new Negociacao(); // Cria um objeto Negociacao vazio
        Cliente cliente = new Cliente(); // Cria um objeto Cliente vazio
        Vendedor vendedor = new Vendedor(); // Cria um objeto Vendedor vazio
        Veiculo veiculo = new Veiculo(); // Cria um objeto Veiculo vazio
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, vendedorId.toString());
            rs = st.executeQuery();
            while(rs.next()) {
                negociacao.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Negociacao
                negociacao.setData(rs.getObject("data", LocalDateTime.class)); // Define a data
                cliente.setId(UUID.fromString(rs.getString("cliente_id"))); // Define o id do Cliente
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id"))); // Define o id do Vendedor
                veiculo.setId(UUID.fromString(rs.getString("veiculo_id"))); // Define o id do Veiculo
                negociacao.setCliente(cliente); // Define o Cliente da Negociacao
                negociacao.setVendedor(vendedor); // Define o Vendedor da Negociacao
                negociacao.setVeiculo(veiculo); // Define o Veiculo da Negociacao
                negociacoes.add(negociacao);
            }
            return negociacoes;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

}
