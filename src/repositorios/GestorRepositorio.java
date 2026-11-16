package repositorios;

import dao.implementacao.GestorDao;
import dao.implementacao.PessoaDao;
import entidades.Gestor;

public class GestorRepositorio extends PessoaRepositorioBase<Gestor> {

    public GestorRepositorio(GestorDao dao, PessoaDao pessoaDao) {
        super(dao, pessoaDao);
    }
}