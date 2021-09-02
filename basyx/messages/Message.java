package messages;

import java.io.Serializable;
import java.util.HashMap;

import AssetID.assetId;

@SuppressWarnings("serial")
public class Message implements Serializable{
	
	

	private String replyWith;
	private String replyBy;
	private String content;
	private Object contentObj; //look into this...
	private String conversationId;
	private String InReply;
	private String ontology;
	private String language;
	private String encoding;
	private String protocol;
	
	
	private HashMap<String,Object> userArgs;
	
	private assetId sender;
	private assetId[] receivers;
	private assetId replyTo;
	
	private Performative performative;
	
	
	public Message() {
		
	}
	
	public void setReplyWith(String replyWith) {
		
		this.replyWith = replyWith;		
		
	}
	public String getReplyWith() {
		return replyWith;
	}
	
	//check types again................
	public String getReplyBy() {
		return replyBy;
	}
	public String getInReply() {
		return InReply;
	}
	public void setInReply(String inReply) {
		InReply = inReply;
	}
	public void setOntolology(String ontology) {
		this.ontology = ontology;
		
	}
	public String getOntology() {
		return ontology;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getLanguage() {
		return language;
	}
	public void setUserArgs(HashMap<String, Object> userArgs) {
		this.userArgs = userArgs;
	}
	public HashMap<String, Object> getUserArgs() {
		return userArgs;
	}
	public void setReplyTo(assetId replyTo) {
		this.replyTo = replyTo;
		
	}
	public assetId getReplyTo() {
		return replyTo;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setPerformative(Performative performative) {
		this.performative = performative;
	}
	public Performative getPerformative() {
		return performative;
	}
	public void setContentObj(Object contentObj) {
		this.contentObj = contentObj;
	}
	public void  setSender(assetId sender) {
		this.sender = sender;
		
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public String getCoversationId() {
		return conversationId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
		
	}
	public assetId getSender() {
		
		return sender;
	}
	public assetId[] getReceivers() {
		return receivers;
	}


	public void setReceivers(assetId[] receivers) {
		this.receivers = receivers;
	}
	
	public Message(Message copy, assetId assetId)
	{
		
		this.setSender(copy.getSender());
		this.setReceivers(new assetId[] {assetId});
		this.setContent(copy.getContent());
		this.setContentObj(copy.getContent());
		this.setConversationId(copy.getCoversationId());
		this.setPerformative(copy.getPerformative());
		this.setProtocol(copy.getProtocol());
		this.setEncoding(copy.getEncoding());
		this.setReplyTo(copy.getReplyTo());
		this.setUserArgs(copy.getUserArgs());
		this.setLanguage(copy.getLanguage());
		this.setOntolology(copy.getOntology());
		this.setInReply(copy.getInReply());
		this.setReplyTo(copy.getReplyTo());
		this.setReplyWith(copy.getReplyWith());
		
		
	}

	
	
	
}
