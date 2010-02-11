package models;

import java.util.Date;
import java.util.List;

import siena.Generator;
import siena.Id;
import siena.Model;

public class Entry extends Model {
	@Id(Generator.AUTO_INCREMENT)
	public Long id;

	public String title;

	public String content;

	public Date date;

	public Long authorId;

	public List<String> tags;

	public Blogger getAuthor() {
		if (this.authorId == null) return null;

		return Model.all(Blogger.class)
			.filter("id", this.authorId)
			.get();
	}

	public List<Comment> getComments() {
		return Model.all(Comment.class)
			.filter("entryId", this.id)
			.filter("visible", Boolean.TRUE)
			.order("-date")
			.fetch();
	}

	public List<Comment> getAdminComments() {
		return Model.all(Comment.class)
			.filter("entryId", this.id)
			.order("-date")
			.fetch();
	}

	public void addComment(String name, String body) {
		Comment comment = new Comment();
		comment.entryId = this.id;
		comment.name = name;
		comment.comment = body;
		comment.date = new Date();
		comment.visible = Boolean.FALSE;
		comment.insert();
	}

	public static Entry findById(Long id) {
		return Model.all(Entry.class)
			.filter("id", id)
			.get();
	}
}
