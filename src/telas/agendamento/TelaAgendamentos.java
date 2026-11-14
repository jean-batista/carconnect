package telas.agendamento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
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
import entidades.Agendamento;
import servicos.AgendamentoServico;

/**
 * Tela 4.1: Painel de Agendamentos de Visitas (Filtrado por perfil).
 */
public class TelaAgendamentos extends JPanel {

    private final AgendamentoServico agendamentoServico;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TelaAgendamentos() {
        this.agendamentoServico = new AgendamentoServico();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        JPanel painelTopo = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Agendamentos de Visitas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnNovo = new JButton("+ Novo Agendamento");
        btnNovo.setBackground(new Color(39, 174, 96));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.addActionListener(e -> abrirFormulario(null));

        JButton btnRemarcar = new JButton("Remarcar");
        btnRemarcar.addActionListener(e -> remarcarSelecionado());

        JButton btnCancelar = new JButton("Cancelar Visita");
        btnCancelar.setBackground(new Color(192, 57, 43));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> cancelarSelecionado());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarTabela());

        painelAcoes.add(btnNovo);
        painelAcoes.add(btnRemarcar);
        painelAcoes.add(btnCancelar);
        painelAcoes.add(btnAtualizar);

        painelTopo.add(painelAcoes, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Solicitado Em", "Data/Hora Visita", "Cliente", "Vendedor"};
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
            List<Agendamento> agendamentos;
            UUID usuarioId = SessaoUsuario.getUsuarioLogado().getId();

            if (SessaoUsuario.isCliente()) {
                agendamentos = agendamentoServico.buscarPorClienteId(usuarioId);
            } else if (SessaoUsuario.isVendedor()) {
                agendamentos = agendamentoServico.buscarPorVendedorId(usuarioId);
            } else {
                agendamentos = agendamentoServico.buscarTodos();
            }

            for (Agendamento a : agendamentos) {
                String nomeCliente = (a.getCliente() != null) ? a.getCliente().getNome() + " " + a.getCliente().getSobrenome() : "-";
                String nomeVendedor = (a.getVendedor() != null) ? a.getVendedor().getNome() + " " + a.getVendedor().getSobrenome() : "-";

                modeloTabela.addRow(new Object[]{
                    a.getId(),
                    a.getAgendadoEm() != null ? a.getAgendadoEm().format(formatter) : "-",
                    a.getAgendadoPara() != null ? a.getAgendadoPara().format(formatter) : "-",
                    nomeCliente,
                    nomeVendedor
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
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um agendamento na tabela.");
            return;
        }
        UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
        Agendamento a = agendamentoServico.buscarPorId(id);
        abrirFormulario(a);
    }

    private void cancelarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um agendamento na tabela.");
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja cancelar este agendamento?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                UUID id = (UUID) modeloTabela.getValueAt(linha, 0);
                Agendamento a = new Agendamento();
                a.setId(id);
                agendamentoServico.excluir(a);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cancelar agendamento: " + ex.getMessage());
            }
        }
    }
}