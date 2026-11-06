package repositorios;

import java.util.List;
import java.util.UUID;

import dao.interfaces.ClienteDao;
import dao.interfaces.PessoaDao;
import entidades.Cliente;
import entidades.Pessoa;

public class ClienteRepositorio implements Repositorio<Cliente, UUID> {

    private final ClienteDao dao;
    private final PessoaDao pessoaDao;

    public ClienteRepositorio(ClienteDao dao, PessoaDao pessoaDao) {
        this.dao = dao;
        this.pessoaDao = pessoaDao;
    }

    @Override
    public Cliente cadastrar(Cliente cliente) {
        // Upcasting implícito: Cliente é uma Pessoa
        Pessoa pessoa = pessoaDao.inserir(cliente);
        Cliente clienteACadastrar = (Cliente) pessoa;
        return dao.cadastro(clienteACadastrar);
    }

    @Override
    public Cliente buscarPorId(UUID id) {
        Cliente cliente = dao.buscarClientePorId(id);
        if (cliente != null) {
            // List<Agendamento> visitas = agendamentoDao.buscarPorClienteId(id);
            // List<Negociacao> compras = negociacaoDao.buscarPorClienteId(id);
            // cliente.setVisitas(visitas);
            // cliente.setCompras(compras);
        }
        return cliente;
    }

    @Override
    public List<Cliente> buscarTodos() {
        List<Cliente> clientes = dao.buscarClientesCadastrados();
        for (Cliente cliente : clientes) {
            // List<Agendamento> visitas = agendamentoDao.buscarPorClienteId(cliente.getId());
            // List<Negociacao> compras = negociacaoDao.buscarPorClienteId(cliente.getId());
            // cliente.setVisitas(visitas);
            // cliente.setCompras(compras);
        }
        return clientes;
    }

    @Override
    public void atualizar(Cliente cliente) {
        // Busca o cliente atual do banco
        Cliente cadastrado = dao.buscarClientePorId(cliente.getId());
        if (cadastrado != null) {
            // Aplica as alterações apenas nos campos não nulos
            atualizarCampos(cadastrado, cliente);
            // Salva o objeto 'cadastrado' (com todos os dados mesclados) no banco
            pessoaDao.atualizar(cadastrado);
        }
    }

    @Override
    public void excluir(Cliente cliente) {
        // Devido ao ON DELETE CASCADE no MySQL, excluir a Pessoa apaga automaticamente o Cliente
        pessoaDao.excluir(cliente);
    }

    private void atualizarCampos(Cliente cadastrado, Cliente dados) {
        if (dados.getId() != null) cadastrado.setId(dados.getId());
        if (dados.getNome() != null) cadastrado.setNome(dados.getNome());
        if (dados.getSobrenome() != null) cadastrado.setSobrenome(dados.getSobrenome());
        if (dados.getEmail() != null) cadastrado.setEmail(dados.getEmail());
        if (dados.getTelefone() != null) cadastrado.setTelefone(dados.getTelefone());
        if (dados.getCpf() != null) cadastrado.setCpf(dados.getCpf());
        if (dados.getSenha() != null) cadastrado.setSenha(dados.getSenha());
    }
}