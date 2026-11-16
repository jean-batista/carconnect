package servicos;

import config.AppConfig;
import entidades.Cliente;
import repositorios.ClienteRepositorio;

public class ClienteServico extends PessoaServicoBase<Cliente> {

    public ClienteServico() {
        super(AppConfig.configurarClienteRepositorio());
    }

    public ClienteServico(ClienteRepositorio repositorio) {
        super(repositorio);
    }
}