package dao.interfaces;

import java.util.List;
import java.util.UUID;

import entidades.Gestor;

public interface GestorDao {

    void cadastrar(Gestor gestor);
    Gestor buscarGestor(UUID id);
    List<Gestor> listarGestores();
    void excluirGestor(Gestor gestor);

}
