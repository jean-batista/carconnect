package telas.negociacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import autenticacao.SessaoUsuario;
import entidades.Cliente;
import entidades.Negociacao;
import entidades.Veiculo;
import entidades.Vendedor;
import servicos.ClienteServico;
import servicos.NegociacaoServico;
import servicos.VeiculoServico;
import servicos.VendedorServico;

/**
 * Tela 4.3: Registro e Histórico de Negociações (Vendas com baixa automática de estoque).
 */
public class TelaNegociacoes extends JPanel {

    private final NegociacaoServico negociacaoServico;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TelaNegociacoes() {
        this.negociacaoServico = new NegociacaoServico();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        JPanel painelTopo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel(SessaoUsuario.isCliente() ? "Minhas Compras" : "Registro e Histórico de Negociações");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Apenas Vendedores e Gestores registram novas vendas no sistema
        if (SessaoUsuario.isVendedor() || SessaoUsuario.isGestor()) {
            JButton btnNovaVenda = new JButton("+ Registrar Nova Venda");
            btnNovaVenda.setBackground(new Color(39, 174, 96));
            btnNovaVenda.setForeground(Color.WHITE);
            btnNovaVenda.addActionListener(e -> abrirDialogNovaVenda());
            painelAcoes.add(btnNovaVenda);
        }

        JButton btnAtualizar = new JButton("Atualizar Histórico");
        btnAtualizar.addActionListener(e -> carregarTabela());
        painelAcoes.add(btnAtualizar);

        painelTopo.add(painelAcoes, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        String[] colunas = {"ID Negociação", "Data", "Cliente", "Vendedor", "Veículo", "Valor (R$)"};
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
            List<Negociacao> negociacoes;
            UUID usuarioId = SessaoUsuario.getUsuarioLogado().getId();

            if (SessaoUsuario.isCliente()) {
                negociacoes = negociacaoServico.buscarPorClienteId(usuarioId);
            } else if (SessaoUsuario.isVendedor()) {
                negociacoes = negociacaoServico.buscarPorVendedorId(usuarioId);
            } else {
                negociacoes = negociacaoServico.buscarTodos();
            }

            for (Negociacao n : negociacoes) {
                String nomeCliente = (n.getCliente() != null) ? n.getCliente().getNome() + " " + n.getCliente().getSobrenome() : "-";
                String nomeVendedor = (n.getVendedor() != null) ? n.getVendedor().getNome() + " " + n.getVendedor().getSobrenome() : "-";
                String descVeiculo = (n.getVeiculo() != null) ? n.getVeiculo().getMarca() + " " + n.getVeiculo().getModelo() + " (" + n.getVeiculo().getAno() + ")" : "-";
                String valor = (n.getVeiculo() != null && n.getVeiculo().getPreco() != null) ? String.format("%,.2f", n.getVeiculo().getPreco()) : "-";

                modeloTabela.addRow(new Object[]{
                    n.getId(),
                    n.getData() != null ? n.getData().format(formatter) : "-",
                    nomeCliente,
                    nomeVendedor,
                    descVeiculo,
                    valor
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar negociações: " + ex.getMessage());
        }
    }

    private void abrirDialogNovaVenda() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Registrar Nova Negociação");
        dialog.setModal(true);
        dialog.setSize(450, 290);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel painelForm = new JPanel(new GridLayout(3, 2, 10, 15));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JComboBox<ItemCombo<Cliente>> cbCliente = new JComboBox<>();
        for (Cliente c : new ClienteServico().buscarTodos()) {
            cbCliente.addItem(new ItemCombo<>(c, c.getNome() + " " + c.getSobrenome() + " - CPF: " + c.getCpf()));
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
                cbVeiculo.addItem(new ItemCombo<>(v, v.getMarca() + " " + v.getModelo() + " (" + v.getAno() + ") - R$ " + String.format("%,.2f", v.getPreco()) + " [Est: " + v.getQuantidadeEstoque() + "]"));
            }
        }

        painelForm.add(new JLabel("Cliente Comprador:"));
        painelForm.add(cbCliente);

        painelForm.add(new JLabel("Vendedor Responsável:"));
        painelForm.add(cbVendedor);

        painelForm.add(new JLabel("Veículo (Em Estoque):"));
        painelForm.add(cbVeiculo);

        dialog.add(painelForm, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JButton btnConfirmar = new JButton("Fechar Venda");
        btnConfirmar.setBackground(new Color(39, 174, 96));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.addActionListener(e -> {
            try {
                @SuppressWarnings("unchecked")
                ItemCombo<Cliente> itemCliente = (ItemCombo<Cliente>) cbCliente.getSelectedItem();
                @SuppressWarnings("unchecked")
                ItemCombo<Vendedor> itemVendedor = (ItemCombo<Vendedor>) cbVendedor.getSelectedItem();
                @SuppressWarnings("unchecked")
                ItemCombo<Veiculo> itemVeiculo = (ItemCombo<Veiculo>) cbVeiculo.getSelectedItem();

                if (itemCliente == null || itemVendedor == null || itemVeiculo == null) {
                    JOptionPane.showMessageDialog(dialog, "Preencha todos os campos para fechar a venda.");
                    return;
                }

                Negociacao novaNegociacao = new Negociacao();
                novaNegociacao.setCliente(itemCliente.getValor());
                novaNegociacao.setVendedor(itemVendedor.getValor());
                novaNegociacao.setVeiculo(itemVeiculo.getValor());

                // Chama o serviço que valida estoque e subtrai 1 unidade automaticamente
                negociacaoServico.cadastrar(novaNegociacao);

                JOptionPane.showMessageDialog(dialog, "Venda registrada com sucesso! O estoque do veículo foi atualizado.");
                carregarTabela();
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao registrar venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());

        painelBotoes.add(btnConfirmar);
        painelBotoes.add(btnCancelar);
        dialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static class ItemCombo<T> {
        private final T valor;
        private final String rotulo;

        public ItemCombo(T valor, String rotulo) {
            this.valor = valor;
            this.rotulo = rotulo;
        }

        public T getValor() {
            return valor;
        }

        @Override
        public String toString() {
            return rotulo;
        }
    }
}