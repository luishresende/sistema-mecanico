package InterfaceGrafica;

import BancoDeDados.TbCidEst;
import BancoDeDados.TbEstado;
import BancoDeDados.TbLogradouro;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.MaskFormatter;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

class InterfaceInsereCliente extends JDialog {

    private InterfaceCliente panelFrame;
    private JTextField nomeField;
    private JFormattedTextField documentoField;
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

    private String tipo;
    private MaskFormatter cpf; 
    private MaskFormatter cnpj;
    private MaskFormatter rgie;
    private MaskFormatter fone;
    private MaskFormatter cep;
    private MaskFormatter dataNascimento;

    public InterfaceInsereCliente(InterfaceCliente panelFrame, Session session) throws ParseException {
        this.panelFrame = panelFrame;
        this.session = session;
        
        // DEFINIÇÃO DO LAYOUT -------------------------------------------------
        JPanel mainPanel = new JPanel(null); // Define o layout como null
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setTitle("Inserindo dados do cliente");
        setResizable(false);
        mainPanel.setPreferredSize(new Dimension(380, 580));
        // ---------------------------------------------------------------------
        Font fonte = new Font("Times New Roman", Font.ROMAN_BASELINE, 14); // FONTE DA PÁGINA
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Habilita panelFrame
                panelFrame.setEnabled(true);
                panelFrame.requestFocus();
            }
        });

        // CONEXÃO COM O BANCO NA TABELA ESTADO
        Criteria estd = session.createCriteria(TbEstado.class);
        ArrayList<TbEstado> estado = (ArrayList<TbEstado>) estd.list();
        // COMBOBOX DO ESTADO --------------------------------------------------
        JComboBox<String> listEstado = new JComboBox<>();
        DefaultComboBoxModel<String> est = new DefaultComboBoxModel<>();
        est.addElement("Selecione...");
        listEstado.setModel(est);
        listEstado.setFont(fonte);
        for (TbEstado descricao : estado) {
            listEstado.addItem(descricao.getEstSigla());
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

                listCidade.setModel(model);

                // Atualizar a interface
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
        listLogradouro.setModel(logr);
        // ---------------------------------------------------------------------
        // CRIANDO OS COMPONENTES LABEL DA TELA --------------------------------
        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setFont(fonte);
        JLabel sexoLabel = new JLabel("Sexo:");
        sexoLabel.setFont(fonte);
        JLabel documentoLabel = new JLabel();
        documentoLabel.setText("CPF/CNPJ:");
        documentoLabel.setFont(fonte);
        JLabel dataNascimentoLabel = new JLabel("Data de Nascimento:");
        dataNascimentoLabel.setFont(fonte);
        JLabel fantasiaLabel = new JLabel("Nome Fantasia:");
        fantasiaLabel.setFont(fonte);
        JLabel rgieLabel = new JLabel("RG/Inscrição Estadual:");
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
        cidadeLabel.setFont(fonte);
        // ---------------------------------------------------------------------
        // INSTANCIANDO AS CAIXAS DE DIALOGO DO TIPO TEXT ----------------------
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
        cpf = new MaskFormatter("###.###.###-##");
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
        // BOTÃO CADASTRAR -----------------------------------------------------
        JButton cadastrarButton = new JButton();
        ImageIcon cads = new ImageIcon(ClassLoader.getSystemResource("resources/salvar.png"));
        Image scaledCads = cads.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
        cadastrarButton.setIcon(new ImageIcon(scaledCads));
        cadastrarButton.setBounds(70, 540, 100, 30);
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
                    tbentidade.setEntRgIe(rgieField.getText());
                    tbentidade.setEntFone(foneField.getText());
                    tbentidade.setEntEmail(emailField.getText());

                    ButtonModel selectedButtonModel = tipoSexoGroup.getSelection();
                    if (selectedButtonModel == sexoMasculino.getModel()) {
                        tbentidade.setEntSexo("M");
                    } else if (selectedButtonModel == sexoFeminino.getModel()) {
                        tbentidade.setEntSexo("F");
                    } else if (selectedButtonModel == sexoOutros.getModel()) {
                        tbentidade.setEntSexo("Outros");
                    } else {
                        tbentidade.setEntSexo(null);
                    }
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    if ("  /  /    ".equals(dataNascimentoField.getText())) {
                        tbentidade.setEntDtNasc(null);
                    } else {
                        Date data = formato.parse(dataNascimentoField.getText());
                        tbentidade.setEntDtNasc(data);
                    }
                    if (pessoaJuridica.isSelected()) {
                        tbentidade.setEntTipo("Juridica");
                    } else if (pessoaFisica.isSelected()) {
                        tbentidade.setEntTipo("Fisica");
                    }
                    session.save(tbentidade);

                    BancoDeDados.TbCliente tbcliente = new BancoDeDados.TbCliente();
                    tbcliente.setTbEntidade(tbentidade);
                    session.save(tbcliente);

                    transaction.commit();
                    JOptionPane.showMessageDialog(null, "Cliente Inserido com Sucesso!");
                    dispose();
                    panelFrame.setEnabled(true);
                    panelFrame.requestFocus();
                } catch (HibernateException ex) {
                    transaction.rollback();
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    session.clear(); // LIMPA O BUFFER QUE FOI ARMAZENADO QUANDO CLICADO EM SALVAR
                } catch (ParseException ex) {
                    Logger.getLogger(InterfaceInsereFuncionario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullPointerException ex) {
                    // ERRO PARA CASO NÃO PREENCHA TODOS OS CAMPOS
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
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
        limparCampos.setBounds(200, 540, 100, 30);
        limparCampos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
                listEstado.setSelectedIndex(0);
                listLogradouro.setSelectedIndex(0);
                listCidade.setSelectedIndex(0);
            }
        });
        // ---------------------------------------------------------------------

        // Quando escolhido esse botão, preenche os campos de pessoa juridica
        pessoaJuridica.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cpf.uninstall();
                cnpj.install(documentoField);
                documentoLabel.setText("CNPJ:");
                dataNascimentoField.setEnabled(false);
                tipoSexoGroup.clearSelection();
                sexoMasculino.setEnabled(false);
                sexoFeminino.setEnabled(false);
                sexoOutros.setEnabled(false);
            }
        });
        // ---------------------------------------------------------------------
        // Quando escolhido esse botão, preenche os campos de pessoa fisica
        pessoaFisica.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cnpj.uninstall();
                cpf.install(documentoField);
                documentoLabel.setText("CPF:");
                dataNascimentoField.setEnabled(true);
                sexoMasculino.setEnabled(true);
                sexoFeminino.setEnabled(true);
                sexoOutros.setEnabled(true);
            }
        });
        // ---------------------------------------------------------------------
        
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
        // Botão de cadastrar e limpar
        mainPanel.add(cadastrarButton);
        mainPanel.add(limparCampos);
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
        documentoField.setBounds(x + labelWidth + 10, y, 120, 20);

        y += yGap;
        dataNascimentoLabel.setBounds(x, y, labelWidth, 20);
        dataNascimentoField.setBounds(x + labelWidth + 10, y, 120, 20);

        y += yGap;
        fantasiaLabel.setBounds(x, y, labelWidth, 20);
        fantasiaField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        rgieLabel.setBounds(x, y, labelWidth, 20);
        rgieField.setBounds(x + labelWidth + 10, y, 120, 20);

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
        
        pessoaFisica.doClick(); // Já começa com o pessoa fisíca marcada
        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(panelFrame);
    }

    // Função que é usada pelo botão limpar
    private void limparCampos() {
        nomeField.setText(null);
        documentoField.setText("");
        fantasiaField.setText(null);
        rgieField.setText("");
        foneField.setText("");
        emailField.setText(null);
        cepField.setText("");
        enderecoField.setText(null);
        numeroField.setText(null);
        complementoField.setText(null);
        bairroField.setText(null);
        dataNascimentoField.setText(null);
    }

    public void showInterface() {
        // Bloqueia a janela anterior
        panelFrame.setEnabled(false);
        // Exibe a janela menor
        setVisible(true);
    }
}
