package telas.autenticacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entidades.Cliente;
import entidades.Gestor;
import entidades.Pessoa;
import entidades.Vendedor;
import servicos.ClienteServico;
import servicos.GestorServico;
import servicos.VendedorServico;

/**
 * Tela 2.1 (Reutilizável): Formulário unificado de Cadastro/Edição de Pessoa (Cliente, Vendedor ou Gestor).
 */
public class TelaFormularioPessoa extends JDialog {

    private final String tipoPerfil; // "CLIENTE", "VENDEDOR" ou "GESTOR"
    private final Pessoa pessoaEdicao;
    private final Runnable onSuccess;

    private JTextField txtNome;
    private JTextField txtSobrenome;
    private JTextField txtCpf;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JPasswordField txtSenha;

    public TelaFormularioPessoa(Frame parent, String tipoPerfil, Pessoa pessoaEdicao, Runnable onSuccess) {
        super(parent, true);
        this.tipoPerfil = tipoPerfil;
        this.pessoaEdicao = pessoaEdicao;
        this.onSuccess = onSuccess;

        configurarJanela();
        inicializarComponentes();
        preencherDadosSeEdicao();
    }

    private void configurarJanela() {
        String acao = (pessoaEdicao == null) ? "Cadastrar " : "Editar ";
        setTitle(acao + tipoPerfil);
        setSize(420, 420);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        JPanel painelCampos = new JPanel(new GridLayout(6, 2, 10, 12));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        painelCampos.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Sobrenome:"));
        txtSobrenome = new JTextField();
        painelCampos.add(txtSobrenome);

        painelCampos.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        painelCampos.add(txtCpf);

        painelCampos.add(new JLabel("E-mail:"));
        txtEmail = new JTextField();
        painelCampos.add(txtEmail);

        painelCampos.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelCampos.add(txtTelefone);

        painelCampos.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        painelCampos.add(txtSenha);

        add(painelCampos, BorderLayout.CENTER);

        // Rodapé
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JButton btnSalvar = new JButton("Salvar");
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

    private void preencherDadosSeEdicao() {
        if (pessoaEdicao != null) {
            txtNome.setText(pessoaEdicao.getNome());
            txtSobrenome.setText(pessoaEdicao.getSobrenome());
            txtCpf.setText(pessoaEdicao.getCpf());
            txtEmail.setText(pessoaEdicao.getEmail());
            txtTelefone.setText(pessoaEdicao.getTelefone());
            txtSenha.setText(pessoaEdicao.getSenha());
        }
    }

    private void salvar() {
        try {
            String nome = txtNome.getText().trim();
            String sobrenome = txtSobrenome.getText().trim();
            String cpf = txtCpf.getText().trim();
            String email = txtEmail.getText().trim();
            String telefone = txtTelefone.getText().trim();
            String senha = new String(txtSenha.getPassword());

            if ("CLIENTE".equalsIgnoreCase(tipoPerfil)) {
                ClienteServico servico = new ClienteServico();
                Cliente c = (pessoaEdicao != null) ? (Cliente) pessoaEdicao : new Cliente();
                preencherAtributos(c, nome, sobrenome, cpf, email, telefone, senha);
                if (pessoaEdicao == null) servico.cadastrar(c);
                else servico.atualizar(c);

            } else if ("VENDEDOR".equalsIgnoreCase(tipoPerfil)) {
                VendedorServico servico = new VendedorServico();
                Vendedor v = (pessoaEdicao != null) ? (Vendedor) pessoaEdicao : new Vendedor();
                preencherAtributos(v, nome, sobrenome, cpf, email, telefone, senha);
                if (pessoaEdicao == null) servico.cadastrar(v);
                else servico.atualizar(v);

            } else if ("GESTOR".equalsIgnoreCase(tipoPerfil)) {
                GestorServico servico = new GestorServico();
                Gestor g = (pessoaEdicao != null) ? (Gestor) pessoaEdicao : new Gestor();
                preencherAtributos(g, nome, sobrenome, cpf, email, telefone, senha);
                if (pessoaEdicao == null) servico.cadastrar(g);
                else servico.atualizar(g);
            }

            if (onSuccess != null) onSuccess.run();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherAtributos(Pessoa p, String nome, String sobrenome, String cpf, String email, String telefone, String senha) {
        p.setNome(nome);
        p.setSobrenome(sobrenome);
        p.setCpf(cpf);
        p.setEmail(email);
        p.setTelefone(telefone);
        p.setSenha(senha);
    }
}