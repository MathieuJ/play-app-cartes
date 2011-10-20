package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class CarteDeck extends Model {
	public CarteModele carte;

	public Integer nombre;

	@ManyToOne
	public DeckModele deck;
}
