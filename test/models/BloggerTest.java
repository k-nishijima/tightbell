package models;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import env.AbstractTestCase;

public class BloggerTest extends AbstractTestCase {

	@Test
	public void 作成テスト() {
		Blogger user = new Blogger();
		user.email = "test";
		user.insert();
	}

	@Test
	public void testConnect() {
		assertNull(Blogger.connect("foo"));

		Blogger testUser = Blogger.connect("test");
		assertNotNull(testUser);
	}

}
