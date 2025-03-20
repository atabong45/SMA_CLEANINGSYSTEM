/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Agent coopératif avec état
package agents;

import gui.EnvironmentGUI2;
import environment.Environment;
import environment.Position;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AgentCooperatif extends Agent {
    private Position position;
    private Environment env;
    private Set<Position> cleanedPositions; // Positions nettoyées par cet agent
    private Set<Position> sharedCleanedPositions; // Positions nettoyées par tous les agents
    private String partnerName; // Nom de l'agent partenaire
    
    @Override
    protected void setup() {
        // Récupérer le nom du partenaire à partir des arguments
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            partnerName = (String) args[0];
        } else {
            System.err.println("Agent Coopératif nécessite le nom du partenaire comme argument");
            doDelete();
            return;
        }
        
        // Initialisation de l'agent
        env = Environment.getInstance();
        position = env.getRandomPosition();
        cleanedPositions = new HashSet<>();
        sharedCleanedPositions = new HashSet<>();
        
        System.out.println("Agent Coopératif " + getLocalName() + " initialisé à la position " + position);
        System.out.println("Partenaire de coopération: " + partnerName);
        
        // Comportement pour écouter les messages
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = receive(mt);
                
                if (msg != null) {
                    try {
                        // Traiter le message reçu contenant une position nettoyée
                        Position cleanedPos = (Position) msg.getContentObject();
                        sharedCleanedPositions.add(cleanedPos);
                        System.out.println(getLocalName() + " a reçu l'information que " + 
                                          msg.getSender().getLocalName() + 
                                          " a nettoyé la position " + cleanedPos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    block();
                }
            }
        });
        
        // Informer l'interface graphique de la position initiale
        EnvironmentGUI2 gui = env.getGUI();
        if (gui != null) {
            gui.updateAgentPosition(getLocalName(), position);
        }
        
        // Comportement principal de nettoyage
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                // Percevoir l'environnement
                boolean isDirty = env.isDirty(position.getX(), position.getY());
                
                if (isDirty) {
                    // Nettoyer si la position actuelle est sale
                    env.clean(position.getX(), position.getY());
                    Position cleanedPos = new Position(position.getX(), position.getY());
                    cleanedPositions.add(cleanedPos);
                    sharedCleanedPositions.add(cleanedPos);
                    
                    // Informer le partenaire
                    informPartner(cleanedPos);
                    
                    System.out.println(getLocalName() + " nettoie la position " + position);
                } else {
                    // Déplacer vers une position non nettoyée
                    position = findNextPosition();
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
    
    // Méthode pour informer le partenaire d'une position nettoyée
    private void informPartner(Position cleanedPos) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(partnerName, AID.ISLOCALNAME));
        
        try {
            msg.setContentObject(cleanedPos);
            send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Méthode pour trouver la prochaine position à visiter
    private Position findNextPosition() {
        // Chercher une position sale qui n'a pas encore été nettoyée
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (dx == 0 && dy == 0) continue; // Ignorer la position actuelle
                
                Position neighbor = new Position(position.getX() + dx, position.getY() + dy);
                if (isValidPosition(neighbor) && !sharedCleanedPositions.contains(neighbor)) {
                    return neighbor;
                }
            }
        }
        
        // Si toutes les positions voisines ont été nettoyées, choisir une position aléatoire
        Position randomPos;
        do {
            randomPos = env.getRandomPosition();
        } while (sharedCleanedPositions.contains(randomPos));
        
        return randomPos;
    }
    
    private boolean isValidPosition(Position pos) {
        return pos.getX() >= 0 && pos.getX() < 10 && pos.getY() >= 0 && pos.getY() < 10;
    }
    
    @Override
    protected void takeDown() {
        System.out.println("Agent Coopératif " + getLocalName() + " terminé.");
        System.out.println("Positions nettoyées personnellement: " + cleanedPositions.size());
        System.out.println("Positions nettoyées au total (partagées): " + sharedCleanedPositions.size());
    }
}
