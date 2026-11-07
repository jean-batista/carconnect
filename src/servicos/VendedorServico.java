package servicos;

import java.util.List;
import java.util.UUID;

import config.AppConfig;
import entidades.Vendedor;
import repositorios.VendedorRepositorio;

/**
 * Camada de serviço para gerenciamento de Vendedores.
 * Centraliza as regras de negócio e validações antes de acionar a persistência via VendedorRepositorio.
 */
public class VendedorServico {

    private final VendedorRepositorio vendedorRepositorio;

    /**
     * Construtor padrão que utiliza o AppConfig para resolver
     * e injetar automaticamente a instância configurada do VendedorRepositorio.
     */
    public VendedorServico() {
        this.vendedorRepositorio = AppConfig.configurarVendedorRepositorio();
    }

    /**
     * Construtor sobrecarregado para permitir injeção manual (útil para testes unitários).
     * @param vendedorRepositorio Instância do repositório de vendedores.
     */
    public VendedorServico(VendedorRepositorio vendedorRepositorio) {
        this.vendedorRepositorio = vendedorRepositorio;
    }

    /**
     * Cadastra um novo vendedor aplicando as validações de regra de negócio.
     * @param vendedor Objeto do vendedor a ser cadastrado.
     * @return Vendedor cadastrado.
     */
    public Vendedor cadastrar(Vendedor vendedor) {
        validarVendedor(vendedor);
        return vendedorRepositorio.cadastrar(vendedor);
    }

    /**
     * Busca um vendedor por seu ID (UUID).
     * @param id Identificador do vendedor.
     * @return Objeto Vendedor com dados e listas (vendas, agendamentos) populados.
     */
    public Vendedor buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para busca não pode ser nulo.");
        }
        return vendedorRepositorio.buscarPorId(id);
    }

    /**
     * Retorna a lista completa de vendedores cadastrados.
     * @return Lista de vendedores.
     */
    public List<Vendedor> buscarTodos() {
        return vendedorRepositorio.buscarTodos();
    }

    /**
     * Atualiza os dados cadastrais do vendedor.
     * @param vendedor Objeto com as alterações a serem aplicadas.
     */
    public void atualizar(Vendedor vendedor) {
        if (vendedor == null || vendedor.getId() == null) {
            throw new IllegalArgumentException("O vendedor e seu ID são obrigatórios para atualização.");
        }
        vendedorRepositorio.atualizar(vendedor);
    }

    /**
     * Exclui o cadastro do vendedor.
     * @param vendedor Objeto do vendedor a ser excluído.
     */
    public void excluir(Vendedor vendedor) {
        if (vendedor == null || vendedor.getId() == null) {
            throw new IllegalArgumentException("O vendedor e seu ID são obrigatórios para exclusão.");
        }
        vendedorRepositorio.excluir(vendedor);
    }

    /**
     * Regras de validação de dados cadastrais do vendedor.
     */
    private void validarVendedor(Vendedor vendedor) {
        if (vendedor == null) {
            throw new IllegalArgumentException("O vendedor não pode ser nulo.");
        }
        if (vendedor.getNome() == null || vendedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do vendedor é obrigatório.");
        }
        if (vendedor.getCpf() == null || vendedor.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do vendedor é obrigatório.");
        }
        if (vendedor.getEmail() == null || !vendedor.getEmail().contains("@")) {
            throw new IllegalArgumentException("E-mail informado é inválido.");
        }
    }
}