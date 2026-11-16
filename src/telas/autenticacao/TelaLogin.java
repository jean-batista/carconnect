package telas.autenticacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

import entidades.Pessoa;
import servicos.ClienteServico;
import servicos.GestorServico;
import servicos.PessoaServicoBase;
import servicos.VendedorServico;
import autenticacao.SessaoUsuario;
import telas.TelaPrincipal;
import telas.utils.UiUtils;

/**
 * Tela de Login com suporte a troca obrigatória de senha no primeiro acesso
 * (quando a senha cadastrada ainda é igual ao CPF provisório).
 */
public class TelaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;

    public TelaLogin() {
        setTitle("CarConnect - Login");
        setSize(400, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel painelRaiz = new JPanel(new BorderLayout());
        painelRaiz.setBackground(UiUtils.COR_FUNDO);
        setContentPane(painelRaiz);

        // Cabeçalho
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));
        painelTopo.setBackground(UiUtils.COR_PRIMARIA);
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("CarConnect");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Sistema de Gerenciamento de Concessionária");
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setForeground(new Color(200, 210, 230));

        painelTopo.add(lblTitulo);
        painelTopo.add(Box.createVerticalStrut(5));
        painelTopo.add(lblSub);
        painelRaiz.add(painelTopo, BorderLayout.NORTH);

        // Formulário
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBackground(UiUtils.COR_FUNDO);
        painelCentro.setBorder(BorderFactory.createEmptyBorder(25, 40, 20, 40));

        txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        txtSenha = new JPasswordField();
        txtSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JButton btnEntrar = UiUtils.criarBotao("Entrar", UiUtils.COR_SUCESSO, this::realizarLogin);
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JButton btnCadastrar = new JButton("Não tem conta? Cadastre-se como Cliente");
        btnCadastrar.setBorderPainted(false);
        btnCadastrar.setContentAreaFilled(false);
        btnCadastrar.setForeground(new Color(41, 128, 185));
        btnCadastrar.addActionListener(e -> new TelaFormularioPessoa(this, "CLIENTE", null,
                () -> JOptionPane.showMessageDialog(this, "Cadastro concluído! Faça login.")).setVisible(true));

        painelCentro.add(new JLabel("E-mail:"));
        painelCentro.add(Box.createVerticalStrut(4));
        painelCentro.add(txtEmail);
        painelCentro.add(Box.createVerticalStrut(12));
        painelCentro.add(new JLabel("Senha:"));
        painelCentro.add(Box.createVerticalStrut(4));
        painelCentro.add(txtSenha);
        painelCentro.add(Box.createVerticalStrut(20));
        painelCentro.add(btnEntrar);
        painelCentro.add(Box.createVerticalStrut(8));
        painelCentro.add(btnCadastrar);

        painelRaiz.add(painelCentro, BorderLayout.CENTER);
    }

    private void realizarLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o e-mail e a senha.");
            return;
        }

        try {
            Pessoa autenticado = buscarUsuario(new GestorServico(), email, senha);
            if (autenticado == null) autenticado = buscarUsuario(new VendedorServico(), email, senha);
            if (autenticado == null) autenticado = buscarUsuario(new ClienteServico(), email, senha);

            if (autenticado == null) {
                JOptionPane.showMessageDialog(this, "E-mail ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verifica se está usando o CPF como senha provisória (primeiro acesso)
            String cpfLimpo = autenticado.getCpf().replaceAll("\\D", "");
            if (senha.equals(autenticado.getCpf()) || senha.equals(cpfLimpo)) {
                if (!exigirTrocaSenhaPrimeiroAcesso(autenticado)) {
                    return; // Cancelou a troca de senha
                }
            }

            SessaoUsuario.iniciarSessao(autenticado);
            new TelaPrincipal().setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao autenticar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private <T extends Pessoa> T buscarUsuario(PessoaServicoBase<T> servico, String email, String senha) {
        List<T> lista = servico.buscarTodos();
        for (T u : lista) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }

    private boolean exigirTrocaSenhaPrimeiroAcesso(Pessoa usuario) {
        JPasswordField pfNova = new JPasswordField();
        Object[] msg = {"Primeiro acesso detectado!\nDefina sua nova senha definitiva (mín. 6 caracteres):", pfNova};
        int opt = JOptionPane.showConfirmDialog(this, msg, "Alteração Obrigatória de Senha", JOptionPane.OK_CANCEL_OPTION);

        if (opt != JOptionPane.OK_OPTION) return false;

        String novaSenha = new String(pfNova.getPassword()).trim();
        if (novaSenha.length() < 6 || novaSenha.equals(usuario.getCpf())) {
            JOptionPane.showMessageDialog(this, "A nova senha deve ter no mínimo 6 caracteres e ser diferente do CPF.");
            return false;
        }

        usuario.setSenha(novaSenha);
        if (usuario instanceof entidades.Gestor g) new GestorServico().atualizar(g);
        else if (usuario instanceof entidades.Vendedor v) new VendedorServico().atualizar(v);
        else if (usuario instanceof entidades.Cliente c) new ClienteServico().atualizar(c);

        JOptionPane.showMessageDialog(this, "Senha atualizada com sucesso!");
        return true;
    }
}