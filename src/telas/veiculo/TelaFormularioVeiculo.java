package telas.veiculo;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entidades.Veiculo;
import servicos.VeiculoServico;
import telas.utils.UiUtils;

/**
 * Modal de Cadastro/Edição de Veículos.
 */
public class TelaFormularioVeiculo extends JDialog {

    private final VeiculoServico veiculoServico = new VeiculoServico();
    private final Veiculo veiculoEdicao;
    private final Runnable onSuccess;

    private JTextField txtMarca, txtModelo, txtAno, txtPreco, txtEstoque;
    private JComboBox<String> cbCombustivel, cbCondicao;

    public TelaFormularioVeiculo(Frame parent, Veiculo veiculoEdicao, Runnable onSuccess) {
        super(parent, true);
        this.veiculoEdicao = veiculoEdicao;
        this.onSuccess = onSuccess;

        setTitle(veiculoEdicao == null ? "Cadastrar Veículo" : "Editar Veículo");
        setSize(400, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        inicializarComponentes();
        preencherSeEdicao();
    }

    private void inicializarComponentes() {
        JPanel campos = new JPanel(new GridLayout(7, 2, 10, 12));
        campos.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

        campos.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        campos.add(txtMarca);

        campos.add(new JLabel("Modelo:"));
        txtModelo = new JTextField();
        campos.add(txtModelo);

        campos.add(new JLabel("Ano:"));
        txtAno = new JTextField();
        campos.add(txtAno);

        campos.add(new JLabel("Combustível:"));
        cbCombustivel = new JComboBox<>(new String[]{"Flex", "Gasolina", "Etanol", "Diesel", "Elétrico", "Híbrido"});
        campos.add(cbCombustivel);

        campos.add(new JLabel("Condição:"));
        cbCondicao = new JComboBox<>(new String[]{"Novo", "Seminovo", "Usado"});
        campos.add(cbCondicao);

        campos.add(new JLabel("Preço (R$):"));
        txtPreco = new JTextField();
        campos.add(txtPreco);

        campos.add(new JLabel("Estoque:"));
        txtEstoque = new JTextField();
        campos.add(txtEstoque);

        add(campos, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        botoes.add(UiUtils.criarBotao("Salvar", UiUtils.COR_SUCESSO, this::salvar));
        botoes.add(UiUtils.criarBotao("Cancelar", null, this::dispose));
        add(botoes, BorderLayout.SOUTH);
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

            if (veiculoEdicao == null) veiculoServico.cadastrar(v);
            else veiculoServico.atualizar(v);

            if (onSuccess != null) onSuccess.run();
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifique os campos numéricos (Ano, Preço e Estoque).");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}