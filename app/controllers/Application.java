package controllers;

import java.util.Date;
import java.util.List;

import models.Partie;
import models.PartieModele;
import models.User;
import models.Zone;
import play.Logger;
import play.libs.F.IndexedEvent;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Scope.Session;
import autre.ChatRoom;
import autre.ChatRoom.Message;
import autre.PartieService;

import com.google.gson.reflect.TypeToken;

public class Application extends Controller {
	@Before
	static void addUser() {
		User user = connected();
		if (user != null) {
			renderArgs.put("user", user);
		}
		renderArgs.put("userList", User.findAll());
	}

	static User connected() {
		if (renderArgs.get("user") != null) {
			return renderArgs.get("user", User.class);
		}
		String username = session.get("user");
		if (username != null) {
			return User.findByName(username);
		}
		return null;
	}

	public static void index() {
		List<PartieModele> listePartieModele = PartieModele.all().fetch();
		List<Partie> listePartie = Partie.all().fetch();
		render(listePartie, listePartieModele);
	}

	public static void createUser(String username) {
		Logger.info("Creation de l'utilisateur : " + username);
		if (username.length() < 4) {
			flash.error("Le nom doit faire au moins 4 caractères");
			index();
		}
		User user = User.findByName(username);

		if (user != null) {
			flash.error("Un compte existe deja avec ce nom");
			index();
		}

		user = new User("toto@toto.com", "", username);
		user.create();
		session.put("user", user.username);

		index();
	}

	public static void logout() {
		session.clear();
		index();
	}

	public static void creerPartie(Long partieModeleId, String nom) {
		PartieModele partieModele = null;
		Partie partie = new Partie(connected(), partieModele, new Date(), nom);
		partie.joueur1 = connected();
		partie.create();
		partie.initZones();
		partie.refresh();
		
		Session.current().put("partieId", partie.id);
		render("Application/selectionDeck.html", partie);
	}

	public static void saveDeck(Long partieId, String texte) {
		Partie partie = Partie.findById(partieId);
		Zone zoneDeck = partie.getZone("Deck", connected());
		String erreurs = PartieService.setDeck(zoneDeck, texte, partie.getNumero(true));
		if (erreurs != null && !erreurs.isEmpty()) {
			flash.error("Erreurs : \n" + erreurs);
		}
		Zone zoneBibliotheque = partie.getZone("Bibliotheque", connected());
		PartieService.chargeBibliotheque(zoneDeck, zoneBibliotheque);
		zoneBibliotheque.save();
		PartieService.pioche(partie, connected(), 7);
		partie.getZone("Main", connected()).save();
		partie.getZone("Bibliotheque", connected()).save();
		index();
	}

	public static void selectionDeck(Long partieId) {
		Logger.info("partie Id " + partieId);
		Session.current().put("partieId", partieId);
		Partie partie = Partie.findById(new Long(Session.current().get("partieId")));
		render("Application/selectionDeck.html", partie);
	}

	public static void rejoindrePartie(Long partieId) {
		Partie partie = Partie.findById(partieId);
		if (partie.joueur2 != null) {
			flash.error("Il y a déjà 2 joueurs");
		} else if (partie.joueur1 == connected()) {
			flash.error("Vous êtes déjà inscrit à cette partie");
		} else {
			partie.joueur2 = connected();
			partie.save();
			render("Application/selectionDeck.html", partie);
		}
	}

	public static void supprimerPartie(Long partieId) {
		Partie partie = Partie.findById(partieId);
		for (Zone zone : partie.listeZone) {
			zone.delete();
		}
		partie.refresh();
		partie.delete();
		index();
	}

	public static void login(String username) {
		User user = User.findByName(username);
		if (user != null) {
			System.out.println("logged :" + user.username);
			session.put("user", user.username);
			flash.success("Welcome, " + user.username);
		} else {
			System.out.println("pas logged");
			flash.put("username", username);
			flash.error("Login failed");
		}
		index();
	}

	public boolean hasChargeDeck(Partie partie) {
		return (connected() != null && !partie.getZone("Deck", connected()).estVide());
	}
	
	public static void say(String message) {
		if (message.trim().length() == 0) return;
		Logger.info(connected().username + " dit " + message);
		ChatRoom.get("main").publish(new Message(connected().username, message));
	}
	
	public static void waitMessages(Long lastReceived) {
		// Here we use continuation to suspend
		// the execution until a new message has been received
		// List messages = await(ChatRoom.get(new
		// Long(Session.current().get("partieId"))).nextMessages(lastReceived));
		List messages = await(ChatRoom.get("main").nextMessages(lastReceived));
		Logger.info("renderJson " + messages.size());
		renderJSON(messages, new TypeToken<List<IndexedEvent<ChatRoom.Event>>>() {
		}.getType());
	}
}