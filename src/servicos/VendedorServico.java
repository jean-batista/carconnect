package servicos;

import config.AppConfig;
import dao.implementacao.AgendamentoDao;
import dao.implementacao.NegociacaoDao;
import dao.implementacao.PessoaDao;
import dao.implementacao.VendedorDao;
import entidades.Vendedor;

public class VendedorServico extends PessoaServicoBase<Vendedor> {

    private final AgendamentoDao agendamentoDao;
    private final NegociacaoDao negociacaoDao;

    public VendedorServico() {
        this(
            AppConfig.criarVendedorDao(),
            AppConfig.criarPessoaDao(),
            AppConfig.criarAgendamentoDao(),
            AppConfig.criarNegociacaoDao()
        );
    }

    public VendedorServico(VendedorDao dao, PessoaDao pessoaDao, AgendamentoDao agendamentoDao, NegociacaoDao negociacaoDao) {
        super(dao, pessoaDao);
        this.agendamentoDao = agendamentoDao;
        this.negociacaoDao = negociacaoDao;
    }

    @Override
    protected void carregarRelacionamentos(Vendedor vendedor) {
        if (vendedor.getId() != null) {
            vendedor.setVendas(negociacaoDao.buscarPorVendedorId(vendedor.getId()));
            vendedor.setAgendamentos(agendamentoDao.buscarPorVendedorId(vendedor.getId()));
        }
    }
}