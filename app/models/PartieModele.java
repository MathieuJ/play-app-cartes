package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class PartieModele extends Model {
	public String nom;

	@OneToMany
	List<ZoneModele> listeZones = new ArrayList();

	String imageBaseUrl;
}
