package telas.agendamento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import autenticacao.SessaoUsuario;
import entidades.Agendamento;
import entidades.Cliente;
import entidades.Vendedor;
import servicos.AgendamentoServico;
import servicos.ClienteServico;
import servicos.VendedorServico;

/**
 * Tela 4.2: Modal para Criar ou Remarcar Agendamento.
 */
public class TelaFormularioAgendamento extends JDialog {

    private final AgendamentoServico agendamentoServico;
    private final ClienteServico clienteServico;
    private final VendedorServico vendedorServico;

    private final Agendamento agendamentoEdicao;
    private final Runnable onSuccess;

    private JComboBox<ItemCombo<Cliente>> cbCliente;
    private JComboBox<ItemCombo<Vendedor>> cbVendedor;
    private JTextField txtDataHora;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TelaFormularioAgendamento(Frame parent, Agendamento agendamentoEdicao, Runnable onSuccess) {
        super(parent, true);
        this.agendamentoServico = new AgendamentoServico();
        this.clienteServico = new ClienteServico();
        this.vendedorServico = new VendedorServico();
        this.agendamentoEdicao = agendamentoEdicao;
        this.onSuccess = onSuccess;

        configurarJanela();
        inicializarComponentes();
        carregarCombos();
        preencherSeEdicao();
    }

    private void configurarJanela() {
        setTitle(agendamentoEdicao == null ? "Novo Agendamento" : "Remarcar Agendamento");
        setSize(430, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 10, 15));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        painelCampos.add(new JLabel("Cliente:"));
        cbCliente = new JComboBox<>();
        painelCampos.add(cbCliente);

        painelCampos.add(new JLabel("Vendedor:"));
        cbVendedor = new JComboBox<>();
        painelCampos.add(cbVendedor);

        painelCampos.add(new JLabel("Data/Hora (dd/MM/yyyy HH:mm):"));
        txtDataHora = new JTextField(LocalDateTime.now().plusDays(1).format(formatter));
        painelCampos.add(txtDataHora);

        add(painelCampos, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JButton btnSalvar = new JButton("Confirmar Agendamento");
        btnSalvar.setBackground(new Color(39, 174, 96));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSalvar.addActionListener(e -> salvar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarCombos() {
        if (SessaoUsuario.isCliente()) {
            Cliente logado = (Cliente) SessaoUsuario.getUsuarioLogado();
            cbCliente.addItem(new ItemCombo<>(logado, logado.getNome() + " " + logado.getSobrenome()));
            cbCliente.setEnabled(false);
        } else {
            List<Cliente> clientes = clienteServico.buscarTodos();
            for (Cliente c : clientes) {
                cbCliente.addItem(new ItemCombo<>(c, c.getNome() + " " + c.getSobrenome() + " (" + c.getCpf() + ")"));
            }
        }

        if (SessaoUsuario.isVendedor()) {
            Vendedor logado = (Vendedor) SessaoUsuario.getUsuarioLogado();
            cbVendedor.addItem(new ItemCombo<>(logado, logado.getNome() + " " + logado.getSobrenome()));
            cbVendedor.setEnabled(false);
        } else {
            List<Vendedor> vendedores = vendedorServico.buscarTodos();
            for (Vendedor v : vendedores) {
                cbVendedor.addItem(new ItemCombo<>(v, v.getNome() + " " + v.getSobrenome()));
            }
        }
    }

    private void preencherSeEdicao() {
        if (agendamentoEdicao != null && agendamentoEdicao.getAgendadoPara() != null) {
            txtDataHora.setText(agendamentoEdicao.getAgendadoPara().format(formatter));
        }
    }

    @SuppressWarnings("unchecked")
    private void salvar() {
        try {
            ItemCombo<Cliente> itemCliente = (ItemCombo<Cliente>) cbCliente.getSelectedItem();
            ItemCombo<Vendedor> itemVendedor = (ItemCombo<Vendedor>) cbVendedor.getSelectedItem();

            if (itemCliente == null || itemVendedor == null) {
                JOptionPane.showMessageDialog(this, "Selecione o cliente e o vendedor.");
                return;
            }

            LocalDateTime dataAgendada = LocalDateTime.parse(txtDataHora.getText().trim(), formatter);

            Agendamento a = (agendamentoEdicao != null) ? agendamentoEdicao : new Agendamento();
            a.setCliente(itemCliente.getValor());
            a.setVendedor(itemVendedor.getValor());
            a.setAgendadoPara(dataAgendada);

            if (agendamentoEdicao == null) {
                agendamentoServico.cadastrar(a);
            } else {
                agendamentoServico.atualizar(a);
            }

            if (onSuccess != null) onSuccess.run();
            dispose();

        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido! Use dd/MM/yyyy HH:mm (Ex: 25/11/2026 14:30)", "Erro de Data", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar agendamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper interno para exibição amigável no JComboBox
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