package autenticacao;

import entidades.Cliente;
import entidades.Gestor;
import entidades.Pessoa;
import entidades.Vendedor;

/**
 * Classe utilitária para manter o estado do usuário autenticado durante a execução da aplicação.
 */
public class SessaoUsuario {

    private static Pessoa usuarioLogado;

    public static void iniciarSessao(Pessoa usuario) {
        usuarioLogado = usuario;
    }

    public static void encerrarSessao() {
        usuarioLogado = null;
    }

    public static Pessoa getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isCliente() {
        return usuarioLogado instanceof Cliente;
    }

    public static boolean isVendedor() {
        return usuarioLogado instanceof Vendedor;
    }

    public static boolean isGestor() {
        return usuarioLogado instanceof Gestor;
    }

    public static String getTipoPerfil() {
        if (isGestor()) return "Gestor";
        if (isVendedor()) return "Vendedor";
        if (isCliente()) return "Cliente";
        return "Visitante";
    }
}
