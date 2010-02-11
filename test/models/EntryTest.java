package models;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import siena.Model;
import env.AbstractTestCase;

public class EntryTest extends AbstractTestCase {

	@Test
	public void 追加テスト() {
		Blogger author = new Blogger();
		author.name = "test author";
		author.insert();
		System.out.println(author.id);

		Entry entry = new Entry();
		entry.authorId = author.id;
		entry.title = "タイトル / "+ author.name;
		entry.content = new Date() + "本文です";
		entry.date = new Date();
		entry.insert();

		System.out.println(entry.id);
	}

	@Test
	public void 一覧テスト() {
		List<Entry> entries = Model.all(Entry.class)
			.fetch(10);

		for (Entry entry : entries) {
			System.out.println(entry.title);
//			entry.delete();
		}
	}

	@Test
	public void あるUserのエントリ一覧テスト() {
		List<Entry> entries = Model.all(Entry.class)
			.filter("authorId", 1L)
			.fetch(10);

		for (Entry entry : entries) {
			System.out.println(entry.title);
		}
	}

	@Test
	public void ID指定() {
		Entry entry = new Entry();
		entry.id = 4L;
		entry.get();

		System.out.println(entry.title +"/"+ entry.content);
	}

	@Test
	public void 最新のエントリを取得() {
		List<Entry> entries = Model.all(Entry.class)
			.order("-date")
			.fetch(1);

		for (Entry entry : entries) {
			System.out.println(entry.id +":"+ entry.title);
		}
	}

	@Test
	public void コメント投稿() {
		List<Entry> entries = Model.all(Entry.class)
			.fetch(10);

		for (Entry entry : entries) {
			for (int i=0; i<2; i++) {
				entry.addComment("foo"+i, "コメントです"+i);
			}
		}
	}

	@Test
	public void コメント取り出し() {
		List<Entry> entries = Model.all(Entry.class)
			.fetch(10);

		for (Entry entry : entries) {
			System.out.println(entry.title);
			List<Comment> comments = entry.getComments();
			for (Comment comment : comments) {
				System.out.println("\t"+ comment.name +"/"+ comment.comment);
			}
		}
	}
}
