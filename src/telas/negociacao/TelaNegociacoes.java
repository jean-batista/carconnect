package telas.negociacao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import entidades.Cliente;
import entidades.Negociacao;
import entidades.Veiculo;
import entidades.Vendedor;
import servicos.ClienteServico;
import servicos.NegociacaoServico;
import servicos.VeiculoServico;
import servicos.VendedorServico;
import autenticacao.SessaoUsuario;
import telas.utils.UiUtils;
import telas.utils.UiUtils.ItemCombo;

/**
 * Registro e Histórico de Negociações (Vendas com baixa automática de estoque).
 */
public class TelaNegociacoes extends JPanel {

    private final NegociacaoServico negociacaoServico = new NegociacaoServico();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaNegociacoes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        JPanel topo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel(SessaoUsuario.isCliente() ? "Minhas Compras" : "Negociações e Vendas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        topo.add(lblTitulo, BorderLayout.WEST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (SessaoUsuario.isVendedor() || SessaoUsuario.isGestor()) {
            acoes.add(UiUtils.criarBotao("+ Registrar Venda", UiUtils.COR_SUCESSO, this::abrirDialogNovaVenda));
        }
        acoes.add(UiUtils.criarBotao("Atualizar", null, this::carregarTabela));
        topo.add(acoes, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        modelo = UiUtils.criarModeloTabela("ID", "Data", "Cliente", "Vendedor", "Veículo", "Valor (R$)");
        tabela = UiUtils.criarTabela(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        try {
            UUID userId = SessaoUsuario.getUsuarioLogado().getId();
            List<Negociacao> lista = SessaoUsuario.isCliente() ? negociacaoServico.buscarPorClienteId(userId)
                    : SessaoUsuario.isVendedor() ? negociacaoServico.buscarPorVendedorId(userId)
                    : negociacaoServico.buscarTodos();

            for (Negociacao n : lista) {
                String cli = (n.getCliente() != null) ? n.getCliente().getNome() + " " + n.getCliente().getSobrenome() : "-";
                String vend = (n.getVendedor() != null) ? n.getVendedor().getNome() + " " + n.getVendedor().getSobrenome() : "-";
                String veic = (n.getVeiculo() != null) ? n.getVeiculo().getMarca() + " " + n.getVeiculo().getModelo() : "-";
                String valor = (n.getVeiculo() != null && n.getVeiculo().getPreco() != null) ? String.format("%,.2f", n.getVeiculo().getPreco()) : "-";

                modelo.addRow(new Object[]{n.getId(), n.getData() != null ? n.getData().format(fmt) : "-", cli, vend, veic, valor});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar negociações: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void abrirDialogNovaVenda() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Registrar Nova Venda");
        dialog.setModal(true);
        dialog.setSize(440, 270);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 14));
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

        JComboBox<ItemCombo<Cliente>> cbCliente = new JComboBox<>();
        for (Cliente c : new ClienteServico().buscarTodos()) {
            cbCliente.addItem(new ItemCombo<>(c, c.getNome() + " " + c.getSobrenome() + " (" + c.getCpf() + ")"));
        }

        JComboBox<ItemCombo<Vendedor>> cbVendedor = new JComboBox<>();
        if (SessaoUsuario.isVendedor()) {
            Vendedor logado = (Vendedor) SessaoUsuario.getUsuarioLogado();
            cbVendedor.addItem(new ItemCombo<>(logado, logado.getNome() + " " + logado.getSobrenome()));
            cbVendedor.setEnabled(false);
        } else {
            for (Vendedor v : new VendedorServico().buscarTodos()) {
                cbVendedor.addItem(new ItemCombo<>(v, v.getNome() + " " + v.getSobrenome()));
            }
        }

        JComboBox<ItemCombo<Veiculo>> cbVeiculo = new JComboBox<>();
        for (Veiculo v : new VeiculoServico().buscarTodos()) {
            if (v.getQuantidadeEstoque() != null && v.getQuantidadeEstoque() > 0) {
                cbVeiculo.addItem(new ItemCombo<>(v, v.getMarca() + " " + v.getModelo() + " - R$ " + String.format("%,.2f", v.getPreco()) + " [Est: " + v.getQuantidadeEstoque() + "]"));
            }
        }

        form.add(new JLabel("Cliente:"));
        form.add(cbCliente);
        form.add(new JLabel("Vendedor:"));
        form.add(cbVendedor);
        form.add(new JLabel("Veículo:"));
        form.add(cbVeiculo);
        dialog.add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        botoes.add(UiUtils.criarBotao("Fechar Venda", UiUtils.COR_SUCESSO, () -> {
            try {
                ItemCombo<Cliente> c = (ItemCombo<Cliente>) cbCliente.getSelectedItem();
                ItemCombo<Vendedor> v = (ItemCombo<Vendedor>) cbVendedor.getSelectedItem();
                ItemCombo<Veiculo> veic = (ItemCombo<Veiculo>) cbVeiculo.getSelectedItem();
                if (c == null || v == null || veic == null) {
                    JOptionPane.showMessageDialog(dialog, "Preencha todos os campos.");
                    return;
                }

                Negociacao n = new Negociacao();
                n.setCliente(c.getValor());
                n.setVendedor(v.getValor());
                n.setVeiculo(veic.getValor());
                negociacaoServico.cadastrar(n);

                JOptionPane.showMessageDialog(dialog, "Venda registrada e estoque atualizado!");
                carregarTabela();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage());
            }
        }));
        botoes.add(UiUtils.criarBotao("Cancelar", null, dialog::dispose));
        dialog.add(botoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}