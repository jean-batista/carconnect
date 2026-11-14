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

import entidades.Vendedor;
import servicos.VendedorServico;
import telas.autenticacao.TelaFormularioPessoa;

/**
 * Tela 2.3: Gestão de Vendedores (Exclusivo Gestor).
 */
public class TelaGestaoVendedores extends JPanel {

    private final VendedorServico vendedorServico;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaGestaoVendedores() {
        this.vendedorServico = new VendedorServico();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        JPanel painelTopo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestão de Vendedores");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("+ Novo Vendedor");
        btnNovo.setBackground(new Color(39, 174, 96));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.addActionListener(e -> abrirFormulario(null));

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarSelecionado());

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(192, 57, 43));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(e -> excluirSelecionado());

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> carregarTabela());

        painelAcoes.add(btnNovo);
        painelAcoes.add(btnEditar);
        painelAcoes.add(btnExcluir);
        painelAcoes.add(btnAtualizar);

        painelTopo.add(painelAcoes, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Sobrenome", "CPF", "E-mail", "Telefone", "Agendamentos", "Vendas"};
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
            List<Vendedor> vendedores = vendedorServico.buscarTodos();
            for (Vendedor v : vendedores) {
                int qtdAgend = (v.getAgendamentos() != null) ? v.getAgendamentos().size() : 0;
                int qtdVendas = (v.getVendas() != null) ? v.getVendas().size() : 0;
                modeloTabela.addRow(new Object[]{
                    v.getId(), v.getNome(), v.getSobrenome(), v.getCpf(), v.getEmail(), v.getTelefone(), qtdAgend, qtdVendas
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar vendedores: " + ex.getMessage());
        }
    }

    private void abrirFormulario(Vendedor vendedor) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        new TelaFormularioPessoa(parent, "VENDEDOR", vendedor, this::carregarTabela).setVisible(true);
    }

    private void editarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um vendedor na tabela.");
            return;
        }
        UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
        Vendedor vendedor = vendedorServico.buscarPorId(id);
        abrirFormulario(vendedor);
    }

    private void excluirSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um vendedor na tabela.");
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este vendedor?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
                Vendedor v = new Vendedor();
                v.setId(id);
                vendedorServico.excluir(v);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
            }
        }
    }
}