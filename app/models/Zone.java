package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class Zone extends Model {
	@ManyToOne
	public Partie partie;

	/*
	 * @OneToOne public ZoneModele zoneModele;
	 */

	public String nom;

	public Partie getPartie() {
		return partie;
	}

	public String getNom() {
		return nom;
	}

	public List<Carte> getListeCarte() {
		return listeCarte;
	}

	public Zone(Partie partie, String nom) {
		super();
		this.partie = partie;
		this.nom = nom;
	}

	public Zone() {
		super();
	}

	@ManyToMany(cascade = CascadeType.ALL)
	@OrderColumn
	@JoinTable(name = "ZONE_CARTE")
	public List<Carte> listeCarte = new ArrayList<Carte>();

	public void add(Carte carte) {
		// carte.zone = this;
		listeCarte.add(carte);
		if ("ChampBataille_1".equals(nom) || "ChampBataille_2".equals(nom)) {
			calculePositions();
		}
	}

	public String toString() {
		return id + " : " + partie.id + " > " + nom;
	}

	public int getTaille() {
		return listeCarte.size();
	}

	public boolean contains(Carte carte) {
		return listeCarte.contains(carte);
	}

	public boolean estVide() {
		return listeCarte.isEmpty();
	}

	public boolean remove(Carte carte) {
		boolean t = listeCarte.remove(carte);
		if ("ChampBataille_1".equals(nom) || "ChampBataille_2".equals(nom)) {
			calculePositions();
		}
		return t;
	}

	private void calculePositions() {
		List<Carte> listeTerrains = new ArrayList();
		List<Carte> listePermanents = new ArrayList();
		for (Carte carte : listeCarte) {
			if (carte.carteModele.id >= 250 && carte.carteModele.id < 255) {
				listeTerrains.add(carte);
			} else {
				listePermanents.add(carte);
			}
		}
		Collections.sort(listeTerrains, new Comparator<Carte>() {
			@Override
			public int compare(Carte o1, Carte o2) {
				// TODO Auto-generated method stub
				return (int) (o1.carteModele.id - o2.carteModele.id);
			}
		});
		int left = 10;
		int top = 10;
		for (Carte carte : listePermanents) {
			carte.top = top;
			carte.left = left;
			left += 80;
		}
		left = -70;
		top = 110;
		Long i = (long) 0;
		for (Carte carte : listeTerrains) {
			if (carte.carteModele.id == i) {
				left += 30;
				top += 0;
			} else {
				left += 80;
				top = 110;
			}
			carte.top = top;
			carte.left = left;

			i = carte.carteModele.id;
		}
		for (Carte carte : listePermanents) {
			carte.save();
		}
		for (Carte carte : listeTerrains) {
			Logger.info(" " + carte.carteModele.id + ' ' + carte.getLeft() + ' ' + carte.getTop());
			carte.save();
		}
	}

	public void vide() {
		listeCarte.clear();
	}
}
