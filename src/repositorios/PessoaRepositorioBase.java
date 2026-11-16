package repositorios;

import java.util.List;
import java.util.UUID;

import dao.implementacao.PessoaDao;
import dao.implementacao.PessoaSubclasseDao;
import entidades.Pessoa;
import repositorios.interfaces.Repositorio;

/**
 * Classe abstrata genérica que centraliza o CRUD e mesclagem de atributos
 * para todas as subclasses de Pessoa (Cliente, Vendedor e Gestor).
 */
public abstract class PessoaRepositorioBase<T extends Pessoa> implements Repositorio<T, UUID> {

    protected final PessoaSubclasseDao<T> dao;
    protected final PessoaDao pessoaDao;

    protected PessoaRepositorioBase(PessoaSubclasseDao<T> dao, PessoaDao pessoaDao) {
        this.dao = dao;
        this.pessoaDao = pessoaDao;
    }

    /**
     * Hook opcional para subclasses carregarem relacionamentos específicos (ex: visitas, vendas).
     */
    protected void carregarRelacionamentos(T entidade) {
        // Por padrão não carrega relacionamentos extras (ex: Gestor)
    }

    @Override
    @SuppressWarnings("unchecked")
    public T cadastrar(T entidade) {
        Pessoa pessoa = pessoaDao.inserir(entidade);
        return dao.inserir((T) pessoa);
    }

    @Override
    public T buscarPorId(UUID id) {
        T entidade = dao.buscarPorId(id);
        if (entidade != null) {
            carregarRelacionamentos(entidade);
        }
        return entidade;
    }

    @Override
    public List<T> buscarTodos() {
        List<T> lista = dao.buscarTodos();
        for (T entidade : lista) {
            carregarRelacionamentos(entidade);
        }
        return lista;
    }

    @Override
    public void atualizar(T entidade) {
        T cadastrado = dao.buscarPorId(entidade.getId());
        if (cadastrado != null) {
            atualizarCamposComuns(cadastrado, entidade);
            pessoaDao.atualizar(cadastrado);
        }
    }

    @Override
    public void excluir(T entidade) {
        pessoaDao.excluir(entidade);
    }

    private void atualizarCamposComuns(T cadastrado, T dados) {
        if (dados.getNome() != null) cadastrado.setNome(dados.getNome());
        if (dados.getSobrenome() != null) cadastrado.setSobrenome(dados.getSobrenome());
        if (dados.getEmail() != null) cadastrado.setEmail(dados.getEmail());
        if (dados.getTelefone() != null) cadastrado.setTelefone(dados.getTelefone());
        if (dados.getCpf() != null) cadastrado.setCpf(dados.getCpf());
        if (dados.getSenha() != null) cadastrado.setSenha(dados.getSenha());
    }
}