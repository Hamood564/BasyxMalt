package XMPP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.basyx.components.configuration.BaSyxConfiguration;


/**
 * Represents a BaSyx XMPP configuration for an XMPP connection.
 * 
 *
 */
public class BaSyxXMPPConfiguration extends BaSyxConfiguration {
	// Prefix for environment variables
	public static final String ENV_PREFIX = "BaSyxXMPP_";

	// Default BaSyx XMPP configuration
	public static final String DEFAULT_USER = "testuser1@desktop-q5e1fr3.tqc.local";
	public static final String DEFAULT_PASS = "123564";
	public static final String DEFAULT_SERVER = "xmpp://desktop-q5e1fr3.tqc.local:5222";
	public static final String DEFAULT_QOS = "1";

	public static final String USER = "user";
	public static final String PASS = "pass";
	public static final String SERVER = "server";
	public static final String QOS = "qos";
	public static final String WHITELIST_PREFIX = "whitelist.";
	public static final String WHITELIST_ELEMENT_PREFIX = "whitelist.element.";

	// The default path for the context properties file
	public static final String DEFAULT_CONFIG_PATH = "xmpp.properties";

	// The default key for variables pointing to the configuration file
	public static final String DEFAULT_FILE_KEY = "BASYX_XMPP";

	public static Map<String, String> getDefaultProperties() {
		Map<String, String> defaultProps = new HashMap<>();
		defaultProps.put(USER, DEFAULT_USER);
		defaultProps.put(PASS, DEFAULT_PASS);
		defaultProps.put(SERVER, DEFAULT_SERVER);
		defaultProps.put(QOS, DEFAULT_QOS);

		return defaultProps;
	}

	/**
	 * Constructor with predefined value map
	 */
	public BaSyxXMPPConfiguration(Map<String, String> values) {
		super(values);
	}

	/**
	 * Empty Constructor - use default values
	 */
	public BaSyxXMPPConfiguration() {
		super(getDefaultProperties());
	}

	/**
	 * Constructor with initial configuration
	 * 
	 * @param user   Username for XMPP connection
	 * @param pass   Password for XMPP connection
	 * @param server XMPP broker address
	 * @param qos    XMPP quality of service level
	 */
	public BaSyxXMPPConfiguration(String user, String pass, String server, int qos) {
		this();
		setUser(user);
		setPass(pass);
		setServer(server);
		setQoS(qos);
	}

	/**
	 * Load all settings except of the whitelist config part
	 */
	public void loadFromEnvironmentVariables() {
		String[] properties = { USER, PASS, SERVER, QOS };
		loadFromEnvironmentVariables(ENV_PREFIX, properties);
	}

	public void loadFromDefaultSource() {
		loadFileOrDefaultResource(DEFAULT_FILE_KEY, DEFAULT_CONFIG_PATH);
		loadFromEnvironmentVariables();
	}

	public String getUser() {
		return getProperty(USER);
	}

	public void setUser(String user) {
		setProperty(USER, user);
	}

	public String getPass() {
		return getProperty(PASS);
	}

	public void setPass(String pass) {
		setProperty(PASS, pass);
	}

	public String getServer() {
		return getProperty(SERVER);
	}

	public void setServer(String server) {
		setProperty(SERVER, server);
	}

	public int getQoS() {
		return Integer.parseInt(getProperty(QOS));
	}

	public void setQoS(int qos) {
		setProperty(QOS, Integer.toString(qos));
	}

	public boolean isWhitelistEnabled(String submodelId) {
		return "true".equals(getProperty(WHITELIST_PREFIX + submodelId));
	}

	public void setWhitelistEnabled(String submodelId, boolean enabled) {
		String propertyName = WHITELIST_PREFIX + submodelId;
		if (enabled) {
			setProperty(propertyName, "true");
		} else {
			setProperty(propertyName, "false");
		}
	}

	public Set<String> getWhitelist(String submodelId) {
		Set<String> whitelist = new HashSet<>();
		String fullPrefix = WHITELIST_ELEMENT_PREFIX + submodelId;
		List<String> properties = getProperties(fullPrefix);
		
		for ( String prop : properties ) {
			if ( getProperty(prop).equals("true") ) {
				// Removes submodel prefix (+ one separator) => whitelist.elements.smid.
				String elementId = prop.substring(fullPrefix.length() + 1);
				whitelist.add(elementId);
			}
		}
		return whitelist;
	}

	public void setWhitelist(String submodelId, List<String> elementIds) {
		String smPrefix = WHITELIST_ELEMENT_PREFIX + submodelId;
		for (String elemId : elementIds) {
			String propName = smPrefix + "." + elemId;
			setProperty(propName, "true");
		}
	}
}
