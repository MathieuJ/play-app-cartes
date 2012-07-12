package autre;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Carte;
import models.CarteModele;
import models.Partie;
import models.User;
import models.Zone;
import play.Logger;
import autre.ChatRoom.ActionGenerale;
import autre.ChatRoom.ActionEngageDegage;
import autre.ChatRoom.Event;

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
		for (int i = 1; i < 500; i++) {
			int a = (int) (Math.random() * n);
			zoneBibliotheque.getListeCarte().add(0, zoneBibliotheque.getListeCarte().remove(a));
		}
	}

	public static String meule(Partie partie, User user, int nombre) {
		Zone zoneDepart = partie.getZone("Bibliotheque", user);
		Zone zoneArrivee = partie.getZone("Cimetiere", user);
		String msg = "";
		for (int i = 0; i < nombre; i++) {
			if (!zoneDepart.getListeCarte().isEmpty()) {
				Carte carte = zoneDepart.getListeCarte().get(0);
				zoneDepart.remove(carte);
				zoneArrivee.add(carte);
				msg += carte.getNom() + " ";
			}
		}
		return msg;
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
	}

	public static void modifiePointsVie(Partie partie, int numJoueur, int nouveauPV) {
		if (numJoueur == 1) {
			partie.pointsVieJoueur1 = nouveauPV;
		} else {
			partie.pointsVieJoueur2 = nouveauPV;
		}
		partie.save();
	}

	public static Event actionGenerale(Partie partie, User userConnecte, String nomAction, String param1, String param2, String param3, String param4) {
		String msg = "";
		if (nomAction.equals("pioche")) {
			User userPioche = partie.getJoueur(new Boolean(param1));
			int nombre = new Integer(param2);
			PartieService.pioche(partie, userPioche, nombre);
			partie.getZone("Bibliotheque", userPioche).save();
			partie.getZone("Main", userPioche).save();
			if (new Boolean(param1)) {
				msg = userConnecte.username + " pioche " + param2 + " carte(s)";
			} else {
				msg = userConnecte.username + " fait piocher " + param2 + " carte(s) à " + partie.getJoueur(new Boolean(param1));
			}
		} else if (nomAction.equals("meule")) {
			User userMeule = partie.getJoueur(new Boolean(param1));
			int nombre = new Integer(param2);
			partie.getZone("Bibliotheque", userMeule).save();
			partie.getZone("Cimetiere", userMeule).save();
			msg = userConnecte + " se meule de " + PartieService.meule(partie, userMeule, nombre);
		} else if (nomAction.equals("attache")) {
			Carte carteAAttacher = Carte.findById(new Long(param1));
			Carte carteCible = Carte.findById(new Long(param2));
			Zone zoneSoi = partie.getZone("ChampBataille", true);
			Zone zoneAdv = partie.getZone("ChampBataille", false);
			carteAAttacher.setAttachee(true);
			if (zoneSoi.contains(carteCible)) {
				if (zoneAdv.contains(carteAAttacher)){
					zoneAdv.remove(carteAAttacher);
					zoneAdv.save();
				}
				int i = zoneSoi.getListeCarte().indexOf(carteCible);
				if (i > zoneSoi.getListeCarte().indexOf(carteAAttacher)) i--;
				zoneSoi.remove(carteAAttacher);
				zoneSoi.add(i, carteAAttacher);
				zoneSoi.save();
			} else {
				if (zoneSoi.contains(carteAAttacher)){
					zoneSoi.remove(carteAAttacher);
					zoneSoi.save();
				}
				int i = zoneAdv.getListeCarte().indexOf(carteCible);
				if (i > zoneSoi.getListeCarte().indexOf(carteAAttacher)) i--;
				zoneAdv.remove(carteAAttacher);
				zoneAdv.add(i, carteAAttacher);
				zoneAdv.save();
			}
			
			carteAAttacher.save();
			msg = userConnecte + " attache " + carteAAttacher.getNom() + " à " + carteCible.getNom();
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
			Zone zoneDepart = partie.getZone(param1, true);
			Logger.info(zoneDepart.nom + " " + param1 + zoneDepart.listeCarte);
			Zone zoneArrivee = partie.getZone(param2, true);
			for (Carte carte : zoneDepart.getListeCarte()) {
				zoneArrivee.add(carte);
			}
			zoneDepart.vide();
			zoneDepart.save();
			zoneArrivee.save();
			msg = userConnecte + " melange " + zoneDepart.nom + zoneDepart.getTaille() + " dans " + zoneArrivee.nom + zoneArrivee.getTaille();

		} else if (nomAction.equals("engageOuDegage")) {
			Carte carte = Carte.findById(new Long(param1));
			carte.setEngagee(!carte.isEngagee());
			carte.save();
			/*if (carte.isEngagee()) {
				return new ActionEngageDegage(userConnecte.username, carte.getId().toString(), "degage");
			} else {
				return new ActionEngageDegage(userConnecte.username, carte.getId().toString(), "engage");
			}*/
			msg=" engage " + carte.getNom();
		} else if (nomAction.equals("mulligan")) {
			Zone zoneMain = partie.getZone("Main", true);
			int taille = zoneMain.getTaille();
			reset(partie, partie.getNumero(true));
			pioche(partie, partie.getJoueur(true), taille - 1);
			partie.save();
			msg = " mulligane.";
		} else if (nomAction.equals("degageTout")) {
			for (Carte carte : partie.getZone("ChampBataille", true).getListeCarte()) {
				carte.setEngagee(false);
				carte.save();
			}
			msg = " degage ses cartes.";
		} else if (nomAction.equals("creeToken")) {
			CarteModele carteModele = CarteModele.find("byNom", param1).first();
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
			msg = nomAction + " avec " + carte.getNom();
		} else if (nomAction.equals("setEtape")) {
			int etape = new Integer(param1);
			switch (etape) {
			case 1:
				partie.tour = 3 - partie.getTour();
				partie.etape = 1;
				msg = "Nouveau tour : " + partie.getJoueur(partie.getTour());
				for (Carte carte : partie.getZone("ChampBataille", partie.getJoueur(partie.getTour())).getListeCarte()) {
					carte.setEngagee(false);
					carte.save();
				}
				break;
			case 2:
				msg = "passe à l'upkeep";
				break;
			case 3:
				msg = "passe à l'étape de pioche";
				break;
			case 4:
				msg = "passe à l'étape principale";
				break;
			case 5:
				msg = "passe à l'étape d'attaque";
				break;
			case 6:
				msg = "passe à l'étape de declaration des defenseurs";
				break;
			case 7:
				msg = "passe à l'étape principale 2";
				break;
			case 8:
				msg = "passe à l'étape de fin de tour";
				break;
			}
			partie.etape = etape;
			partie.save();

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
			deplace(carte, partie.getZone(param2, true), partie.getZone(param3, partie.getJoueur(carte.proprietaire)));
			

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
			reset(partie, 1);
			reset(partie, 2);

			partie.pointsVieJoueur1 = 20;
			partie.pointsVieJoueur2 = 20;
			partie.tour = (int) (Math.random() * 2) + 1;
			partie.etape = 3;
			partie.save();
			msg = " redemarre la partie.";
		}
		return new ActionGenerale(partie.getNumero(true), userConnecte.username, msg);
	}

	private static void reset(Partie partie, int i) {
		partie.getZone("Cimetiere_" + i).vide();
		partie.getZone("ChampBataille_" + i).vide();
		partie.getZone("Main_" + i).vide();
		partie.getZone("Exil_" + i).vide();
		partie.getZone("Bibliotheque_" + i).vide();
		
		for (Carte carte : partie.getZone("Deck_" + i).getListeCarte()){
			if (carte.isEngagee() || carte.isAttachee() || carte.isRetournee())
			carte.setEngagee(false);
			carte.setRetournee(false);
			carte.setAttachee(false);
			carte.save();
		}
		
		chargeBibliotheque(partie.getZone("Deck_" + i), partie.getZone("Bibliotheque_" + i));

		pioche(partie, partie.getJoueur(i), 7);

		partie.getZone("Cimetiere_" + i).save();
		partie.getZone("ChampBataille_" + i).save();
		partie.getZone("Main_" + i).save();
		partie.getZone("Exil_" + i).save();
		partie.getZone("Bibliotheque_" + i).save();
	}

	private static void deplace(Carte carte, Zone zoneDepart, Zone zoneArrivee) {
		zoneDepart.remove(carte);
		if (!carte.isToken()){
			if ((zoneDepart.getNom().endsWith("1") && zoneArrivee.getNom().endsWith("2")) || (zoneDepart.getNom().endsWith("2") && zoneArrivee.getNom().endsWith("1"))) {
				// TODO
			}
			zoneArrivee.add(carte);
		}
		carte.setEngagee(false);
		carte.setRetournee(false);
		carte.setRetournee(false);
		carte.setAttachee(false);
		carte.save();
		zoneDepart.save();
		zoneArrivee.save();
	}

}
