package telas.gestor;

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

import entidades.Pessoa;
import servicos.PessoaServicoBase;
import telas.utils.UiUtils;
import telas.autenticacao.TelaFormularioPessoa;

/**
 * Tela genérica e reutilizável para Gestão de Clientes, Vendedores e Gestores.
 * Substitui 3 telas separadas eliminando duplicação de código.
 */
public class TelaGestaoPessoas<T extends Pessoa> extends JPanel {

    private final String tipoPerfil;
    private final String tituloPlural;
    private final PessoaServicoBase<T> servico;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaGestaoPessoas(String tipoPerfil, String tituloPlural, PessoaServicoBase<T> servico) {
        this.tipoPerfil = tipoPerfil;
        this.tituloPlural = tituloPlural;
        this.servico = servico;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        JPanel topo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestão de " + tituloPlural);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        topo.add(lblTitulo, BorderLayout.WEST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acoes.add(UiUtils.criarBotao("+ Novo", UiUtils.COR_SUCESSO, () -> abrirFormulario(null)));
        acoes.add(UiUtils.criarBotao("Editar", null, this::editarSelecionado));
        acoes.add(UiUtils.criarBotao("Excluir", UiUtils.COR_PERIGO, this::excluirSelecionado));
        acoes.add(UiUtils.criarBotao("Atualizar", null, this::carregarTabela));
        topo.add(acoes, BorderLayout.EAST);

        add(topo, BorderLayout.NORTH);

        modelo = UiUtils.criarModeloTabela("ID", "Nome", "Sobrenome", "CPF", "E-mail", "Telefone");
        tabela = UiUtils.criarTabela(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        try {
            for (T p : servico.buscarTodos()) {
                modelo.addRow(new Object[]{p.getId(), p.getNome(), p.getSobrenome(), p.getCpf(), p.getEmail(), p.getTelefone()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar " + tituloPlural.toLowerCase() + ": " + ex.getMessage());
        }
    }

    private void abrirFormulario(T pessoa) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        new TelaFormularioPessoa(parent, tipoPerfil, pessoa, this::carregarTabela).setVisible(true);
    }

    private void editarSelecionado() {
        UUID id = getIdSelecionado();
        if (id != null) abrirFormulario(servico.buscarPorId(id));
    }

    private void excluirSelecionado() {
        UUID id = getIdSelecionado();
        if (id == null) return;

        if (JOptionPane.showConfirmDialog(this, "Deseja excluir este registro?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                T entidade = servico.buscarPorId(id);
                if (entidade != null) {
                    servico.excluir(entidade);
                    carregarTabela();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
            }
        }
    }

    private UUID getIdSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um registro na tabela.");
            return null;
        }
        return (UUID) modelo.getValueAt(linha, 0);
    }
}