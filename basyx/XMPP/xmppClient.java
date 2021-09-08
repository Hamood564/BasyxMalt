package XMPP;

import java.util.*;
import java.util.logging.*;


import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.restapi.api.ISubmodelAPI;
import org.eclipse.basyx.submodel.restapi.api.ISubmodelAPIFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
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
		
		public XMPPAddress(String addr) {
			if(addr.regionMatches(0, "xmpp://", 0, 7)== false) {
				System.out.println("XMPP Address Request found. Looking Up......");				
			}
			xmppAddress = addr.substring(7,addr.length());
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
			return null;
		}
		
		//get ID
		public String getID() {
			return xmppAddress;
		}
		
		public String xmpptoString() {
			return new String("xmpp://" + xmppAddress);
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
	
	
	//XMPP Message Transport Protocol connection initiate
	
	public static class XMPPmtp{
		
		private String prefix = "basyx_mtp_xmpp_";
		private String[] protocols = {"xmpp"};
		private String Msg_NameExt= "basyx.msgs.mtp.xmpp";
		public XMPPConnection xmpp_connect;
		
		static Logger myLogger = Logger.getLogger(XMPPmtp.class.getName());
		
		
		
		//XMPP Login to server, with userid and pass
		private void xmppLogin(String server, String user, String pass) throws Exception{
			  try {
				  if(myLogger.isLoggable(null))
					  myLogger.log(null,"login: creating XMPP Connection to server" + server + ", with user:" + user);
					  xmpp_connect = new XMPPConnection(server);
				  
				  xmpp_connect.connect();
				  if(myLogger.isLoggable(null))
					  myLogger.log(null,"login: XMPP Connection is Connected" + xmpp_connect.isConnected());
				  xmpp_connect.login(user, pass, "acc");
				  if(myLogger.isLoggable(null))
					  myLogger.log(null,"login: XMPP Connection is Authenticated" + xmpp_connect.isAuthenticated());
				  Presence p = new Presence(Presence.Type.available);
				  xmpp_connect.sendPacket(p);
			  }catch(XMPPException e) {
				  throw new Exception("Cannot Login to XMPP Server", e);				  
			  }  
		  }
		
		//Disconnect from XMPP Server
		private void logout() {
			xmpp_connect.disconnect();
		}
		
		
		
	}
	
	
	//For testing....................
	
	
	
	
	

	@Override
	public ISubmodelAPI getSubmodelAPI(Submodel submodel) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	

		
	}
	
	


