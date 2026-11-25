LOC « Catalogue, recherche et statistiques » (liste qui était sous ton nom dans le doc, que j'ai faite)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

✅ CRUD complet des lieux : PlaceListActivity + PlaceFormActivity

✅ Ajout / modification / suppression des visites : VisitFormActivity

✅ Association des lieux et visites aux voyages : VisitDao + VisitWithPlace

✅ Dates, notes et itinéraires

   - Le calendrier n'autorise que les dates du voyage

   - Itinéraire groupé par jour (« Jour 1 · vendredi 4 décembre ») et trié chronologiquement

✅ Écran détail d'un voyage avec ses lieux et visites : TripDetailActivity

✅ Cohérence des données liées

   - Lieu utilisé par des visites → suppression refusée (« utilisé par X visite(s) »)

   - Voyage raccourci → alerte « X visite(s) hors des dates du voyage »

   - Suppression d'un voyage → ses visites sont supprimées aussi

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

TESTS (tous verts)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

JUnit :

   - VisitRulesTest (6 tests) : dates dans le voyage, numéro de jour, itinéraire, cohérence, validation d'un lieu

   - TripImportTest (3 tests) : conversion, enregistrements invalides, doublons

Espresso :

   - PlaceScreensTest (2 tests) : formulaire vide → message d'erreur ; lieu ajouté → visible dans la liste

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

À NOTER

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

- Mes écrans sont des Activities (pas des Fragments).

- Dates stockées en millisecondes UTC (format MaterialDatePicker).

- Projet : JDK 25 obligatoire (Gradle JDK dans les Settings).

- Il reste le bloc Paramètres / DataStore / navigation / intégration (pas encore fait dans la branche d'Irina).
 
