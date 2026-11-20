package servicos;

import java.time.Year;
import java.util.UUID;

import config.AppConfig;
import dao.implementacao.VeiculoDao;
import entidades.Veiculo;

public class VeiculoServico extends ServicoBase<Veiculo, UUID> {

    public VeiculoServico() {
        super(AppConfig.criarVeiculoDao());
    }

    public VeiculoServico(VeiculoDao dao) {
        super(dao);
    }

    @Override
    public void atualizar(Veiculo veiculo) {
        validar(veiculo);
        Veiculo cadastrado = dao.buscarPorId(veiculo.getId());
        if (cadastrado != null) {
            atualizarCampos(cadastrado, veiculo);
            dao.atualizar(cadastrado);
        }
    }

    public void atualizarEstoque(Veiculo veiculo, Integer novasUnidades) {
        if (veiculo == null || veiculo.getId() == null) {
            throw new IllegalArgumentException("O veículo e seu ID são obrigatórios.");
        }
        if (novasUnidades == null || novasUnidades < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
        ((VeiculoDao) dao).atualizarEstoque(veiculo, novasUnidades);
    }

    private void atualizarCampos(Veiculo cadastrado, Veiculo dados) {
        if (dados.getMarca() != null) cadastrado.setMarca(dados.getMarca());
        if (dados.getModelo() != null) cadastrado.setModelo(dados.getModelo());
        if (dados.getAno() != null) cadastrado.setAno(dados.getAno());
        if (dados.getCombustivel() != null) cadastrado.setCombustivel(dados.getCombustivel());
        if (dados.getCondicao() != null) cadastrado.setCondicao(dados.getCondicao());
        if (dados.getPreco() != null) cadastrado.setPreco(dados.getPreco());
        if (dados.getQuantidadeEstoque() != null) cadastrado.setQuantidadeEstoque(dados.getQuantidadeEstoque());
    }

    @Override
    protected void validar(Veiculo v) {
        super.validar(v);
        if (v.getMarca() == null || v.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("A marca do veículo é obrigatória.");
        }
        if (v.getModelo() == null || v.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("O modelo do veículo é obrigatório.");
        }
        if (v.getPreco() == null || v.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço do veículo deve ser maior que zero.");
        }
        if (v.getQuantidadeEstoque() != null && v.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
        int anoAtual = Year.now().getValue();
        if (v.getAno() == null || v.getAno() < 1900 || v.getAno() > anoAtual + 1) {
            throw new IllegalArgumentException("Ano do veículo inválido.");
        }
    }
}