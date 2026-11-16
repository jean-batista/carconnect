package telas.autenticacao;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
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
import autenticacao.SessaoUsuario;
import telas.utils.UiUtils;

/**
 * Formulário unificado de Cadastro/Edição de Pessoa.
 * Se cadastrado por um usuário já logado (Gestor/Vendedor), oculta o campo senha
 * e define o CPF automaticamente como senha provisória.
 */
public class TelaFormularioPessoa extends JDialog {

    private final String tipoPerfil;
    private final Pessoa pessoaEdicao;
    private final Runnable onSuccess;
    private final boolean cadastroInterno;

    private JTextField txtNome, txtSobrenome, txtCpf, txtEmail, txtTelefone;
    private JPasswordField txtSenha;

    public TelaFormularioPessoa(Frame parent, String tipoPerfil, Pessoa pessoaEdicao, Runnable onSuccess) {
        super(parent, true);
        this.tipoPerfil = tipoPerfil;
        this.pessoaEdicao = pessoaEdicao;
        this.onSuccess = onSuccess;
        this.cadastroInterno = (SessaoUsuario.getUsuarioLogado() != null && pessoaEdicao == null);

        setTitle((pessoaEdicao == null ? "Cadastrar " : "Editar ") + tipoPerfil);
        setSize(400, cadastroInterno ? 340 : 390);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        inicializarComponentes();
        preencherDadosSeEdicao();
    }

    private void inicializarComponentes() {
        int linhas = cadastroInterno ? 5 : 6;
        JPanel painelCampos = new JPanel(new GridLayout(linhas, 2, 10, 12));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

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

        if (!cadastroInterno) {
            painelCampos.add(new JLabel("Senha:"));
            txtSenha = new JPasswordField();
            painelCampos.add(txtSenha);
        }

        add(painelCampos, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        painelBotoes.add(UiUtils.criarBotao("Salvar", UiUtils.COR_SUCESSO, this::salvar));
        painelBotoes.add(UiUtils.criarBotao("Cancelar", null, this::dispose));
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void preencherDadosSeEdicao() {
        if (pessoaEdicao != null) {
            txtNome.setText(pessoaEdicao.getNome());
            txtSobrenome.setText(pessoaEdicao.getSobrenome());
            txtCpf.setText(pessoaEdicao.getCpf());
            txtEmail.setText(pessoaEdicao.getEmail());
            txtTelefone.setText(pessoaEdicao.getTelefone());
            if (txtSenha != null) txtSenha.setText(pessoaEdicao.getSenha());
        }
    }

    private void salvar() {
        try {
            String cpf = txtCpf.getText().trim();
            // Senha provisória = CPF quando cadastrado internamente por Gestor/Vendedor
            String senha = cadastroInterno ? cpf.replaceAll("\\D", "") : new String(txtSenha.getPassword());

            if ("CLIENTE".equalsIgnoreCase(tipoPerfil)) {
                Cliente c = (pessoaEdicao != null) ? (Cliente) pessoaEdicao : new Cliente();
                preencher(c, cpf, senha);
                ClienteServico s = new ClienteServico();
                if (pessoaEdicao == null) s.cadastrar(c); else s.atualizar(c);
            } else if ("VENDEDOR".equalsIgnoreCase(tipoPerfil)) {
                Vendedor v = (pessoaEdicao != null) ? (Vendedor) pessoaEdicao : new Vendedor();
                preencher(v, cpf, senha);
                VendedorServico s = new VendedorServico();
                if (pessoaEdicao == null) s.cadastrar(v); else s.atualizar(v);
            } else if ("GESTOR".equalsIgnoreCase(tipoPerfil)) {
                Gestor g = (pessoaEdicao != null) ? (Gestor) pessoaEdicao : new Gestor();
                preencher(g, cpf, senha);
                GestorServico s = new GestorServico();
                if (pessoaEdicao == null) s.cadastrar(g); else s.atualizar(g);
            }

            if (cadastroInterno) {
                JOptionPane.showMessageDialog(this, "Cadastrado com sucesso!\nSenha provisória definida como o CPF do usuário.");
            }
            if (onSuccess != null) onSuccess.run();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencher(Pessoa p, String cpf, String senha) {
        p.setNome(txtNome.getText().trim());
        p.setSobrenome(txtSobrenome.getText().trim());
        p.setCpf(cpf);
        p.setEmail(txtEmail.getText().trim());
        p.setTelefone(txtTelefone.getText().trim());
        p.setSenha(senha);
    }
}