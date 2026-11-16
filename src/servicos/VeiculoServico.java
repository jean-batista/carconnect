package servicos;

import java.time.Year;
import java.util.UUID;

import config.AppConfig;
import entidades.Veiculo;
import repositorios.VeiculoRepositorio;

public class VeiculoServico extends ServicoBase<Veiculo, UUID> {

    public VeiculoServico() {
        super(AppConfig.configurarVeiculoRepositorio());
    }

    public VeiculoServico(VeiculoRepositorio repositorio) {
        super(repositorio);
    }

    public void atualizarEstoque(Veiculo veiculo, Integer novasUnidades) {
        if (veiculo == null || veiculo.getId() == null) {
            throw new IllegalArgumentException("O veículo e seu ID são obrigatórios.");
        }
        if (novasUnidades == null || novasUnidades < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
        ((VeiculoRepositorio) repositorio).atualizarEstoque(veiculo, novasUnidades);
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