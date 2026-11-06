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
import dao.interfaces.VendedorDao;
import entidades.Cliente;
import entidades.Negociacao;
import entidades.Vendedor;

public class VendedorDaoImpl implements VendedorDao {

    // Conexao do Banco de Dados
    private Connection connection = null;

    // Recebe a conexao do Banco de Dados via construtor
    public VendedorDaoImpl(Connection connection) {
        this.connection = connection; // Define a conexao do banco no atributo da classe
    }

    @Override
    public void cadastro(Vendedor vendedor) {
        String sql = "INSERT INTO tabela_vendedor (id) VALUES (?)";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, vendedor.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            // Ocorre um erro caso nao houver linhas alteradas
            if(linhasAlteradas == 0) throw new BancoException("Não foi possível cadastrar o vendedor");
        } catch (Exception e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    // TODO implementar metodo caso tiver solucao
    @Override
    public void efetuarVenda(Cliente cliente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'efetuarVenda'");
    }

    // TODO implementar metodo caso tiver solucao
    @Override
    public List<Negociacao> historicoDeVendas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'historicoDeVendas'");
    }

    @Override
    public Vendedor buscarPorId(UUID id) {
        String sql = "SELECT * FROM tabela_vendedor WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        Vendedor vendedor = new Vendedor(); // Cria um objeto Vendedor vazio
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();
            while(rs.next()) {
                vendedor.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Venedor
            }
            return vendedor;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Vendedor> listarVendedoresCadastrados() {
        String sql = "SELECT * FROM tabela_vendedor";
        Statement st = null;
        ResultSet rs = null;
        Vendedor vendedor = new Vendedor(); // Cria um objeto Cliente vazio
        List<Vendedor> vendedores = new ArrayList<>(); // Cria uma lista de vendedores vazia
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                vendedor.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Pessoa
                vendedores.add(vendedor);
            }
            return vendedores;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    // TODO implementar metodo caso tiver solucao
    @Override
    public void atualizarCadastro(Vendedor vendedor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarCadastro'");
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
