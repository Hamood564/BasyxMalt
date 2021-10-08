
package XMPP;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import XMPP.xmppClient.XMPPAddress;
import XMPP.xmppClient.XmppManager;


import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;


/**
 * Implementation of common parts of XMPP event propagation services.
 * Extend this class to make a service XMPP extendable
 *  
 *
 */
public class XMPPEventService {
	private static Logger logger = LoggerFactory.getLogger(XMPPEventService.class);

	// The XMPPClient
	protected XMPPConnection connection;


	// QoS for MQTT messages (1, 2 or 3).
	protected int qos = 1;
	
	/**
	 * Constructor for creating an XMPPCOnnection (without authentication)
	 * @param serverEndpoint
	 * @param clientId
	 * @throws MqttException
	 */
	
	public XMPPEventService(String serverEndpoint, String clientId) throws Exception {
		XMPPAddress xmpp = new XMPPAddress(serverEndpoint);	 
		String username = clientId;
		String password = "";
		String Host = xmpp.getHost();
		int Port = Integer.parseInt(xmpp.getPort());
		XmppManager xmppManager = new XmppManager(Host,Port); 
		xmppManager.init();
		SASLAuthentication.supportSASLMechanism("PLAIN", 0);
		SmackConfiguration.setPacketReplyTimeout(10000);
		xmppManager.performLogin(username, password);
		xmppManager.setStatus(true, "Connected");
		
	}
	
	/**
	 * Constructor for creating an XMPPConnection with authentication
	 * @param serverEndpoint
	 * @param clientId
	 * @param user
	 * @param pw
	 * @throws Exception
	 */
	public XMPPEventService(String serverEndpoint, String clientId, String user, String pw)
			throws Exception {
		XMPPAddress xmpp = new XMPPAddress(serverEndpoint);	 
		String username = clientId;
		String password = pw;
		String Host = xmpp.getHost();
		int Port = Integer.parseInt(xmpp.getPort());
		XmppManager xmppManager = new XmppManager(Host,Port); 
		xmppManager.init();
		SASLAuthentication.supportSASLMechanism("PLAIN", 0);
		SmackConfiguration.setPacketReplyTimeout(10000);
		xmppManager.performLogin(username, password);
		xmppManager.setStatus(true, "Connected");
	}
	
	/**
	 * Constructor for creating an XMPPConnection with existing connection
	 * @param client
	 * @throws MqttException
	 */
	public XMPPEventService(XMPPConnection connection) throws Exception {
		this.connection = connection;
		connection.connect();
	}

	/**
	 * Sets the QoS for XMPP messages
	 * 
	 * @param qos
	 */
	public void setQoS(int qos) {
		if (qos >= 0 && qos <= 3) {
			this.qos = qos;
		} else {
			throw new IllegalArgumentException("Invalid QoS: " + qos);
		}
	}

	/**
	 * Gets the QoS for XMPP messages
	 * 
	 * @param qos
	 */
	public int getQoS() {
		return this.qos;
	}
	
	/**
	 * Sends XMPP message to
	 */
	protected void createXMPPEntry(String AssetJID, String AssetName, String serverEndpoint) {
		
		XMPPAddress xmpp = new XMPPAddress(serverEndpoint);	 
		String Host = xmpp.getHost();
		int Port = Integer.parseInt(xmpp.getPort());
		try {
			XmppManager xmppManager = new XmppManager(Host,Port);
			xmppManager.createEntry(AssetJID, AssetName);
//			xmppManager.sendMessage(XMPPMessage, Assetaddress);
			
		}  catch (Exception e) {
			logger.error("Could not create xmpp entry ", e);
		}
		
	}
	
	/**
	 * Sends XMPP message 
	 */
	
	protected void sendXMPPmessage(String serverEndpoint, String Assetaddress, String XMPPMessage) {
			XMPPAddress xmpp = new XMPPAddress(serverEndpoint);	 
			String Host = xmpp.getHost();
			int Port = Integer.parseInt(xmpp.getPort());
			
			try {
				XmppManager xmppManager = new XmppManager(Host,Port);
				xmppManager.sendMessage(XMPPMessage, Assetaddress);
			}  catch (Exception e) {
				logger.error("Could not create xmpp entry ", e);
			}
  }
	
}
