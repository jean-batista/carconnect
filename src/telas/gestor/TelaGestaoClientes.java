package telas.gestor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import entidades.Cliente;
import servicos.ClienteServico;
import telas.autenticacao.TelaFormularioPessoa;

/**
 * Tela 2.2: Listagem e Gestão de Clientes (CRUD + visualização de histórico).
 */
public class TelaGestaoClientes extends JPanel {

    private final ClienteServico clienteServico;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaGestaoClientes() {
        this.clienteServico = new ClienteServico();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        // Topo
        JPanel painelTopo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestão de Clientes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("+ Novo Cliente");
        btnNovo.setBackground(new Color(39, 174, 96));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.addActionListener(e -> abrirFormulario(null));

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarSelecionado());

        JButton btnHistorico = new JButton("Ver Histórico");
        btnHistorico.addActionListener(e -> verHistoricoSelecionado());

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(192, 57, 43));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(e -> excluirSelecionado());

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> carregarTabela());

        painelAcoes.add(btnNovo);
        painelAcoes.add(btnEditar);
        painelAcoes.add(btnHistorico);
        painelAcoes.add(btnExcluir);
        painelAcoes.add(btnAtualizar);

        painelTopo.add(painelAcoes, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID", "Nome", "Sobrenome", "CPF", "E-mail", "Telefone", "Visitas", "Compras"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Cliente> clientes = clienteServico.buscarTodos();
            for (Cliente c : clientes) {
                int qtdVisitas = (c.getVisitas() != null) ? c.getVisitas().size() : 0;
                int qtdCompras = (c.getCompras() != null) ? c.getCompras().size() : 0;
                modeloTabela.addRow(new Object[]{
                    c.getId(), c.getNome(), c.getSobrenome(), c.getCpf(), c.getEmail(), c.getTelefone(), qtdVisitas, qtdCompras
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar clientes: " + ex.getMessage());
        }
    }

    private void abrirFormulario(Cliente cliente) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        new TelaFormularioPessoa(parent, "CLIENTE", cliente, this::carregarTabela).setVisible(true);
    }

    private void editarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela.");
            return;
        }
        UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
        Cliente cliente = clienteServico.buscarPorId(id);
        abrirFormulario(cliente);
    }

    private void verHistoricoSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela.");
            return;
        }
        UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
        Cliente c = clienteServico.buscarPorId(id);
        int visitas = (c.getVisitas() != null) ? c.getVisitas().size() : 0;
        int compras = (c.getCompras() != null) ? c.getCompras().size() : 0;

        String msg = String.format("Cliente: %s %s\nCPF: %s\nTotal de Visitas Agendadas: %d\nTotal de Compras Realizadas: %d",
                c.getNome(), c.getSobrenome(), c.getCpf(), visitas, compras);
        JOptionPane.showMessageDialog(this, msg, "Histórico do Cliente", JOptionPane.INFORMATION_MESSAGE);
    }

    private void excluirSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela.");
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
                Cliente c = new Cliente();
                c.setId(id);
                clienteServico.excluir(c);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
            }
        }
    }
}