Dans ce système multi-agent (SMA), chaque agent est dans son propre conteneur et opère indépendamment, sauf dans un troisième conteneur où deux agents collaborent.

1. Conteneur 1 : Agent Réactif Sans État
Cet agent fonctionne uniquement sur la base de stimuli immédiats sans mémoriser d’informations.
Scénario :
L’agent détecte une saleté à proximité et se dirige immédiatement vers elle pour la nettoyer.
Une fois la tâche terminée, il continue à errer aléatoirement jusqu’à détecter une nouvelle saleté.
Il ne garde aucune trace des endroits déjà nettoyés, ce qui peut entraîner des nettoyages redondants.
Mise en œuvre :
Capteurs de proximité pour détecter la saleté.
Règles simples :
Si une saleté est détectée → Nettoyer.
Sinon → Se déplacer de manière aléatoire.
Cet agent fonctionne bien dans un environnement où la répartition des saletés est homogène, mais il risque d’être inefficace si certaines zones sont nettoyées plusieurs fois inutilement.

2. Conteneur 2 : Agent Réactif Avec État
Cet agent mémorise les zones nettoyées pour éviter les déplacements inutiles.
Scénario :
L’agent enregistre les positions nettoyées et adapte ses déplacements pour couvrir de nouvelles zones.
Il suit un schéma d’exploration méthodique, en quadrillant l’espace pour garantir une couverture efficace.
Contrairement à l’agent sans état, il ne retourne pas nettoyer une zone déjà propre.
Mise en œuvre :
Stockage des coordonnées des zones nettoyées.
Règles d’action :
Si une saleté est détectée → Nettoyer et enregistrer la zone comme propre.
Sinon → Se déplacer vers une zone non explorée.
Cet agent est plus efficace, car il maximise la couverture et évite les nettoyages inutiles.

3. Conteneur 3 : Deux Agents Réactifs Avec État Qui Coopèrent
Dans ce conteneur, deux agents avec état échangent des informations pour éviter de nettoyer les mêmes zones.
Scénario :
Chaque agent suit une stratégie méthodique pour nettoyer le sol.
Avant d’aller vers une zone, un agent consulte une mémoire partagée pour savoir si elle a déjà été nettoyée par l’autre.
S’ils détectent une saleté, ils la nettoient et mettent à jour la mémoire commune.
Cela permet d’optimiser le nettoyage et de réduire le temps perdu.
Mise en œuvre :
Chaque agent enregistre ses actions dans une base de données partagée ou communique via un protocole d’échange.
Règles d’action :
Si une saleté est détectée et que la zone n’a pas été marquée comme propre → Nettoyer et enregistrer la zone.
Sinon → Vérifier la mémoire commune et choisir une zone non nettoyée.
Cette approche optimise le nettoyage en garantissant une distribution efficace des tâches.

Résumé
Conteneur 1 : Agent sans état, nettoie de manière aléatoire sans optimiser ses déplacements.
Conteneur 2 : Agent avec état, mémorise les zones nettoyées pour éviter les redondances.
Conteneur 3 : Deux agents avec état, collaborent en partageant les informations pour optimiser le nettoyage.
Cette conception permet d’explorer différents niveaux d’intelligence et de coopération entre agents dans un SMA.

