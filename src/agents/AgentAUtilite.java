package agents;

import gui.EnvironmentGUI2;
import environment.Environment;
import environment.Position;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;
import java.util.List;

public class AgentAUtilite extends Agent {
    private Position position;
    private Environment env;
    private List<Position> dirtyPositions;
    private List<Position> path;
    private int totalCleaned = 0;
    private int energyConsumed = 0;
    private static final int MAX_ENERGY = 100;
    
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
        
        // Calculer un chemin optimal en considérant l'utilité
        calculerCheminOptimal();
        
        System.out.println("Agent à Utilité " + getLocalName() + " initialisé. Objectif : nettoyer efficacement");
        
        // Informer l'interface graphique de la position initiale
        EnvironmentGUI2 gui = env.getGUI();
        if (gui != null) {
            gui.updateAgentPosition(getLocalName(), position);
        }
        
        // Comportement principal exécuté toutes les 1000 ms
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                // Vérifier l'énergie restante
                if (energyConsumed >= MAX_ENERGY) {
                    System.out.println(getLocalName() + ": Energie épuisée. Bilan : " + 
                        totalCleaned + " positions nettoyées.");
                    doDelete();
                    return;
                }
                
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
                    totalCleaned++;
                    System.out.println(getLocalName() + " nettoie la position " + position);
                }
                
                // Consommer de l'énergie
                energyConsumed++;
                
                // Mettre à jour la position dans l'interface graphique
                EnvironmentGUI2 gui = env.getGUI();
                if (gui != null) {
                    gui.updateAgentPosition(getLocalName(), position);
                }
            }
        });
    }
    
    // Méthode pour calculer un chemin optimal avec une fonction d'utilité
    private void calculerCheminOptimal() {
        while (!dirtyPositions.isEmpty() && energyConsumed < MAX_ENERGY) {
            // Calculer l'utilité de chaque position sale restante
            Position bestPosition = trouverPositionLaPlusUtile(position, dirtyPositions);
            
            if (bestPosition != null) {
                path.add(bestPosition);
                dirtyPositions.remove(bestPosition);
                position = bestPosition;
            }
        }
    }
    
    // Trouver la position la plus utile selon plusieurs critères
    private Position trouverPositionLaPlusUtile(Position from, List<Position> positions) {
        Position bestPosition = null;
        double maxUtility = -1;
        
        for (Position pos : positions) {
            // Calculer l'utilité basée sur plusieurs facteurs
            double utility = calculerUtilite(from, pos);
            
            if (utility > maxUtility) {
                maxUtility = utility;
                bestPosition = pos;
            }
        }
        
        return bestPosition;
    }
    
    // Fonction d'utilité complexe
    private double calculerUtilite(Position from, Position to) {
        // Distance de Manhattan
        int distance = from.distanceTo(to);
        
        // Facteur de saleté (plus la position est sale, plus elle a de valeur)
        double dirtyFactor = env.isDirty(to.getX(), to.getY()) ? 1.0 : 0.1;
        
        // Pénalité d'énergie
        double energyPenalty = 1.0 / (MAX_ENERGY - energyConsumed + 1);
        
        // Utilité = importance de la position / (distance * pénalité d'énergie)
        return dirtyFactor / (distance * energyPenalty);
    }
    
    @Override
    protected void takeDown() {
        System.out.println("Agent à Utilité " + getLocalName() + " terminé.");
        System.out.println("Positions nettoyées : " + totalCleaned + 
                           ", Energie consommée : " + energyConsumed);
    }
}
