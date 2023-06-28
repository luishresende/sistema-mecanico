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

class InterfaceInsereEstoque extends JDialog {

    private InterfaceEstoque panelFrame;
    private final JTextField descricaoField;
    private final JFormattedTextField minimoField;
    private final JFormattedTextField maximoField;
    private final JFormattedTextField custoField;
    private final JFormattedTextField lucroField;
    private final JFormattedTextField finalField;
    private final Session session;

    public InterfaceInsereEstoque(InterfaceEstoque panelFrame, Session session) {
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
        
        // VERIFICAÇÃO SE A TELA ATUAL ESTÁ ABERTA -----------------------------
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Habilita panelFrame
                panelFrame.setEnabled(true); // DEIXA A TELA ANTERIOR ABILITADA
                panelFrame.requestFocus();
            }
        });  
        // ---------------------------------------------------------------------

        Font fonte = new Font("Times New Roman", Font.BOLD, 16);
        // COMBOBOX DO TIPO DE UNIDADE DO PRODUTO ------------------------------
        JComboBox<String> listUnidade = new JComboBox<>();
        DefaultComboBoxModel<String> prod = new DefaultComboBoxModel<>();
        prod.addElement("Selecione..."); // PALAVRA QUE VAI FICAR ANTES DE APARACER A LISTA DE TODOS OS ESTADOS
        prod.addElement("UN");
        prod.addElement("KG");
        prod.addElement("PC");
        prod.addElement("MT");
        listUnidade.setFont(fonte);
        listUnidade.setModel(prod);
        // ---------------------------------------------------------------------

        // CONEXÃO COM O BANCO NA TABELA FORNECEDOR
        Criteria estd = session.createCriteria(TbFornecedor.class);
        ArrayList<TbFornecedor> fornecedor = (ArrayList<TbFornecedor>) estd.list();
        // COMBOBOX DO FORNECEDOR ----------------------------------------------
        JComboBox<String> listFornecedor = new JComboBox<>();
        DefaultComboBoxModel<String> forn = new DefaultComboBoxModel<>();
        forn.addElement("Selecione..."); // PALAVRA QUE VAI FICAR ANTES DE APARACER AS LITA DE TODOS OS ESTADOS
        listFornecedor.setModel(forn);
        listFornecedor.setFont(fonte);
        for (TbFornecedor descricao : fornecedor) {
            listFornecedor.addItem(descricao.getTbEntidade().getEntNome());
        }
        // ---------------------------------------------------------------------

        // Define o formato para números de ponto flutuante --------------------
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
        JLabel fornecedorLabel = new JLabel("Fornecedor");
        fornecedorLabel.setFont(fonte);
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

        // BOTÃO CADASTRAR -----------------------------------------------------
        JButton cadastrar = new JButton();
        ImageIcon cads = new ImageIcon(ClassLoader.getSystemResource("resources/salvar.png"));
        Image scaledCads = cads.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
        cadastrar.setIcon(new ImageIcon(scaledCads));
        cadastrar.setBounds(70, 270, 100, 30);
        cadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Transaction transaction = session.beginTransaction();
                try {
                    String hql = "SELECT f.forId FROM TbFornecedor f WHERE f.tbEntidade.entNome = '" + listFornecedor.getSelectedItem() + "'";
                    Query query = session.createQuery(hql);

                    int fornId = (int) query.uniqueResult();

                    BancoDeDados.TbPeca tbpeca = new BancoDeDados.TbPeca();
                    tbpeca.setPeDescricao(descricaoField.getText());
                    tbpeca.setPeQuantMin(Float.parseFloat(minimoField.getText().replace(",", ".")));
                    session.save(tbpeca);

                    Object forne = session.load(TbFornecedor.class, fornId);
                    BancoDeDados.TbFornecedorHasPeca tbfornecedor = new BancoDeDados.TbFornecedorHasPeca();
                    tbfornecedor.setTbFornecedor((TbFornecedor) forne);
                    tbfornecedor.setTbPeca(tbpeca);
                    tbfornecedor.setFpValorCompra(Float.parseFloat(custoField.getText().replace(",", ".")));
                    session.save(tbfornecedor);

                    BancoDeDados.TbEstoque tbestoque = new BancoDeDados.TbEstoque();
                    tbestoque.setTbFornecedorHasPeca(tbfornecedor);
                    tbestoque.setEstoMedida((String) listUnidade.getSelectedItem());
                    tbestoque.setEstoMargeLucro(Float.parseFloat(lucroField.getText().replace(",", ".")));
                    tbestoque.setEstoQuantidade(Float.parseFloat(maximoField.getText().replace(",", ".")));
                    tbestoque.setEstoValorUni(Float.parseFloat(finalField.getText().replace(".", "").replace(",", ".")));

                    session.save(tbestoque);

                    transaction.commit();
                    JOptionPane.showMessageDialog(null, "Peça Inserida no Estoque com Sucesso!");
                    dispose();
                    panelFrame.setEnabled(true); // ABILITA A TEL ANTERIOR
                    panelFrame.requestFocus();
                } catch (HibernateException ex) {
                    transaction.rollback();
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    session.clear();
                }
            }
        });
        // ---------------------------------------------------------------------
        // BOTÃO LIMPAR --------------------------------------------------------
        JButton limparCampos = new JButton();
        limparCampos = new JButton();
        ImageIcon limp = new ImageIcon(ClassLoader.getSystemResource("resources/limpar.png"));
        Image scaledLimpar = limp.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
        limparCampos.setIcon(new ImageIcon(scaledLimpar));
        limparCampos.setBounds(200, 270, 100, 30);
        limparCampos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                session.clear();
                limparCampos();
                listUnidade.setSelectedIndex(0);
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
        mainPanel.add(cadastrar);
        mainPanel.add(limparCampos);
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
        // ---------------------------------------------------------------------
        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(panelFrame);
    }

    private void limparCampos() {
        descricaoField.setText("");
        minimoField.setValue(0.00f);
        maximoField.setValue(0.00f);
        custoField.setValue(0.00f);
        lucroField.setValue(0.00f);
        finalField.setValue(0.00f);
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
            finalField.setValue(valorTotal);
        }
    }
}
