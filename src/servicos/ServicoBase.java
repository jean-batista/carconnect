package servicos;

import java.util.List;
import repositorios.interfaces.Repositorio;

/**
 * Classe abstrata genérica que centraliza as operações CRUD básicas
 * e validações de nulidade/ID para todos os serviços do sistema.
 */
public abstract class ServicoBase<T, ID> {

    protected final Repositorio<T, ID> repositorio;

    protected ServicoBase(Repositorio<T, ID> repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Hook opcional para validações específicas de regra de negócio antes de salvar/atualizar.
     */
    protected void validar(T entidade) {
        if (entidade == null) {
            throw new IllegalArgumentException("A entidade não pode ser nula.");
        }
    }

    public T cadastrar(T entidade) {
        validar(entidade);
        return repositorio.cadastrar(entidade);
    }

    public T buscarPorId(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para busca não pode ser nulo.");
        }
        return repositorio.buscarPorId(id);
    }

    public List<T> buscarTodos() {
        return repositorio.buscarTodos();
    }

    public void atualizar(T entidade) {
        validar(entidade);
        repositorio.atualizar(entidade);
    }

    public void excluir(T entidade) {
        if (entidade == null) {
            throw new IllegalArgumentException("A entidade para exclusão não pode ser nula.");
        }
        repositorio.excluir(entidade);
    }
}