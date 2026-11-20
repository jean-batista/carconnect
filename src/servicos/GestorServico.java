package servicos;

import config.AppConfig;
import dao.implementacao.GestorDao;
import dao.implementacao.PessoaDao;
import entidades.Gestor;

public class GestorServico extends PessoaServicoBase<Gestor> {

    public GestorServico() {
        this(AppConfig.criarGestorDao(), AppConfig.criarPessoaDao());
    }

    public GestorServico(GestorDao dao, PessoaDao pessoaDao) {
        super(dao, pessoaDao);
    }
}