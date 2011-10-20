package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class DeckModele extends Model {

	@OneToMany(mappedBy = "deck")
	public List<CarteDeck> listeCarteDeck;
}
