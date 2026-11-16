package telas.veiculo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import entidades.Veiculo;
import servicos.VeiculoServico;
import autenticacao.SessaoUsuario;
import telas.utils.UiUtils;

/**
 * Catálogo e Gestão de Estoque de Veículos.
 */
public class TelaCatalogoVeiculos extends JPanel {

    private final VeiculoServico veiculoServico = new VeiculoServico();
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaCatalogoVeiculos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        JPanel topo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Catálogo de Veículos");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        topo.add(lblTitulo, BorderLayout.WEST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (SessaoUsuario.isGestor()) {
            acoes.add(UiUtils.criarBotao("+ Novo Veículo", UiUtils.COR_SUCESSO, () -> abrirFormulario(null)));
            acoes.add(UiUtils.criarBotao("Editar", null, this::editarSelecionado));
            acoes.add(UiUtils.criarBotao("Ajustar Estoque", null, this::ajustarEstoqueSelecionado));
            acoes.add(UiUtils.criarBotao("Excluir", UiUtils.COR_PERIGO, this::excluirSelecionado));
        }
        acoes.add(UiUtils.criarBotao("Atualizar", null, this::carregarTabela));
        topo.add(acoes, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        modelo = UiUtils.criarModeloTabela("ID", "Marca", "Modelo", "Ano", "Combustível", "Condição", "Preço (R$)", "Estoque");
        tabela = UiUtils.criarTabela(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        try {
            for (Veiculo v : veiculoServico.buscarTodos()) {
                modelo.addRow(new Object[]{
                    v.getId(), v.getMarca(), v.getModelo(), v.getAno(),
                    v.getCombustivel(), v.getCondicao(), String.format("%,.2f", v.getPreco()), v.getQuantidadeEstoque()
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
        UUID id = getIdSelecionado();
        if (id != null) abrirFormulario(veiculoServico.buscarPorId(id));
    }

    private void ajustarEstoqueSelecionado() {
        UUID id = getIdSelecionado();
        if (id == null) return;
        Veiculo v = veiculoServico.buscarPorId(id);

        String input = JOptionPane.showInputDialog(this, "Novo estoque para " + v.getMarca() + " " + v.getModelo() + ":", v.getQuantidadeEstoque());
        if (input != null && !input.trim().isEmpty()) {
            try {
                veiculoServico.atualizarEstoque(v, Integer.parseInt(input.trim()));
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }

    private void excluirSelecionado() {
        UUID id = getIdSelecionado();
        if (id == null) return;
        if (JOptionPane.showConfirmDialog(this, "Excluir este veículo?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                Veiculo v = new Veiculo();
                v.setId(id);
                veiculoServico.excluir(v);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
            }
        }
    }

    private UUID getIdSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um veículo na tabela.");
            return null;
        }
        return (UUID) modelo.getValueAt(linha, 0);
    }
}