package InterfaceGrafica;

import javax.swing.*;
import java.awt.*;
import org.hibernate.Session;

public abstract class InterfaceAbstrata extends JFrame{
    protected final JFrame PanelFrame = this;
    protected final JFrame mainFrame;
    private final Session session;
    protected String[] buttonLabels = {"Inserir", "Alterar", "Remover", "Atualizar"};
    protected String[] buttonIcons = {"resources/inserir.png", "resources/alterar.png", "resources/excluir.png","resources/atualizar.png"};
    protected JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    public InterfaceAbstrata(JFrame mainFrame, Session session, String titulo) {
        setTitle(titulo);
        this.mainFrame = mainFrame;
        this.session = session;

        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.6);
        int height = (int) (screenSize.height * 0.6);
        setSize(width, height);
        setResizable(false);
        
        // Painel da janela menor
        JPanel smallPanel = new JPanel(new BorderLayout());

        // Parte inferior com a tabela
        JTable table = createTable(session);
        table.setEnabled(true); // Torna a tabela não editável

        // Adiciona os painéis no painel da janela menor
        smallPanel.add(buttonPanel, BorderLayout.NORTH);
        smallPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Adiciona o painel da janela menor na janela menor
        getContentPane().add(smallPanel);

        // Centraliza a janela menor em relação à janela principal
        setLocationRelativeTo(mainFrame);

        // Configura um listener para quando a janela menor for fechada
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Habilita a janela principal
                mainFrame.setEnabled(true);
                mainFrame.requestFocus();
            }
        });
    }

    public void showInterface() {
        // Exibe a janela menor
        mainFrame.setEnabled(false);
        setVisible(true);
    }

    protected JButton createSmallButton(String iconPath) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(50, 50));
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(iconPath));
        Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setFocusPainted(false);
        return button;
    }

    protected abstract JTable createTable(Session session);
}