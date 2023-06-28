package InterfaceGrafica;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class InterfaceAlterarEntidade<T> extends JDialog{
    private JFrame panelFrame;
    private JTextField nomeField;
    private JFormattedTextField documentoField = new JFormattedTextField();
    private JTextField fantasiaField;
    private JFormattedTextField rgieField;
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
    private JFormattedTextField dataNascimentoField;
    private JRadioButton pessoaFisica;
    private JRadioButton pessoaJuridica;
    private JRadioButton sexoMasculino;
    private JRadioButton sexoFeminino;
    private JRadioButton sexoOutros;
    private Session session;
    private String bairroId;
    private String endPId;
    private String endId;
    private String cpfCnpj;
    private SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
    private MaskFormatter cnpj;
    private MaskFormatter CPF;
    private MaskFormatter rgie;
    private MaskFormatter fone;
    private MaskFormatter dataNascimento;
    private MaskFormatter cep;
    
    public InterfaceAlterarEntidade(JFrame panelFrame, Session session, String cpf, String tipo) throws ParseException{
        this.panelFrame = panelFrame;
        this.session = session;
        
        // DEFINIÇÃO DO LAYOUT -------------------------------------------------
        JPanel mainPanel = new JPanel(null); // Define o layout como null
        mainPanel.setPreferredSize(new Dimension(380, 580));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setTitle("Alterando dados do cliente");
        // ---------------------------------------------------------------------
        
        Font fonte = new Font("Times New Roman", Font.ROMAN_BASELINE, 14);
        // Desabilitando a PanelFrame
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Habilita panelFrame
                panelFrame.setVisible(true);
                panelFrame.setEnabled(true);
                panelFrame.toFront();
            }
        });
        
        // CONEXÃO COM O BANCO NA TABELA ESTADO
        String hql = "SELECT est.estSigla FROM TbEstado est";
        Query query = session.createQuery(hql);
        List<String> estados = (List<String>) query.list();
        
        // COMBOBOX DO ESTADO --------------------------------------------------
        JComboBox<String> listEstado = new JComboBox<>();
        DefaultComboBoxModel<String> est = new DefaultComboBoxModel<>();
        est.addElement("Selecione...");
        listEstado.setModel(est); 
        listEstado.setFont(fonte);
        for (String estado : estados) {
            listEstado.addItem(estado);
        }
        // ---------------------------------------------------------------------
        // COMBOBOX DA CIDADE --------------------------------------------------
        JComboBox<String> listCidade = new JComboBox<>();
        listCidade.setFont(fonte);
        listEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEstado(listEstado, listCidade);
            }
        });
        // ---------------------------------------------------------------------
        // CONEXÃO COM O BANCO NA TABELA LOGRADOURO ----------------------------
        hql = "SELECT log.logDescricao FROM TbLogradouro log";
        query = session.createQuery(hql);
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
        JLabel documentoLabel = new JLabel("CPF:");
        documentoLabel.setFont(fonte);
        JLabel dataNascimentoLabel = new JLabel("Data de Nascimento:");
        dataNascimentoLabel.setFont(fonte);
        JLabel fantasiaLabel = new JLabel("Nome Fantasia:");
        fantasiaLabel.setFont(fonte);
        JLabel rgieLabel = new JLabel("RG:");
        rgieLabel.setFont(fonte);
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
        CPF = new MaskFormatter("###.###.###-##");
        rgie = new MaskFormatter("##############");
        fone = new MaskFormatter("(##) #####-####");
        cep = new MaskFormatter("#####-###");
        dataNascimento = new MaskFormatter("##/##/####");
        // ---------------------------------------------------------------------
        // INSTANCIANDO AS CAIXAS DE DIALOGO DO TIPO TEXT FROMATTED ------------
        documentoField = new JFormattedTextField();
        rgieField = new JFormattedTextField();
        foneField = new JFormattedTextField();
        cepField = new JFormattedTextField();
        dataNascimentoField = new JFormattedTextField();
        // ---------------------------------------------------------------------
        // SETANDO AS FONTES PARA CADA CAIXA DE DIALOGO ------------------------
        documentoField.setFont(fonte);
        rgieField.setFont(fonte);
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
        // CONFIGURAÇÃO DE TODOS OS RADIOSBUTTON -------------------------------
        pessoaFisica = new JRadioButton("Pessoa Física");
        pessoaFisica.setBounds(10, 10, 150, 20);
        pessoaFisica.setSelected(true);
        pessoaFisica.setFont(fonte);

        pessoaJuridica = new JRadioButton("Pessoa Jurídica");
        pessoaJuridica.setBounds(170, 10, 150, 20);
        pessoaJuridica.setFont(fonte);

        ButtonGroup tipoClienteGroup = new ButtonGroup();
        tipoClienteGroup.add(pessoaFisica);
        tipoClienteGroup.add(pessoaJuridica);

        sexoMasculino = new JRadioButton("Masculino");
        sexoMasculino.setBounds(80, 70, 90, 20);
        sexoMasculino.setSelected(true);
        sexoMasculino.setFont(fonte);

        sexoFeminino = new JRadioButton("Feminino");
        sexoFeminino.setBounds(170, 70, 80, 20);
        sexoFeminino.setFont(fonte);

        sexoOutros = new JRadioButton("Outro");
        sexoOutros.setBounds(250, 70, 60, 20);
        sexoOutros.setFont(fonte);

        ButtonGroup tipoSexoGroup = new ButtonGroup();
        tipoSexoGroup.add(sexoMasculino);
        tipoSexoGroup.add(sexoFeminino);
        tipoSexoGroup.add(sexoOutros);
        // ---------------------------------------------------------------------
        // BOTÃO SALVAR --------------------------------------------------------
        JButton alterarButton = new JButton();
        ImageIcon cads = new ImageIcon(ClassLoader.getSystemResource("resources/salvar.png"));
        Image scaledCads = cads.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
        alterarButton.setIcon(new ImageIcon(scaledCads));
        alterarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hql = "SELECT c.cepId FROM TbCidEst c WHERE c.tbCidade.cidDescricao = '" + listCidade.getSelectedItem() + "' AND c.tbEstado = '" + listEstado.getSelectedItem() + "'";
                Query query = session.createQuery(hql);

                String hql2 = "SELECT l.logId FROM TbLogradouro l WHERE l.logDescricao = '" + listLogradouro.getSelectedItem() + "'";
                Query query2 = session.createQuery(hql2);

                int logId = (int) query2.uniqueResult();
                int cidId = (int) query.uniqueResult();

                Transaction transaction = session.beginTransaction();
                try {
                    String dataNascimento = new String();
                    if(dataNascimentoField.getValue() == null){
                        dataNascimento = null;
                    } else {
                        String data = dataNascimentoField.getText();
                        dataNascimento = "'" + data.substring(6, 10) + "-" + data.substring(3, 5) + "-" + data.substring(3, 5) + "'";
                    }
                    String sexo = new String();
                    if(sexoFeminino.isSelected()){
                        sexo = "F";
                    }else if(sexoMasculino.isSelected()){
                        sexo = "M";
                    }else if(sexoOutros.isSelected()){
                        sexo = "Outros";
                    }else {
                        sexo = null;
                    }
                    
                    // Consulta que ira atualizar a entidade
                    String hqlUpEntidade = "UPDATE TbEntidade ent set ent.entNome = '" +  nomeField.getText() + "', " +
                                                   "ent.entNomeFantasia = '" +  fantasiaField.getText() + "', " +
                                                   "ent.entRgIe = '" +  rgieField.getText() + "', " +
                                                   "ent.entFone = '" +  foneField.getText() + "', " +
                                                   "ent.entEmail = '" +  emailField.getText() + "', " +
                                                   "ent.entSexo = '" +  sexo + "', " +
                                                   "ent.entDtNasc = " + dataNascimento + " "+
                                                   "WHERE ent.entCpfCnpj = '" +  cpfCnpj + "'";
                    
                    // Consulta que ira atualizar o endereço
                    String hqlUpEndereco = "UPDATE TbEndereco ende SET ende.endNumero = '" + numeroField.getText() + "', " +
                                                   "ende.endComplemento = '" + complementoField.getText() + "' " +
                                                   "WHERE ende.endId = '" + endId + "'";
                    
                    // Consulta que ira atualizar o endereço postal
                    String hqlUpEndPostal = "UPDATE TbEndPostal endP SET endP.tbLogradouro = '" + logId + "', " +
                                                   "endP.endPNomerua = '" + enderecoField.getText() + "', " +
                                                   "endP.endPCep = '" + cepField.getText() + "', " +
                                                   "endP.tbCidEst = '" + cidId + "' " +
                                                   "WHERE endP.endPId = '" + endPId + "'";
                    
                    // Consulta que ira atualizar o bairro
                    String hqlUpBairro = "UPDATE TbBairro bai SET baiDescricao = '" + bairroField.getText() + "' WHERE baiId = '" + bairroId +"'";
                            
                    //Criando as querys
                    Query updateQueryEntidade = session.createQuery(hqlUpEntidade);
                    Query updateQueryEndereco = session.createQuery(hqlUpEndereco);
                    Query updateQueryEndPostal = session.createQuery(hqlUpEndPostal);
                    Query updateQueryBairro = session.createQuery(hqlUpBairro);
                    
                    //Executando as querys
                    updateQueryEntidade.executeUpdate();
                    updateQueryEndereco.executeUpdate();
                    updateQueryEndPostal.executeUpdate();
                    updateQueryBairro.executeUpdate();
                    
                    // Commit caso tudo ocorra bem
                    transaction.commit();
                    JOptionPane.showMessageDialog(null, "Cliente Alterado com Sucesso!");
                    dispose(); // FECHA ATUAL JANELA
                    panelFrame.setEnabled(true); // ATIVA A JANELA ANTERIOR
                    panelFrame.requestFocus();
                } catch (HibernateException ex) {
                    transaction.rollback();
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // ---------------------------------------------------------------------
        
        if(tipo.equals("F")){
            pessoaFisica.setVisible(false);
            pessoaJuridica.setVisible(false);
            JButton alterarFuncionario = new JButton();
            alterarFuncionario.addActionListener(e -> {
                InterfaceAlterarCredenciaisFuncionario alterar;
                alterar = new InterfaceAlterarCredenciaisFuncionario(this, session, cpf);
                alterar.showInterface();
            });
            
            alterarButton.setBounds(70, 540, 100, 30);
            
            alterarFuncionario.setBounds(200, 540, 100, 30);
            ImageIcon cred = new ImageIcon(ClassLoader.getSystemResource("resources/alteraCredenciais.png"));
            Image scaledCred = cred.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
            alterarFuncionario.setIcon(new ImageIcon(scaledCred));
            
            mainPanel.add(alterarFuncionario);
            mainPanel.add(alterarButton);
        } else {
            alterarButton.setBounds(140, 540, 100, 30);
            mainPanel.add(alterarButton);
        }
        
        // Quando escolhido esse botão, preenche os campos de pessoa juridica
        pessoaJuridica.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CPF.uninstall();
                cnpj.install(documentoField);
                documentoLabel.setText("CNPJ:");
                dataNascimentoField.setEnabled(false);
                sexoMasculino.setEnabled(false);
                sexoFeminino.setEnabled(false);
                sexoOutros.setEnabled(false);
            }
        });

        pessoaFisica.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cnpj.uninstall();
                CPF.install(documentoField);
                documentoLabel.setText("CPF:");
                dataNascimentoField.setEnabled(true);
                sexoMasculino.setEnabled(true);
                sexoFeminino.setEnabled(true);
                sexoOutros.setEnabled(true);
            }
        });
        
        // ADICIONA OS COMPONENTES NO PAINEL PRINCIPAL -------------------------
        // Pessoa fisica e juridica
        mainPanel.add(pessoaFisica);
        mainPanel.add(pessoaJuridica);
        // Nome 
        mainPanel.add(nomeLabel);
        mainPanel.add(nomeField);
        // Documento
        mainPanel.add(documentoLabel);
        mainPanel.add(documentoField);
        // Sexo
        mainPanel.add(sexoLabel);
        mainPanel.add(sexoMasculino);
        mainPanel.add(sexoFeminino);
        mainPanel.add(sexoOutros);
        // Nome fantasia
        mainPanel.add(fantasiaLabel);
        mainPanel.add(fantasiaField);
        // RG/IE e formata só para aceitar numero
        mainPanel.add(rgieLabel);
        mainPanel.add(rgieField);
        rgie.install(rgieField);
        // Data Nascimento e formata em forma de data
        mainPanel.add(dataNascimentoLabel);
        mainPanel.add(dataNascimentoField);
        dataNascimento.install(dataNascimentoField);
        // Telefone e formata na forma de telefone
        mainPanel.add(foneLabel);
        mainPanel.add(foneField);
        fone.install(foneField);
        // Email
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);
        // CEP e formata na forma de cep
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
        // Combobox do Estado
        mainPanel.add(estadoLabel);
        mainPanel.add(listEstado);
        // Combobox da Cidade
        mainPanel.add(cidadeLabel);
        mainPanel.add(listCidade);
        // ---------------------------------------------------------------------

        // Define as coordenadas de posicionamento dos componentes -------------
        int x = 10;
        int y = 40;
        int yGap = 30;
        int labelWidth = 150;
        int fieldWidth = 200;

        nomeLabel.setBounds(x, y, labelWidth, 20);
        nomeField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);
        
        y += yGap;
        sexoLabel.setBounds(x, y, labelWidth, 20);
        
        y += yGap;
        documentoLabel.setBounds(x, y, labelWidth, 20);
        documentoField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);
        
        y += yGap;
        dataNascimentoLabel.setBounds(x, y, labelWidth, 20);
        dataNascimentoField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);
        
        y += yGap;
        fantasiaLabel.setBounds(x, y, labelWidth, 20);
        fantasiaField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        rgieLabel.setBounds(x, y, labelWidth, 20);
        rgieField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        foneLabel.setBounds(x, y, labelWidth, 20);
        foneField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        emailLabel.setBounds(x, y, labelWidth, 20);
        emailField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        cepLabel.setBounds(x, y, labelWidth, 20);
        cepField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        logradouroLabel.setBounds(x, y, labelWidth, 20);
        listLogradouro.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        enderecoLabel.setBounds(x, y, labelWidth, 20);
        enderecoField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        numeroLabel.setBounds(x, y, labelWidth, 20);
        numeroField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

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
        // ---------------------------------------------------------------------
        
        pessoaFisica.doClick();
        // Obtendo e inserindo todos os dados do cliente
        hql = "SELECT ent.entTipo, ent.entNome, ent.entNomeFantasia, ent.entSexo, ent.entCpfCnpj, "
                + "ent.entDtNasc, ent.entRgIe, ent.entFone, ent.entEmail, ent.tbEndereco.tbEndPostal.endPCep, "
                + "ent.tbEndereco.tbEndPostal.tbLogradouro.logDescricao, ent.tbEndereco.tbEndPostal.endPNomerua, "
                + "ent.tbEndereco.endNumero, ent.tbEndereco.endComplemento, ent.tbEndereco.tbEndPostal.tbBairro.baiDescricao, "
                + "ent.tbEndereco.tbEndPostal.tbCidEst.tbEstado.estSigla, ent.tbEndereco.tbEndPostal.tbCidEst.tbCidade.cidDescricao, "
                + "ent.tbEndereco.endId, ent.tbEndereco.tbEndPostal.endPId, ent.tbEndereco.tbEndPostal.tbBairro.baiId "
                + "FROM TbEntidade ent WHERE ent.entCpfCnpj = '" + cpf + "'";
        
        query = session.createQuery(hql);
        List<Object[]> results = query.list();
        for (Object[] result : results) {
            if(result[0].toString().equals("Fisico")){
                pessoaFisica.doClick();
            }else{
                pessoaJuridica.doClick();
            }
            pessoaJuridica.setEnabled(false);
            pessoaFisica.setEnabled(false);
            
            nomeField.setText(result[1].toString());
            fantasiaField.setText(result[2].toString());
  
            if(result[3] == null){
                sexoMasculino.setEnabled(false);
                sexoFeminino.setEnabled(false);
                sexoOutros.setEnabled(false);
            } else if(result[3].equals("M")){
                sexoMasculino.doClick();
            } else if(result[3].equals("F")){
                sexoFeminino.doClick();
            } else{
                sexoOutros.doClick();
            }
            documentoField.setText(result[4].toString());
            documentoField.setEditable(false);
            cpfCnpj = result[4].toString();
            
            if(result[5] == null){
                dataNascimentoField.setEditable(false);
            } else {
                Date data = (Date) result[5];
                String dataContent = formato.format(data);
                dataNascimentoField.setText(dataContent.replace("-", ""));
            }

            rgieField.setText(result[6].toString().replace("_", ""));
            foneField.setText(result[7].toString());
            emailField.setText(result[8].toString());
            cepField.setText(result[9].toString());
            listLogradouro.setSelectedItem(result[10]);
            enderecoField.setText(result[11].toString());
            numeroField.setText(result[12].toString());
            complementoField.setText(result[13].toString());
            bairroField.setText(result[14].toString());
            listEstado.setSelectedItem(result[15]);
            listCidade.setSelectedItem(result[16]);
            endId = result[17].toString();
            endPId = result[18].toString();
            bairroId = result[19].toString();
        }
        
        // Adicione o painel principal à janela de diálogo
        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(panelFrame);
    }
    
    private void updateEstado(JComboBox listEstado, JComboBox listCidade){
        String selectedEstado = (String) listEstado.getSelectedItem();
        // Obter as cidades correspondentes ao estado selecionado
        String hql = "SELECT ce.tbCidade.cidDescricao FROM TbCidEst ce WHERE ce.tbEstado.estSigla = '" + selectedEstado + "'";
        Query query = session.createQuery(hql);
        List<String> cidades = (List<String>) query.list();

        DefaultComboBoxModel<String> modelEst = new DefaultComboBoxModel<>();
        modelEst.addElement("Selecione..."); // PALAVRA QUE VAI FICAR ANTES DE APARACER AS LITA DE TODOS OS ESTADOS
        for (String cidade : cidades) {
            modelEst.addElement(cidade);
        }
        //ArrayList<TbEstado> estado = (ArrayList<TbEstado>) estd.list();

        listCidade.setModel(modelEst);


        // Atualizar a interface
        revalidate();
        repaint();
    }
    
    public void showInterface() {
        // Exibe a janela menor
        panelFrame.setEnabled(false);
        setVisible(true);
    }
}
