package repositorios;

import java.util.List;
import java.util.UUID;

import dao.interfaces.AgendamentoDao;
import dao.interfaces.NegociacaoDao;
import dao.interfaces.PessoaDao;
import dao.interfaces.VendedorDao;
import entidades.Agendamento;
import entidades.Negociacao;
import entidades.Pessoa;
import entidades.Vendedor;

public class VendedorRepositorio implements Repositorio<Vendedor, UUID> {

    private final VendedorDao dao;
    private final PessoaDao pessoaDao;
    private final AgendamentoDao agendamentoDao;
    private final NegociacaoDao negociacaoDao;

    // Construtor completo com injeção de dependências desacoplada
    public VendedorRepositorio(VendedorDao dao, PessoaDao pessoaDao, AgendamentoDao agendamentoDao, NegociacaoDao negociacaoDao) {
        this.dao = dao;
        this.pessoaDao = pessoaDao;
        this.agendamentoDao = agendamentoDao;
        this.negociacaoDao = negociacaoDao;
    }

    @Override
    public Vendedor cadastrar(Vendedor vendedor) {
        // Upcasting implícito: Vendedor é uma Pessoa
        Pessoa pessoa = pessoaDao.inserir(vendedor);
        Vendedor vendedorACadastrar = (Vendedor) pessoa;
        return dao.cadastro(vendedorACadastrar);
    }

    @Override
    public Vendedor buscarPorId(UUID id) {
        Vendedor vendedor = dao.buscarVendedorPorId(id);
        if (vendedor != null) {
            carregarRelacionamentos(vendedor);
        }
        return vendedor;
    }

    @Override
    public List<Vendedor> buscarTodos() {
        List<Vendedor> vendedores = dao.buscarVendedoresCadastrados();
        for (Vendedor vendedor : vendedores) {
            carregarRelacionamentos(vendedor);
        }
        return vendedores;
    }

    @Override
    public void atualizar(Vendedor vendedor) {
        Vendedor cadastrado = dao.buscarVendedorPorId(vendedor.getId());
        if (cadastrado != null) {
            atualizarCampos(cadastrado, vendedor);
            pessoaDao.atualizar(cadastrado);
        }
    }

    @Override
    public void excluir(Vendedor vendedor) {
        // Devido ao ON DELETE CASCADE no MySQL, excluir a Pessoa apaga automaticamente o Vendedor
        pessoaDao.excluir(vendedor);
    }

    private void carregarRelacionamentos(Vendedor vendedor) {
        if (vendedor.getId() != null) {
            List<Negociacao> vendas = negociacaoDao.buscarNegociacoesPorVendedor(vendedor.getId());
            List<Agendamento> agendamentos = agendamentoDao.buscarAgendamentosPorVendedor(vendedor.getId());
            vendedor.setVendas(vendas);
            vendedor.setAgendamentos(agendamentos);
        }
    }

    private void atualizarCampos(Vendedor cadastrado, Vendedor dados) {
        if (dados.getId() != null) cadastrado.setId(dados.getId());
        if (dados.getNome() != null) cadastrado.setNome(dados.getNome());
        if (dados.getSobrenome() != null) cadastrado.setSobrenome(dados.getSobrenome());
        if (dados.getEmail() != null) cadastrado.setEmail(dados.getEmail());
        if (dados.getTelefone() != null) cadastrado.setTelefone(dados.getTelefone());
        if (dados.getCpf() != null) cadastrado.setCpf(dados.getCpf());
        if (dados.getSenha() != null) cadastrado.setSenha(dados.getSenha());
    }
}