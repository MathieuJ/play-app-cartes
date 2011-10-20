package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class User extends Model {

	public String email;
	public String password;
	public String username;
	public boolean isAdmin;

	public User(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;
	}

	public static User findByName(String username) {
		return User.find("byUsername", username).first();
	}

}
