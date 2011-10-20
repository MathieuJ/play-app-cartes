package controllers;

import java.util.List;

import models.CarteModele;
import play.Logger;

public class BiblioPage extends Application {

	public static void index() {
		List<CarteModele> listeCarteModele = CarteModele.all().fetch(20);
		for (CarteModele cm : listeCarteModele) {
			Logger.info(cm.nom + " " + cm.imageUrl);
		}
		render(listeCarteModele);
	}

}