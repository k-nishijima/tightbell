package controllers;

import java.util.Date;
import java.util.List;

import models.Blogger;
import models.Comment;
import models.Entry;
import play.Play;
import play.data.validation.Required;
import play.modules.gae.GAE;
import play.mvc.Before;
import play.mvc.Controller;
import siena.Model;

import com.google.appengine.api.users.User;

public class Admin extends Controller {
	private static final int ERROR_NOT_BLOGGER = 500;
	private static final int ERROR_REQ_ADD_BLOGGER = 501;

	@Before
	static void checkConnected() {
		User user = GAE.getUser();
		if (user == null) {
			Application.login();
		} else {
			boolean isContinue = false;
			if (user.getEmail().equals(
					Play.configuration.getProperty("blog.admin.email"))) {
				isContinue = true;
				session.put(Application.ROLE_ADMIN, "true");
			}
			Blogger blogger = Blogger.findByEmail(user.getEmail());
			if (blogger != null) {
				isContinue = true;
				session.put(Application.ROLE_BLOGGER, "true");
			}

			if (isContinue) {
				renderArgs.put("user", user);
			} else {
				Application.loginError(ERROR_NOT_BLOGGER);
			}
		}
	}

	public static void index() {
		if (GAE.isLoggedIn()) {
			Admin.home(null);
		}
		render();
	}

	public static void home(Long id) {
		List<Entry> entries = Model.all(Entry.class).order("-date").fetch(20);

		Entry edit = new Entry();
		if (id != null && id > 0) {
			edit.id = id;
			edit.get();
		}

		render(entries, edit);
	}

	public static void entries() {
		List<Entry> entries = Model.all(Entry.class)
			.order("-date")
			.fetch(20);

		render(entries);
	}

	public static void editEntry(Long id, @Required String title, @Required String content) {
		User author = (User) renderArgs.get("user");
		Blogger blogger = Blogger.findByEmail(author.getEmail());
		if (blogger == null) {
			Application.loginError(ERROR_REQ_ADD_BLOGGER);
		}

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			home(null);
		}

		Entry entry = new Entry();
		entry.title = title;
		entry.content = content;
		entry.date = new Date();
		entry.authorId = blogger.id;

		if (id != null && id > 0) {
			entry.id = id;
			entry.update();
		} else {
			entry.insert();
		}

		entries();
	}

	public static void deleteEntry(Long id) {
		Entry entry = new Entry();
		entry.id = id;
		entry.get();

		List<Comment> comments = entry.getComments();
		for (Comment comment : comments) {
			comment.delete();
		}

		entry.delete();
		entries();
	}

	public static void bloggers(Long id) {
		List<Blogger> bloggers = Model.all(Blogger.class)
			.order("email")
			.fetch();

		Blogger edit = new Blogger();
		if (id != null && id > 0) {
			edit.id = id;
			edit.get();
		}
		render(bloggers, edit);
	}

	public static void editBlogger(Long id, @Required String email, @Required String name) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			bloggers(null);
		}

		User admin = (User) renderArgs.get("user");
		if (admin.getEmail().equals(
				Play.configuration.getProperty("blog.admin.email"))) {
			Blogger blogger = new Blogger();
			blogger.name = name;
			blogger.email = email;
			if (id != null && id > 0) {
				blogger.id = id;
				blogger.update();
			} else {
				blogger.insert();
			}
		}

		bloggers(null);
	}

	public static void deleteBlogger(Long id) {
		User admin = (User) renderArgs.get("user");
		if (admin.getEmail().equals(
				Play.configuration.getProperty("blog.admin.email"))) {
			Blogger edit = new Blogger();
			edit.id = id;
			edit.get();
			edit.delete();
		}
		bloggers(null);
	}

	public static void updateComment(Long id) {
		Comment comment = new Comment();
		comment.id = id;
		comment.get();

		comment.visible = !comment.visible;
		comment.update();

		home(comment.entryId);
	}

	public static void deleteComment(Long id) {
		Comment comment = new Comment();
		comment.id = id;
		comment.get();
		Long entryId = comment.entryId;
		comment.delete();

		home(entryId);
	}
}
