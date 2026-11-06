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
import dao.interfaces.VeiculoDao;
import entidades.Veiculo;

public class VeiculoDaoImpl implements VeiculoDao {

    // Conexao do Banco de Dados
    private Connection connection = null;

    // Recebe a conexao do Banco de Dados via construtor
    public VeiculoDaoImpl(Connection connection) {
        this.connection = connection; // Define a conexao do banco no atributo da classe
    }

    @Override
    public void cadastro(Veiculo veiculo) {
        String sql = "INSERT INTO tabela_veiculo (id, marca, modelo, ano, combustivel, condicao, preco, quantidade_estoque) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = null;
        try {
            veiculo.setId(UUID.randomUUID()); // Gera um UUID aleatoriamente

            st = connection.prepareStatement(sql);
            st.setString(1, veiculo.getId().toString()); // Define o id
            st.setString(2, veiculo.getMarca()); // Define a marca
            st.setString(3, veiculo.getModelo()); // Define o modelo
            st.setInt(4, veiculo.getAno()); // Define o ano
            st.setString(5, veiculo.getCombustivel()); // Define o combustivel
            st.setString(6, veiculo.getCondicao()); // Define a condicao
            st.setDouble(7, veiculo.getPreco()); // Define o preco
            st.setInt(8, veiculo.getQuantidadeEstoque()); // Define a quantidade em estoque
            int linhasAlteradas = st.executeUpdate(); // Executa as mudancas
            
            // Ocorre um erro caso nao houver linhas alteradas
            if(linhasAlteradas == 0) throw new BancoException("Não foi possível cadastrar o veiculo");
        } catch (Exception e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            // Fecha a conexao com o banco de dados independentemente so `try` ou `catch`
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public Veiculo buscarVeiculoPeloId(UUID id) {
        String sql = "SELECT * FROM tabela_veiculo WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        Veiculo veiculo = new Veiculo(); // Cria um objeto Vendedor vazio
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();
            while(rs.next()) {
                veiculo.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Veiculo
                veiculo.setMarca(rs.getString("marca")); // Define a marca no objeto Veiculo
                veiculo.setModelo(rs.getString("modelo")); // Define o modelo no objeto Veiculo
                veiculo.setAno(rs.getInt("ano")); // Define o ano no objeto Veiculo
                veiculo.setCombustivel(rs.getString("combustivel")); // Define o combustivel no obejto Veiculo
                veiculo.setCondicao(rs.getString("condicao")); // Define a condicao no objeto Veiculo
                veiculo.setPreco(rs.getDouble("preco")); // Define o preco no objeto Veiculo
                veiculo.setQuantidadeEstoque(rs.getInt("quantidade_estoque")); // Define a quantidade em estoque no objeto Veiculo
            }
            return veiculo;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Veiculo> buscarVeiculos() {
        String sql = "SELECT * FROM tabela_veiculo WHERE id = ?";
        Statement st = null;
        ResultSet rs = null;
        List<Veiculo> veiculos = new ArrayList<>(); // Cria uma lista de veiculos vazia
        Veiculo veiculo = new Veiculo(); // Cria um objeto Vendedor vazio
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                veiculo.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Veiculo
                veiculo.setMarca(rs.getString("marca")); // Define a marca no objeto Veiculo
                veiculo.setModelo(rs.getString("modelo")); // Define o modelo no objeto Veiculo
                veiculo.setAno(rs.getInt("ano")); // Define o ano no objeto Veiculo
                veiculo.setCombustivel(rs.getString("combustivel")); // Define o combustivel no obejto Veiculo
                veiculo.setCondicao(rs.getString("condicao")); // Define a condicao no objeto Veiculo
                veiculo.setPreco(rs.getDouble("preco")); // Define o preco no objeto Veiculo
                veiculo.setQuantidadeEstoque(rs.getInt("quantidade_estoque")); // Define a quantidade em estoque no objeto Veiculo
                veiculos.add(veiculo);
            }
            return veiculos;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void atualizar(Veiculo veiculo) {
        if(veiculo.getId() == null) throw new BancoException("O id do veículo não pode ser nulo");
        String sql = "UPDATE tabela_veiculo SET marca = ?, modelo = ?, ano = ?, combustivel = ?, condicao = ?, preco = ? WHERE id = ?";
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(sql);
            if(veiculo.getMarca() != null) st.setString(1, veiculo.getMarca());
            if(veiculo.getModelo() != null) st.setString(2, veiculo.getModelo());
            if(veiculo.getAno() != null) st.setInt(3, veiculo.getAno());
            if(veiculo.getCombustivel() != null) st.setString(4, veiculo.getCombustivel());
            if(veiculo.getCondicao() != null) st.setString(5, veiculo.getCondicao());
            if(veiculo.getPreco() != null) st.setDouble(6, veiculo.getPreco());
            st.setString(7, veiculo.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            if(linhasAlteradas == 0) throw new BancoException("O veículo não pode ser atualizado");
        } catch(SQLException e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void excluir(Veiculo veiculo) {
        String sql = "DELETE FROM tabela_veiculo WHERE id = ?";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, veiculo.getId().toString());
            int linhasAlteradas = st.executeUpdate();
            if(linhasAlteradas == 0) throw new BancoException("O veículo não pode ser excluído");
        } catch(SQLException e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void atualizarEstoque(Veiculo veiculo, Integer unidades) {
        String sql = "UPDATE tabela_veiculo SET quantidade_estoque = ? WHERE id = ?";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setInt(1, veiculo.getQuantidadeEstoque());
            st.setString(2, veiculo.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            if(linhasAlteradas == 0) throw new BancoException("Não foi possível alterar a quantidade no estoque");
        } catch(Exception e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }
    
}
