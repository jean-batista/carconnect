package repositorios;

import java.util.List;

// O tipo T representa o tipo da classe a ser usada
// O tipo I representa o tipo de id que a classe usa
public interface Repositorio <T, I> {
    
    T cadastrar(T t);
    T buscarPorId(I i);
    List<T> buscarTodos();
    void atualizar(T t);
    void excluir(T t);

}
