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
import dao.interfaces.AgendamentoDao;
import entidades.Agendamento;
import entidades.Cliente;
import entidades.Vendedor;

public class AgendamentoDaoImpl implements AgendamentoDao {

    // Conexao do Banco de Dados
    private Connection connection = null;

    // Recebe a conexao do Banco de Dados via construtor
    public AgendamentoDaoImpl(Connection connection) {
        this.connection = connection; // Define a conexao do banco no atributo da classe
    }

    @Override
    public void cadastro(Agendamento agendamento) {
        String sql = "INSERT INTO tabela_agendamento (id, agendado_em, agendado_para, cliente_id, vendedor_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement st = null;
        
        agendamento.setId(UUID.randomUUID()); // Gera um UUID automaticamente se necessário

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, agendamento.getId().toString());
            st.setObject(2, agendamento.getAgendadoEm());
            st.setObject(3, agendamento.getAgendadoPara());
            st.setString(4, agendamento.getCliente().getId().toString());
            st.setString(5, agendamento.getVendedor().getId().toString());
            
            int linhasAlteradas = st.executeUpdate();

            if (linhasAlteradas == 0) {
                throw new BancoException("Não foi possível cadastrar o agendamento");
            }
        } catch (SQLException e) {
            throw new BancoException("Erro ao cadastrar agendamento: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public Agendamento buscarAgendamentoPorId(UUID id) {
        String sql = "SELECT * FROM tabela_agendamento WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();

            if (rs.next()) {
                Agendamento agendamento = new Agendamento();
                agendamento.setId(UUID.fromString(rs.getString("id")));
                agendamento.setAgendadoEm(rs.getObject("agendado_em", LocalDateTime.class));
                agendamento.setAgendadoPara(rs.getObject("agendado_para", LocalDateTime.class));

                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(rs.getString("cliente_id")));
                agendamento.setCliente(cliente);

                Vendedor vendedor = new Vendedor();
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id")));
                agendamento.setVendedor(vendedor);

                return agendamento;
            }
            return null;
        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar agendamento por ID: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Agendamento> buscarAgendamentos() {
        String sql = "SELECT * FROM tabela_agendamento";
        Statement st = null;
        ResultSet rs = null;
        List<Agendamento> agendamentos = new ArrayList<>();

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Agendamento agendamento = new Agendamento();
                agendamento.setId(UUID.fromString(rs.getString("id")));
                agendamento.setAgendadoEm(rs.getObject("agendado_em", LocalDateTime.class));
                agendamento.setAgendadoPara(rs.getObject("agendado_para", LocalDateTime.class));

                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(rs.getString("cliente_id")));
                agendamento.setCliente(cliente);

                Vendedor vendedor = new Vendedor();
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id")));
                agendamento.setVendedor(vendedor);

                agendamentos.add(agendamento);
            }
            return agendamentos;
        } catch (SQLException e) {
            throw new BancoException("Erro ao listar agendamentos: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Agendamento> buscarAgendamentosPorCliente(UUID clienteId) {
        String sql = "SELECT * FROM tabela_agendamento WHERE cliente_id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Agendamento> agendamentos = new ArrayList<>();

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, clienteId.toString());
            rs = st.executeQuery(); // Usando executeQuery() sem passar a String sql novamente

            while (rs.next()) {
                Agendamento agendamento = new Agendamento();
                agendamento.setId(UUID.fromString(rs.getString("id")));
                agendamento.setAgendadoEm(rs.getObject("agendado_em", LocalDateTime.class));
                agendamento.setAgendadoPara(rs.getObject("agendado_para", LocalDateTime.class));

                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(rs.getString("cliente_id")));
                agendamento.setCliente(cliente);

                Vendedor vendedor = new Vendedor();
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id")));
                agendamento.setVendedor(vendedor);

                agendamentos.add(agendamento);
            }
            return agendamentos;
        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar agendamentos por cliente: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Agendamento> buscarAgendamentosPorVendedor(UUID vendedorId) {
        String sql = "SELECT * FROM tabela_agendamento WHERE vendedor_id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Agendamento> agendamentos = new ArrayList<>();

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, vendedorId.toString());
            rs = st.executeQuery(); // Usando executeQuery() sem passar a String sql novamente

            while (rs.next()) {
                Agendamento agendamento = new Agendamento();
                agendamento.setId(UUID.fromString(rs.getString("id")));
                agendamento.setAgendadoEm(rs.getObject("agendado_em", LocalDateTime.class));
                agendamento.setAgendadoPara(rs.getObject("agendado_para", LocalDateTime.class));

                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(rs.getString("cliente_id")));
                agendamento.setCliente(cliente);

                Vendedor vendedor = new Vendedor();
                vendedor.setId(UUID.fromString(rs.getString("vendedor_id")));
                agendamento.setVendedor(vendedor);

                agendamentos.add(agendamento);
            }
            return agendamentos;
        } catch (SQLException e) {
            throw new BancoException("Erro ao buscar agendamentos por vendedor: " + e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void atualizar(Agendamento agendamento) {
        if (agendamento.getId() == null) {
            throw new BancoException("O id do agendamento não pode ser nulo para atualização");
        }
        
        String sql = "UPDATE tabela_agendamento SET agendado_para = ?, cliente_id = ?, vendedor_id = ? WHERE id = ?";
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(sql);
            st.setObject(1, agendamento.getAgendadoPara());
            st.setString(2, agendamento.getCliente().getId().toString());
            st.setString(3, agendamento.getVendedor().getId().toString());
            st.setString(4, agendamento.getId().toString());
            
            int linhasAlteradas = st.executeUpdate();

            if (linhasAlteradas == 0) {
                throw new BancoException("O agendamento não foi encontrado para atualização");
            }
        } catch (SQLException e) {
            throw new BancoException("Erro ao atualizar agendamento: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public void excluir(Agendamento agendamento) {
        String sql = "DELETE FROM tabela_agendamento WHERE id = ?";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, agendamento.getId().toString());
            int linhasAlteradas = st.executeUpdate();
            
            if (linhasAlteradas == 0) {
                throw new BancoException("O agendamento não pôde ser excluído");
            }
        } catch (SQLException e) {
            throw new BancoException("Erro ao excluir agendamento: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }
}