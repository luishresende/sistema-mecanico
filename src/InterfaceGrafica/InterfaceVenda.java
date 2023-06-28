package InterfaceGrafica;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

class InterfaceVenda extends InterfaceAbstrata {

    private final Session session;

    public InterfaceVenda(JFrame mainFrame, Session session) {
        super(mainFrame, session, "Vendas");
        this.session = session;
    }
    
    @Override
    protected JTable createTable(Session session) {

        String hql = "SELECT v.venId, v.tbCliente.tbEntidade.entNome, v.venData, v.venTotal, v.tbTipoPagamento.tpDescricao "
                + "FROM TbVenda v";
        Query query = session.createQuery(hql);
        String[] columnNames = {"ID", "Nome Cliente", "Valor Total", "Data", "Tipo Pagamento"};

        // Modelo da tabela não editável
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Object[]> results = query.list();
        for (Object[] result : results) {
            int id = (int) result[0];
            String nome = (String) result[1];
            java.sql.Timestamp timestamp = (java.sql.Timestamp) result[2];
            Float valorTotal = (Float) result[3];
            String pagamento = (String) result[4];

            model.addRow(new Object[]{id, nome, valorTotal, timestamp, pagamento});
        }

        // Cria a tabela
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        // Parte superior com os botões flutuantes

        JButton button = createSmallButton(buttonIcons[0]);
        JButton button2 = createSmallButton(buttonIcons[3]);
        String label = buttonLabels[0];
        String label2 = buttonLabels[3];

        if (label.equals("Inserir")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    InterfaceInsereVenda inserir = new InterfaceInsereVenda(mainFrame, session);
                    inserir.showInterface();
                }
            });
        } else if (label2.equals("Atualizar")) {
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateTableData(model);
                }
            });
        }

        buttonPanel.add(button);
        buttonPanel.add(button2);
        Font fonte = new Font("Times New Roman", Font.ROMAN_BASELINE, 14);
        table.setFont(fonte);
        return table;
    }

    private void updateTableData(DefaultTableModel model) {
        String hql = "SELECT v.venId, v.tbCliente.tbEntidade.entNome, v.venData, v.venTotal, v.tbTipoPagamento.tpDescricao"
                + " FROM TbVenda v";
        Query query = session.createQuery(hql);

        List<Object[]> results = query.list();
        model.setRowCount(0); // Limpa os dados da tabela antes de atualizar
        for (Object[] result : results) {
            int id = (int) result[0];
            String nome = (String) result[1];
            java.sql.Timestamp timestamp = (java.sql.Timestamp) result[2];
            Float valortotal = (Float) result[3];
            String pagamento = (String) result[4];

            model.addRow(new Object[]{id, nome, timestamp, valortotal, pagamento});
        }
    }
}
