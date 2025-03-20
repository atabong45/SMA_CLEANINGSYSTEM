/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Agent réactif sans état
package agents;

import environment.Environment;
import environment.Position;
import gui.EnvironmentGUI2;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class AgentReactifSansEtat extends Agent {
    private Position position;
    private Environment env;
    
    @Override
    protected void setup() {
        // Initialisation de l'agent
        env = Environment.getInstance();
        position = env.getRandomPosition();
        
        System.out.println("Agent Réactif Sans État " + getLocalName() + " initialisé à la position " + position);
        
        // Informer l'interface graphique de la position initiale
        EnvironmentGUI2 gui = env.getGUI();
        if (gui != null) {
            gui.updateAgentPosition(getLocalName(), position);
        }
        
        // Comportement principal exécuté toutes les 1000 ms
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                // Percevoir l'environnement
                boolean isDirty = env.isDirty(position.getX(), position.getY());
                
                if (isDirty) {
                    // Nettoyer si la position actuelle est sale
                    env.clean(position.getX(), position.getY());
                    System.out.println(getLocalName() + " nettoie la position " + position);
                } else {
                    // Se déplacer aléatoirement
                    position = position.getRandomNeighbor(10, 10);
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
    
    @Override
    protected void takeDown() {
        System.out.println("Agent Réactif Sans État " + getLocalName() + " terminé.");
    }
}
