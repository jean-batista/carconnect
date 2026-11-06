package banco;

/*
    ==============================================================================
    == Classe responsavel por lancar as excecoes relacionadas ao banco de dados ==
    ==============================================================================
*/

public class BancoException extends RuntimeException {
    
    // Metodo reponsavel por lancar uma excecao
    public BancoException(String mensagem) {
        super(mensagem); // Envia a mensagem para o construtor da superclasse
    }
}
