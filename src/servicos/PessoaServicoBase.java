package servicos;

import java.util.UUID;

import dao.implementacao.PessoaDao;
import dao.implementacao.PessoaSubclasseDao;
import entidades.Pessoa;

/**
 * Classe abstrata intermediária que unifica validações cadastrais,
 * inserção em tabela_pessoa + subclasse e mesclagem de campos na atualização.
 */
public abstract class PessoaServicoBase<T extends Pessoa> extends ServicoBase<T, UUID> {

    protected final PessoaDao pessoaDao;

    protected PessoaServicoBase(PessoaSubclasseDao<T> dao, PessoaDao pessoaDao) {
        super(dao);
        this.pessoaDao = pessoaDao;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T cadastrar(T entidade) {
        validar(entidade);
        Pessoa pessoa = pessoaDao.inserir(entidade);
        return dao.inserir((T) pessoa);
    }

    @Override
    public void atualizar(T entidade) {
        validar(entidade);
        T cadastrado = dao.buscarPorId(entidade.getId());
        if (cadastrado != null) {
            atualizarCamposComuns(cadastrado, entidade);
            pessoaDao.atualizar(cadastrado);
        }
    }

    @Override
    public void excluir(T entidade) {
        if (entidade == null) {
            throw new IllegalArgumentException("A entidade para exclusão não pode ser nula.");
        }
        pessoaDao.excluir(entidade);
    }

    private void atualizarCamposComuns(T cadastrado, T dados) {
        if (dados.getNome() != null) cadastrado.setNome(dados.getNome());
        if (dados.getSobrenome() != null) cadastrado.setSobrenome(dados.getSobrenome());
        if (dados.getEmail() != null) cadastrado.setEmail(dados.getEmail());
        if (dados.getTelefone() != null) cadastrado.setTelefone(dados.getTelefone());
        if (dados.getCpf() != null) cadastrado.setCpf(dados.getCpf());
        if (dados.getSenha() != null) cadastrado.setSenha(dados.getSenha());
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