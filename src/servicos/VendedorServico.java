package servicos;

import config.AppConfig;
import entidades.Vendedor;
import repositorios.VendedorRepositorio;

public class VendedorServico extends PessoaServicoBase<Vendedor> {

    public VendedorServico() {
        super(AppConfig.configurarVendedorRepositorio());
    }

    public VendedorServico(VendedorRepositorio repositorio) {
        super(repositorio);
    }
}