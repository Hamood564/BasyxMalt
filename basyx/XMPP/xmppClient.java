package XMPP;

// Domain Name : DESKTOP-Q5E1FR3.tqc.local
//Server Host Name (FQDN):DESKTOP-Q5E1FR3.tqc.local
//Admin Console Port:	 9090
//Secure Admin Console Port:	9091
//admin email: admin@tqc.com pass: 123564


import java.util.*;
import java.util.logging.*;

import org.eclipse.basyx.extensions.submodel.mqtt.MqttSubmodelAPI;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.restapi.api.ISubmodelAPI;
import org.eclipse.basyx.submodel.restapi.api.ISubmodelAPIFactory;
import org.eclipse.basyx.vab.modelprovider.api.IModelProvider;
import org.eclipse.basyx.vab.modelprovider.lambda.VABLambdaProvider;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.pubsub.PresenceState;


//Constructing a SubModel API that emits XMPP events and messages


public class xmppClient implements ISubmodelAPIFactory{
	
	//Creating Basic Time and Data structure for msgs
	public static class BasicMsgDT{
		Calendar cal= Calendar.getInstance();
		short year;
		short month;
		short day;
		short hr;
		short min;
		short sec;
		short millisec;
		char typeDesignator = 'T';
		
		public BasicMsgDT(Date date) {
			cal.setTime(date);
		}
		
		public BasicMsgDT(String str){
			fromString(str);
			
		}

