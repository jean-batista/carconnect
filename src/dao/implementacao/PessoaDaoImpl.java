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
import dao.interfaces.PessoaDao;
import entidades.Pessoa;

public class PessoaDaoImpl implements PessoaDao {

    // Conexao do Banco de Dados
    private Connection connection = null;

    // Recebe a conexao do Banco de Dados via construtor
    public PessoaDaoImpl(Connection connection) {
        this.connection = connection; // Define a conexao do banco no atributo da classe
    }

    @Override
    public Pessoa inserir(Pessoa pessoa) {
        String sql = "INSERT INTO tabela_pessoa (id, nome, sobrenome, email, telefone, cpf, senha) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = null;
        try {
            pessoa.setId(UUID.randomUUID()); // Gera um UUID aleatoriamente

            st = connection.prepareStatement(sql);
            st.setString(1, pessoa.getId().toString()); // Define o id
            st.setString(2, pessoa.getNome()); // Define o nome
            st.setString(3, pessoa.getSobrenome()); // Define o sobrenome
            st.setString(4, pessoa.getEmail()); // Define o email
            st.setString(5, pessoa.getTelefone()); // Define o telefone
            st.setString(6, pessoa.getCpf()); // Define o cpf
            st.setString(7, pessoa.getSenha()); // Define a senha
            int linhasAlteradas = st.executeUpdate(); // Executa as mudancas
            
            // Ocorre um erro caso nao houver linhas alteradas
            if(linhasAlteradas == 0) throw new BancoException("Não foi possível inserir o usuário");
            return pessoa;
        } catch (Exception e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            // Fecha a conexao com o banco de dados independentemente so `try` ou `catch`
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public Pessoa buscarPorId(UUID id) {
        String sql = "SELECT * FROM tabela_pessoa WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        Pessoa pessoa = new Pessoa(); // Cria um objeto Pessoa vazio
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();
            while(rs.next()) {
                pessoa.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Pessoa
                pessoa.setNome(rs.getString("nome")); // Define o nome no objeto Pessoa
                pessoa.setSobrenome(rs.getString("sobrenome")); // Define o sobrenome no objeto Pessoa
                pessoa.setEmail(rs.getString("email")); // Define o email no objeto Pessoa
                pessoa.setTelefone(rs.getString("telefone")); // Define o telefone no objeto Pessoa
                pessoa.setCpf(rs.getString("cpf")); // Define o cpf no objeto Pessoa
                pessoa.setSenha(rs.getString("senha")); // Define a senha no objeto Pessoa
            }
            return pessoa;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Pessoa> buscarPessoas() {
        String sql = "SELECT * FROM tabela_pessoa";
        Statement st = null;
        ResultSet rs = null;
        Pessoa pessoa = null; // Inicia uma variavel Pessoa
        List<Pessoa> pessoas = new ArrayList<>(); // Cria uma lista de pessoas vazia
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                pessoa = new Pessoa(); // Cria um novo objeto Pessoa vazio
                pessoa.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Pessoa
                pessoa.setNome(rs.getString("nome")); // Define o nome no objeto Pessoa
                pessoa.setSobrenome(rs.getString("sobrenome")); // Define o sobrenome no objeto Pessoa
                pessoa.setEmail(rs.getString("email")); // Define o email no objeto Pessoa
                pessoa.setTelefone(rs.getString("telefone")); // Define o telefone no objeto Pessoa
                pessoa.setCpf(rs.getString("cpf")); // Define o cpf no objeto Pessoa
                pessoa.setSenha(rs.getString("senha")); // Define a senha no objeto Pessoa
                pessoas.add(pessoa);
            }
            return pessoas;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void atualizar(Pessoa pessoa) {
        if(pessoa.getId() == null) throw new BancoException("O id do usuário não pode ser nulo");
        String sql = "UPDATE tabela_pessoa SET nome = ?, sobrenome = ?, email = ?, telefone = ?, cpf = ?, senha = ? WHERE id = ?";
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(sql);
            if(pessoa.getNome() != null) st.setString(1, pessoa.getNome());
            if(pessoa.getSobrenome() != null) st.setString(2, pessoa.getSobrenome());
            if(pessoa.getEmail() != null) st.setString(3, pessoa.getEmail());
            if(pessoa.getTelefone() != null) st.setString(4, pessoa.getTelefone());
            if(pessoa.getCpf() != null) st.setString(5, pessoa.getCpf());
            if(pessoa.getSenha() != null) st.setString(6, pessoa.getSenha());
            st.setString(7, pessoa.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            if(linhasAlteradas == 0) throw new BancoException("O usuário não pode ser atualizado");
        } catch(SQLException e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void excluir(Pessoa pessoa) {
        String sql = "DELETE FROM tabela_pessoa WHERE id = ?";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, pessoa.getId().toString());
            int linhasAlteradas = st.executeUpdate();
            if(linhasAlteradas == 0) throw new BancoException("O usuário não pode ser excluído");
        } catch(SQLException e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }
    
}
