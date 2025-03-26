package agents;

import gui.EnvironmentGUI2;
import environment.Environment;
import environment.Position;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;
import java.util.List;

public class AgentABut extends Agent {
    private Position position;
    private Environment env;
    private List<Position> dirtyPositions;
    private List<Position> path;
    
    @Override
    protected void setup() {
        // Initialisation de l'agent
        env = Environment.getInstance();
        position = env.getRandomPosition();
        dirtyPositions = new ArrayList<>();
        path = new ArrayList<>();
        
        // Identifier toutes les positions sales initialement
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (env.isDirty(x, y)) {
                    dirtyPositions.add(new Position(x, y));
                }
            }
        }
        
        // Calculer un chemin pour nettoyer toutes les positions sales
        calculerCheminOptimal();
        
        System.out.println("Agent à But " + getLocalName() + " initialisé. Objectif : nettoyer " + dirtyPositions.size() + " positions");
        
        // Informer l'interface graphique de la position initiale
        EnvironmentGUI2 gui = env.getGUI();
        if (gui != null) {
            gui.updateAgentPosition(getLocalName(), position);
        }
        
        // Comportement principal exécuté toutes les 1000 ms
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                // Si le chemin est vide, mission accomplie
                if (path.isEmpty()) {
                    System.out.println(getLocalName() + ": Tous les objectifs de nettoyage atteints!");
                    doDelete();
                    return;
                }
                
                // Se déplacer vers la prochaine position cible
                Position nextPosition = path.remove(0);
                position = nextPosition;
                
                // Nettoyer la position si elle est sale
                if (env.isDirty(position.getX(), position.getY())) {
                    env.clean(position.getX(), position.getY());
                    System.out.println(getLocalName() + " nettoie la position " + position);
                }
                
                // Mettre à jour la position dans l'interface graphique
                EnvironmentGUI2 gui = env.getGUI();
                if (gui != null) {
                    gui.updateAgentPosition(getLocalName(), position);
                }
            }
        });
    }
    
    // Méthode pour calculer un chemin optimal de nettoyage
    private void calculerCheminOptimal() {
        // Algorithme simple de plus proche voisin
        while (!dirtyPositions.isEmpty()) {
            // Trouver la position sale la plus proche
            Position closestDirty = trouverPositionLaPlusProche(position, dirtyPositions);
            
            if (closestDirty != null) {
                path.add(closestDirty);
                dirtyPositions.remove(closestDirty);
                position = closestDirty;
            }
        }
    }
    
    // Trouver la position la plus proche dans une liste
    private Position trouverPositionLaPlusProche(Position from, List<Position> positions) {
        Position closestPosition = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Position pos : positions) {
            int distance = from.distanceTo(pos);
            if (distance < minDistance) {
                minDistance = distance;
                closestPosition = pos;
            }
        }
        
        return closestPosition;
    }
    
    @Override
    protected void takeDown() {
        System.out.println("Agent à But " + getLocalName() + " terminé.");
    }
}