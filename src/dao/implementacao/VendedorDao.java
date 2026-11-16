package dao.implementacao;

import java.sql.Connection;
import entidades.Vendedor;

public class VendedorDao extends PessoaSubclasseDao<Vendedor> {
    public VendedorDao(Connection connection) {
        super(connection, "tabela_vendedor", Vendedor::new);
    }
}