package repositorios;

import java.util.List;
import java.util.UUID;

import dao.interfaces.GestorDao;
import dao.interfaces.PessoaDao;
import dao.interfaces.VeiculoDao;
import entidades.Gestor;
import entidades.Pessoa;
import entidades.Veiculo;
import repositorios.interfaces.Repositorio;

public class GestorRepositorio implements Repositorio<Gestor, UUID> {

    private final GestorDao dao;
    private final PessoaDao pessoaDao;
    private final VeiculoDao veiculoDao;

    // Construtor desacoplado com injeção de dependências
    public GestorRepositorio(GestorDao dao, PessoaDao pessoaDao, VeiculoDao veiculoDao) {
        this.dao = dao;
        this.pessoaDao = pessoaDao;
        this.veiculoDao = veiculoDao;
    }

    @Override
    public Gestor cadastrar(Gestor gestor) {
        // Upcasting implícito: Gestor é uma Pessoa
        Pessoa pessoa = pessoaDao.inserir(gestor);
        Gestor gestorACadastrar = (Gestor) pessoa;
        
        // Cadastra o gestor na tabela_gestor
        dao.cadastrar(gestorACadastrar);

        return gestorACadastrar;
    }

    @Override
    public Gestor buscarPorId(UUID id) {
        Gestor gestor = dao.buscarGestor(id);
        if (gestor != null) {
            carregarVeiculos(gestor);
        }
        return gestor;
    }

    @Override
    public List<Gestor> buscarTodos() {
        List<Gestor> gestores = dao.listarGestores();
        for (Gestor gestor : gestores) {
            carregarVeiculos(gestor);
        }
        return gestores;
    }

    @Override
    public void atualizar(Gestor gestor) {
        Gestor cadastrado = dao.buscarGestor(gestor.getId());
        if (cadastrado != null) {
            atualizarCampos(cadastrado, gestor);
            pessoaDao.atualizar(cadastrado);
        }
    }

    @Override
    public void excluir(Gestor gestor) {
        // Devido ao ON DELETE CASCADE configurado na FK do MySQL,
        // excluir a Pessoa apaga automaticamente o registro da tabela_gestor e os vínculos em gestor_veiculo!
        pessoaDao.excluir(gestor);
    }

    private void carregarVeiculos(Gestor gestor) {
        if (gestor.getId() != null) {
            List<Veiculo> veiculos = veiculoDao.buscarVeiculosPorGestorId(gestor.getId());
            gestor.setVeiculos(veiculos);
        }
    }

    private void atualizarCampos(Gestor cadastrado, Gestor dados) {
        if (dados.getId() != null) cadastrado.setId(dados.getId());
        if (dados.getNome() != null) cadastrado.setNome(dados.getNome());
        if (dados.getSobrenome() != null) cadastrado.setSobrenome(dados.getSobrenome());
        if (dados.getEmail() != null) cadastrado.setEmail(dados.getEmail());
        if (dados.getTelefone() != null) cadastrado.setTelefone(dados.getTelefone());
        if (dados.getCpf() != null) cadastrado.setCpf(dados.getCpf());
        if (dados.getSenha() != null) cadastrado.setSenha(dados.getSenha());
        if (dados.getVeiculos() != null) cadastrado.setVeiculos(dados.getVeiculos());
    }
}