package servicos;

import java.util.UUID;

import entidades.Pessoa;
import repositorios.PessoaRepositorioBase;

/**
 * Classe abstrata intermediária que unifica as validações cadastrais
 * de Cliente, Vendedor e Gestor (Nome, CPF, E-mail e Senha).
 */
public abstract class PessoaServicoBase<T extends Pessoa> extends ServicoBase<T, UUID> {

    protected PessoaServicoBase(PessoaRepositorioBase<T> repositorio) {
        super(repositorio);
    }

    @Override
    protected void validar(T pessoa) {
        super.validar(pessoa);
        if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        if (pessoa.getCpf() == null || pessoa.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF é obrigatório.");
        }
        if (pessoa.getEmail() == null || !pessoa.getEmail().contains("@")) {
            throw new IllegalArgumentException("E-mail informado é inválido.");
        }
        if (pessoa.getSenha() == null || pessoa.getSenha().trim().length() < 6) {
            throw new IllegalArgumentException("A senha deve conter no mínimo 6 caracteres.");
        }
    }
}