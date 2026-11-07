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
            // Gera obrigatoriamente um novo UUID para a entidade Veiculo no cadastro
            veiculo.setId(UUID.randomUUID());

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
            
            if (linhasAlteradas == 0) {
                throw new BancoException("Não foi possível cadastrar o veículo");
            }
        } catch (SQLException e) {
            throw new BancoException("Erro ao cadastrar veículo: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public Veiculo buscarVeiculoPeloId(UUID id) {
        String sql = "SELECT * FROM tabela_veiculo WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();
            
            if (rs.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setId(UUID.fromString(rs.getString("id")));
                veiculo.setMarca(rs.getString("marca"));
                veiculo.setModelo(rs.getString("modelo"));
                veiculo.setAno(rs.getInt("ano"));
                veiculo.setCombustivel(rs.getString("combustivel"));
                veiculo.setCondicao(rs.getString("condicao"));
                veiculo.setPreco(rs.getDouble("preco"));
                veiculo.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                return veiculo;
            }
            return null;
        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar veículo por ID: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Veiculo> buscarVeiculosPorGestorId(UUID gestorId) {
        String sql = "SELECT v.id, v.marca, v.modelo, v.ano, v.combustivel, v.condicao, v.preco, v.quantidade_estoque " +
                     "FROM tabela_veiculo v " +
                     "INNER JOIN gestor_veiculo gv ON v.id = gv.veiculo_id " +
                     "WHERE gv.gestor_id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Veiculo> veiculos = new ArrayList<>();

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, gestorId.toString());
            rs = st.executeQuery();

            while (rs.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setId(UUID.fromString(rs.getString("id")));
                veiculo.setMarca(rs.getString("marca"));
                veiculo.setModelo(rs.getString("modelo"));
                veiculo.setAno(rs.getInt("ano"));
                veiculo.setCombustivel(rs.getString("combustivel"));
                veiculo.setCondicao(rs.getString("condicao"));
                veiculo.setPreco(rs.getDouble("preco"));
                veiculo.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));

                veiculos.add(veiculo);
            }
            return veiculos;
        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar veículos por ID de gestor: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Veiculo> buscarVeiculos() {
        String sql = "SELECT * FROM tabela_veiculo";
        Statement st = null;
        ResultSet rs = null;
        List<Veiculo> veiculos = new ArrayList<>();
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setId(UUID.fromString(rs.getString("id")));
                veiculo.setMarca(rs.getString("marca"));
                veiculo.setModelo(rs.getString("modelo"));
                veiculo.setAno(rs.getInt("ano"));
                veiculo.setCombustivel(rs.getString("combustivel"));
                veiculo.setCondicao(rs.getString("condicao"));
                veiculo.setPreco(rs.getDouble("preco"));
                veiculo.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                
                veiculos.add(veiculo);
            }
            return veiculos;
        } catch (SQLException e) {
            throw new BancoException("Erro ao listar veículos: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void atualizar(Veiculo veiculo) {
        if (veiculo.getId() == null) {
            throw new BancoException("O id do veículo não pode ser nulo para atualização");
        }
        
        String sql = "UPDATE tabela_veiculo SET marca = ?, modelo = ?, ano = ?, combustivel = ?, condicao = ?, preco = ?, quantidade_estoque = ? WHERE id = ?";
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, veiculo.getMarca());
            st.setString(2, veiculo.getModelo());
            st.setInt(3, veiculo.getAno());
            st.setString(4, veiculo.getCombustivel());
            st.setString(5, veiculo.getCondicao());
            st.setDouble(6, veiculo.getPreco());
            st.setInt(7, veiculo.getQuantidadeEstoque());
            st.setString(8, veiculo.getId().toString());

            int linhasAlteradas = st.executeUpdate();

            if (linhasAlteradas == 0) {
                throw new BancoException("O veículo não foi encontrado para atualização");
            }
        } catch (SQLException e) {
            throw new BancoException("Erro ao atualizar veículo: " + e.getMessage());
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
            
            if (linhasAlteradas == 0) {
                throw new BancoException("O veículo não pôde ser excluído");
            }
        } catch (SQLException e) {
            throw new BancoException("Erro ao excluir veículo: " + e.getMessage());
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
            st.setInt(1, unidades);
            st.setString(2, veiculo.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            if (linhasAlteradas == 0) {
                throw new BancoException("Não foi possível alterar a quantidade no estoque");
            }
            veiculo.setQuantidadeEstoque(unidades);
        } catch (SQLException e) {
            throw new BancoException("Erro ao atualizar estoque: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }
}