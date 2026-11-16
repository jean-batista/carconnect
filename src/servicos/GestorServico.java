package servicos;

import config.AppConfig;
import entidades.Gestor;
import repositorios.GestorRepositorio;

public class GestorServico extends PessoaServicoBase<Gestor> {

    public GestorServico() {
        super(AppConfig.configurarGestorRepositorio());
    }

    public GestorServico(GestorRepositorio repositorio) {
        super(repositorio);
    }
}