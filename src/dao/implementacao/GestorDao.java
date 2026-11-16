package dao.implementacao;

import java.sql.Connection;
import entidades.Gestor;

public class GestorDao extends PessoaSubclasseDao<Gestor> {
    public GestorDao(Connection connection) {
        super(connection, "tabela_gestor", Gestor::new);
    }
}