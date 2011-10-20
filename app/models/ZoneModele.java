package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class ZoneModele extends Model {
	public String nom;

	public String image;

	public Integer gauche, top, width, height;

	public Boolean isImage;

	public Boolean isPublique;

	public ZoneModele(String nom) {
		this.nom = nom;
	}

	public ZoneModele() {

	}

}
