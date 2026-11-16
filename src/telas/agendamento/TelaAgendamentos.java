package telas.agendamento;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import entidades.Agendamento;
import servicos.AgendamentoServico;
import autenticacao.SessaoUsuario;
import telas.utils.UiUtils;

/**
 * Painel de Agendamentos de Visitas (filtrado por perfil).
 */
public class TelaAgendamentos extends JPanel {

    private final AgendamentoServico agendamentoServico = new AgendamentoServico();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaAgendamentos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        JPanel topo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Agendamentos de Visitas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        topo.add(lblTitulo, BorderLayout.WEST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acoes.add(UiUtils.criarBotao("+ Novo Agendamento", UiUtils.COR_SUCESSO, () -> abrirFormulario(null)));
        acoes.add(UiUtils.criarBotao("Remarcar", null, this::remarcarSelecionado));
        acoes.add(UiUtils.criarBotao("Cancelar Visita", UiUtils.COR_PERIGO, this::cancelarSelecionado));
        acoes.add(UiUtils.criarBotao("Atualizar", null, this::carregarTabela));
        topo.add(acoes, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        modelo = UiUtils.criarModeloTabela("ID", "Solicitado Em", "Data/Hora Visita", "Cliente", "Vendedor");
        tabela = UiUtils.criarTabela(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        try {
            UUID userId = SessaoUsuario.getUsuarioLogado().getId();
            List<Agendamento> lista = SessaoUsuario.isCliente() ? agendamentoServico.buscarPorClienteId(userId)
                    : SessaoUsuario.isVendedor() ? agendamentoServico.buscarPorVendedorId(userId)
                    : agendamentoServico.buscarTodos();

            for (Agendamento a : lista) {
                String cliente = (a.getCliente() != null) ? a.getCliente().getNome() + " " + a.getCliente().getSobrenome() : "-";
                String vendedor = (a.getVendedor() != null) ? a.getVendedor().getNome() + " " + a.getVendedor().getSobrenome() : "-";
                modelo.addRow(new Object[]{
                    a.getId(),
                    a.getAgendadoEm() != null ? a.getAgendadoEm().format(fmt) : "-",
                    a.getAgendadoPara() != null ? a.getAgendadoPara().format(fmt) : "-",
                    cliente, vendedor
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar agendamentos: " + ex.getMessage());
        }
    }

    private void abrirFormulario(Agendamento agendamento) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        new TelaFormularioAgendamento(parent, agendamento, this::carregarTabela).setVisible(true);
    }

    private void remarcarSelecionado() {
        UUID id = getIdSelecionado();
        if (id != null) abrirFormulario(agendamentoServico.buscarPorId(id));
    }

    private void cancelarSelecionado() {
        UUID id = getIdSelecionado();
        if (id == null) return;
        if (JOptionPane.showConfirmDialog(this, "Cancelar este agendamento?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                Agendamento a = new Agendamento();
                a.setId(id);
                agendamentoServico.excluir(a);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cancelar: " + ex.getMessage());
            }
        }
    }

    private UUID getIdSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um agendamento na tabela.");
            return null;
        }
        return (UUID) modelo.getValueAt(linha, 0);
    }
}