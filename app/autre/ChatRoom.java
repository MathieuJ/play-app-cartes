package autre;

import java.util.HashMap;
import java.util.List;

import play.Logger;
import play.libs.F.ArchivedEventStream;
import play.libs.F.EventStream;
import play.libs.F.IndexedEvent;
import play.libs.F.Promise;
import play.mvc.Scope.Session;

public class ChatRoom {

	// ~~~~~~~~~ Let's chat!

	final ArchivedEventStream<ChatRoom.Event> chatEvents = new ArchivedEventStream<ChatRoom.Event>(
			100);

	/**
	 * For WebSocket, when a user join the room we return a continuous event
	 * stream of ChatEvent
	 */
	public EventStream<ChatRoom.Event> join(String user) {
		chatEvents.publish(new Join(user));
		return chatEvents.eventStream();
	}

	/**
	 * A user leave the room
	 */
	public void leave(String user) {
		chatEvents.publish(new Leave(user));
	}

	/**
	 * A user say something on the room
	 */
	public void say(String user, String text) {
		if (text == null || text.trim().equals("")) {
			return;
		}
		chatEvents.publish(new Message(user, text));
	}

	/**
	 * Action magic
	 */
	public void action(String user, String action, String param1, String param2) {
		chatEvents.publish(new Action(user, action, param1, param2));
	}

	public void actionGenerale(int numUser, String user, String action) {
		chatEvents.publish(new ActionGenerale(numUser, user, action));
	}

	/**
	 * For long polling, as we are sometimes disconnected, we need to pass the
	 * last event seen id, to be sure to not miss any message
	 */
	public Promise<List<IndexedEvent<ChatRoom.Event>>> nextMessages(
			long lastReceived) {
		return chatEvents.nextEvents(lastReceived);
	}

	/**
	 * For active refresh, we need to retrieve the whole message archive at each
	 * refresh
	 */
	public List<ChatRoom.Event> archive() {
		return chatEvents.archive();
	}

	// ~~~~~~~~~ Chat room events

	public static abstract class Event {

		final public String type;
		final public Long timestamp;

		public Event(String type) {
			this.type = type;
			this.timestamp = System.currentTimeMillis();
		}

	}

	public static class Join extends Event {

		final public String user;

		public Join(String user) {
			super("join");
			this.user = user;
		}

	}

	public static class Leave extends Event {

		final public String user;

		public Leave(String user) {
			super("leave");
			this.user = user;
		}

	}

	public static class Message extends Event {

		final public String user;
		final public String texte;

		public Message(String user, String text) {
			super("message");
			this.user = user;
			this.texte = text;
		}

	}

	public static class Action extends Event {
		final public String texte;
		final public String user;
		final public String param1;
		final public String param2;

		public Action(String user, String action, String param1, String param2) {
			super("action");
			this.user = user;
			this.param1 = param1;
			this.param2 = param2;
			this.texte = action;
		}

	}

	public static class ActionGenerale extends Event {
		final public String texte;
		final public String user;
		final public int numUser;

		public ActionGenerale(int numUser, String user, String action) {
			super("actionGenerale");
			this.user = user;
			this.texte = action;
			this.numUser = numUser;
		}
	}

	// ~~~~~~~~~ Chat room factory

	static HashMap<String, ChatRoom> mapInstance = new HashMap();

	public static ChatRoom get(String partieId) {
		
		Logger.info("PARTIE ID " + partieId);
		if (!mapInstance.containsKey(partieId)) {
			mapInstance.put(partieId, new ChatRoom());
		}
		Logger.info("nb msg : "
				+ mapInstance.get(partieId).chatEvents.archive().size());
		return mapInstance.get(partieId);
	}
}
