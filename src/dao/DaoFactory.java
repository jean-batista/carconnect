package dao;

import banco.BancoDeDados;
import dao.implementacao.AgendamentoDaoImpl;
import dao.implementacao.ClienteDaoImpl;
import dao.implementacao.GestorDaoImpl;
import dao.implementacao.NegociacaoDaoImpl;
import dao.implementacao.PessoaDaoImpl;
import dao.implementacao.VeiculoDaoImpl;
import dao.implementacao.VendedorDaoImpl;
import dao.interfaces.AgendamentoDao;
import dao.interfaces.ClienteDao;
import dao.interfaces.GestorDao;
import dao.interfaces.NegociacaoDao;
import dao.interfaces.PessoaDao;
import dao.interfaces.VeiculoDao;
import dao.interfaces.VendedorDao;

public class DaoFactory {

    public static PessoaDao criarPessoaDao() {
        return new PessoaDaoImpl(BancoDeDados.getConnection());
    }

    public static ClienteDao criarClienteDao() {
        return new ClienteDaoImpl(BancoDeDados.getConnection());
    }

    public static VendedorDao criarVendedorDao() {
        return new VendedorDaoImpl(BancoDeDados.getConnection());
    }

    public static GestorDao criarGestorDao() {
        return new GestorDaoImpl(BancoDeDados.getConnection());
    }

    public static VeiculoDao criarVeiculoDao() {
        return new VeiculoDaoImpl(BancoDeDados.getConnection());
    }

    public static AgendamentoDao criarAgendamentoDao() {
        return new AgendamentoDaoImpl(BancoDeDados.getConnection());
    }

    public static NegociacaoDao criarNegociacaoDao() {
        return new NegociacaoDaoImpl(BancoDeDados.getConnection());
    }

}