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
        
        // Gera um UUID automaticamente caso ainda nao tenha sido definido
        if (negociacao.getId() == null) {
            negociacao.setId(UUID.randomUUID());
        }

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, negociacao.getId().toString()); // Define o id
            st.setObject(2, negociacao.getData()); // Define a data
            st.setString(3, negociacao.getCliente().getId().toString()); // Define id do Cliente
            st.setString(4, negociacao.getVendedor().getId().toString()); // Define o id do Vendedor
            st.setString(5, negociacao.getVeiculo().getId().toString()); // Define o id do Veiculo
            
            int linhasAlteradas = st.executeUpdate(); // Executa as mudancas
            
            if (linhasAlteradas == 0) {
                throw new BancoException("Não foi possível cadastrar a negociação");
            }
        } catch (SQLException e) {
            throw new BancoException("Erro ao cadastrar negociação: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public Negociacao buscarNegociacaoPorId(UUID id) {
        String sql = "SELECT * FROM tabela_negociacao WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();

            if (rs.next()) {
                Negociacao negociacao = new Negociacao();
                negociacao.setId(UUID.fromString(rs.getString("id")));
                negociacao.setData(rs.getObject("data", LocalDateTime.class));

                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(rs.getString("cliente_id")));
                negociacao.setCliente(cliente);

                Vendedor vendedor = new Vendedor();
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id")));
                negociacao.setVendedor(vendedor);

                Veiculo veiculo = new Veiculo();
                veiculo.setId(UUID.fromString(rs.getString("veiculo_id")));
                negociacao.setVeiculo(veiculo);

                return negociacao;
            }
            return null;
        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar negociação por ID: " + e.getMessage());
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
        List<Negociacao> negociacoes = new ArrayList<>();

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Negociacao negociacao = new Negociacao();
                negociacao.setId(UUID.fromString(rs.getString("id")));
                negociacao.setData(rs.getObject("data", LocalDateTime.class));

                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(rs.getString("cliente_id")));
                negociacao.setCliente(cliente);

                Vendedor vendedor = new Vendedor();
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id")));
                negociacao.setVendedor(vendedor);

                Veiculo veiculo = new Veiculo();
                veiculo.setId(UUID.fromString(rs.getString("veiculo_id")));
                negociacao.setVeiculo(veiculo);

                negociacoes.add(negociacao);
            }
            return negociacoes;
        } catch (SQLException e) {
            throw new BancoException("Erro ao listar negociações: " + e.getMessage());
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
        List<Negociacao> negociacoes = new ArrayList<>();

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, clienteId.toString());
            rs = st.executeQuery();

            while (rs.next()) {
                Negociacao negociacao = new Negociacao();
                negociacao.setId(UUID.fromString(rs.getString("id")));
                negociacao.setData(rs.getObject("data", LocalDateTime.class));

                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(rs.getString("cliente_id")));
                negociacao.setCliente(cliente);

                Vendedor vendedor = new Vendedor();
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id")));
                negociacao.setVendedor(vendedor);

                Veiculo veiculo = new Veiculo();
                veiculo.setId(UUID.fromString(rs.getString("veiculo_id")));
                negociacao.setVeiculo(veiculo);

                negociacoes.add(negociacao);
            }
            return negociacoes;
        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar negociações por cliente: " + e.getMessage());
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
        List<Negociacao> negociacoes = new ArrayList<>();

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, vendedorId.toString());
            rs = st.executeQuery();

            while (rs.next()) {
                Negociacao negociacao = new Negociacao();
                negociacao.setId(UUID.fromString(rs.getString("id")));
                negociacao.setData(rs.getObject("data", LocalDateTime.class));

                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(rs.getString("cliente_id")));
                negociacao.setCliente(cliente);

                Vendedor vendedor = new Vendedor();
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id")));
                negociacao.setVendedor(vendedor);

                Veiculo veiculo = new Veiculo();
                veiculo.setId(UUID.fromString(rs.getString("veiculo_id")));
                negociacao.setVeiculo(veiculo);

                negociacoes.add(negociacao);
            }
            return negociacoes;
        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar negociações por vendedor: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }
}