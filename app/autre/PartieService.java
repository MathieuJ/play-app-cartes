package autre;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Carte;
import models.CarteModele;
import models.Partie;
import models.User;
import models.Zone;
import play.Logger;

public class PartieService {

	public static String setDeck(Zone zoneDeck, String texte, int numProprietaire) {
		zoneDeck.vide();

		Pattern p = Pattern.compile("([0-9]+)\\s(['\\-A-Za-z\\s]*)$", Pattern.MULTILINE);
		Matcher matcher = p.matcher(texte);
		String erreurs = null;
		while (matcher.find()) {
			Logger.info(matcher.group(2).trim());
			CarteModele cm = CarteModele.find("byNom", matcher.group(2).trim()).first();
			if (cm == null) {
				System.out.println("carte non trouvee : [" + matcher.group(2).trim() + "]");
				erreurs += "Carte " + matcher.group(2) + " non trouvee. \n";
			} else {
				Logger.info("carte trouvee " + cm);
				for (int i = 0; i < new Integer(matcher.group(1)); i++) {
					Carte carte = new Carte(cm);
					carte.setProprietaire(numProprietaire);
					zoneDeck.add(carte);
				}
			}
		}
		zoneDeck.save();
		return erreurs;
	}

	public static void chargeBibliotheque(Zone zoneDeck, Zone zoneBibliotheque) {
		if (zoneDeck.getTaille() == 0) {
			return;
		}
		// on colle le deck dans la bibli
		for (Carte carte : zoneDeck.getListeCarte()) {
			zoneBibliotheque.add(carte);
		}
		// on mélange les cartes
		int n = zoneBibliotheque.getTaille();
		for (int i = 1; i < 100; i++) {
			int a = (int) (Math.random() * n);
			zoneBibliotheque.getListeCarte().add(0, zoneBibliotheque.getListeCarte().remove(a));
		}
		zoneBibliotheque.save();
	}

	public static void meule(Partie partie, User user, int nombre) {
		Zone zoneDepart = partie.getZone("Bibliotheque", user);
		Zone zoneArrivee = partie.getZone("Cimetiere", user);
		for (int i = 0; i < nombre; i++) {
			if (!zoneDepart.getListeCarte().isEmpty()) {
				Carte carte = zoneDepart.getListeCarte().get(0);
				zoneDepart.remove(carte);
				zoneArrivee.add(carte);
			}
		}
		zoneArrivee.save();
		zoneDepart.save();
	}

	public static void pioche(Partie partie, User userPioche, int nombre) {
		Zone zoneDepart = partie.getZone("Bibliotheque", userPioche);
		Zone zoneArrivee = partie.getZone("Main", userPioche);
		for (int i = 0; i < nombre; i++) {
			if (!zoneDepart.estVide()) {
				Carte carte = zoneDepart.getListeCarte().get(0);
				zoneDepart.remove(carte);
				zoneArrivee.add(carte);
			} else {
				Logger.info("Erreur : zone depart vide");
			}
		}
		zoneDepart.save();
		zoneArrivee.save();
	}

	public static void modifiePointsVie(Partie partie, int numJoueur, int nouveauPV) {
		if (numJoueur == 1) {
			partie.pointsVieJoueur1 = nouveauPV;
		} else {
			partie.pointsVieJoueur2 = nouveauPV;
		}
		partie.save();
	}

