package InterfaceGrafica;

import BancoDeDados.TbCliente;
import BancoDeDados.TbEstoque;
import BancoDeDados.TbTipoPagamento;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

class InterfaceInsereVenda extends JFrame {

    protected static boolean isSmallWindowOpen = false;
    private final JFrame smallFrame;
    private final JFrame mainFrame;
    private final JFormattedTextField valorItemField;
    private final JFormattedTextField descontoField;
    private final JFormattedTextField totalVendaField;
    private Session session;
    protected String[] buttonLabels = {"Inserir Produto", "Inserir Serviço", "Excluir", "Concluir Venda"};
    protected String[] buttonIcons = {"resources/inserirproduto.png", "resources/inserirservico.png",
        "resources/excluir2.png", "resources/finalizar.png"};
    protected JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private BancoDeDados.TbVenda tbvenda;
    private ArrayList<Object[]> vendaItems = new ArrayList<>();
    private DefaultTableModel model;
    private JComboBox<String> listCliente;
    private JComboBox<String> listPagamento;
    private Float ValorTotal = 0.00f;
    private float totalValor = 0.00f;

    public InterfaceInsereVenda(JFrame mainFrame, Session session) {
        this.smallFrame = new JFrame("Inserindo Vendas"); // TELA ATUAL
        this.mainFrame = mainFrame; // TELA ANTERIOR
        this.session = session;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.6);
        int height = (int) (screenSize.height * 0.6);
        smallFrame.setSize(width, height);
        smallFrame.setResizable(false);
        smallFrame.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        // Verifica se a janela menor está aberta
        isSmallWindowOpen = false;

