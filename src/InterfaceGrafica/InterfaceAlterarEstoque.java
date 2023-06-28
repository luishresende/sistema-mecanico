package InterfaceGrafica;

import BancoDeDados.TbFornecedor;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.NumberFormatter;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

class InterfaceAlterarEstoque extends JDialog {

    private final JTextField descricaoField;
    private final JFormattedTextField minimoField;
    private final JFormattedTextField maximoField;
    private final JFormattedTextField custoField;
    private final JFormattedTextField lucroField;
    private final JFormattedTextField finalField;
    private InterfaceEstoque panelFrame;
    private Session session;

    public InterfaceAlterarEstoque(InterfaceEstoque panelFrame, Session session, int estoId) {
        this.panelFrame = panelFrame;
        this.session = session;
        
        // DEFINIÇÃO DO LAYOUT -------------------------------------------------
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setTitle("Inserindo Produto");
        setResizable(false);
        // ---------------------------------------------------------------------
        JPanel mainPanel = new JPanel(null); // DEFINE O LAYOUT COMO NULL
        mainPanel.setPreferredSize(new Dimension(380, 320));
        Font fonte = new Font("Times New Roman", Font.BOLD, 16);
        // VERIFICAÇÃO SE A TELA ATUAL ESTÁ ABERTA -----------------------------
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Habilita panelFrame
                panelFrame.setVisible(true);
                panelFrame.setEnabled(true); // DEIXA A TELA ANTERIOR ABILITADA
                panelFrame.requestFocus();
            }
        });  
        // ---------------------------------------------------------------------
        // COMBOBOX DO TIPO DE UNIDADE DO PRODUTO ------------------------------
        JComboBox<String> listUnidade = new JComboBox<>();
        DefaultComboBoxModel<String> uni = new DefaultComboBoxModel<>();
        uni.addElement("Selecione..."); // PALAVRA QUE VAI FICAR ANTES DE APARACER A LISTA
        uni.addElement("UN");
        uni.addElement("KG");
        uni.addElement("PC");
        uni.addElement("MT");
        listUnidade.setFont(fonte);
        listUnidade.setModel(uni);
        // ---------------------------------------------------------------------
        
        // CONEXÃO COM O BANCO NA TABELA FORNECEDOR
        Criteria estd = session.createCriteria(TbFornecedor.class);
        ArrayList<TbFornecedor> fornecedor = (ArrayList<TbFornecedor>) estd.list();
        // COMBOBOX DO FORNECEDOR ----------------------------------------------
        JComboBox<String> listFornecedor = new JComboBox<>();
        DefaultComboBoxModel<String> forn = new DefaultComboBoxModel<>();
        forn.addElement("Selecione..."); // PALAVRA QUE VAI FICAR ANTES DE APARACER A LISTA
        listFornecedor.setModel(forn);
        listFornecedor.setFont(fonte);
        for (TbFornecedor descricao : fornecedor) {
            listFornecedor.addItem(descricao.getTbEntidade().getEntNome());
        }
        // ---------------------------------------------------------------------

        // Define o formato para números de ponto flutuante -------------------
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        decimalFormat.setParseBigDecimal(true);
        // Cria um NumberFormatter com o formato definido
        NumberFormatter formatter = new NumberFormatter(decimalFormat);
        formatter.setValueClass(Float.class);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        // ---------------------------------------------------------------------
        // Cria os campos de entrada formatados --------------------------------
        minimoField = new JFormattedTextField(formatter);
        minimoField.setFont(fonte);
        maximoField = new JFormattedTextField(formatter);
        maximoField.setFont(fonte);
        custoField = new JFormattedTextField(formatter);
        custoField.setFont(fonte);
        lucroField = new JFormattedTextField(formatter);
        lucroField.setFont(fonte);
        finalField = new JFormattedTextField(formatter);
        finalField.setFont(fonte);
        JLabel fornecedorLabel = new JLabel("Fornecedor");
        fornecedorLabel.setFont(fonte);
        // ---------------------------------------------------------------------
        
        // CRIANDO OS COMPONENTES LABEL DA TELA --------------------------------
        JLabel unidadeLabel = new JLabel("Unidade");
        unidadeLabel.setFont(fonte);
        JLabel descricaoLabel = new JLabel("Descrição do Produto");
        descricaoLabel.setFont(fonte);
        JLabel minimoLabel = new JLabel("Estoque Mínimo"); // float
        minimoLabel.setFont(fonte);
        JLabel maximoLabel = new JLabel("Estoque Atual"); // float
        maximoLabel.setFont(fonte);
        JLabel custoLabel = new JLabel("Valor Custo"); // float
        custoLabel.setFont(fonte);
        JLabel lucroLabel = new JLabel("Margem Lucro(%)"); // float 
        lucroLabel.setFont(fonte);
        JLabel finalLabel = new JLabel("VALOR FINAL:"); // float
        finalLabel.setFont(fonte);
        // ---------------------------------------------------------------------
        // CRIANDO AS CAIXAS DE DIALOGO ----------------------------------------
        descricaoField = new JTextField(20);
        // ---------------------------------------------------------------------
        // SETANDO AS FONTES PARA CADA CAIXA DE DIALOGO ------------------------
        descricaoField.setFont(fonte);
        // ---------------------------------------------------------------------
        
        // AÇÃO QUE ATUALIZA O VALOR TOTAL QUANDO INSERIDO O VALOR NO CUSTO ----
        custoField.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                atualizarValorTotal();
            }
        });
        // ---------------------------------------------------------------------
        // AÇÃO QUE ATUALIZA O VALOR TOTAL QUANDO INSERIDO O VALOR NO CUSTO ----
        lucroField.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                atualizarValorTotal();
            }
        });
        // ---------------------------------------------------------------------
        
        // BOTÃO ALTERA -----------------------------------------------------
        JButton alterar = new JButton();
        ImageIcon cads = new ImageIcon(ClassLoader.getSystemResource("resources/salvar.png"));
        Image scaledCads = cads.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
        alterar.setIcon(new ImageIcon(scaledCads));
        alterar.setBounds(140, 270, 100, 30);
        alterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hqlPecaId = "SELECT e.tbFornecedorHasPeca.tbPeca.peId FROM TbEstoque e WHERE e.estoId = '" + estoId + "'"; // BUSCA O ID DA PEÇA NA TABELA PEÇA
                String hqlForPecaId = "SELECT e.tbFornecedorHasPeca.fpId FROM TbEstoque e WHERE e.estoId = '" + estoId + "'"; // BUSCA O FORPECAID NA TABELA FORNECEDOR HAS PECA
                Query querySelectPeca = session.createQuery(hqlPecaId); // RECEBE O QUE FOI SELECIONADO DA TABELA PEÇA
                Query querySelectForPeca = session.createQuery(hqlForPecaId); // RECEBE O QUE FOI SELECIONADO DA TABELA FORNECEDOR HAS PECA
                
                Transaction transaction = session.beginTransaction();
                try {
                    int pecaId = (int) querySelectPeca.uniqueResult(); // RECEBE O ID DA PEÇA 
                    int fppeId = (int) querySelectForPeca.uniqueResult(); // RECEBE O ID FORNECEDOR HAS PECA QUE É O ID VINCULADO AO ID DA PEÇA
                    
                    // ATUALIZA NA PEÇA 
                    String hqlUpdateProduto = "UPDATE TbPeca pe SET pe.peDescricao = '" + descricaoField.getText() +"', " +
                                              "pe.peQuantMin = '" + minimoField.getText().replace(".","").replace(",",".") + "' " +
                                              "WHERE pe.peId = '" + pecaId + "'";
                    // ATUALIZA NA TABELA ESTOQUE
                    String hqlUpdateEstoque = "UPDATE TbEstoque es SET es.estoQuantidade = '" + maximoField.getText().replace(".","").replace(",",".") + "', " +
                                              "es.estoValorUni = '" + finalField.getText().replace(".","").replace(",",".") + "', " +
                                              "es.estoMargeLucro = '" + lucroField.getText().replace(",", ".") + "', " +
                                              "es.estoMedida = '" + listUnidade.getSelectedItem() + "' " +
                                              "WHERE es.estoId = '" + estoId + "'";
                    // ATUALIZA NA TABELA FORNECEDOR HAS PECA
                    String hqlUpdateHasFornecedor = "UPDATE TbFornecedorHasPeca fp SET fp.fpValorCompra = '" + custoField.getText().replace(".","").replace(",",".") + "' " +
                                                    "WHERE fp.fpId = '" + fppeId + "'";

                    Query queryUpdateProduto = session.createQuery(hqlUpdateProduto);
                    Query queryUpdateEstoque = session.createQuery(hqlUpdateEstoque);
                    Query queryUpdateFornePeca = session.createQuery(hqlUpdateHasFornecedor);
                   
                    queryUpdateProduto.executeUpdate();
                    queryUpdateEstoque.executeUpdate();
                    queryUpdateFornePeca.executeUpdate();

                    transaction.commit();
                    JOptionPane.showMessageDialog(null, "Peça atualizada com sucesso!");
                    dispose();
                    panelFrame.setEnabled(true); // HABILITA A TELA ANTERIOR
                } catch (HibernateException ex) {
                    transaction.rollback();
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    session.clear();
                }
            }
        });
        // ---------------------------------------------------------------------
        // ADICIONA OS COMPONENTES NO PAINEL PRINCIPAL -------------------------
        // Unidade
        mainPanel.add(unidadeLabel);
        mainPanel.add(listUnidade);
        // Descrição
        mainPanel.add(descricaoLabel);
        mainPanel.add(descricaoField);
        // Quantidade Minima
        mainPanel.add(minimoLabel);
        mainPanel.add(minimoField);
        minimoField.setValue(0.00f);
        minimoField.setSize(70, 21);
        // Quantidade Maxima
        mainPanel.add(maximoLabel);
        mainPanel.add(maximoField);
        maximoField.setValue(0.00f);
        maximoField.setSize(70, 21);
        // Valor Custo
        mainPanel.add(custoLabel);
        mainPanel.add(custoField);
        custoField.setValue(0.00f);
        custoField.setSize(70, 21);
        // Margem de Lucro
        mainPanel.add(lucroLabel);
        mainPanel.add(lucroField);
        lucroField.setValue(0.00f);
        lucroField.setSize(70, 21);
        // Valor Final
        mainPanel.add(finalLabel);
        mainPanel.add(finalField);
        finalField.setEditable(false);
        finalField.setValue(0.00f);
        finalField.setSize(70, 21);
        // Botão de cadastrar e limpar
        mainPanel.add(alterar);
        // Fornecedor
        mainPanel.add(fornecedorLabel);
        mainPanel.add(listFornecedor);
        // ---------------------------------------------------------------------
        // Define as coordenadas de posicionamento dos componentes -------------
        int x = 10;
        int y = 20;
        int yGap = 30;
        int labelWidth = 150;
        int fieldWidth = 200;

        unidadeLabel.setBounds(x, y, labelWidth, 20);
        listUnidade.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        descricaoLabel.setBounds(x, y, labelWidth, 20);
        descricaoField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        minimoLabel.setBounds(x, y, labelWidth, 20);
        minimoField.setBounds(x + labelWidth + 10, y, 100, 20);

        y += yGap;
        maximoLabel.setBounds(x, y, labelWidth, 20);
        maximoField.setBounds(x + labelWidth + 10, y, 100, 20);

        y += yGap;
        custoLabel.setBounds(x, y, labelWidth, 20);
        custoField.setBounds(x + labelWidth + 10, y, 100, 20);

        y += yGap;
        lucroLabel.setBounds(x, y, labelWidth, 20);
        lucroField.setBounds(x + labelWidth + 10, y, 100, 20);

        y += yGap;
        finalLabel.setBounds(x, y, labelWidth, 20);
        finalField.setBounds(x + labelWidth + 10, y, 100, 20);

        y += yGap;
        fornecedorLabel.setBounds(x, y, labelWidth, 20);
        listFornecedor.setBounds(x + labelWidth + 10, y, fieldWidth, 20);
        
        
        // Obtendo e inserindo todos os dados do produto
        String hql = "SELECT es.estoMedida, es.tbFornecedorHasPeca.tbPeca.peDescricao, es.tbFornecedorHasPeca.tbPeca.peQuantMin, es.estoQuantidade, es.tbFornecedorHasPeca.fpValorCompra, es.estoMargeLucro, es.tbFornecedorHasPeca.tbFornecedor.tbEntidade.entNome FROM TbEstoque es WHERE es.estoId = '" + estoId +"'";
        
        Query query = session.createQuery(hql);
        java.util.List<Object[]> results = query.list();
        for (Object[] result : results) {
            listUnidade.setSelectedItem(result[0].toString());
            descricaoField.setText(result[1].toString());
            minimoField.setValue(Float.parseFloat(result[2].toString()));
            maximoField.setValue(Float.parseFloat(result[3].toString()));
            System.out.println(result[4]);
            custoField.setValue(Float.parseFloat(result[4].toString()));
            lucroField.setValue(Float.parseFloat(result[5].toString()));
            listFornecedor.setSelectedItem(result[6].toString());
        }
        
        listFornecedor.setEnabled(false);
        atualizarValorTotal();
        // ---------------------------------------------------------------------
        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(panelFrame);
    }

    public void showInterface() {
        // Exibe a janela menor
        panelFrame.setEnabled(false);
        setVisible(true);
    }

    private void atualizarValorTotal() {
        Float custo = null;
        if (custoField.getValue() != null) {
            custo = (Float) custoField.getValue();
        }

        Float lucro = null;
        if (lucroField.getValue() != null) {
            lucro = (Float) lucroField.getValue();
        }

        if (custo != null && lucro != null) {
            Float valorTotal = custo + (custo * lucro / 100);
            System.out.println(valorTotal);
            finalField.setValue(valorTotal);
        }
    }
}
