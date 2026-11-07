package servicos;

import java.util.List;
import java.util.UUID;

import config.AppConfig;
import entidades.Gestor;
import repositorios.GestorRepositorio;

/**
 * Camada de serviço para gerenciamento de Gestores.
 * Centraliza as regras de negócio e validações antes de acionar a persistência via GestorRepositorio.
 */
public class GestorServico {

    private final GestorRepositorio gestorRepositorio;

    /**
     * Construtor padrão que utiliza o AppConfig para resolver
     * e injetar automaticamente a instância configurada do GestorRepositorio.
     */
    public GestorServico() {
        this.gestorRepositorio = AppConfig.configurarGestorRepositorio();
    }

    /**
     * Construtor sobrecarregado para permitir injeção manual (útil para testes unitários).
     * @param gestorRepositorio Instância do repositório de gestores.
     */
    public GestorServico(GestorRepositorio gestorRepositorio) {
        this.gestorRepositorio = gestorRepositorio;
    }

    /**
     * Cadastra um novo gestor aplicando as validações de regra de negócio.
     * @param gestor Objeto do gestor a ser cadastrado.
     * @return Gestor cadastrado.
     */
    public Gestor cadastrar(Gestor gestor) {
        validarGestor(gestor);
        return gestorRepositorio.cadastrar(gestor);
    }

    /**
     * Busca um gestor por seu ID (UUID).
     * @param id Identificador do gestor.
     * @return Objeto Gestor com dados e lista de veículos vinculados populados.
     */
    public Gestor buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para busca não pode ser nulo.");
        }
        return gestorRepositorio.buscarPorId(id);
    }

    /**
     * Retorna a lista completa de gestores cadastrados.
     * @return Lista de gestores.
     */
    public List<Gestor> buscarTodos() {
        return gestorRepositorio.buscarTodos();
    }

    /**
     * Atualiza os dados cadastrais do gestor.
     * @param gestor Objeto com as alterações a serem aplicadas.
     */
    public void atualizar(Gestor gestor) {
        if (gestor == null || gestor.getId() == null) {
            throw new IllegalArgumentException("O gestor e seu ID são obrigatórios para atualização.");
        }
        gestorRepositorio.atualizar(gestor);
    }

    /**
     * Exclui o cadastro do gestor.
     * @param gestor Objeto do gestor a ser excluído.
     */
    public void excluir(Gestor gestor) {
        if (gestor == null || gestor.getId() == null) {
            throw new IllegalArgumentException("O gestor e seu ID são obrigatórios para exclusão.");
        }
        gestorRepositorio.excluir(gestor);
    }

    /**
     * Regras de validação de dados cadastrais do gestor.
     */
    private void validarGestor(Gestor gestor) {
        if (gestor == null) {
            throw new IllegalArgumentException("O gestor não pode ser nulo.");
        }
        if (gestor.getNome() == null || gestor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do gestor é obrigatório.");
        }
        if (gestor.getCpf() == null || gestor.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do gestor é obrigatório.");
        }
        if (gestor.getEmail() == null || !gestor.getEmail().contains("@")) {
            throw new IllegalArgumentException("E-mail informado é inválido.");
        }
        if (gestor.getSenha() == null || gestor.getSenha().trim().length() < 6) {
            throw new IllegalArgumentException("A senha do gestor deve conter no mínimo 6 caracteres.");
        }
    }
}