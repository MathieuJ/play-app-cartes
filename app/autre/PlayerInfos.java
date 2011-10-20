package autre;

import javax.persistence.Embeddable;

import play.db.jpa.GenericModel;

@Embeddable
public class PlayerInfos extends GenericModel {
	public int pointsVie;

	public int compteursPoison;

}
