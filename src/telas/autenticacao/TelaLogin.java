package telas.autenticacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import autenticacao.SessaoUsuario;
import entidades.Cliente;
import entidades.Gestor;
import entidades.Pessoa;
import entidades.Vendedor;
import servicos.ClienteServico;
import servicos.GestorServico;
import servicos.VendedorServico;
import telas.TelaPrincipal;

/**
 * Tela 1.1: Autenticação de usuários (Cliente, Vendedor ou Gestor).
 */
public class TelaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnCadastrar;

    public TelaLogin() {
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("CarConnect - Login");
        setSize(420, 400);
        setMinimumSize(new Dimension(420, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        // Painel raiz com pintura explícita para forçar renderização no Linux/Wayland
        JPanel painelRaiz = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(245, 246, 250));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        painelRaiz.setOpaque(true);
        setContentPane(painelRaiz);

        // Cabeçalho
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));
        painelTopo.setOpaque(true);
        painelTopo.setBackground(new Color(25, 42, 86));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("CarConnect");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Sistema de Gerenciamento de Concessionária");
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(200, 210, 230));

        painelTopo.add(lblTitulo);
        painelTopo.add(Box.createVerticalStrut(5));
        painelTopo.add(lblSubtitulo);
        painelRaiz.add(painelTopo, BorderLayout.NORTH);

        // Formulário Central usando BoxLayout (mais estável no X11/Wayland)
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setOpaque(true);
        painelCentro.setBackground(new Color(245, 246, 250));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(25, 40, 20, 40));

        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblEmail.setFont(new Font("Dialog", Font.BOLD, 13));

        txtEmail = new JTextField();
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtEmail.setPreferredSize(new Dimension(320, 35));

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSenha.setFont(new Font("Dialog", Font.BOLD, 13));

        txtSenha = new JPasswordField();
        txtSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtSenha.setPreferredSize(new Dimension(320, 35));

        btnEntrar = new JButton("Entrar");
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnEntrar.setPreferredSize(new Dimension(320, 40));
        btnEntrar.setOpaque(true);
        btnEntrar.setBackground(new Color(39, 174, 96));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Dialog", Font.BOLD, 14));
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.addActionListener(e -> realizarLogin());

        btnCadastrar = new JButton("Não tem conta? Cadastre-se como Cliente");
        btnCadastrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCadastrar.setBorderPainted(false);
        btnCadastrar.setContentAreaFilled(false);
        btnCadastrar.setForeground(new Color(41, 128, 185));
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrar.addActionListener(e -> abrirCadastroCliente());

        painelCentro.add(lblEmail);
        painelCentro.add(Box.createVerticalStrut(5));
        painelCentro.add(txtEmail);
        painelCentro.add(Box.createVerticalStrut(15));
        painelCentro.add(lblSenha);
        painelCentro.add(Box.createVerticalStrut(5));
        painelCentro.add(txtSenha);
        painelCentro.add(Box.createVerticalStrut(20));
        painelCentro.add(btnEntrar);
        painelCentro.add(Box.createVerticalStrut(10));
        painelCentro.add(btnCadastrar);

        painelRaiz.add(painelCentro, BorderLayout.CENTER);
    }

    private void realizarLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o e-mail e a senha.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Pessoa autenticado = autenticarUsuario(email, senha);
            if (autenticado != null) {
                SessaoUsuario.iniciarSessao(autenticado);
                new TelaPrincipal().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "E-mail ou senha inválidos!", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Pessoa autenticarUsuario(String email, String senha) {
        GestorServico gestorServico = new GestorServico();
        VendedorServico vendedorServico = new VendedorServico();
        ClienteServico clienteServico = new ClienteServico();

        // Verifica entre Gestores
        List<Gestor> gestores = gestorServico.buscarTodos();
        for (Gestor g : gestores) {
            if (g.getEmail().equalsIgnoreCase(email) && g.getSenha().equals(senha)) {
                return g;
            }
        }

        // Verifica entre Vendedores
        List<Vendedor> vendedores = vendedorServico.buscarTodos();
        for (Vendedor v : vendedores) {
            if (v.getEmail().equalsIgnoreCase(email) && v.getSenha().equals(senha)) {
                return v;
            }
        }

        // Verifica entre Clientes
        List<Cliente> clientes = clienteServico.buscarTodos();
        for (Cliente c : clientes) {
            if (c.getEmail().equalsIgnoreCase(email) && c.getSenha().equals(senha)) {
                return c;
            }
        }

        return null;
    }

    private void abrirCadastroCliente() {
        new TelaFormularioPessoa(this, "CLIENTE", null, () -> {
            JOptionPane.showMessageDialog(this, "Cadastro concluído! Faça login para continuar.");
        }).setVisible(true);
    }

}