package repositorios;

import dao.implementacao.AgendamentoDao;
import dao.implementacao.ClienteDao;
import dao.implementacao.NegociacaoDao;
import dao.implementacao.PessoaDao;
import entidades.Cliente;

public class ClienteRepositorio extends PessoaRepositorioBase<Cliente> {

    private final AgendamentoDao agendamentoDao;
    private final NegociacaoDao negociacaoDao;

    public ClienteRepositorio(ClienteDao dao, PessoaDao pessoaDao, AgendamentoDao agendamentoDao, NegociacaoDao negociacaoDao) {
        super(dao, pessoaDao);
        this.agendamentoDao = agendamentoDao;
        this.negociacaoDao = negociacaoDao;
    }

    @Override
    protected void carregarRelacionamentos(Cliente cliente) {
        if (cliente.getId() != null) {
            cliente.setVisitas(agendamentoDao.buscarPorClienteId(cliente.getId()));
            cliente.setCompras(negociacaoDao.buscarPorClienteId(cliente.getId()));
        }
    }
}