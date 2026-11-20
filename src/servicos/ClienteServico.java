package servicos;

import config.AppConfig;
import dao.implementacao.AgendamentoDao;
import dao.implementacao.ClienteDao;
import dao.implementacao.NegociacaoDao;
import dao.implementacao.PessoaDao;
import entidades.Cliente;

public class ClienteServico extends PessoaServicoBase<Cliente> {

    private final AgendamentoDao agendamentoDao;
    private final NegociacaoDao negociacaoDao;

    public ClienteServico() {
        this(
            AppConfig.criarClienteDao(),
            AppConfig.criarPessoaDao(),
            AppConfig.criarAgendamentoDao(),
            AppConfig.criarNegociacaoDao()
        );
    }

    public ClienteServico(ClienteDao dao, PessoaDao pessoaDao, AgendamentoDao agendamentoDao, NegociacaoDao negociacaoDao) {
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