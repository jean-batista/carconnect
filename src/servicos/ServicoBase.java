package servicos;

import java.util.List;
import dao.DaoGenerico;

/**
 * Classe abstrata genérica que centraliza as operações CRUD básicas
 * e validações de nulidade/ID diretamente sobre o DaoGenerico.
 */
public abstract class ServicoBase<T, ID> {

    protected final DaoGenerico<T, ID> dao;

    protected ServicoBase(DaoGenerico<T, ID> dao) {
        this.dao = dao;
    }

    /**
     * Hook opcional para validações específicas de regra de negócio antes de salvar/atualizar.
     */
    protected void validar(T entidade) {
        if (entidade == null) {
            throw new IllegalArgumentException("A entidade não pode ser nula.");
        }
    }

    /**
     * Hook opcional para carregar relacionamentos (ex: Cliente e Vendedor em Agendamento).
     */
    protected void carregarRelacionamentos(T entidade) {
        // Por padrão não carrega relacionamentos extras
    }

    protected List<T> preencherLista(List<T> lista) {
        for (T item : lista) {
            carregarRelacionamentos(item);
        }
        return lista;
    }

    public T cadastrar(T entidade) {
        validar(entidade);
        return dao.inserir(entidade);
    }

    public T buscarPorId(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para busca não pode ser nulo.");
        }
        T entidade = dao.buscarPorId(id);
        if (entidade != null) {
            carregarRelacionamentos(entidade);
        }
        return entidade;
    }

    public List<T> buscarTodos() {
        return preencherLista(dao.buscarTodos());
    }

    public void atualizar(T entidade) {
        validar(entidade);
        dao.atualizar(entidade);
    }

    public void excluir(T entidade) {
        if (entidade == null) {
            throw new IllegalArgumentException("A entidade para exclusão não pode ser nula.");
        }
        dao.excluir(entidade);
    }
}