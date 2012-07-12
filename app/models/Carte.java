package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Carte extends Model implements Comparable {
	@ManyToOne()
	public CarteModele carteModele;

	public boolean retournee = false;

	public boolean engagee = false;

	public boolean attachee = false;

	public int proprietaire = 0;

	public boolean transformee = false;

	public boolean token = false;

	public String marqueur = "";

	public String getMarqueur() {
		return marqueur;
	}

	public void setMarqueur(String marqueur) {
		this.marqueur = marqueur;
	}

	public boolean isToken() {
		return token;
	}

	public void setToken(boolean token) {
		this.token = token;
	}

	@Column(name = "gauche")
	public int left;

	public int top;

	public Carte(CarteModele cm) {
		this.carteModele = cm;
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public String getNom() {
		return carteModele.getNom();
	}

	public String getImageUrl() {
		if (transformee) {
			return carteModele.getImageUrl().replace("a.jpg", "b.jpg");
		} else {
			return carteModele.getImageUrl();
		}
	}

	public void setProprietaire(int numProprietaire) {
		this.proprietaire = numProprietaire;
	}

	public boolean isRetournee() {
		return retournee;
	}

	public void setRetournee(boolean retournee) {
		this.retournee = retournee;
	}

	public boolean isEngagee() {
		return engagee;
	}

	public void setEngagee(boolean engagee) {
		this.engagee = engagee;
	}

	public boolean isAttachee() {
		return attachee;
	}

	public void setAttachee(boolean attachee) {
		this.attachee = attachee;
	}

	public boolean isTransformee() {
		return transformee;
	}

	public void setTransformee(boolean transformee) {
		this.transformee = transformee;
	}

	public boolean isTerrain() {
		if (carteModele == null) {
			return false;
		}
		return (carteModele.id >= 250 && carteModele.id < 255);
	}

	@Override
	public int compareTo(Object o) {
		Carte c = (Carte) o;
		return (int) (carteModele.id - c.carteModele.id);

	}

}
