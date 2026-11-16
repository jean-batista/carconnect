package dao;

import banco.BancoDeDados;
import dao.implementacao.AgendamentoDao;
import dao.implementacao.ClienteDao;
import dao.implementacao.GestorDao;
import dao.implementacao.NegociacaoDao;
import dao.implementacao.PessoaDao;
import dao.implementacao.VeiculoDao;
import dao.implementacao.VendedorDao;

public class DaoFactory {

    public static PessoaDao criarPessoaDao() {
        return new PessoaDao(BancoDeDados.getConnection());
    }

    public static ClienteDao criarClienteDao() {
        return new ClienteDao(BancoDeDados.getConnection());
    }

    public static VendedorDao criarVendedorDao() {
        return new VendedorDao(BancoDeDados.getConnection());
    }

    public static GestorDao criarGestorDao() {
        return new GestorDao(BancoDeDados.getConnection());
    }

    public static VeiculoDao criarVeiculoDao() {
        return new VeiculoDao(BancoDeDados.getConnection());
    }

    public static AgendamentoDao criarAgendamentoDao() {
        return new AgendamentoDao(BancoDeDados.getConnection());
    }

    public static NegociacaoDao criarNegociacaoDao() {
        return new NegociacaoDao(BancoDeDados.getConnection());
    }
}