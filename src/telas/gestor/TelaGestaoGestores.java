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

import entidades.Gestor;
import servicos.GestorServico;
import telas.autenticacao.TelaFormularioPessoa;

/**
 * Tela 2.4: Gestão de Gestores (Exclusivo Gestor).
 */
public class TelaGestaoGestores extends JPanel {

    private final GestorServico gestorServico;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaGestaoGestores() {
        this.gestorServico = new GestorServico();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        JPanel painelTopo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestão de Gestores");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("+ Novo Gestor");
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

        String[] colunas = {"ID", "Nome", "Sobrenome", "CPF", "E-mail", "Telefone", "Veículos Gerenciados"};
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
            List<Gestor> gestores = gestorServico.buscarTodos();
            for (Gestor g : gestores) {
                int qtdVeiculos = (g.getVeiculos() != null) ? g.getVeiculos().size() : 0;
                modeloTabela.addRow(new Object[]{
                    g.getId(), g.getNome(), g.getSobrenome(), g.getCpf(), g.getEmail(), g.getTelefone(), qtdVeiculos
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar gestores: " + ex.getMessage());
        }
    }

    private void abrirFormulario(Gestor gestor) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        new TelaFormularioPessoa(parent, "GESTOR", gestor, this::carregarTabela).setVisible(true);
    }

    private void editarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um gestor na tabela.");
            return;
        }
        UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
        Gestor gestor = gestorServico.buscarPorId(id);
        abrirFormulario(gestor);
    }

    private void excluirSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um gestor na tabela.");
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este gestor?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
                Gestor g = new Gestor();
                g.setId(id);
                gestorServico.excluir(g);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
            }
        }
    }
}