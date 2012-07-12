package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import play.Logger;
import play.db.jpa.Model;
import play.mvc.Scope.Session;

@Entity
public class Partie extends Model {
	public int compteursPoisonJoueur1;

	public int compteursPoisonJoueur2;

	@OneToOne
	public User joueur1;

	@OneToOne
	public User joueur2;

	@OneToMany(mappedBy = "partie")
	public List<Zone> listeZone = new ArrayList<Zone>();

	public String nom;

	public Date date;

	public int tour;

	// 1 : upkeep, 2: draw; 3: Main, 4: declare attackers, 5: declare blockers, 6:
	// Main 2, 7: EOT.
	public int etape = 3;

	public String getNom() {
		return nom;
	}

	public Date getDate() {
		return date;
	}

	@OneToOne
	public PartieModele partieModele;

	public int pointsVieJoueur1;

	public int pointsVieJoueur2;

	public Boolean isDemarree() {
		return (joueur2 != null && !getZone("Deck_1").estVide() && !getZone("Deck_2").estVide());
	}

	@Transient
	private User joueurSoi;

	public User getSoi() {
		if (joueurSoi == null) {
			String username = Session.current().get("user");
			if (username != null) {
				joueurSoi = User.findByName(username);
			}
		}
		return joueurSoi;
	}

	public int getNumero(boolean soi) {
		// Logger.info("js " + getSoi() + " " + joueur1);
		if (getSoi().id == joueur1.id ^ soi) {
			// Logger.info("connecte : " + joueurSoi.id + " joueur1 : " + joueur1.id +
			// " soi " + soi + " res : " + 2);
			return 2;
		} else {
			// Logger.info("connecte : " + joueurSoi.id + " joueur1 : " + joueur1.id +
			// " soi " + soi + " res : " + 1);
			return 1;
		}
	}

	public Partie(User user, PartieModele partieModele, Date date, String nom) {
		super();
		this.joueur1 = user;
		this.partieModele = partieModele;
		pointsVieJoueur1 = 20;
		pointsVieJoueur2 = 20;
		this.date = date;
		this.nom = nom;
		this.tour = (int) (Math.random() * 2) + 1;
	}

	public int getTour() {
		return tour;
	}

	public void setTour(int tour) {
		this.tour = tour;
	}

	public User getJoueur(boolean soi) {
		if (getNumero(soi) == 1) {
			// Logger.info("get joueur " + soi + " " + getNumero(soi) + " " +
			// joueur1.username);
			return joueur1;
		} else {
			// Logger.info("get joueur " + soi + " " + getNumero(soi) + " " +
			// joueur2.username);
			return joueur2;
		}
	}

	// public User getJoueur(int numero) {
	// if (numero == 1) {
	// return joueur1;
	// } else {
	// return joueur2;
	// }
	// }

	public User getJoueur1() {
		return joueur1;
	}

	public User getJoueur2() {
		return joueur2;
	}

	/*
	 * public List<Carte> getListeCarteAdversaire(String zoneNom, User user) {
	 * Zone z; if (user == joueur1) { z = getZone(zoneNom + "_2"); } else { z =
	 * getZone(zoneNom + "_1"); } return z.listeCarte; }
	 * 
	 * public List<Carte> getListeCarteSoi(String zoneNom, User user) { Zone z; if
	 * (user == joueur1) { z = getZone(zoneNom + "_1"); } else { z =
	 * getZone(zoneNom + "_2"); }
	 * 
	 * return z.listeCarte; }
	 */

	/*
	 * public List<Zone> getListeZone() { return listeZone; }
	 */

	/*
	 * public List<String> getListeZoneStr() { List l = new ArrayList();
	 * 
	 * for (Zone z : listeZone) { l.add(z.getNom()); } return l; }
	 */

	public PartieModele getPartieModele() {
		return partieModele;
	}

	public int getPointsVie(boolean soi) {
		if (getNumero(soi) == 1) {
			return pointsVieJoueur1;
		} else {
			return pointsVieJoueur2;
		}
	}

	public Zone getZone(String zoneNom) {
		for (Zone zone : listeZone) {
			if (zone.getNom().equals(zoneNom)) {
				//Logger.info("get zone " + zoneNom + " " + zone.getTaille());
				return zone;
			}
		}
		return null;
	}

	/**
	 * UTILE
	 * 
	 * @param zoneNom
	 * @param joueur
	 * @return
	 */
	public Zone getZone(String zoneNom, User joueur) {
		if (joueur == joueur1) {
			return getZone(zoneNom + "_1");
		} else {
			return getZone(zoneNom + "_2");
		}
	}

	public Zone getZone(String zoneNom, boolean soi) {
		// Logger.info("get Zone " + zoneNom + " " + soi + " " + getNumero(soi));
		return getZone(zoneNom + "_" + getNumero(soi));
	}

	public void initZones() {
		new Zone(this, "Deck_1").create();
		new Zone(this, "Deck_2").create();
		new Zone(this, "Bibliotheque_1").create();
		new Zone(this, "Bibliotheque_2").create();
		new Zone(this, "Cimetiere_1").create();
		new Zone(this, "Cimetiere_2").create();
		new Zone(this, "ChampBataille_1").create();
		new Zone(this, "ChampBataille_2").create();
		new Zone(this, "Main_1").create();
		new Zone(this, "Main_2").create();
		new Zone(this, "Exil_1").create();
		new Zone(this, "Exil_2").create();
	}

	public User getJoueur(Integer integer) {
		if (integer == 1) {
			return joueur1;
		} else {
			return joueur2;
		}
	}

}
