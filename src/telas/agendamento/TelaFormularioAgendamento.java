package telas.agendamento;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entidades.Agendamento;
import entidades.Cliente;
import entidades.Vendedor;
import servicos.AgendamentoServico;
import servicos.ClienteServico;
import servicos.VendedorServico;
import autenticacao.SessaoUsuario;
import telas.utils.UiUtils;
import telas.utils.UiUtils.ItemCombo;

/**
 * Modal para Criar ou Remarcar Agendamento.
 */
public class TelaFormularioAgendamento extends JDialog {

    private final AgendamentoServico agendamentoServico = new AgendamentoServico();
    private final Agendamento agendamentoEdicao;
    private final Runnable onSuccess;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private JComboBox<ItemCombo<Cliente>> cbCliente;
    private JComboBox<ItemCombo<Vendedor>> cbVendedor;
    private JTextField txtDataHora;

    public TelaFormularioAgendamento(Frame parent, Agendamento agendamentoEdicao, Runnable onSuccess) {
        super(parent, true);
        this.agendamentoEdicao = agendamentoEdicao;
        this.onSuccess = onSuccess;

        setTitle(agendamentoEdicao == null ? "Novo Agendamento" : "Remarcar Agendamento");
        setSize(420, 280);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        inicializarComponentes();
        carregarCombos();
        if (agendamentoEdicao != null && agendamentoEdicao.getAgendadoPara() != null) {
            txtDataHora.setText(agendamentoEdicao.getAgendadoPara().format(fmt));
        }
    }

    private void inicializarComponentes() {
        JPanel campos = new JPanel(new GridLayout(3, 2, 10, 14));
        campos.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

        campos.add(new JLabel("Cliente:"));
        cbCliente = new JComboBox<>();
        campos.add(cbCliente);

        campos.add(new JLabel("Vendedor:"));
        cbVendedor = new JComboBox<>();
        campos.add(cbVendedor);

        campos.add(new JLabel("Data/Hora (dd/MM/yyyy HH:mm):"));
        txtDataHora = new JTextField(LocalDateTime.now().plusDays(1).format(fmt));
        campos.add(txtDataHora);

        add(campos, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        botoes.add(UiUtils.criarBotao("Confirmar", UiUtils.COR_SUCESSO, this::salvar));
        botoes.add(UiUtils.criarBotao("Cancelar", null, this::dispose));
        add(botoes, BorderLayout.SOUTH);
    }

    private void carregarCombos() {
        if (SessaoUsuario.isCliente()) {
            Cliente logado = (Cliente) SessaoUsuario.getUsuarioLogado();
            cbCliente.addItem(new ItemCombo<>(logado, logado.getNome() + " " + logado.getSobrenome()));
            cbCliente.setEnabled(false);
        } else {
            for (Cliente c : new ClienteServico().buscarTodos()) {
                cbCliente.addItem(new ItemCombo<>(c, c.getNome() + " " + c.getSobrenome() + " (" + c.getCpf() + ")"));
            }
        }

        if (SessaoUsuario.isVendedor()) {
            Vendedor logado = (Vendedor) SessaoUsuario.getUsuarioLogado();
            cbVendedor.addItem(new ItemCombo<>(logado, logado.getNome() + " " + logado.getSobrenome()));
            cbVendedor.setEnabled(false);
        } else {
            for (Vendedor v : new VendedorServico().buscarTodos()) {
                cbVendedor.addItem(new ItemCombo<>(v, v.getNome() + " " + v.getSobrenome()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void salvar() {
        try {
            ItemCombo<Cliente> itemC = (ItemCombo<Cliente>) cbCliente.getSelectedItem();
            ItemCombo<Vendedor> itemV = (ItemCombo<Vendedor>) cbVendedor.getSelectedItem();
            if (itemC == null || itemV == null) {
                JOptionPane.showMessageDialog(this, "Selecione o cliente e o vendedor.");
                return;
            }

            Agendamento a = (agendamentoEdicao != null) ? agendamentoEdicao : new Agendamento();
            a.setCliente(itemC.getValor());
            a.setVendedor(itemV.getValor());
            a.setAgendadoPara(LocalDateTime.parse(txtDataHora.getText().trim(), fmt));

            if (agendamentoEdicao == null) agendamentoServico.cadastrar(a);
            else agendamentoServico.atualizar(a);

            if (onSuccess != null) onSuccess.run();
            dispose();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Use o formato dd/MM/yyyy HH:mm (Ex: 25/11/2026 14:30)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}