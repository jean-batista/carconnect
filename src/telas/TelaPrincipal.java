package telas;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import autenticacao.SessaoUsuario;
import entidades.Pessoa;
import servicos.ClienteServico;
import servicos.GestorServico;
import servicos.VendedorServico;
import telas.agendamento.TelaAgendamentos;
import telas.autenticacao.TelaLogin;
import telas.gestor.TelaGestaoPessoas;
import telas.negociacao.TelaNegociacoes;
import telas.utils.UiUtils;
import telas.veiculo.TelaCatalogoVeiculos;

/**
 * Dashboard Principal com menu adaptável ao perfil logado e CardLayout central.
 */
public class TelaPrincipal extends JFrame {

    private JPanel painelConteudo;
    private CardLayout cardLayout;

    public TelaPrincipal() {
        setTitle("CarConnect - Painel (" + SessaoUsuario.getTipoPerfil() + ")");
        setSize(1020, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        Pessoa usuario = SessaoUsuario.getUsuarioLogado();
        String nome = (usuario != null) ? usuario.getNome() + " " + usuario.getSobrenome() : "Usuário";

        // Barra Superior
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(UiUtils.COR_PRIMARIA);
        topo.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblLogo = new JLabel("CarConnect  |  " + SessaoUsuario.getTipoPerfil().toUpperCase());
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel dir = new JPanel();
        dir.setOpaque(false);
        JLabel lblUser = new JLabel("Olá, " + nome + "   ");
        lblUser.setForeground(Color.WHITE);
        dir.add(lblUser);
        dir.add(UiUtils.criarBotao("Sair", UiUtils.COR_PERIGO, () -> {
            SessaoUsuario.encerrarSessao();
            new TelaLogin().setVisible(true);
            dispose();
        }));

        topo.add(lblLogo, BorderLayout.WEST);
        topo.add(dir, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        // Menu Lateral e Área Central
        JPanel menu = new JPanel(new GridLayout(10, 1, 0, 8));
        menu.setBackground(UiUtils.COR_SECUNDARIA);
        menu.setPreferredSize(new Dimension(210, 0));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);

        JLabel lblHome = new JLabel("Bem-vindo ao CarConnect! Selecione uma opção no menu.", SwingConstants.CENTER);
        lblHome.setFont(new Font("SansSerif", Font.PLAIN, 17));
        painelConteudo.add(lblHome, "HOME");

        adicionarTela(menu, "Catálogo de Veículos", "VEICULOS", new TelaCatalogoVeiculos());
        adicionarTela(menu, "Agendamentos", "AGENDAMENTOS", new TelaAgendamentos());
        adicionarTela(menu, "Negociações / Vendas", "NEGOCIACOES", new TelaNegociacoes());

        if (SessaoUsuario.isVendedor() || SessaoUsuario.isGestor()) {
            adicionarTela(menu, "Clientes", "CLIENTES",
                    new TelaGestaoPessoas<>("CLIENTE", "Clientes", new ClienteServico()));
        }

        if (SessaoUsuario.isGestor()) {
            adicionarTela(menu, "Vendedores", "VENDEDORES",
                    new TelaGestaoPessoas<>("VENDEDOR", "Vendedores", new VendedorServico()));
            adicionarTela(menu, "Gestores", "GESTORES",
                    new TelaGestaoPessoas<>("GESTOR", "Gestores", new GestorServico()));
        }

        add(menu, BorderLayout.WEST);
        add(painelConteudo, BorderLayout.CENTER);
    }

    private void adicionarTela(JPanel menu, String rotulo, String chave, JPanel tela) {
        painelConteudo.add(tela, chave);
        menu.add(UiUtils.criarBotao(rotulo, new Color(47, 54, 64), () -> cardLayout.show(painelConteudo, chave)));
    }
}