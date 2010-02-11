package env;


import java.util.HashMap;
import java.util.Map;

import com.google.apphosting.api.ApiProxy;

public class Environment implements ApiProxy.Environment {

	@Override
	public String getAppId() {
		return "junittest";
	}

	@Override
	public Map<String, Object> getAttributes() {
		return new HashMap<String, Object>();
	}

	@Override
	public String getAuthDomain() {
		return null;
	}

	@Override
	public String getEmail() {
		return null;
	}

	@Override
	public String getRequestNamespace() {
		return null;
	}

	@Override
	public String getVersionId() {
		return "1.0";
	}

	@Override
	public boolean isAdmin() {
		return false;
	}

	@Override
	public boolean isLoggedIn() {
		return false;
	}

}
