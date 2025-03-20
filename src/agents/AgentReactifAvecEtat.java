/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Agent réactif avec état
package agents;

import gui.EnvironmentGUI2;
import environment.Environment;
import environment.Position;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.HashSet;
import java.util.Set;

public class AgentReactifAvecEtat extends Agent {
    private Position position;
    private Environment env;
    private Set<Position> cleanedPositions; // État: positions déjà nettoyées
    private Set<Position> visitedPositions; // État: positions déjà visitées
    
    @Override
    protected void setup() {
        // Initialisation de l'agent
        env = Environment.getInstance();
        position = env.getRandomPosition();
        cleanedPositions = new HashSet<>();
        visitedPositions = new HashSet<>();
        
        System.out.println("Agent Réactif Avec État " + getLocalName() + " initialisé à la position " + position);
        
        // Informer l'interface graphique de la position initiale
        EnvironmentGUI2 gui = env.getGUI();
        if (gui != null) {
            gui.updateAgentPosition(getLocalName(), position);
        }
        
        // Comportement principal exécuté toutes les 1000 ms
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                // Ajouter la position actuelle aux positions visitées
                visitedPositions.add(position);
                
                // Percevoir l'environnement
                boolean isDirty = env.isDirty(position.getX(), position.getY());
                
                if (isDirty) {
                    // Nettoyer si la position actuelle est sale
                    env.clean(position.getX(), position.getY());
                    cleanedPositions.add(new Position(position.getX(), position.getY()));
                    System.out.println(getLocalName() + " nettoie la position " + position);
                } else {
                    // Déplacer vers une position non visitée de préférence
                    Position nextPosition = findNextPosition();
                    position = nextPosition;
                    System.out.println(getLocalName() + " se déplace vers " + position);
                }
                
                // Mettre à jour la position dans l'interface graphique
                EnvironmentGUI2 gui = env.getGUI();
                if (gui != null) {
                    gui.updateAgentPosition(getLocalName(), position);
                }
                
                // Vérifier si le nettoyage est terminé
                if (!env.hasDirt()) {
                    System.out.println(getLocalName() + ": Nettoyage terminé!");
                    doDelete();
                }
            }
        });
    }
    
    // Méthode pour trouver la prochaine position à visiter
    private Position findNextPosition() {
        // Chercher une position non visitée dans le voisinage
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Ignorer la position actuelle
                
                Position neighbor = new Position(position.getX() + dx, position.getY() + dy);
                if (!visitedPositions.contains(neighbor) && isValidPosition(neighbor)) {
                    return neighbor;
                }
            }
        }
        
        // Si toutes les positions voisines ont été visitées, choisir une position aléatoire
        return position.getRandomNeighbor(10, 10);
    }
    
    private boolean isValidPosition(Position pos) {
        return pos.getX() >= 0 && pos.getX() < 10 && pos.getY() >= 0 && pos.getY() < 10;
    }
    
    @Override
    protected void takeDown() {
        System.out.println("Agent Réactif Avec État " + getLocalName() + " terminé.");
        System.out.println("Positions nettoyées: " + cleanedPositions.size());
    }
}
