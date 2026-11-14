package telas.veiculo;

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

import autenticacao.SessaoUsuario;
import entidades.Veiculo;
import servicos.VeiculoServico;

/**
 * Tela 3.1: Catálogo e Gestão de Estoque de Veículos.
 */
public class TelaCatalogoVeiculos extends JPanel {

    private final VeiculoServico veiculoServico;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaCatalogoVeiculos() {
        this.veiculoServico = new VeiculoServico();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        JPanel painelTopo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Catálogo de Veículos");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Botões exclusivos de Gestor
        if (SessaoUsuario.isGestor()) {
            JButton btnNovo = new JButton("+ Novo Veículo");
            btnNovo.setBackground(new Color(39, 174, 96));
            btnNovo.setForeground(Color.WHITE);
            btnNovo.addActionListener(e -> abrirFormulario(null));

            JButton btnEditar = new JButton("Editar");
            btnEditar.addActionListener(e -> editarSelecionado());

            JButton btnEstoque = new JButton("Ajustar Estoque");
            btnEstoque.addActionListener(e -> ajustarEstoqueSelecionado());

            JButton btnExcluir = new JButton("Excluir");
            btnExcluir.setBackground(new Color(192, 57, 43));
            btnExcluir.setForeground(Color.WHITE);
            btnExcluir.addActionListener(e -> excluirSelecionado());

            painelAcoes.add(btnNovo);
            painelAcoes.add(btnEditar);
            painelAcoes.add(btnEstoque);
            painelAcoes.add(btnExcluir);
        }

        JButton btnAtualizar = new JButton("Atualizar Catálogo");
        btnAtualizar.addActionListener(e -> carregarTabela());
        painelAcoes.add(btnAtualizar);

        painelTopo.add(painelAcoes, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Marca", "Modelo", "Ano", "Combustível", "Condição", "Preço (R$)", "Estoque"};
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
            List<Veiculo> veiculos = veiculoServico.buscarTodos();
            for (Veiculo v : veiculos) {
                modeloTabela.addRow(new Object[]{
                    v.getId(),
                    v.getMarca(),
                    v.getModelo(),
                    v.getAno(),
                    v.getCombustivel(),
                    v.getCondicao(),
                    String.format("%,.2f", v.getPreco()),
                    v.getQuantidadeEstoque()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar veículos: " + ex.getMessage());
        }
    }

    private void abrirFormulario(Veiculo veiculo) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        new TelaFormularioVeiculo(parent, veiculo, this::carregarTabela).setVisible(true);
    }

    private void editarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um veículo na tabela.");
            return;
        }
        UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
        Veiculo v = veiculoServico.buscarPorId(id);
        abrirFormulario(v);
    }

    private void ajustarEstoqueSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um veículo na tabela.");
            return;
        }
        UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
        Veiculo v = veiculoServico.buscarPorId(id);

        String input = JOptionPane.showInputDialog(this, "Nova quantidade em estoque para " + v.getMarca() + " " + v.getModelo() + ":", v.getQuantidadeEstoque());
        if (input != null && !input.trim().isEmpty()) {
            try {
                int novasUnidades = Integer.parseInt(input.trim());
                veiculoServico.atualizarEstoque(v, novasUnidades);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar estoque: " + ex.getMessage());
            }
        }
    }

    private void excluirSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um veículo na tabela.");
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja excluir este veículo do catálogo?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
                Veiculo v = new Veiculo();
                v.setId(id);
                veiculoServico.excluir(v);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir veículo: " + ex.getMessage());
            }
        }
    }
}