	public static String actionGenerale(Partie partie, User userConnecte, String nomAction, String param1, String param2, String param3, String param4) {
		String msg = "";
		if (nomAction.equals("pioche")) {
			User userPioche = partie.getJoueur(new Boolean(param1));
			int nombre = new Integer(param2);
			PartieService.pioche(partie, userPioche, nombre);

			if (new Boolean(param1)) {
				msg = userConnecte.username + " pioche " + param2 + " carte(s)";
			} else {
				msg = userConnecte.username + " fait piocher " + param2 + " carte(s) à " + partie.getJoueur(new Boolean(param1));
			}
		} else if (nomAction.equals("meule")) {
			User userMeule = partie.getJoueur(new Boolean(param1));
			int nombre = new Integer(param2);
			PartieService.meule(partie, userMeule, nombre);
			msg = userConnecte + " se meule " + param3 + " de carte(s)";
		} else if (nomAction.equals("deplacePaquet")) {
			Zone zoneDepart = partie.getZone(param1, true);
			Zone zoneArrivee = partie.getZone(param2, true);
			String[] listeSelectionnee = param3.split(",");
			for (String selection : listeSelectionnee){
				Carte carte = Carte.findById(new Long(selection));
				Logger.info("Carte " + carte + " " + zoneDepart.nom + " "  + zoneArrivee.nom);
				for (Carte carte2 : zoneDepart.getListeCarte()) {
					Logger.info("Carte " + carte2.id);
				}
				if (zoneDepart.remove(carte)) {
					Logger.info("Carte trouvee");
					zoneArrivee.add(carte);
				} else {
					Logger.info("Carte trouvee");
				}
			}
			zoneDepart.save();
			zoneArrivee.save();
			
			msg = userConnecte + " remet dans " + zoneArrivee.nom + " de carte(s)";
		} else if (nomAction.equals("melange")) {
			Zone zoneDepart = partie.getZone("Cimetiere_1");// param1, new
			                                                // Boolean(param3));
			Zone zoneArrivee = partie.getZone("Bibliotheque_1");// , new
			                                                    // Boolean(param3));
			for (Carte carte : zoneDepart.getListeCarte()) {
				zoneDepart.remove(carte);
				zoneArrivee.add(carte);
			}
			zoneDepart.save();
			zoneArrivee.save();
			msg = userConnecte + " melange " + zoneDepart.nom + zoneDepart.getTaille() + " dans " + zoneArrivee.nom + zoneArrivee.getTaille();

		} else if (nomAction.equals("engageCapacite")) {
			Carte carte = Carte.findById(new Long(param1));
			carte.setEngagee(!carte.isEngagee());
			carte.save();
			msg = nomAction + " " + carte.getNom();
		} else if (nomAction.equals("degageTout")) {
			for (Carte carte : partie.getZone("ChampBataille", true).getListeCarte()) {
				carte.setEngagee(false);
				carte.save();
			}
			msg = " degage ses cartes.";
		} else if (nomAction.equals("creeToken")) {
			CarteModele carteModele = CarteModele.findById(new Long(param1));
			Carte carte = new Carte(carteModele);
			carte.setProprietaire(partie.getNumero(true));
			carte.setToken(true);
			Zone zone = partie.getZone("ChampBataille", true);
			zone.add(carte);
			zone.save();
			carte.save();
			msg = " cree un token.";
		} else if (nomAction.equals("attaque")) {
			Carte carte = Carte.findById(new Long(param1));
			carte.setEngagee(!carte.isEngagee());
			carte.save();
			msg = nomAction + " " + carte.getNom();
		} else if (nomAction.equals("prochaineEtape")) {
			partie.tour += 1;
			partie.save();
			msg = "Tour " + partie.tour;
		} else if (nomAction.equals("transforme")) {
			Carte carte = Carte.findById(new Long(param1));
			carte.setTransformee(!carte.isTransformee());
			carte.save();
			msg = "Transformation de " + carte.getNom();
		} else if (nomAction.equals("modifiePV")) {
			int numJoueur = new Integer(param1);
			int nouveauPV = new Integer(param2);
			PartieService.modifiePointsVie(partie, numJoueur, nouveauPV);
			msg = " passed a " + param2 + " PV";
		} else if (nomAction.equals("deplace")) {
			Carte carte = Carte.findById(new Long(param1));
			deplace(carte, partie.getZone(param2, true), partie.getZone(param3, true));
			

			String strAction = " ??? ";
			if ("Main".equals(param2)) {
				if ("ChampBataille".equals(param3)) {
					strAction = "joue";
				} else if ("Cimetiere".equals(param3)) {
					strAction = "défausse";
				} else if ("Exil".equals(param3)) {
					strAction = "exile";
				} else if ("Bibliotheque".equals(param3)) {
					strAction = "remet en bibliotheque";
				}
			} else if ("ChampBataille".equals(param2)) {
				if ("Main".equals(param3)) {
					strAction = "renvoie en main";
				} else if ("Cimetiere".equals(param3)) {
					strAction = "met au cimetiere";
				} else if ("Exil".equals(param3)) {
					strAction = "exile";
				} else if ("Bibliotheque".equals(param3)) {
					strAction = "remet en bibliotheque";
				}
			} else if ("Cimetiere".equals(param2)) {
				if ("ChampBataille".equals(param3)) {
					strAction = "exhume";
				} else if ("Main".equals(param3)) {
					strAction = "remet en main";
				} else if ("Exil".equals(param3)) {
					strAction = "exile";
				} else if ("Bibliotheque".equals(param3)) {
					strAction = "remet en bibliotheque";
				}
			}
			msg = " " + strAction + " " + carte.getNom();
		} else if (nomAction.equals("reset")) {
			partie.getZone("Cimetiere_1").vide();
			partie.getZone("Cimetiere_2").vide();
			partie.getZone("ChampBataille_1").vide();
			partie.getZone("ChampBataille_2").vide();
			partie.getZone("Main_1").vide();
			partie.getZone("Main_2").vide();
			partie.getZone("Exil_1").vide();
			partie.getZone("Exil_2").vide();
			partie.getZone("Bibliotheque_1").vide();
			partie.getZone("Bibliotheque_2").vide();

			chargeBibliotheque(partie.getZone("Deck_1"), partie.getZone("Bibliotheque_1"));
			chargeBibliotheque(partie.getZone("Deck_2"), partie.getZone("Bibliotheque_2"));

			partie.getZone("Cimetiere_1").save();
			partie.getZone("Cimetiere_2").save();
			partie.getZone("ChampBataille_1").save();
			partie.getZone("ChampBataille_2").save();
			partie.getZone("Main_1").save();
			partie.getZone("Main_2").save();
			partie.getZone("Exil_1").save();
			partie.getZone("Exil_2").save();
			partie.getZone("Bibliotheque_1").save();
			partie.getZone("Bibliotheque_2").save();

			partie.pointsVieJoueur1 = 20;
			partie.pointsVieJoueur2 = 20;

		}
		return msg;
	}

	private static void deplace(Carte carte, Zone zoneDepart, Zone zoneArrivee) {
		zoneDepart.remove(carte);
		if (!carte.isToken()){
			zoneArrivee.add(carte);
		}
		zoneDepart.save();
		zoneArrivee.save();
	}

}
