/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Classe pour représenter une position
package environment;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position) obj;
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
    
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
    
    // Calculer la distance de Manhattan entre deux positions
    public int distanceTo(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }
    
    // Méthode pour obtenir une position voisine dans une direction aléatoire
    public Position getRandomNeighbor(int maxX, int maxY) {
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        
        int direction = (int)(Math.random() * 4);
        int newX = Math.max(0, Math.min(maxX - 1, x + dx[direction]));
        int newY = Math.max(0, Math.min(maxY - 1, y + dy[direction]));
        
        return new Position(newX, newY);
    }
}
