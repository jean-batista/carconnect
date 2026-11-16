package telas.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Utilitário centralizado para criação de componentes Swing padronizados,
 * evitando repetição de estilização em todas as telas.
 */
public class UiUtils {

    public static final Color COR_PRIMARIA = new Color(25, 42, 86);
    public static final Color COR_SECUNDARIA = new Color(39, 60, 117);
    public static final Color COR_SUCESSO = new Color(39, 174, 96);
    public static final Color COR_PERIGO = new Color(192, 57, 43);
    public static final Color COR_FUNDO = new Color(245, 246, 250);

    public static JButton criarBotao(String texto, Color fundo, Runnable acao) {
        JButton btn = new JButton(texto);
        if (fundo != null) {
            btn.setBackground(fundo);
            btn.setForeground(Color.WHITE);
        }
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (acao != null) {
            btn.addActionListener(e -> acao.run());
        }
        return btn;
    }

    public static DefaultTableModel criarModeloTabela(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static JTable criarTabela(DefaultTableModel modelo) {
        JTable tabela = new JTable(modelo);
        tabela.setRowHeight(24);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        return tabela;
    }

    /**
     * Wrapper genérico reutilizado em todos os JComboBox do sistema.
     */
    public static class ItemCombo<T> {
        private final T valor;
        private final String rotulo;

        public ItemCombo(T valor, String rotulo) {
            this.valor = valor;
            this.rotulo = rotulo;
        }

        public T getValor() {
            return valor;
        }

        @Override
        public String toString() {
            return rotulo;
        }
    }
}