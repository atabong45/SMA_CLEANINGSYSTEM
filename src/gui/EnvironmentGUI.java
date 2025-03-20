/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;
import environment.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnvironmentGUI extends JFrame {
    private Environment env;
    private JPanel gridPanel;
    private JButton resetButton;
    private Timer timer;
    
    public EnvironmentGUI(Environment env) {
        this.env = env;
        setTitle("Environnement de Nettoyage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);
        
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
        add(controlPanel, BorderLayout.SOUTH);
        
        // Initialiser la grille
        updateGrid();
        
        // Mettre à jour la grille toutes les 500 ms
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGrid();
            }
        });
        timer.start();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void updateGrid() {
        gridPanel.removeAll();
        
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                JPanel cell = new JPanel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell.setBackground(env.isDirty(x, y) ? Color.DARK_GRAY : Color.WHITE);
                gridPanel.add(cell);
            }
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    @SuppressWarnings("checked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    public static void main(String[] args) {
        Environment env = Environment.getInstance(10, 10);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EnvironmentGUI(env);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
