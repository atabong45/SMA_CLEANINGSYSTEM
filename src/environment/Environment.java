/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Classe pour représenter l'environnement
package environment;

import gui.EnvironmentGUI2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {
    private boolean[][] floor;
    private int width;
    private int height;
    private Random random;
    private static Environment instance;
    private EnvironmentGUI2 gui; // Référence à l'interface graphique
    
    // Getter et setter pour l'interface graphique
    public void setGUI(EnvironmentGUI2 gui) {
        this.gui = gui;
    }
    
    public EnvironmentGUI2 getGUI() {
        return this.gui;
    }
    
    // Pattern Singleton pour l'environnement
    public static Environment getInstance(int width, int height) {
        if (instance == null) {
            instance = new Environment(width, height);
        }
        return instance;
    }
    
    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment(10, 10); // Taille par défaut
        }
        return instance;
    }
    
    private Environment(int width, int height) {
        this.width = width;
        this.height = height;
        this.floor = new boolean[width][height];
        this.random = new Random();
        
        // Initialisation aléatoire des saletés (true = sale, false = propre)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                floor[x][y] = random.nextDouble() < 0.3; // 30% de chance d'être sale
            }
        }
    }
    
    public boolean isDirty(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return floor[x][y];
        }
        return false;
    }
    
    public void clean(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            floor[x][y] = false;
        }
    }
    
    public Position getRandomDirtyPosition() {
        List<Position> dirtyPositions = new ArrayList<>();
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (floor[x][y]) {
                    dirtyPositions.add(new Position(x, y));
                }
            }
        }
        
        if (!dirtyPositions.isEmpty()) {
            return dirtyPositions.get(random.nextInt(dirtyPositions.size()));
        }
        
        return null; // Pas de position sale trouvée
    }
    
    public Position getRandomPosition() {
        return new Position(random.nextInt(width), random.nextInt(height));
    }
    
    // Méthode pour vérifier s'il reste des saletés
    public boolean hasDirt() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (floor[x][y]) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // Pour afficher l'état actuel du sol
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(floor[x][y] ? "X" : ".");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    // Méthode pour réinitialiser l'environnement
    public void reset() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                floor[x][y] = random.nextDouble() < 0.3;
            }
        }
    }
}
