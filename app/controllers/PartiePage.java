package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Carte;
import models.Partie;
import models.User;
import models.Zone;
import play.Logger;
import play.libs.F.IndexedEvent;
import play.mvc.Before;
import play.mvc.Scope.Session;
import autre.ChatRoom;
import autre.ChatRoom.ActionGenerale;
import autre.ChatRoom.Event;
import autre.ChatRoom.Message;
import autre.PartieService;

import com.google.gson.reflect.TypeToken;

public class PartiePage extends Application {
	@Before
	static void addUser() {
		Logger.info("get Partie : renderargs " + renderArgs.get("partieId") + ", params : " + params.get("partieId"));
		User user = connected();
		if (user != null) {
			renderArgs.put("user", user);
		}
		renderArgs.put("userList", User.findAll());
	}

	public static void index(Long partieId) {
		Partie partie = Partie.findById(partieId);
		Session.current().put("partieId", partieId);
		render(partie);
	}

	/**
	 * 
	 * @param zoneNom
	 *          Nom de la zone à afficher : Bibliotheque, Cimetiere, Exil, ..
	 * @param soi
	 *          vrai si zone à soi, faux si adverse
	 * @param param1
	 *          tout, top ou bottom. Ne concerne que la bibli pour top et bottom
	 *          normalement..
	 * @param param2
	 *          nombre à montrer si top ou bottom
	 */
	public static void loadZone(String zoneNom, String soi, String param1, String param2) {
		Partie partie = Partie.findById(new Long(Session.current().get("partieId")));
		Zone zone = partie.getZone(zoneNom, new Boolean(soi));
		Logger.info("LoadZone " + param1 + " " + new Boolean(soi) + " " + zoneNom + " " + zone.getNom() + " " + soi + " " + param1 + " " + param2);
		
		List<Carte> listeCarte = new ArrayList();
		if ("top".equals(param1)) {
			for (int i = 0; i < new Integer(param2); i++) {
				listeCarte.add(zone.getListeCarte().get(i));
			}
		} else if ("bottom".equals(param1)) {
			for (int i = 0; i < new Integer(param2); i++) {
				listeCarte.add(zone.getListeCarte().get(i));
			}
		} else {
			listeCarte = zone.getListeCarte();
		}
		Logger.info("____________________" + listeCarte.size());
		String nom = zone.getNom();
		render("PartiePage/zone.html", listeCarte, nom);
	}

	public static void updatePartie() {
		Logger.info("update partie");
		Partie partie = Partie.findById(new Long(Session.current().get("partieId")));
		render("PartiePage/partie.html", partie);
	}

	public static void say(String message) {
		if (message.trim().length() == 0) {
			return;
		}
		Logger.info(connected().username + " dit " + message);
		ChatRoom.get(Session.current().get("partieId")).publish(new Message(connected().username, message));
	}

	/**
	 * user: gars qui fait l'action, puis param 1 , 2, 3 , 4
	 * 
	 * @pioche boolean_soi Nombre
	 * @meule numJoueur Nombre
	 * @sacrifie carteId
	 * @detruit carteId
	 * @exile carteId
	 * @joue carteId
	 * @attache carteId carteIdCible
	 * @engage carteId
	 * @degage carteId
	 * @creeToken // A FAIRE
	 * @modifiePointsVie user nombre
	 * @modifiePoison user nombre
	 * @melange Zone_X
	 * @voir NON ____ zone boolean_soi top/bottom/tout nombre (si top ou bottom)
	 */

	public static void actionGenerale(String nomAction, String param1, String param2, String param3, String param4) {

		Logger.info("_____________________ Action Generale : id de la partie " + new Long(Session.current().get("partieId")) + " _User:" + connected() + " _action:" + nomAction + " _p1:" + param1
		    + " _p2:" + param2 + " " + param3 + " " + param4);
		Partie partie = Partie.findById(new Long(Session.current().get("partieId")));
		Event event = PartieService.actionGenerale(partie, connected(), nomAction, param1, param2, param3, param4);

		Logger.info("_____________________ action generale " + connected().username);
		ChatRoom.get(Session.current().get("partieId")).publish(event);
	}

	public static void waitMessages(Long lastReceived) {
		// Here we use continuation to suspend
		// the execution until a new message has been received
		// List messages = await(ChatRoom.get(new
		// Long(Session.current().get("partieId"))).nextMessages(lastReceived));
		List messages = await(ChatRoom.get(Session.current().get("partieId")).nextMessages(lastReceived));
		Logger.info("renderJson " + messages.size());
		renderJSON(messages, new TypeToken<List<IndexedEvent<ChatRoom.Event>>>() {
		}.getType());
	}

	public static void leave(String user) {
		// ChatRoom.get(new Long(Session.current().get("partieId"))).leave(user);
		ChatRoom.get(Session.current().get("partieId")).leave(user);
		Application.index();
	}

}