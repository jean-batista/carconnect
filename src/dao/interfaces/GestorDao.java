package dao.interfaces;

import java.util.List;
import java.util.UUID;

import entidades.Gestor;

public interface GestorDao {

    void cadastrar(Gestor gestor);
    Gestor buscarGestor(UUID id);
    List<Gestor> listarGestores();
    void atualizarCadastro(Gestor gestor);
    void excluirGestor(Gestor gestor);

}
