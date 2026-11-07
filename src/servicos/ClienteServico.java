package servicos;

import java.util.List;
import java.util.UUID;

import config.AppConfig;
import entidades.Cliente;
import repositorios.ClienteRepositorio;

/**
 * Camada de serviço para gerenciamento de Clientes.
 * Centraliza as regras de negócio e validações antes de acionar a persistência via ClienteRepositorio.
 */
public class ClienteServico {

    private final ClienteRepositorio clienteRepositorio;

    /**
     * Construtor padrão que utiliza o AppConfig para resolver
     * e injetar automaticamente a instância configurada do ClienteRepositorio.
     */
    public ClienteServico() {
        this.clienteRepositorio = AppConfig.configurarClienteRepositorio();
    }

    /**
     * Construtor sobrecarregado para permitir injeção manual (útil para testes unitários).
     * @param clienteRepositorio Instância do repositório de clientes.
     */
    public ClienteServico(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    /**
     * Cadastra um novo cliente com validações de regra de negócio.
     * @param cliente Objeto do cliente a ser cadastrado.
     * @return Cliente cadastrado com ID populado.
     */
    public Cliente cadastrar(Cliente cliente) {
        validarCliente(cliente);
        return clienteRepositorio.cadastrar(cliente);
    }

    /**
     * Busca um cliente pelo seu identificador único (UUID).
     * @param id Identificador do cliente.
     * @return Objeto Cliente com dados e relacionamentos carregados, ou null se não encontrado.
     */
    public Cliente buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para busca não pode ser nulo.");
        }
        return clienteRepositorio.buscarPorId(id);
    }

    /**
     * Retorna a lista de todos os clientes cadastrados.
     * @return Lista de clientes.
     */
    public List<Cliente> buscarTodos() {
        return clienteRepositorio.buscarTodos();
    }

    /**
     * Atualiza os dados de um cliente existente.
     * @param cliente Objeto contendo o ID e os novos dados a serem mesclados.
     */
    public void atualizar(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("O cliente e seu ID são obrigatórios para atualização.");
        }
        clienteRepositorio.atualizar(cliente);
    }

    /**
     * Exclui o cadastro de um cliente.
     * @param cliente Objeto do cliente a ser removido.
     */
    public void excluir(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("O cliente e seu ID são obrigatórios para exclusão.");
        }
        clienteRepositorio.excluir(cliente);
    }

    /**
     * Validações básicas de negócio para o cliente.
     */
    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente não pode ser nulo.");
        }
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do cliente é obrigatório.");
        }
        if (cliente.getEmail() == null || !cliente.getEmail().contains("@")) {
            throw new IllegalArgumentException("E-mail informado é inválido.");
        }
    }
}