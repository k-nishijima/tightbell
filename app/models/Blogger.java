package models;

import java.util.List;

import javax.persistence.Entity;

import siena.Generator;
import siena.Id;
import siena.Model;

public class Blogger extends Model {
	@Id(Generator.AUTO_INCREMENT)
	public Long id;

	public String email;
	public String name;
	public boolean isAdmin;

	public static Blogger connect(String email) {
		List<Blogger> users = Model.all(Blogger.class)
			.filter("email", email)
			.fetch(1);
		return users.size() >0 ? users.get(0) : null;
	}

	public static Blogger findByEmail(String email) {
		List<Blogger> users = Model.all(Blogger.class)
			.filter("email", email)
			.fetch(1);
		return users.size() >0 ? users.get(0) : null;
	}

}
