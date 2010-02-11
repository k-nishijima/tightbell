package controllers;

import java.util.List;

import models.Entry;
import play.Play;
import play.data.validation.Required;
import play.i18n.Messages;
import play.modules.gae.GAE;
import play.mvc.Before;
import play.mvc.Controller;
import siena.Model;

public class Application extends Controller {
	public static final String ROLE_ADMIN = "isAdmin";
	public static final String ROLE_BLOGGER = "isBlogger";

	@Before
	static void addDefaults() {
		renderArgs.put("blogTitle", Play.configuration
				.getProperty("blog.title"));
		renderArgs.put("blogBaseline", Play.configuration
				.getProperty("blog.baseline"));
		renderArgs.put("isBlogger", session.get(ROLE_BLOGGER));
	}

	public static void login() {
		GAE.login("Application.index");
	}

	public static void loginError(int errorCode) {
		String message = Messages.get("error."+ errorCode);
		render(message);
	}

	public static void logout() {
		GAE.logout("Application.index");
	}

	public static void index() {
		List<Entry> entries = Model.all(Entry.class)
			.order("-date")
			.fetch(1);
		Entry frontPost = entries.size() > 0 ? entries.get(0) : null;

		List<Entry> olderPosts = Model.all(Entry.class)
			.order("-date")
			.fetch(10, 1);

		render(frontPost, olderPosts);
	}

	public static void show(Long id) {
		Entry entry = Entry.findById(id);
		render(entry);
	}

	public static void postComment(Long id, @Required String name, @Required String comment) {
		Entry entry = Entry.findById(id);
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			render("Application/show.html", entry);

		} else {
			entry.addComment(name, comment);
			flash.success("Thanks for posting %s", name);
			show(id);
		}
	}

}