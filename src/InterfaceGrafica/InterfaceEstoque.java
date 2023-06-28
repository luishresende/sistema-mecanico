package InterfaceGrafica;

import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

class InterfaceEstoque extends InterfaceAbstrata {

    private final Session session;
    private String selectedPeca; // Variável para armazenar o CPF selecionado
    //private InterfaceAbstrata panelFrame = this;
    private InterfaceEstoque panelFrame = this;

    public InterfaceEstoque(JFrame mainFrame, Session session) {
        super(mainFrame, session, "Estoque");
        this.session = session;
    }

    @Override
    protected JTable createTable(Session session) {
        // Código para buscar os dados do banco de dados usando Hibernate
        String hql = "SELECT e.estoId, e.tbFornecedorHasPeca.tbPeca.peDescricao, e.estoQuantidade, e.estoValorUni, e.tbFornecedorHasPeca.tbFornecedor.tbEntidade.entNomeFantasia FROM TbEstoque e";
        Query query = session.createQuery(hql);

        String[] columnNames = {"Vínculo ID", "Nome Produto", "Quantidade em Estoque", "Valor Unitário R$", "Fornecedor"};

        // Modelo da tabela não editável
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Object[]> results = query.list();
        for (Object[] result : results) {
            int estoId = (int) result[0];
            String nome = (String) result[1];
            float qtd_e = (float) result[2];
            Float valor = (Float) result[3];
            String fornecedor = (String) result[4];

            model.addRow(new Object[]{estoId, nome, qtd_e, valor, fornecedor}); // Adicione outras colunas conforme necessário
        }
        // Cria a tabela
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        // Parte superior com os botões flutuantes
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createSmallButton(buttonIcons[i]);
            String label = buttonLabels[i];

            if (label.equals("Inserir")) {
                button.addActionListener(e -> {
                    InterfaceInsereEstoque inserir = new InterfaceInsereEstoque(panelFrame, session);
                    inserir.showInterface();
                });
            } else if (label.equals("Alterar")) {
                button.addActionListener(e -> {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1){
                        int selectedEstoque = (int) table.getValueAt(selectedRow, 0);
                        InterfaceAlterarEstoque alterar = new InterfaceAlterarEstoque(panelFrame, session, selectedEstoque);
                        alterar.showInterface();
                    }
                });
            } else if (label.equals("Remover")) {
                button.addActionListener(e -> {
                    int selectedRow = table.getSelectedRow();

                    // Verifica se uma linha está selecionada
                    if (selectedRow != -1) {
                        // Obtém o CPF da linha selecionada
                        int selectedPeca = (int) table.getValueAt(selectedRow, 0);
                        Transaction transaction = session.beginTransaction();
                        try {
                            String hqlDeleteEstoque = "DELETE FROM TbEstoque e WHERE e.tbFornecedorHasPeca = '" + selectedPeca + "'";
                            Query deleteQueryEstoque = session.createQuery(hqlDeleteEstoque);
                            deleteQueryEstoque.executeUpdate();
                            
                            String hqlDeletePeca = "DELETE FROM TbPeca p WHERE peId in (SELECT fp.TbProduto.peId FROM TbFornecedorHasPeca fp WHERE fp.fpId = '" + selectedPeca + "')";
                            Query deleteQueryPeca = session.createQuery(hqlDeletePeca);
                            deleteQueryPeca.executeUpdate();

                            String hqlDeleteFornecedorHasPeca = "DELETE FROM TbFornecedorHasPeca fhp WHERE fhp.fpId = '" + selectedPeca + "'";
                            Query deleteQueryFornecedorHasPeca = session.createQuery(hqlDeleteFornecedorHasPeca);
                            deleteQueryFornecedorHasPeca.executeUpdate();
                                
                            transaction.commit();
                            JOptionPane.showMessageDialog(null, "Produto Removido");
                            updateTableData(model);
                        }   catch (org.hibernate.exception.ConstraintViolationException chaveEstrangeira) {
                            // Lidar com a exceção de violação de restrição de chave estrangeira
                            JOptionPane.showMessageDialog(null, "Ocorreu um erro: Produdo está vinculado a vendas já realizadas", "Erro", JOptionPane.ERROR_MESSAGE);
                        } catch (HibernateException ex) {
                            transaction.rollback();
                            JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            } else if (label.equals("Atualizar")) {
                button.addActionListener(e -> {
                    updateTableData(model);
                });
            }

            buttonPanel.add(button);
        }
        Font fonte = new Font("Times New Roman", Font.ROMAN_BASELINE, 14);
        table.setFont(fonte);
        return table;
    }

    public void updateTableData(DefaultTableModel model) {
        String hql = "SELECT e.estoId, e.tbFornecedorHasPeca.tbPeca.peDescricao, e.estoQuantidade, e.estoValorUni, e.tbFornecedorHasPeca.tbFornecedor.tbEntidade.entNomeFantasia FROM TbEstoque e";
        Query query = session.createQuery(hql);

        model.setRowCount(0); // Limpa os dados da tabela antes de atualizar
        List<Object[]> results = query.list();
        for (Object[] result : results) {
            int estoId = (int) result[0];
            String nome = (String) result[1];
            float qtd_e = (float) result[2];
            Float valor = (Float) result[3];
            String fornecedor = (String) result[4];

            model.addRow(new Object[]{estoId, nome, qtd_e, valor, fornecedor}); // Adicione outras colunas conforme necessário
        }
    }
}
