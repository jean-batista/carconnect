package telas.veiculo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entidades.Veiculo;
import servicos.VeiculoServico;

/**
 * Tela 3.2: Modal de Cadastro/Edição de Veículos.
 */
public class TelaFormularioVeiculo extends JDialog {

    private final VeiculoServico veiculoServico;
    private final Veiculo veiculoEdicao;
    private final Runnable onSuccess;

    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtAno;
    private JComboBox<String> cbCombustivel;
    private JComboBox<String> cbCondicao;
    private JTextField txtPreco;
    private JTextField txtEstoque;

    public TelaFormularioVeiculo(Frame parent, Veiculo veiculoEdicao, Runnable onSuccess) {
        super(parent, true);
        this.veiculoServico = new VeiculoServico();
        this.veiculoEdicao = veiculoEdicao;
        this.onSuccess = onSuccess;

        configurarJanela();
        inicializarComponentes();
        preencherSeEdicao();
    }

    private void configurarJanela() {
        setTitle(veiculoEdicao == null ? "Cadastrar Veículo" : "Editar Veículo");
        setSize(420, 440);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        JPanel painelCampos = new JPanel(new GridLayout(7, 2, 10, 12));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        painelCampos.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        painelCampos.add(txtMarca);

        painelCampos.add(new JLabel("Modelo:"));
        txtModelo = new JTextField();
        painelCampos.add(txtModelo);

        painelCampos.add(new JLabel("Ano:"));
        txtAno = new JTextField();
        painelCampos.add(txtAno);

        painelCampos.add(new JLabel("Combustível:"));
        cbCombustivel = new JComboBox<>(new String[]{"Flex", "Gasolina", "Etanol", "Diesel", "Elétrico", "Híbrido"});
        painelCampos.add(cbCombustivel);

        painelCampos.add(new JLabel("Condição:"));
        cbCondicao = new JComboBox<>(new String[]{"Novo", "Seminovo", "Usado"});
        painelCampos.add(cbCondicao);

        painelCampos.add(new JLabel("Preço (R$):"));
        txtPreco = new JTextField();
        painelCampos.add(txtPreco);

        painelCampos.add(new JLabel("Quantidade em Estoque:"));
        txtEstoque = new JTextField();
        painelCampos.add(txtEstoque);

        add(painelCampos, BorderLayout.CENTER);

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

    private void preencherSeEdicao() {
        if (veiculoEdicao != null) {
            txtMarca.setText(veiculoEdicao.getMarca());
            txtModelo.setText(veiculoEdicao.getModelo());
            txtAno.setText(String.valueOf(veiculoEdicao.getAno()));
            cbCombustivel.setSelectedItem(veiculoEdicao.getCombustivel());
            cbCondicao.setSelectedItem(veiculoEdicao.getCondicao());
            txtPreco.setText(String.valueOf(veiculoEdicao.getPreco()));
            txtEstoque.setText(String.valueOf(veiculoEdicao.getQuantidadeEstoque()));
        }
    }

    private void salvar() {
        try {
            Veiculo v = (veiculoEdicao != null) ? veiculoEdicao : new Veiculo();
            v.setMarca(txtMarca.getText().trim());
            v.setModelo(txtModelo.getText().trim());
            v.setAno(Integer.parseInt(txtAno.getText().trim()));
            v.setCombustivel((String) cbCombustivel.getSelectedItem());
            v.setCondicao((String) cbCondicao.getSelectedItem());
            v.setPreco(Double.parseDouble(txtPreco.getText().trim().replace(",", ".")));
            v.setQuantidadeEstoque(Integer.parseInt(txtEstoque.getText().trim()));

            if (veiculoEdicao == null) {
                veiculoServico.cadastrar(v);
            } else {
                veiculoServico.atualizar(v);
            }

            if (onSuccess != null) onSuccess.run();
            dispose();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Verifique os campos numéricos (Ano, Preço e Estoque).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar veículo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}