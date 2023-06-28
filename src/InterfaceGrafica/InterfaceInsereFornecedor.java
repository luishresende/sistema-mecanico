package InterfaceGrafica;

import BancoDeDados.TbCidEst;
import BancoDeDados.TbEstado;
import BancoDeDados.TbLogradouro;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.MaskFormatter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

class InterfaceInsereFornecedor extends JDialog {
    private InterfaceFornecedor panelFrame;
    private JTextField nomeField;
    private JFormattedTextField documentoField;
    private JTextField fantasiaField;
    private JFormattedTextField ieField;
    private JFormattedTextField foneField;
    private JTextField emailField;
    private JTextField enderecoField;
    private JTextField bairroField;
    private JFormattedTextField cepField;
    private JTextField logradouroField;
    private JTextField numeroField;
    private JTextField complementoField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JRadioButton pessoaFisicaRadioButton;
    private JRadioButton pessoaJuridicaRadioButton;
    private JTextField dataNascimentoField;
    private Session session;
    
    private final MaskFormatter cnpj;
    private final MaskFormatter ie;
    private final MaskFormatter fone;
    private final MaskFormatter cep;
    private final MaskFormatter dataNascimento;
    

    public InterfaceInsereFornecedor(InterfaceFornecedor panelFrame, Session session) throws ParseException {
        this.panelFrame = panelFrame;
        this.session = session;
        
        // DEFINIÇÃO DO LAYOUT -------------------------------------------------
        JPanel mainPanel = new JPanel(null); // Define o layout como null
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setTitle("Inserindo dados do fornecedor");
        mainPanel.setPreferredSize(new Dimension(380, 550));
        // ---------------------------------------------------------------------
        Font fonte = new Font("Times New Roman", Font.ROMAN_BASELINE, 14); // FONTE DA PÁGINA
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
        // CONEXÃO COM O BANCO NA TABELA ESTADO
        String EST = "SELECT est.estSigla FROM TbEstado est";
        Query estd = session.createQuery(EST);
        List<String> estado = (List<String>) estd.list();
        
        // COMBOBOX DO ESTADO --------------------------------------------------
        JComboBox<String> listEstado = new JComboBox<>();
        DefaultComboBoxModel<String> est = new DefaultComboBoxModel<>();
        est.addElement("Selecione..."); // PALAVRA QUE VAI FICAR ANTES DE APARACER AS LITA DE TODOS OS ESTADOS
        listEstado.setModel(est);
        listEstado.setFont(fonte);
        for (String estados : estado) {
            listEstado.addItem(estados);
        }
        // ---------------------------------------------------------------------

        // COMBOBOX DA CIDADE --------------------------------------------------
        JComboBox<String> listCidade = new JComboBox<>();
        listCidade.setFont(fonte);

        listEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedEstado = (String) listEstado.getSelectedItem();

                // Obter as cidades correspondentes ao estado selecionado
                String hql = "SELECT ce.tbCidade.cidDescricao FROM TbCidEst ce WHERE ce.tbEstado.estSigla = '" + selectedEstado + "'";
                Query query = session.createQuery(hql);
                List<String> cidades = (List<String>) query.list();

                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                model.addElement("Selecione..."); // PALAVRA QUE VAI FICAR ANTES DE APARACER AS LITA DE TODOS OS ESTADOS
                for (String cidade : cidades) {
                    model.addElement(cidade);
                }
                ArrayList<TbEstado> estado = (ArrayList<TbEstado>) estd.list();

                listCidade.setModel(model);

                // ATUALIZAR A INTERFACE
                revalidate();
                repaint();
            }
        });
        // ---------------------------------------------------------------------
        // CONEXÃO COM O BANCO NA TABELA LOGRADOURO ----------------------------
        String hql = "SELECT log.logDescricao FROM TbLogradouro log";
        Query query = session.createQuery(hql);
        java.util.List<String> logradouros = (java.util.List<String>) query.list();
        // ---------------------------------------------------------------------

        // COMBOBOX DO LOGRADOURO ----------------------------------------------
        JComboBox<String> listLogradouro = new JComboBox<>();
        DefaultComboBoxModel<String> logr = new DefaultComboBoxModel<>();
        logr.addElement("Selecione..."); // PALAVRA QUE VAI FICAR ANTES DE APARACER A LISTA DE TODOS OS ESTADOS
        listLogradouro.setModel(logr);
        listLogradouro.setFont(fonte);
        for (String logradouro : logradouros) {
            logr.addElement(logradouro);
        }
        // ---------------------------------------------------------------------
        
        // CRIANDO OS COMPONENTES LABEL DA TELA --------------------------------
        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setFont(fonte);
        JLabel sexoLabel = new JLabel("Sexo:");
        sexoLabel.setFont(fonte);
        JLabel documentoLabel = new JLabel();
        documentoLabel.setText("CNPJ:");
        documentoLabel.setFont(fonte);
        JLabel dataNascimentoLabel = new JLabel("Data de Nascimento:");
        dataNascimentoLabel.setFont(fonte);
        JLabel fantasiaLabel = new JLabel("Nome Fantasia:");
        fantasiaLabel.setFont(fonte);
        JLabel ieLabel = new JLabel("RG/Inscrição Estadual:");
        ieLabel.setFont(fonte);
        JLabel foneLabel = new JLabel("Fone:");
        foneLabel.setFont(fonte);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(fonte);
        JLabel cepLabel = new JLabel("CEP:");
        cepLabel.setFont(fonte);
        JLabel logradouroLabel = new JLabel("Logradouro:");
        logradouroLabel.setFont(fonte);
        JLabel enderecoLabel = new JLabel("Endereço:");
        enderecoLabel.setFont(fonte);
        JLabel numeroLabel = new JLabel("Número:");
        numeroLabel.setFont(fonte);
        JLabel complementoLabel = new JLabel("Complemento:");
        complementoLabel.setFont(fonte);
        JLabel bairroLabel = new JLabel("Bairro:");
        bairroLabel.setFont(fonte);
        JLabel estadoLabel = new JLabel("Estado:");
        estadoLabel.setFont(fonte);
        JLabel cidadeLabel = new JLabel("Cidade:");
        cidadeLabel.setFont(fonte);
        // ---------------------------------------------------------------------
        // CRIANDO AS CAIXAS DE DIALOGO ----------------------------------------
        nomeField = new JTextField(20);
        fantasiaField = new JTextField(20);
        emailField = new JTextField(20);
        enderecoField = new JTextField(20);
        numeroField = new JTextField(20);
        complementoField = new JTextField(20);
        bairroField = new JTextField(20);
        // ---------------------------------------------------------------------
        // FORMATAÇÕES ---------------------------------------------------------
        cnpj = new MaskFormatter("##.###.###/####-##");
        ie = new MaskFormatter("##############");
        fone = new MaskFormatter("(##) #####-####");
        cep = new MaskFormatter("#####-###");
        dataNascimento = new MaskFormatter("##/##/####");
        // ---------------------------------------------------------------------
        // INSTANCIANDO OS OBJETOS GLOBAIS -------------------------------------
        documentoField = new JFormattedTextField();
        ieField = new JFormattedTextField();
        foneField = new JFormattedTextField();
        cepField = new JFormattedTextField();
        dataNascimentoField = new JFormattedTextField();
        // ---------------------------------------------------------------------
        // SETANDO AS FONTES PARA CADA CAIXA DE DIALOGO ------------------------
        documentoField.setFont(fonte);
        ieField.setFont(fonte);
        foneField.setFont(fonte);
        cepField.setFont(fonte);
        dataNascimentoField.setFont(fonte);
        nomeField.setFont(fonte);
        fantasiaField.setFont(fonte);
        emailField.setFont(fonte);
        enderecoField.setFont(fonte);
        numeroField.setFont(fonte);
        complementoField.setFont(fonte);
        bairroField.setFont(fonte);
        // ---------------------------------------------------------------------
        // BOTÃO SALVAR --------------------------------------------------------
        JButton cadastrarButton = new JButton();
        ImageIcon cads = new ImageIcon(ClassLoader.getSystemResource("resources/salvar.png"));
        Image scaledCads = cads.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
        cadastrarButton.setIcon(new ImageIcon(scaledCads));
        cadastrarButton.setBounds(70, 500, 100, 30);
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hql = "SELECT c.cepId FROM TbCidEst c WHERE c.tbCidade.cidDescricao = '" + listCidade.getSelectedItem() + "' AND c.tbEstado = '" + listEstado.getSelectedItem() + "'";
                Query query = session.createQuery(hql);

                String hql2 = "SELECT l.logId FROM TbLogradouro l WHERE l.logDescricao = '" + listLogradouro.getSelectedItem() + "'";
                Query query2 = session.createQuery(hql2);

                Transaction transaction = session.beginTransaction();
                try {
                    int logId = (int) query2.uniqueResult();
                    int cidId = (int) query.uniqueResult();
                    BancoDeDados.TbBairro tbBairro = new BancoDeDados.TbBairro();
                    tbBairro.setBaiDescricao(bairroField.getText());
                    session.save(tbBairro);

                    BancoDeDados.TbEndPostal tbEndPostal = new BancoDeDados.TbEndPostal();
                    tbEndPostal.setTbBairro(tbBairro);
                    tbEndPostal.setEndPNomerua(enderecoField.getText());
                    tbEndPostal.setEndPCep(cepField.getText());
                    Object log = session.load(TbLogradouro.class, logId);
                    tbEndPostal.setTbLogradouro((TbLogradouro) log);
                    Object cidest = session.load(TbCidEst.class, cidId);
                    tbEndPostal.setTbCidEst((TbCidEst) cidest);
                    session.save(tbEndPostal);

                    BancoDeDados.TbEndereco tbendereco = new BancoDeDados.TbEndereco();
                    tbendereco.setTbEndPostal(tbEndPostal);
                    tbendereco.setEndNumero(numeroField.getText());
                    tbendereco.setEndComplemento(complementoField.getText());
                    session.save(tbendereco);

                    BancoDeDados.TbEntidade tbentidade = new BancoDeDados.TbEntidade();
                    tbentidade.setEntCpfCnpj(documentoField.getText());
                    tbentidade.setTbEndereco(tbendereco);
                    tbentidade.setEntNome(nomeField.getText());
                    tbentidade.setEntNomeFantasia(fantasiaField.getText());
                    tbentidade.setEntRgIe(ieField.getText());
                    tbentidade.setEntFone(foneField.getText());
                    tbentidade.setEntEmail(emailField.getText());
                    tbentidade.setEntTipo("Juridica");
                    session.save(tbentidade);

                    BancoDeDados.TbFornecedor tbfornecedor = new BancoDeDados.TbFornecedor();
                    tbfornecedor.setTbEntidade(tbentidade);
                    session.save(tbfornecedor);

                    transaction.commit();
                    JOptionPane.showMessageDialog(null, "Fornecedor Inserido com Sucesso!");
                    dispose();
                    panelFrame.setEnabled(true);
                    panelFrame.requestFocus();
                } catch (HibernateException ex) {
                    transaction.rollback();
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    session.clear();
                } catch (NullPointerException ex) {
                    // Bloco de código executado quando ocorrer a exceção
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                }
            }
        });

        JButton limparCampos = new JButton();
        limparCampos = new JButton();
        ImageIcon limp = new ImageIcon(ClassLoader.getSystemResource("resources/limpar.png"));
        Image scaledLimpar = limp.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
        limparCampos.setIcon(new ImageIcon(scaledLimpar));
        limparCampos.setBounds(200, 500, 100, 30);
        limparCampos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
                listEstado.setSelectedIndex(0);
                listLogradouro.setSelectedIndex(0);
                listCidade.setSelectedIndex(0);
            }
        });
        
        // ADICIONA OS COMPONENTES NO PAINEL PRINCIPAL
        // Nome 
        mainPanel.add(nomeLabel);
        mainPanel.add(nomeField);
        
        // Documento
        mainPanel.add(documentoLabel);
        mainPanel.add(documentoField);
        cnpj.install(documentoField);
        
        // Nome fantasia
        mainPanel.add(fantasiaLabel);
        mainPanel.add(fantasiaField);

        // RG/IE e formata só para aceitar numero
        mainPanel.add(ieLabel);
        mainPanel.add(ieField);
        ie.install(ieField);

        // Telefone e formata na forma de telefone
        mainPanel.add(foneLabel);
        mainPanel.add(foneField);
        fone.install(foneField);

        // Email
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);

        // CEP
        mainPanel.add(cepLabel);
        mainPanel.add(cepField);
        cep.install(cepField);
        
        // Logradouro
        mainPanel.add(logradouroLabel);
        mainPanel.add(listLogradouro);
        
        // Endereco
        mainPanel.add(enderecoLabel);
        mainPanel.add(enderecoField);
        
        // Numero
        mainPanel.add(numeroLabel);
        mainPanel.add(numeroField);
        
        // Complemento
        mainPanel.add(complementoLabel);
        mainPanel.add(complementoField);
        
        // Bairro
        mainPanel.add(bairroLabel);
        mainPanel.add(bairroField);
        
        // Adiciona o JComboBox ao JFrame
        mainPanel.add(estadoLabel);
        mainPanel.add(listEstado);
        
        mainPanel.add(cidadeLabel);
        mainPanel.add(listCidade);
        
        // botão para limpar
        mainPanel.add(cadastrarButton);
        mainPanel.add(limparCampos);

        // Define as coordenadas de posicionamento dos componentes
        int x = 10;
        int y = 40;
        int yGap = 30;
        int labelWidth = 150;
        int fieldWidth = 200;

        nomeLabel.setBounds(x, y, labelWidth, 20);
        nomeField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        documentoLabel.setBounds(x, y, labelWidth, 20);
        documentoField.setBounds(x + labelWidth + 10, y, 120, 20);
        
        y += yGap;
        fantasiaLabel.setBounds(x, y, labelWidth, 20);
        fantasiaField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        ieLabel.setBounds(x, y, labelWidth, 20);
        ieField.setBounds(x + labelWidth + 10, y, 120, 20);

        y += yGap;
        foneLabel.setBounds(x, y, labelWidth, 20);
        foneField.setBounds(x + labelWidth + 10, y, 120, 20);

        y += yGap;
        emailLabel.setBounds(x, y, labelWidth, 20);
        emailField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        cepLabel.setBounds(x, y, labelWidth, 20);
        cepField.setBounds(x + labelWidth + 10, y, 120, 20);

        y += yGap;
        logradouroLabel.setBounds(x, y, labelWidth, 20);
        listLogradouro.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        enderecoLabel.setBounds(x, y, labelWidth, 20);
        enderecoField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        numeroLabel.setBounds(x, y, labelWidth, 20);
        numeroField.setBounds(x + labelWidth + 10, y, 120, 20);

        y += yGap;
        complementoLabel.setBounds(x, y, labelWidth, 20);
        complementoField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        bairroLabel.setBounds(x, y, labelWidth, 20);
        bairroField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        estadoLabel.setBounds(x, y, labelWidth, 20);
        listEstado.setBounds(x + labelWidth + 10, y, fieldWidth, 20);
        
        y += yGap;
        cidadeLabel.setBounds(x, y, labelWidth, 20);
        listCidade.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        // Adicione o painel principal à janela de diálogo
        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(panelFrame);
    }

    private void limparCampos() {
        nomeField.setText("");
        documentoField.setText("");
        fantasiaField.setText("");
        ieField.setText("");
        foneField.setText("");
        emailField.setText("");
        cepField.setText("");
        enderecoField.setText("");
        numeroField.setText("");
        complementoField.setText("");
        bairroField.setText("");
    }
    
    public void showInterface() {
        // Exibe a janela menor
        panelFrame.setEnabled(false);
        setVisible(true);
    }
}
