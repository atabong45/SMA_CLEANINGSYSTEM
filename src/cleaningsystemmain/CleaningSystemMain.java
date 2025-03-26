/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cleaningsystemmain;

import environment.Environment;
import gui.EnvironmentGUI2;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javax.swing.SwingUtilities;

public class CleaningSystemMain {
    
    public static void main(String[] args) {
        try {
            // Initialiser l'environnement
            Environment env = Environment.getInstance(10, 10);
            
            // Créer et initialiser l'interface graphique
            SwingUtilities.invokeLater(() -> {
                EnvironmentGUI2 gui = new EnvironmentGUI2(env);
                env.setGUI(gui);
            });
            
            // Initialiser le runtime JADE
            Runtime rt = Runtime.instance();
            
            // Créer les profils des conteneurs
            ProfileImpl profile1 = new ProfileImpl();
            profile1.setParameter(Profile.CONTAINER_NAME, "mon_Conteneur-1");
            profile1.setParameter(Profile.MAIN_HOST, "localhost");
            
            ProfileImpl profile2 = new ProfileImpl();
            profile2.setParameter(Profile.CONTAINER_NAME, "Conteneur-2");
            profile2.setParameter(Profile.MAIN_HOST, "localhost");
            
            ProfileImpl profile3 = new ProfileImpl();
            profile3.setParameter(Profile.CONTAINER_NAME, "Conteneur-3");
            profile3.setParameter(Profile.MAIN_HOST, "localhost");

            ProfileImpl profile4 = new ProfileImpl();
            profile3.setParameter(Profile.CONTAINER_NAME, "Conteneur-4");
            profile3.setParameter(Profile.MAIN_HOST, "localhost");

            ProfileImpl profile5 = new ProfileImpl();
            profile3.setParameter(Profile.CONTAINER_NAME, "Conteneur-5");
            profile3.setParameter(Profile.MAIN_HOST, "localhost");
            
            ProfileImpl mainProfile = new ProfileImpl();
            mainProfile.setParameter(Profile.GUI, "true");
            
            // Créer les conteneurs
            AgentContainer mainContainer = rt.createMainContainer(mainProfile);
            AgentContainer container1 = rt.createAgentContainer(profile1);
            AgentContainer container2 = rt.createAgentContainer(profile2);
            AgentContainer container3 = rt.createAgentContainer(profile3);
            AgentContainer container4 = rt.createAgentContainer(profile4);    
            AgentContainer container5 = rt.createAgentContainer(profile5);            

            System.out.println("Conteneurs créés");
            
            // Attendre que l'interface graphique soit initialisée
            Thread.sleep(1000);
            
            // Créer et démarrer l'agent sans état dans le conteneur 1
            // AgentController agentSansEtat = container1.createNewAgent(
            //     "AgentSansEtat", 
            //     "agents.AgentReactifSansEtat", 
            //     null);
            //agentSansEtat.start();
            
            // Créer et démarrer l'agent avec état dans le conteneur 2
            // AgentController agentAvecEtat = container2.createNewAgent(
            //     "AgentAvecEtat", 
            //     "agents.AgentReactifAvecEtat", 
            //     null);
            // agentAvecEtat.start();
            
            // Créer et démarrer les agents coopératifs dans le conteneur 3
            // AgentController agentCoop1 = container3.createNewAgent(
            //     "AgentCoop1", 
            //     "agents.AgentCooperatif", 
            //     new Object[]{"AgentCoop2"});
            
            // AgentController agentCoop2 = container3.createNewAgent(
            //     "AgentCoop2", 
            //     "agents.AgentCooperatif", 
            //     new Object[]{"AgentCoop1"});
            
            // agentCoop1.start();
            // agentCoop2.start();

            // Créer et démarrer l'agent a but dans le conteneur 4
            // AgentController agentABut = container4.createNewAgent(
            //     "AgentABut", 
            //     "agents.AgentABut", 
            //     null);
            // agentABut.start();

            //Créer et démarrer l'agent a but dans le conteneur 4
            AgentController agentAUtilite = container5.createNewAgent(
                "AgentAUtilite", 
                "agents.AgentAUtilite", 
                null);
            agentAUtilite.start();

            System.out.println("Système multi-agent de nettoyage démarré");
            
        } catch (StaleProxyException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