		private void fromString(String str) {
			
			if(str != null) {
				cal.set(Calendar.YEAR,Integer.parseInt(str.substring(0,4)));
				cal.set(Calendar.MONTH,Integer.parseInt(str.substring(0,4))-1);
				cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(str.substring(6,8)));
				typeDesignator=str.charAt(8);
				cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(str.substring(9,11)));
				cal.set(Calendar.MINUTE,Integer.parseInt(str.substring(11,13)));
				cal.set(Calendar.SECOND,Integer.parseInt(str.substring(13,15)));
				cal.set(Calendar.MILLISECOND,Integer.parseInt(str.substring(15,18)));
			}
			
		}
		
		
		//setting values
		
		public short getYear() {
			return (short) cal.get(Calendar.YEAR);			
		}
		
		public void setYear (short value) {
			cal.set(Calendar.YEAR, value);			
		}
		
		
		public short getMonth() {
			return (short) cal.get(Calendar.MONTH);			
		}
		
		public void setMonth (short value) {
			cal.set(Calendar.MONTH, value-1);			
		}
				
		public short getDay() {
			return (short) cal.get(Calendar.DAY_OF_MONTH);			
		}
		
		public void setDay (short value) {
			cal.set(Calendar.DAY_OF_MONTH, value);			
		}
		
		public short getHr() {
			return (short) cal.get(Calendar.HOUR_OF_DAY);			
		}
		
		public void setHr (short value) {
			cal.set(Calendar.HOUR_OF_DAY, value);			
		}
		
		public short getMin() {
			return (short) cal.get(Calendar.MINUTE);			
		}
		
		public void setMin (short value) {
			cal.set(Calendar.MINUTE, value);			
		}
		
		public short getSec() {
			return (short) cal.get(Calendar.SECOND);			
		}
		
		public void setSec (short value) {
			cal.set(Calendar.SECOND, value);			
		}
		
		public short getMilliSec() {
			return (short) cal.get(Calendar.MILLISECOND);			
		}
		
		public void setMilliSec (short value) {
			cal.set(Calendar.MILLISECOND, value);			
		}
		
		public char getTypeDesignator() {
		    return typeDesignator;
		  }
		 
		public void setTypeDesignator(char value) {
		    this.typeDesignator = value;
		  }
		  
		
		//converting to string
		public String toString() {
		 String strDate = cal.get(Calendar.YEAR)+
				 paddedInt(2,cal.get(Calendar.MONTH)+1)+
				 paddedInt(2,cal.get(Calendar.DAY_OF_MONTH))+"T";
		 if( cal.get(Calendar.AM_PM) == Calendar.PM ) { 
			    strDate=strDate+paddedInt(2,12+cal.get(Calendar.HOUR));
		    }
		 else {
			    strDate=strDate+paddedInt(2,cal.get(Calendar.HOUR));
		    }
		 strDate=strDate+paddedInt(2,cal.get(Calendar.MINUTE))+
				    paddedInt(2,cal.get(Calendar.SECOND))+
				    paddedInt(3,cal.get(Calendar.MILLISECOND));
			    
		 return strDate;
		  }

		private String paddedInt(int size, int value) {
			String res = String.valueOf(value);
			while(res.length() < size) {
				res = 0 + res;
			}
			return res.toString();
		}
		
		public Date getTime() {
			return cal.getTime();
			
		}
		
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////	
//    // For testing.....
//    public static void main(String[] arg) {
//    System.out.println("Initial date: 20030812Z171910154");
//    BasicMsgDT bfdt = new BasicMsgDT("20030812Z171910154");
//    System.out.println(bfdt.toString());
//    System.out.println("Year:  "+bfdt.getYear());
//    System.out.println("Month: "+bfdt.getMonth());
//    System.out.println("Day:   "+bfdt.getDay());
//    System.out.println("Hour:  "+bfdt.getHr());
//    System.out.println("Min:   "+bfdt.getMin());
//    System.out.println("Sec:   "+bfdt.getSec());
//    System.out.println("Milli: "+bfdt.getMilliSec());
//    System.out.println(new BasicMsgDT(new BasicMsgDT("20030812Z171910154").getTime()));
//    System.out.println();
//    System.out.println("Current date: "+new BasicMsgDT(new Date()).toString());
//    }
////////////////////////////////////////////////////////////////////////////////////////////////////////  
	
	
	//XMPP Address class
	
	public static class XMPPAddress{
		private String xmppAddress;
		private String xmppPort =null;
		public XMPPAddress(String addr) {
			if(addr.regionMatches(0, "xmpp://", 0, 7)== false) {
				System.out.println("XMPP Address Request found. Looking Up......");				
			}
			addr = addr.substring(7);
			addr =addr.trim();
			xmppAddress = addr.substring(0,addr.indexOf(':'));
			addr = addr.substring(addr.indexOf(':'));
			addr = addr.trim();
			xmppPort = addr.substring(1, addr.length());
			
		}
		
		//get protocol
		public String getProtocol() {
			return "xmpp";
		}
		
		//get host
		public String getHost() {
			return xmppAddress;
		}
		
		//get port
		public String getPort() {
			return xmppPort;
		}
		
		//get ID
		public String getID() {
			return xmppAddress;
		}
		
		public String xmpptoString() {
			return new String("xmpp://" + xmppAddress + ":" + xmppPort);
		}
		
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////	
//	//For Testing
//	public static void main(String[] arg) {
//		System.out.println("xmpp://creeep.im");
//		XMPPAddress xmpp = new XMPPAddress("xmpp://creep.im");
//		System.out.println(xmpp.toString());
//		System.out.println("Protocol :" + xmpp.getProtocol());
//		System.out.println("Host :" + xmpp.getHost());
//		System.out.println("Port : "+ xmpp.getPort());
//		System.out.println("ID: " + xmpp.getID());
//		System.out.println("The full address is : " + xmpp.xmpptoString());
//			}  
//////////////////////////////////////////////////////////////////////////////////////////////////	
	
///*****************************************************************************************	
	//XMPP Message Transport Protocol connection initiate-under development (TBD)<--HERE
	
//	public static class XMPPmtp{
//		
//		private String prefix = "basyx_mtp_xmpp_";
//		private String[] protocols = {"xmpp"};
//		private String Msg_NameExt= "basyx.msgs.mtp.xmpp";
//		public XMPPConnection xmpp_connect;
//		
//		static Logger myLogger = Logger.getLogger(XMPPmtp.class.getName());
//		
//		
//		
//		//XMPP Login to server, with userid and pass
//		private void xmppLogin(String Host, String user, String pass) throws Exception{
//			  try {
////				  
//				  XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
//						  .setUsernameAndPassword(user,pass)
//						  .setXmppDomain(Host)
//						  .setHost(Host)
//						  .build();
//				  AbstractXMPPConnection xmpp_connection = new XMPPTCPConnection(config);
//				  xmpp_connection.connect();
//				  xmpp_connection.login();
//		  
////				  xmpp_connect = new XMPPConnection(Host);
////				  xmpp_connect.connect();
////				  xmpp_connect.login(user, pass);
////				  Presence p = new Presence(Presence.Type.available);
////				  xmpp_connect.sendPacket(p);
////				  System.out.println("Reached here2");
//				  
////				  if(myLogger.isLoggable(null))
////					  myLogger.log(null,"login: creating XMPP Connection to server" + Host + ", with user:" + user);
////					  xmpp_connect = new XMPPConnection(Host);
////				  
////				  if(myLogger.isLoggable(null))
////					  myLogger.log(null,"login: XMPP Connection is Connected" + xmpp_connect.isConnected());
////				  xmpp_connect.login(user, pass, "acc");
////				  if(myLogger.isLoggable(null))
////					  myLogger.log(null,"login: XMPP Connection is Authenticated" + xmpp_connect.isAuthenticated());
////				  Presence p = new Presence(Presence.Type.available);
////				  xmpp_connect.sendPacket(p);
//			  }catch(XMPPException e) {
//				  throw new Exception("Cannot Login to XMPP Server", e);				  
//			  }  
//		  }
//		
//		//Disconnect from XMPP Server
//		private void logout() {
//			xmpp_connect.disconnect();
//		}
//		
//		
//		
//	}
	/////////////////////////////////////////////////<--END HERE
	
/////////////////////////////////////////////////////////////////////////////////////////
	
//	//For Testing
//	public static void main(String[] arg) {
//		System.out.println("xmpp://jix.im");
//		XMPPAddress xmpp = new XMPPAddress("xmpp://jix.im");
//		XMPPmtp xmtp = new XMPPmtp();
//		String Host = xmpp.getHost();
//		String user = "xerox564123";
//		String pass = "123h_564";
//		System.out.println("Reached here1");
//		  
//		try {
//		
//			xmtp.xmppLogin(Host, user, pass);
//			xmtp.logout();
//			
//		
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}}
///////////////////////////////////////////////////////////////////////////////////////////////	
///////////***********************************************************************////////////	
	
	
	
	
	
	
	//Creating XMPPManager Class for methods of XMPP communication
	
	
	public static class XmppManager {
		 
		private static final int packetReplyTimeout = 500; // millis
		 
		private String server;
		private int port;
		 
		private ConnectionConfiguration config;
		private XMPPConnection connection;
		 
		private ChatManager chatManager;
		private MessageListener messageListener;
		 
		public XmppManager(String server, int port) {
		this.server = server;
		this.port = port;
		}
		 
		public void init() throws XMPPException {
		 
		System.out.println(String.format("Initializing connection to server %1$s port %2$d", server, port));
		 
		SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);
		 
		config = new ConnectionConfiguration(server, port);
		config.setSASLAuthenticationEnabled(true);
		config.setSecurityMode(SecurityMode.disabled);
		 
		connection = new XMPPConnection(config);
		connection.connect();
		 
		System.out.println("Connected: " + connection.isConnected());
		 
		chatManager = connection.getChatManager();
		messageListener = new MyMessageListener();
		 
		}
		 
		public void performLogin(String username, String password) throws XMPPException {
		if (connection!=null && connection.isConnected()) {
		connection.login(username, password);
		}
		}
		 
		public void setStatus(boolean available, String status) {
		 
		Presence.Type type = available? Type.available: Type.unavailable;
		Presence presence = new Presence(type);
		 
		presence.setStatus(status);
		connection.sendPacket(presence);
		 
		}
		 
		public void destroy() {
		if (connection!=null && connection.isConnected()) {
		connection.disconnect();
		}
		}
		 
		public void sendMessage(String message, String AssetJID) throws XMPPException {
		System.out.println(String.format("Sending mesage '%1$s' to user %2$s", message, AssetJID));
		Chat chat = chatManager.createChat(AssetJID, messageListener);
		chat.sendMessage(message);
		}
		//check roster...............
		public void printRoster() throws Exception {
			 Roster roster = connection.getRoster();
			 Collection<org.jivesoftware.smack.RosterEntry> entries = roster.getEntries();  
			 for (org.jivesoftware.smack.RosterEntry entry : entries) {
			  System.out.println(String.format("Buddy:%1$s - Status:%2$s", 
			      entry.getName(), entry.getStatus()));}
			 }
		 
		public void createEntry(String user, String name) throws Exception {
		System.out.println(String.format("Creating entry for buddy '%1$s' with name %2$s", user, name));
		Roster roster = connection.getRoster();
		roster.createEntry(user, name, null);
		}
		 
		class MyMessageListener implements MessageListener {
		 
		@Override
		public void processMessage(Chat chat, Message message) {
		String from = message.getFrom();
		String body = message.getBody();
		System.out.println(String.format("Received message '%1$s' from %2$s", body, from));
		}
		 
		}
		 
		
		}	
	
	
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
//	///For testing.....................
//
//	 
//	public static void main(String[] args) throws Exception {
//		
//		System.out.println("xmpp://desktop-q5e1fr3.tqc.local");
//		XMPPAddress xmpp = new XMPPAddress("xmpp://desktop-q5e1fr3.tqc.local:5222");	 
//		String username = "testuser1@desktop-q5e1fr3.tqc.local";
//		String password = "123564";
//		String Host = xmpp.getHost();
//		int Port = Integer.parseInt(xmpp.getPort());
//		XmppManager xmppManager = new XmppManager(Host,Port);
//		 
//		xmppManager.init();
//		SASLAuthentication.supportSASLMechanism("PLAIN", 0);
//		SmackConfiguration.setPacketReplyTimeout(10000);
//		xmppManager.performLogin(username, password);
//		
//		xmppManager.setStatus(true, "Hello everyone");
//		 
//		String AssetJID = "testuser2";
//		String AssetName = "testuser2";
//		xmppManager.createEntry(AssetJID, AssetName);
//		 
//		xmppManager.sendMessage("Hello mate", "testuser2@desktop-q5e1fr3.tqc.local");
//		 
//		boolean isRunning = true;
//		xmppManager.printRoster();
//		while (isRunning) {
//		Thread.sleep(50);
//		}
//		
//		xmppManager.printRoster();
//		xmppManager.destroy();
//}
//	
//	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	

	@Override
	public ISubmodelAPI getSubmodelAPI(Submodel submodel) {
		// Get the submodel's id from the given provider
		String submodelId = submodel.getIdentification().getId();
		// Create the API
		IModelProvider provider = new VABLambdaProvider(submodel);
		
		// Configure the API according to the given configs
		
		XMPPAddress xmpp = new XMPPAddress("xmpp://desktop-q5e1fr3.tqc.local:5222");	 
		String username = "testuser1@desktop-q5e1fr3.tqc.local";
		String password = "123564";
		String Host = xmpp.getHost();
		int Port = Integer.parseInt(xmpp.getPort());
		XmppManager xmppManager = new XmppManager(Host,Port);
 
		xmppManager.init();
		SASLAuthentication.supportSASLMechanism("PLAIN", 0);
		SmackConfiguration.setPacketReplyTimeout(10000);
		xmppManager.performLogin(username, password);		
		xmppManager.setStatus(true, "Hello everyone");
		String AssetJID = "testuser2";
		String AssetName = "testuser2";
		xmppManager.createEntry(AssetJID, AssetName);		 
		xmppManager.sendMessage("Hello mate", "testuser2@desktop-q5e1fr3.tqc.local");
		 
		boolean isRunning = true;
		xmppManager.printRoster();
		while (isRunning) {
		Thread.sleep(50);
		}
		
		xmppManager.printRoster();
		xmppManager.destroy();
}
		
		
		
//		
//		String brokerEndpoint = config.getServer();
//		String clientId = submodelId;
//		MqttSubmodelAPI api;
//		try {
//			if (config.getUser() != null) {
//				String user = config.getUser();
//				String pass = config.getPass();
//				api = new MqttSubmodelAPI(observedApi, brokerEndpoint, clientId, user, pass.toCharArray());
//			} else {
//				api = new MqttSubmodelAPI(observedApi, brokerEndpoint, clientId);
//			}
//			setWhitelist(api, smId);
//		} catch (MqttException e) {
//			logger.error("Could not create MqttSubmodelApi", e);
//			return observedApi;
//		}
//		return api;
	}
	

	
	
	
	

		
	}
	
	


