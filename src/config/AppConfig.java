package config;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import banco.BancoDeDados;
import dao.DaoFactory;
import dao.implementacao.*;
import entidades.Gestor;
import servicos.GestorServico;
import telas.autenticacao.TelaLogin;

public class AppConfig {

    public static void iniciar() {
        SwingUtilities.invokeLater(() -> {
            try {
                if (BancoDeDados.getConnection() == null) {
                    throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados.");
                }

                configurarAdministrador();

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
    }

    public static PessoaDao criarPessoaDao() { return DaoFactory.criarPessoaDao(); }
    public static ClienteDao criarClienteDao() { return DaoFactory.criarClienteDao(); }
    public static VendedorDao criarVendedorDao() { return DaoFactory.criarVendedorDao(); }
    public static GestorDao criarGestorDao() { return DaoFactory.criarGestorDao(); }
    public static VeiculoDao criarVeiculoDao() { return DaoFactory.criarVeiculoDao(); }
    public static AgendamentoDao criarAgendamentoDao() { return DaoFactory.criarAgendamentoDao(); }
    public static NegociacaoDao criarNegociacaoDao() { return DaoFactory.criarNegociacaoDao(); }

    private static void configurarAdministrador() {
        GestorServico servico = new GestorServico();
        List<Gestor> lista = servico.buscarTodos();

        if (lista.isEmpty()) {
            Gestor gestor = new Gestor(
                null,
                "Administrador",
                "Inicial",
                "administrador@inicial.com",
                "00000000000",
                "00000000000",
                "admin123"
            );
            servico.cadastrar(gestor);
        }
    }
}