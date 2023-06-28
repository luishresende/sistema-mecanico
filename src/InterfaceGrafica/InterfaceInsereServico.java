package InterfaceGrafica;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import org.hibernate.Session;

class InterfaceInsereServico extends JFrame {

    protected static boolean isSmallWindowOpen = false;
    private final JFrame mainFrame;
    private final JFormattedTextField valorServicoField;
    private final JTextField modeloField;
    private final JTextField marcaField;
    private final JTextField placaField;
    private final JFormattedTextField kmPercorridoField;
    private final JFormattedTextField valorKMField;
    private final JTextArea descricaoArea;
    private Session session;
    private JButton salvar;
    

    public InterfaceInsereServico(JFrame mainFrame, Session session, JButton salvar) {
        this.mainFrame = mainFrame; // TELA ANTERIOR
        this.session = session;
        this.salvar = salvar;
                
        // DEFININDO A DIMENSÃO DA JANELA ---------------------------------------------
        JPanel mainPanel = new JPanel(null);
        mainPanel.setPreferredSize(new Dimension(380, 350));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setTitle("Inserindo Serviço");
        setResizable(false);
        // ----------------------------------------------------------------------------

        // Verifica se a janela menor está aberta
        isSmallWindowOpen = false;

        // Define o formato para números de ponto flutuante
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        decimalFormat.setParseBigDecimal(true);

        // Cria um NumberFormatter com o formato definido
        NumberFormatter formatter = new NumberFormatter(decimalFormat);
        formatter.setValueClass(Float.class);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        
        Font fonte = new Font("Times New Roman", Font.BOLD, 14);
        
        // DESCRIÇÃO E VALOR DO SERVIÇO --------------------
        JLabel valorServicoLabel = new JLabel("Valor do Serviço");
        valorServicoLabel.setFont(fonte);
        valorServicoField = new JFormattedTextField(formatter);
        valorServicoField.setValue(0.00f);
        valorServicoField.setFont(fonte);
        valorServicoField.setPreferredSize(new Dimension(100, 30));
        
        mainPanel.add(valorServicoLabel);
        mainPanel.add(valorServicoField);
        
        JLabel descricaoLabel = new JLabel("Descrição Serviço");
        descricaoLabel.setFont(fonte);
        descricaoArea = new JTextArea();
        descricaoArea.setFont(fonte);
        descricaoArea.setLineWrap(true); // Permite que o texto pule de linha automaticamente 
        descricaoArea.setWrapStyleWord(true); // Quebra a linha no espaço em branco mais próximo
        
        mainPanel.add(descricaoLabel);
        mainPanel.add(descricaoArea);
        // ----------------------------------------------------------
        
        // VEICULO -----------------------------------------
        JLabel modeloLabel = new JLabel("Modelo");
        modeloLabel.setFont(fonte);
        modeloLabel.setAlignmentY(TOP_ALIGNMENT);
        modeloField = new JTextField();
        modeloField.setFont(fonte);
        modeloField.setPreferredSize(new Dimension(200, 30));

        JLabel marcaLabel = new JLabel("Marca");
        marcaLabel.setFont(fonte);
        marcaField = new JTextField();
        marcaField.setFont(fonte);
        marcaField.setPreferredSize(new Dimension(200, 30));

        JLabel placaLabel = new JLabel("Placa");
        placaLabel.setFont(fonte);
        placaField = new JTextField();
        placaField.setFont(fonte);
        placaField.setPreferredSize(new Dimension(100, 30));
        
        mainPanel.add(modeloLabel);
        mainPanel.add(modeloField);
        mainPanel.add(marcaLabel);
        mainPanel.add(marcaField);
        mainPanel.add(placaLabel);
        mainPanel.add(placaField);
        // ----------------------------------------------------------
        
        // KM ---------------------------------------------
        JLabel kmPercorridoLabel = new JLabel("KM Percorrido:");
        kmPercorridoLabel.setFont(fonte);
        kmPercorridoField = new JFormattedTextField(formatter);
        kmPercorridoField.setValue(0.00f);
        kmPercorridoField.setFont(fonte);
        kmPercorridoField.setPreferredSize(new Dimension(100, 30));
        
        JLabel valorKMLabel = new JLabel("Valor por KM:");
        valorKMLabel.setFont(fonte);
        valorKMField = new JFormattedTextField(formatter);
        valorKMField.setValue(0.00f);
        valorKMField.setFont(fonte);
        valorKMField.setPreferredSize(new Dimension(100, 30));
        
        mainPanel.add(kmPercorridoLabel);
        mainPanel.add(kmPercorridoField);
        mainPanel.add(valorKMLabel);
        mainPanel.add(valorKMField);
        // ----------------------------------------------------------

        // BOTÕES -----------------------------------------------------------------------
        // BOTÃO SALVAR ---------------------------------------
        ImageIcon slv = new ImageIcon(ClassLoader.getSystemResource("resources/salvar.png"));
        Image scaledSlv = slv.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
        salvar.setIcon(new ImageIcon(scaledSlv));
        salvar.setBounds(80, 300, 100, 30);
        mainPanel.add(salvar);
        // ----------------------------------------------------
        
        // BOTÃO LIMPAR ---------------------------------------
        JButton limpar = new JButton();
        ImageIcon lip = new ImageIcon(ClassLoader.getSystemResource("resources/limpar.png"));
        Image scaledLip = lip.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
        limpar.setIcon(new ImageIcon(scaledLip));
        limpar.setBounds(200, 300, 100, 30);
        limpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        mainPanel.add(limpar);
        // ----------------------------------------------------
        // --------------------------------------------------------------------------------
        
        // Define as coordenadas de posicionamento dos componentes
        int x = 10;
        int y = 70;
        int yGap = 30;
        int labelWidth = 150;
        int fieldWidth = 200;
        
        descricaoLabel.setBounds(10, 10, labelWidth, 20);
        descricaoArea.setBounds(170, 10, 200, 70);
        
        y += yGap;
        valorServicoLabel.setBounds(x, y, labelWidth, 20);
        valorServicoField.setBounds(x + labelWidth + 10, y, 100, 20);

        y += yGap;
        modeloLabel.setBounds(x, y, labelWidth, 20);
        modeloField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        marcaLabel.setBounds(x, y, labelWidth, 20);
        marcaField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        placaLabel.setBounds(x, y, labelWidth, 20);
        placaField.setBounds(x + labelWidth + 10, y, fieldWidth, 20);

        y += yGap;
        kmPercorridoLabel.setBounds(x, y, labelWidth, 20);
        kmPercorridoField.setBounds(x + labelWidth + 10, y, 100, 20);

        y += yGap;
        valorKMLabel.setBounds(x, y, labelWidth, 20);
        valorKMField.setBounds(x + labelWidth + 10, y, 100, 20);
        
        // Adicione o painel principal à janela de diálogo
        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(mainFrame);
        
    }
    
    private void limparCampos() {
        descricaoArea.setText("");
        valorServicoField.setValue(0.00f);
        modeloField.setText("");
        marcaField.setText("");
        placaField.setText("");
        kmPercorridoField.setValue(0.00f);
        valorKMField.setValue(0.00f);
    }

    public void showInterface() {
        // Desabilita a janela anterior (mainFrame)
        mainFrame.setEnabled(false);
        setVisible(true);
    }

    public Object[] getServicoInputs() {
        // Obtenha os valores dos inputs
        float valorServico = Float.parseFloat(valorServicoField.getText().replace(".", "").replace(",", "."));
        String descricaoServico = descricaoArea.getText();
        String modelo = modeloField.getText();
        String marca = marcaField.getText();
        String placa = placaField.getText();
        float KmRodado = Float.parseFloat(kmPercorridoField.getText().replace(",", "."));
        float KmValor = Float.parseFloat(valorKMField.getText().replace(",", "."));

        // Crie um array com os valores
        Object[] servicoInputs = {"Serviço", descricaoServico, valorServico, 1, KmRodado, KmValor, modelo, marca, placa};
        return servicoInputs;
    }
}
