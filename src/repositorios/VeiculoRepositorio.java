package repositorios;

import java.util.List;
import java.util.UUID;

import dao.implementacao.VeiculoDao;
import entidades.Veiculo;
import repositorios.interfaces.Repositorio;

public class VeiculoRepositorio implements Repositorio<Veiculo, UUID> {

    private final VeiculoDao dao;

    public VeiculoRepositorio(VeiculoDao dao) {
        this.dao = dao;
    }

    @Override
    public Veiculo cadastrar(Veiculo veiculo) {
        return dao.inserir(veiculo);
    }

    @Override
    public Veiculo buscarPorId(UUID id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<Veiculo> buscarTodos() {
        return dao.buscarTodos();
    }

    @Override
    public void atualizar(Veiculo veiculo) {
        Veiculo cadastrado = dao.buscarPorId(veiculo.getId());
        if (cadastrado != null) {
            atualizarCampos(cadastrado, veiculo);
            dao.atualizar(cadastrado);
        }
    }

    @Override
    public void excluir(Veiculo veiculo) {
        dao.excluir(veiculo);
    }

    public void atualizarEstoque(Veiculo veiculo, Integer novasUnidades) {
        dao.atualizarEstoque(veiculo, novasUnidades);
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
}