        // Painel da janela menor
        JPanel smallPanel = new JPanel();
        smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.Y_AXIS));

        // Define o formato para números de ponto flutuante
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        decimalFormat.setParseBigDecimal(true);

        // Cria um NumberFormatter com o formato definido
        NumberFormatter formatter = new NumberFormatter(decimalFormat);
        formatter.setValueClass(Float.class);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        // PAINEL DE VALORES ----------------------------------------------------
        // Adiciona os componentes acima da tabela
        JPanel valores = new JPanel(new FlowLayout(FlowLayout.CENTER));

        Font fonte = new Font("Times New Roman", Font.BOLD, 20);
        JLabel valorItemLabel = new JLabel("Valor do Item");
        valorItemLabel.setFont(fonte);
        valorItemField = new JFormattedTextField(formatter);
        valorItemField.setValue(0.00f);
        valorItemField.setFont(fonte);
        valorItemField.setPreferredSize(new Dimension(100, 30));
        valorItemField.setEditable(false);

        JLabel descontoLabel = new JLabel("Desconto");
        descontoLabel.setFont(fonte);
        descontoField = new JFormattedTextField(formatter);
        descontoField.setValue(0.00f);
        descontoField.setFont(fonte);
        descontoField.setPreferredSize(new Dimension(100, 30));

        Font fonte2 = new Font("Times New Roman", Font.BOLD, 26);
        JLabel totalVendaLabel = new JLabel("TOTAL R$");
        totalVendaLabel.setFont(fonte);
        totalVendaField = new JFormattedTextField(formatter);
        totalVendaField.setValue(0.00f);
        totalVendaField.setFont(fonte2);
        totalVendaField.setEditable(false);
        totalVendaField.setPreferredSize(new Dimension(200, 50));

        valores.add(valorItemLabel);
        valores.add(valorItemField);
        valores.add(descontoLabel);
        valores.add(descontoField);
        valores.add(totalVendaLabel);
        valores.add(totalVendaField);
        // ------------------------------------------------------------------------

        // DEFINOÇÕES PARA O COMBOBOX ---------------------------------------------
        Font box = new Font("Times New Roman", Font.BOLD, 18);
        JPanel comboBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // CONEXÃO COM O BANCO TB_CLIENTE
        String hql = "SELECT cli.tbEntidade.entNome FROM TbCliente cli";
        Query query = session.createQuery(hql);
        java.util.List<String> clientes = (java.util.List<String>) query.list();

        // COMBOBOX DO CLIENTE
        listCliente = new JComboBox<>();
        DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>();
        model2.addElement("Cliente...");
        for (String cliente : clientes) {
            model2.addElement(cliente);
        }
        listCliente.setModel(model2);
        listCliente.setFont(box);
        comboBox.add(listCliente);

        // CONEXÃO COM O BANCO TB_TIPOPAGAMENTO
        Criteria pgm = session.createCriteria(TbTipoPagamento.class);
        ArrayList<TbTipoPagamento> pagamento = (ArrayList<TbTipoPagamento>) pgm.list();
        // COMBOBOX DO PAGAMENTO
        listPagamento = new JComboBox<>();
        DefaultComboBoxModel<String> pagm = new DefaultComboBoxModel<>();
        pagm.addElement("Tipo de Pagamento...");
        listPagamento.setModel(pagm);
        for (TbTipoPagamento desc : pagamento) {
            listPagamento.addItem(desc.getTpDescricao());
        }
        listPagamento.setFont(box);
        comboBox.add(listPagamento);
        // ------------------------------------------------------------------------

        // TABELA -----------------------------------------------------------------
        JTable table = createTable(session, tbvenda);
        table.setEnabled(true); // Torna a tabela não editável
        // -----------------------------------------------------------------------

        // Adiciona os painéis no painel da janela menor
        smallPanel.add(comboBox);
        smallPanel.add(buttonPanel);
        smallPanel.add(new JScrollPane(table), BorderLayout.NORTH);
        smallPanel.add(valores);

        // Adiciona o painel da janela menor na janela menor
        smallFrame.getContentPane().add(smallPanel);

        // Centraliza a janela menor em relação à janela principal
        int x = mainFrame.getX() + (mainFrame.getWidth() - smallFrame.getWidth()) / 2;
        int y = mainFrame.getY() + (mainFrame.getHeight() - smallFrame.getHeight()) / 2;
        smallFrame.setLocation(x, y);

        // Configura um listener para quando a janela menor for fechada
        smallFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                isSmallWindowOpen = false;
                // Habilita a janela principal
                mainFrame.setEnabled(true);
                mainFrame.requestFocus();
            }
        });
    }

    public void showInterface() {
        // Desabilita a janela anterior (mainFrame)
        mainFrame.setEnabled(false);
        // Exibe a janela atual (smallFrame)
        smallFrame.setVisible(true);
    }

    // TABELA PARA INSERIR OS DADOS DA VENDA
    protected JButton createSmallButton(String iconPath) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(150, 50));
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(iconPath));
        Image scaledImage = icon.getImage().getScaledInstance(120, 45, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setFocusPainted(false);
        return button;
    }

    protected JTable createTable(Session session, BancoDeDados.TbVenda tbvenda) {
        // Criação da tabela e modelo
        String[] columnNames = {"Tipo", "Descrição", "Valor Serviço/Peça", "Quantidade", "KM Percorrido", "Valor/KM"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1 || column == 2) { // Impede a edição das colunas "Descrição" e "Valor"
                    return false;
                }
                return true; // Permite a edição das outras colunas
            }
        };
        String[] columnNames2 = {"ID", "Nome Produto", "Quantidade em Estoque", "Valor Unitário", "Quantidade Mínima", "Fornecedor"};
        DefaultTableModel model2 = new DefaultTableModel(columnNames2, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // Cria a tabela
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (row >= 0 && column >= 0) {
                    Object novoValor = model.getValueAt(row, column);
                    int novoValorInt = Integer.parseInt((String) novoValor);
                    vendaItems.get(row)[column] = novoValorInt;
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int column3 = 2; // Índice da coluna 3
                int column4 = 3; // Índice da coluna 4

                if (row >= 0 && column3 >= 0 && column4 >= 0) {
                    // Obtém os valores das colunas 3 e 4
                    String valorColuna3 = table.getValueAt(row, column3).toString();
                    String valorColuna4 = table.getValueAt(row, column4).toString();

                    try {
                        // Converte os valores para float
                        float valor3 = Float.parseFloat(valorColuna3);
                        float valor4 = Float.parseFloat(valorColuna4);
                        float valorMultiplicado = valor3 * valor4;

                        // Define o valor no campo valorItemField
                        valorItemField.setValue(valorMultiplicado);
                    } catch (NumberFormatException ex) {
                        // Tratar caso os valores não sejam números válidos
                        valorItemField.setValue(0.0f);
                    }
                }
            }
        });
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.UPDATE) {
                    float totalVenda = 0.0f;

                    for (int row = 0; row < table.getRowCount(); row++) {
                        String valorColuna3 = table.getValueAt(row, 2).toString();
                        String valorColuna4 = table.getValueAt(row, 3).toString();
                        String valorColuna5 = table.getValueAt(row, 4).toString();
                        String valorColuna6 = table.getValueAt(row, 5).toString();
                        try {
                            float valor3 = Float.parseFloat(valorColuna3);
                            float valor4 = Float.parseFloat(valorColuna4);
                            float valor5 = Float.parseFloat(valorColuna5);
                            float valor6 = Float.parseFloat(valorColuna6);
                            float valorMultiplicado = valor3 * valor4 + (valor5 * valor6);

                            totalVenda += valorMultiplicado;
                        } catch (NumberFormatException ex) {
                            // Tratar caso os valores não sejam números válidos
                        }
                    }
                    totalValor = totalVenda;
                    totalVendaField.setValue(totalVenda);
                }
            }
        });

        descontoField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarTotalVenda();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarTotalVenda();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarTotalVenda();
            }

            private void atualizarTotalVenda() {
                try {
                    float novoDesconto = Float.parseFloat(descontoField.getText().replace(",", "."));
                    totalVendaField.setValue(totalValor - novoDesconto);
                } catch (NumberFormatException ex) {
                    // Tratar exceção se o valor inserido não for um número válido
                    totalVendaField.setValue(totalValor);
                }
            }
        });

        // Parte superior com os botões flutuantes
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createSmallButton(buttonIcons[i]);
            String label = buttonLabels[i];

            if (label.equals("Inserir Produto")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Criação da janela com a tabela
                            JDialog dialog = new JDialog(smallFrame, "Selecione um produto");
                            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                            dialog.setSize(600, 400);
                            dialog.setResizable(false); // A TELA NÃO PODE SER ALTERADO O TAMANHO
                            dialog.setLocationRelativeTo(smallFrame); // DEIXA A TELA DE SELECIONAR PRODUTO NO MEIO
                            smallFrame.setVisible(true); // DEIXA A TELA DE VENDA VISIVEL
                            smallFrame.setEnabled(false); // A TELA DE VENDA FICA DESATIVADA
                            // VERIFICAÇÃO SE A TELA ATUAL ESTÁ ABERTA -----------------------------
                            /* Essa verificação, é caso o usuário feche a janela sem selecionar nada, 
                             para não ocorrer de fechar a tela que tem a tabela com os produtos inseridos na venda */
                            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                                @Override
                                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                                    // Habilita panelFrame
                                    smallFrame.setVisible(true);
                                    smallFrame.setEnabled(true); // DEIXA A TELA ANTERIOR ABILITADA
                                    smallFrame.requestFocus();
                                }
                            });
                            // ---------------------------------------------------------------------
                            InterfaceEstoque tabela = new InterfaceEstoque(mainFrame, session);
                            tabela.updateTableData(model2);
                            // Criação da tabela dentro do dialog
                            JTable dialogTable = new JTable(model2);
                            JScrollPane dialogScrollPane = new JScrollPane(dialogTable);
                            dialog.add(dialogScrollPane);

                            // Adiciona o MouseListener para capturar o clique duplo na tabela
                            dialogTable.addMouseListener(new MouseInputAdapter() {
                                public void mouseClicked(MouseEvent e) {
                                    if (e.getClickCount() == 2) { // Verifica se foi um clique duplo
                                        int selectedRow = dialogTable.getSelectedRow();
                                        if (selectedRow != -1) {
                                            String selectedProduct = (String) dialogTable.getValueAt(selectedRow, 1);
                                            Float selectedProduct_valor = (Float) dialogTable.getValueAt(selectedRow, 3);
                                            Object[] vendaItem = {"Produto", selectedProduct, selectedProduct_valor, 0, 0, 0};
                                            vendaItems.add(vendaItem);
                                            atualizarTabela();

                                            smallFrame.setEnabled(true); // ATIVA A TELA DE VENDA
                                            dialog.dispose(); // FECHA A JANELA DE SELECIONAR PRODUTO
                                        }
                                    }
                                }
                            });

                            dialog.setVisible(true);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            } else if (label.equals("Inserir Serviço")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton salvar = new JButton();
                        InterfaceInsereServico inserir = new InterfaceInsereServico(mainFrame, session, salvar);
                        inserir.showInterface();
                        salvar.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                vendaItems.add(inserir.getServicoInputs());
                                inserir.dispose();
                                atualizarTabela();
                            }
                        });
                    }
                });
            } else if (label.equals("Excluir")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int indiceSelecionado = table.getSelectedRow();
                        if (indiceSelecionado >= 0) {
                            vendaItems.remove(indiceSelecionado);
                            atualizarTabela();
                        }
                    }
                });
            } else if (label.equals("Concluir Venda")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (listCliente.getSelectedItem() == "Inserir Cliente..." || listPagamento.getSelectedItem() == "Tipo de Pagamento...") {
                            JOptionPane.showMessageDialog(null, "Informe um Cliente/Pagamento");
                        } else {
                            InsereBanco();
                        }
                    }
                });
            }
            buttonPanel.add(button);
        }

        return table;
    }

    public void atualizarTabela() {
        model.setRowCount(0); // Limpa o modelo da tabela

        for (Object[] item : vendaItems) {
            model.addRow(item); // Adiciona cada item ao modelo da tabela
        }
    }

    public void adicionarServico(Object[] servicoInputs) {
        vendaItems.add(servicoInputs);
        atualizarTabela();
    }

    private void InsereBanco() {
        Transaction transaction = session.beginTransaction();
        String hql2 = "SELECT c.cliId FROM TbCliente c WHERE c.tbEntidade.entNome = '" + listCliente.getSelectedItem() + "'";
        Query query2 = session.createQuery(hql2);

        String hql3 = "SELECT p.tpId FROM TbTipoPagamento p WHERE p.tpDescricao = '" + listPagamento.getSelectedItem() + "'";
        Query query3 = session.createQuery(hql3);
        try {
            int PagId = (int) query3.uniqueResult();
            int CliId = (int) query2.uniqueResult();

            BancoDeDados.TbVenda tbven = new BancoDeDados.TbVenda();
            Object cliente = session.load(TbCliente.class, CliId);
            tbven.setTbCliente((TbCliente) cliente);
            Object pagamento = session.load(TbTipoPagamento.class, PagId);
            tbven.setTbTipoPagamento((TbTipoPagamento) pagamento);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            tbven.setVenData(timestamp);
            tbven.setVenTotal(ValorTotal);
            session.save(tbven);

            for (Object[] item : vendaItems) {
                if ("Produto".equals(item[0])) {
                    ValorTotal = ValorTotal + ((int) item[3] * (float) item[2]);
                    String hql = "SELECT e.estoId FROM TbEstoque e WHERE e.tbFornecedorHasPeca.tbPeca.peDescricao = '" + item[1] + "'";
                    Query query = session.createQuery(hql);
                    int EstoqueId = (int) query.uniqueResult();

                    String EstoqueAtual = "SELECT e.estoQuantidade FROM TbEstoque e WHERE e.tbFornecedorHasPeca.tbPeca.peDescricao = '" + item[1] + "'";
                    Query queryestoqueqnt = session.createQuery(EstoqueAtual);

                    String UpdateEstoque = "UPDATE TbEstoque e SET e.estoQuantidade = " + ((float) queryestoqueqnt.uniqueResult() - (int) item[3]) + "WHERE e.estoId = '" + EstoqueId + "'";
                    Query queryestoqueup = session.createQuery(UpdateEstoque);
                    queryestoqueup.executeUpdate();

                    BancoDeDados.TbVenPeca tbvenpeca = new BancoDeDados.TbVenPeca();
                    Object estoque = session.load(TbEstoque.class, EstoqueId);
                    tbvenpeca.setTbEstoque((TbEstoque) estoque);
                    tbvenpeca.setTbVenda(tbven);
                    tbvenpeca.setVpQuantidade((Integer) item[3]);
                    session.save(tbvenpeca);
                } else {
                    ValorTotal = ValorTotal + (float) item[2] + ((float) item[4] * (float) item[5]);
                    BancoDeDados.TbVeiculo tbveiculo = new BancoDeDados.TbVeiculo();
                    tbveiculo.setVeiMarca((String) item[7]);
                    tbveiculo.setVeiModelo((String) item[6]);
                    tbveiculo.setVeiPlaca((String) item[8]);
                    session.save(tbveiculo);

                    BancoDeDados.TbVendaSer tbvens = new BancoDeDados.TbVendaSer();
                    tbvens.setTbVenda(tbven);
                    tbvens.setVsSerDescricao((String) item[1]);
                    tbvens.setTbVeiculo(tbveiculo);
                    tbvens.setVsValorKm((float) item[5]);
                    tbvens.setVsKmPercorrido((float) item[4]);
                    tbvens.setVsValorServico((float) item[2]);
                    session.save(tbvens);
                }
            }

            tbven.setVenTotal(ValorTotal - Float.parseFloat(descontoField.getText().replace(",", ".")));
            session.save(tbven);
            transaction.commit();
            JOptionPane.showMessageDialog(null, "Venda Concluida");
            smallFrame.dispose();
        } catch (HibernateException ex) {
            transaction.rollback();
            Throwable rootCause = ex.getCause();
            SQLException sqlException = (SQLException) rootCause;
            String errorMessage = sqlException.getMessage();
            JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + errorMessage, "Erro", JOptionPane.ERROR_MESSAGE);
            session.clear();
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
        }

    }
}
