package models;

import javax.persistence.Entity;

import org.apache.commons.lang.StringEscapeUtils;

import play.db.jpa.Model;

@Entity
public class CarteModele extends Model {
	public String nom;

	public String imageUrl;

	public String getNom() {
		return StringEscapeUtils.escapeHtml(nom).replace('\'', ' ');
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getImageUrl() {
		// return imageUrl;
		// return "http://ff7.ruhkey.de/mtg/" + StringEscapeUtils.escapeHtml(nom) +
		// "_ZEN.jpg";
		return imageUrl; // "http://www.starwynngames.co.uk/images/" +
		                 // nom.replace(' ', '_').replaceAll("Æ",
		                 // "Ae").replaceAll(",", "").replaceAll("'",
		                 // "").replaceAll("-", "_") + ".jpg";
	}
}
