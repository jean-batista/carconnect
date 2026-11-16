package config;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import banco.BancoDeDados;
import dao.DaoFactory;
import entidades.Gestor;
import repositorios.AgendamentoRepositorio;
import repositorios.ClienteRepositorio;
import repositorios.GestorRepositorio;
import repositorios.NegociacaoRepositorio;
import repositorios.VeiculoRepositorio;
import repositorios.VendedorRepositorio;
import servicos.GestorServico;
import telas.autenticacao.TelaLogin;

public class AppConfig {

    public static void iniciar() {
        // Inicia a interface gráfica na Event Dispatch Thread (EDT) do Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Valida se a conexão com o banco de dados está operando antes de abrir o sistema
                if (BancoDeDados.getConnection() == null) {
                    throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados.");
                }

                // Abre a tela inicial de autenticação
                TelaLogin telaLogin = new TelaLogin();
                telaLogin.setVisible(true);
                telaLogin.toFront();
                telaLogin.repaint();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Falha crítica ao iniciar o CarConnect:\n" + e.getMessage() +
                    "\n\nVerifique se o servidor MySQL está ativo e as credenciais estão corretas.",
                    "Erro de Inicialização",
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        });

        configurarAdministrador();
    }
    
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

    private static void configurarAdministrador() {
        GestorServico servico = new GestorServico();

        List<Gestor> lista = servico.buscarTodos();

        if(lista.isEmpty()) {

            Gestor gestor = new Gestor(
                null, 
                "Administrador", 
                "Inicial", 
                "administrador@inicial.com", 
                "00000000000", 
                "00000000000", 
                "admin123", 
                null
            );

            servico.cadastrar(gestor);
        }
    }

}
