package telas;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import autenticacao.SessaoUsuario;
import entidades.Pessoa;
import telas.agendamento.TelaAgendamentos;
import telas.autenticacao.TelaLogin;
import telas.gestor.TelaGestaoClientes;
import telas.gestor.TelaGestaoGestores;
import telas.gestor.TelaGestaoVendedores;
import telas.negociacao.TelaNegociacoes;
import telas.veiculo.TelaCatalogoVeiculos;

/**
 * Tela 1.2: Dashboard Principal com menu adaptável ao perfil logado.
 */
public class TelaPrincipal extends JFrame {

    private JPanel painelConteudo;
    private CardLayout cardLayout;

    public TelaPrincipal() {
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("CarConnect - Painel de Controle (" + SessaoUsuario.getTipoPerfil() + ")");
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        Pessoa usuario = SessaoUsuario.getUsuarioLogado();
        String nomeUsuario = (usuario != null) ? usuario.getNome() + " " + usuario.getSobrenome() : "Usuário";

        // Barra Superior
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(25, 42, 86));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblLogo = new JLabel("CarConnect  |  " + SessaoUsuario.getTipoPerfil().toUpperCase());
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel painelUsuario = new JPanel();
        painelUsuario.setOpaque(false);
        JLabel lblBemVindo = new JLabel("Olá, " + nomeUsuario + "   ");
        lblBemVindo.setForeground(Color.WHITE);
        lblBemVindo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(192, 57, 43));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(e -> {
            SessaoUsuario.encerrarSessao();
            new TelaLogin().setVisible(true);
            dispose();
        });

        painelUsuario.add(lblBemVindo);
        painelUsuario.add(btnSair);

        painelSuperior.add(lblLogo, BorderLayout.WEST);
        painelSuperior.add(painelUsuario, BorderLayout.EAST);
        add(painelSuperior, BorderLayout.NORTH);

        // Menu Lateral
        JPanel painelMenu = new JPanel(new GridLayout(10, 1, 0, 8));
        painelMenu.setBackground(new Color(39, 60, 117));
        painelMenu.setPreferredSize(new Dimension(220, 0));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        // Área Central (CardLayout para alternar painéis sem abrir dezenas de janelas)
        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);

        // Painel de Boas-vindas
        JPanel painelHome = new JPanel(new BorderLayout());
        JLabel lblHome = new JLabel("Bem-vindo ao CarConnect! Selecione uma opção no menu lateral.", SwingConstants.CENTER);
        lblHome.setFont(new Font("SansSerif", Font.PLAIN, 18));
        painelHome.add(lblHome, BorderLayout.CENTER);
        painelConteudo.add(painelHome, "HOME");

        // Módulo de Veículos (Todos têm acesso)
        painelConteudo.add(new TelaCatalogoVeiculos(), "VEICULOS");
        adicionarBotaoMenu(painelMenu, "Catálogo de Veículos", () -> cardLayout.show(painelConteudo, "VEICULOS"));

        // Módulo de Agendamentos (Todos têm acesso)
        painelConteudo.add(new TelaAgendamentos(), "AGENDAMENTOS");
        adicionarBotaoMenu(painelMenu, "Agendamentos", () -> cardLayout.show(painelConteudo, "AGENDAMENTOS"));

        // Módulo de Negociações (Todos têm acesso)
        painelConteudo.add(new TelaNegociacoes(), "NEGOCIACOES");
        adicionarBotaoMenu(painelMenu, "Negociações / Vendas", () -> cardLayout.show(painelConteudo, "NEGOCIACOES"));

        // Módulo de Clientes (Apenas Vendedor e Gestor)
        if (SessaoUsuario.isVendedor() || SessaoUsuario.isGestor()) {
            painelConteudo.add(new TelaGestaoClientes(), "CLIENTES");
            adicionarBotaoMenu(painelMenu, "Clientes", () -> cardLayout.show(painelConteudo, "CLIENTES"));
        }

        // Módulos Administrativos (Apenas Gestor)
        if (SessaoUsuario.isGestor()) {
            painelConteudo.add(new TelaGestaoVendedores(), "VENDEDORES");
            adicionarBotaoMenu(painelMenu, "Vendedores", () -> cardLayout.show(painelConteudo, "VENDEDORES"));

            painelConteudo.add(new TelaGestaoGestores(), "GESTORES");
            adicionarBotaoMenu(painelMenu, "Gestores", () -> cardLayout.show(painelConteudo, "GESTORES"));
        }

        add(painelMenu, BorderLayout.WEST);
        add(painelConteudo, BorderLayout.CENTER);
    }

    private void adicionarBotaoMenu(JPanel menu, String texto, Runnable acao) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(47, 54, 64));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> acao.run());
        menu.add(btn);
    }
}