package controllers;

import java.util.List;

import models.CarteModele;

public class BiblioPage extends Application {

	public static void index() {
		List<CarteModele> listeCarteModele = CarteModele.all().fetch();
		render(listeCarteModele);
	}

}