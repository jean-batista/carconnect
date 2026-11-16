package dao.implementacao;

import java.sql.Connection;
import entidades.Cliente;

public class ClienteDao extends PessoaSubclasseDao<Cliente> {
    public ClienteDao(Connection connection) {
        super(connection, "tabela_cliente", Cliente::new);
    }
}