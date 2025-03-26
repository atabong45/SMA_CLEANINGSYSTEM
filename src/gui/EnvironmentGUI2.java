package gui;

import environment.Environment;
import environment.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentGUI2 extends JFrame {
    private Environment env;
    private JPanel gridPanel;
    private JButton resetButton;
    private Timer timer;
    
    // Stocker les positions des agents
    private Map<String, Position> agentPositions;
    
    // Couleurs pour différents types d'agents
    private final Color AGENT_SANS_ETAT_COLOR = Color.BLUE;
    private final Color AGENT_AVEC_ETAT_COLOR = Color.GREEN;
    private final Color AGENT_COOP1_COLOR = Color.RED;
    private final Color AGENT_COOP2_COLOR = Color.ORANGE;
    private final Color AGENT_A_BUT_COLOR = Color.YELLOW;
    
    public EnvironmentGUI2(Environment env) {
        this.env = env;
        this.agentPositions = new HashMap<>();
        
        setTitle("Environnement de Nettoyage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 650);
        
        gridPanel = new JPanel(new GridLayout(10, 10));
        add(gridPanel, BorderLayout.CENTER);
        
        resetButton = new JButton("Réinitialiser");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                env.reset();
                updateGrid();
            }
        });
        
        JPanel controlPanel = new JPanel();
        controlPanel.add(resetButton);
        
        // Ajouter une légende pour les couleurs
        JPanel legendPanel = new JPanel(new GridLayout(1, 5));
        addLegendItem(legendPanel, "Sol propre", Color.WHITE);
        addLegendItem(legendPanel, "Sol sale", Color.DARK_GRAY);
        addLegendItem(legendPanel, "Agent sans état", AGENT_SANS_ETAT_COLOR);
        addLegendItem(legendPanel, "Agent avec état", AGENT_AVEC_ETAT_COLOR);
        addLegendItem(legendPanel, "Agents coopératifs", AGENT_COOP1_COLOR);
        addLegendItem(legendPanel, "Agents a but", AGENT_A_BUT_COLOR);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(controlPanel, BorderLayout.NORTH);
        southPanel.add(legendPanel, BorderLayout.SOUTH);
        
        add(southPanel, BorderLayout.SOUTH);
        
        // Initialiser la grille
        updateGrid();
        
        // Mettre à jour la grille toutes les 200 ms
        timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGrid();
            }
        });
        timer.start();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void addLegendItem(JPanel panel, String text, Color color) {
        JPanel item = new JPanel(new BorderLayout());
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(15, 15));
        item.add(colorBox, BorderLayout.WEST);
        item.add(new JLabel(" " + text), BorderLayout.CENTER);
        panel.add(item);
    }
    
    // Méthode pour mettre à jour la position d'un agent
    public void updateAgentPosition(String agentName, Position position) {
        agentPositions.put(agentName, position);
        updateGrid();
    }
    
    private void updateGrid() {
        gridPanel.removeAll();
        
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                final int currentX = x;
                final int currentY = y;
                
                JPanel cell = new JPanel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                
                // Définir la couleur de fond en fonction de l'état de la cellule
                if (env.isDirty(x, y)) {
                    cell.setBackground(Color.DARK_GRAY);
                } else {
                    cell.setBackground(Color.WHITE);
                }
                
                // Vérifier si un agent est présent sur cette cellule
                boolean agentPresent = false;
                for (Map.Entry<String, Position> entry : agentPositions.entrySet()) {
                    Position pos = entry.getValue();
                    if (pos.getX() == x && pos.getY() == y) {
                        String agentName = entry.getKey();
                        JLabel agentLabel = new JLabel("●");
                        agentLabel.setFont(new Font("Arial", Font.BOLD, 24));
                        
                        // Définir la couleur en fonction du type d'agent
                        if (agentName.equals("AgentSansEtat")) {
                            agentLabel.setForeground(AGENT_SANS_ETAT_COLOR);
                        } else if (agentName.equals("AgentAvecEtat")) {
                            agentLabel.setForeground(AGENT_AVEC_ETAT_COLOR);
                        } else if (agentName.equals("AgentCoop1")) {
                            agentLabel.setForeground(AGENT_COOP1_COLOR);
                        } else if (agentName.equals("AgentCoop2")) {
                            agentLabel.setForeground(AGENT_COOP2_COLOR);
                        } else if (agentName.equals("AgentABut")) {
                            agentLabel.setForeground(AGENT_A_BUT_COLOR);
                        }
                        
                        cell.add(agentLabel);
                        agentPresent = true;
                        break;
                    }
                }
                
                gridPanel.add(cell);
            }
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }
    
    public static void main(String[] args) {
        Environment env = Environment.getInstance(10, 10);
        SwingUtilities.invokeLater(() -> new EnvironmentGUI2(env));
    }
}