package config;

import dao.DaoFactory;
import repositorios.AgendamentoRepositorio;
import repositorios.ClienteRepositorio;
import repositorios.GestorRepositorio;
import repositorios.NegociacaoRepositorio;
import repositorios.VeiculoRepositorio;
import repositorios.VendedorRepositorio;

public class AppConfig {
    
    public static ClienteRepositorio configurarClienteRepositorio() {
        return new ClienteRepositorio(
            DaoFactory.criarClienteDao(), 
            DaoFactory.criarPessoaDao(), 
            DaoFactory.criarAgendamentoDao(), 
            DaoFactory.criarNegociacaoDao()
        );
    }

    public static VendedorRepositorio configurarVendedorRepositorio() {
        return new VendedorRepositorio(
            DaoFactory.criarVendedorDao(), 
            DaoFactory.criarPessoaDao(), 
            DaoFactory.criarAgendamentoDao(), 
            DaoFactory.criarNegociacaoDao()
        );
    }

    public static GestorRepositorio configurarGestorRepositorio() {
        return new GestorRepositorio(
            DaoFactory.criarGestorDao(), 
            DaoFactory.criarPessoaDao(), 
            DaoFactory.criarVeiculoDao()
        );
    }

    public static VeiculoRepositorio configurarVeiculoRepositorio() {
        return new VeiculoRepositorio(DaoFactory.criarVeiculoDao());
    }

    public static AgendamentoRepositorio configurarAgendamentoRepositorio() {
        return new AgendamentoRepositorio(
            DaoFactory.criarAgendamentoDao(), 
            DaoFactory.criarClienteDao(), 
            DaoFactory.criarVendedorDao()
        );
    }

    public static NegociacaoRepositorio configurarNegociacaoRepositorio() {
        return new NegociacaoRepositorio(
            DaoFactory.criarNegociacaoDao(),
            DaoFactory.criarClienteDao(),
            DaoFactory.criarVendedorDao(),
            DaoFactory.criarVeiculoDao()
        );
    }

}
