package env;


import java.io.File;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

public class AbstractTestCase {

	@Before
	public void setUp() throws Exception {
		// ApiProxyの設定
		ApiProxy.setEnvironmentForCurrentThread(new Environment());
		ApiProxy.setDelegate(new ApiProxyLocalImpl(new File(".")) {
		});
	}

	@After
	public void tearDown() throws Exception {
		// ApiProxyの設定を破棄
		ApiProxy.setDelegate(null);
		ApiProxy.setEnvironmentForCurrentThread(null);
	}

}
