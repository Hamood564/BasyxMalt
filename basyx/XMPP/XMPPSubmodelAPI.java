
package XMPP;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.reference.IKey;
import org.eclipse.basyx.submodel.metamodel.api.reference.IReference;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.ISubmodelElement;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.operation.IOperation;
import org.eclipse.basyx.submodel.restapi.api.ISubmodelAPI;
import org.eclipse.basyx.vab.modelprovider.VABPathTools;
import org.jivesoftware.smack.XMPPConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation variant for the SubmodelAPI that triggers XMPP events for
 * different CRUD operations on the submodel. Has to be based on a backend
 * implementation of the ISubmodelAPI to forward its method calls.
 * 
 */
public class XMPPSubmodelAPI extends XMPPEventService implements ISubmodelAPI {
	private static Logger logger = LoggerFactory.getLogger(XMPPSubmodelAPI.class);

	// List of topics
	public static final String TOPIC_CREATESUBMODEL = "BaSyxSubmodel_createdSubmodel";
	public static final String TOPIC_ADDELEMENT = "BaSyxSubmodel_addedSubmodelElement";
	public static final String TOPIC_DELETEELEMENT = "BaSyxSubmodel_removedSubmodelElement";
	public static final String TOPIC_UPDATEELEMENT = "BaSyxSubmodel_updatedSubmodelElement";
	public static final String AssetJID = "testuser2";
	public static final String AssetName = "testuser2";
	public static final String Assetaddress = "testuser2@desktop-q5e1fr3.tqc.local";
	public static LinkedList<String> myMessages;
	public static String serverEndpoint;
	
	// The underlying SubmodelAPI
	protected ISubmodelAPI observedAPI;

	// Submodel Element whitelist for filtering
	protected boolean useWhitelist = false;
	protected Set<String> whitelist = new HashSet<>();

	/**
	 * Constructor for adding this XMPP extension on top of another SubmodelAPI
	 * 
	 * @param observedAPI The underlying submodelAPI
	 */
	public XMPPSubmodelAPI(ISubmodelAPI observedAPI, String serverEndpoint, String clientId) throws Exception {
		super(serverEndpoint, clientId);
		logger.info("Create new XMPP submodel for endpoint " + serverEndpoint);
		XMPPEventService xmppeventservice = new XMPPEventService(serverEndpoint, clientId);
		this.observedAPI = observedAPI;
		createXMPPEntry(AssetJID, AssetName, serverEndpoint);
		String XMPPMessage =myMessages.get(0); 
		sendXMPPmessage(serverEndpoint, Assetaddress, XMPPMessage);
	}

	/**
	 * Constructor for adding this XMPP extension on top of another SubmodelAPI
	 * 
	 * @param observedAPI The underlying submodelAPI
	 * @throws Exception
	 */
	public XMPPSubmodelAPI(ISubmodelAPI observedAPI, String serverEndpoint, String clientId, String user, String pw)
			throws Exception {
		super(serverEndpoint, clientId, user, pw);
		logger.info("Create new XMPP submodel for endpoint " + serverEndpoint);
		XMPPEventService xmppeventservice = new XMPPEventService(serverEndpoint, clientId, user, pw);
		this.observedAPI = observedAPI;
		createXMPPEntry(AssetJID, AssetName, serverEndpoint);
		String XMPPMessage =myMessages.get(0); 
		sendXMPPmessage(serverEndpoint, Assetaddress, XMPPMessage);
	}

	/**
	 * Constructor for adding this XMPP extension on top of another SubmodelAPI.
	 * 
	 * @param observedAPI The underlying submodelAPI
	 * @param client      An already connected XMPP client
	 * @throws Exception 
	 */
	public XMPPSubmodelAPI(ISubmodelAPI observedAPI, XMPPConnection connection, String serverEndpoint, String XMPPMessage) throws Exception {
		super(connection);
		this.observedAPI = observedAPI;
		this.connection = connection;
		sendXMPPmessage(serverEndpoint, Assetaddress, XMPPMessage);
	}

	/**
	 * Adds a submodel element to the filter whitelist. Can also be a path for nested submodel elements.
	 * 
	 * @param element
	 */
	public void observeSubmodelElement(String shortId) {
		whitelist.add(VABPathTools.stripSlashes(shortId));
	}

	/**
	 * Sets a new filter whitelist.
	 * 
	 * @param element
	 */
	public void setWhitelist(Set<String> shortIds) {
		this.whitelist.clear();
		for (String entry : shortIds) {
			this.whitelist.add(VABPathTools.stripSlashes(entry));
		}
	}

	/**
	 * Disables the submodel element filter whitelist
	 * 
	 * @param element
	 */
	public void disableWhitelist() {
		useWhitelist = false;
	}

	/**
	 * Enables the submodel element filter whitelist
	 * 
	 * @param element
	 */
	public void enableWhitelist() {
		useWhitelist = true;
	}

	@Override
	public ISubmodel getSubmodel() {
		return observedAPI.getSubmodel();
	}

	@Override
	public void addSubmodelElement(ISubmodelElement elem) {
		observedAPI.addSubmodelElement(elem);
		if (filter(elem.getIdShort())) {
			String XMPPMessage =myMessages.get(0); 
			sendXMPPmessage(serverEndpoint, Assetaddress, XMPPMessage);
		}
	}

	@Override
	public void addSubmodelElement(String idShortPath, ISubmodelElement elem) {
		observedAPI.addSubmodelElement(idShortPath, elem);
		if (filter(idShortPath)) {
			String XMPPMessage = myMessages.get(0);
			sendXMPPmessage(serverEndpoint, Assetaddress, XMPPMessage);
		}
	}

	@Override
	public ISubmodelElement getSubmodelElement(String idShortPath) {
		return observedAPI.getSubmodelElement(idShortPath);
	}

	@Override
	public void deleteSubmodelElement(String idShortPath) {
		observedAPI.deleteSubmodelElement(idShortPath);
		if (filter(idShortPath)) {
			String XMPPMessage = myMessages.get(0);
			sendXMPPmessage(serverEndpoint, Assetaddress, XMPPMessage);
		}
	}

	@Override
	public Collection<IOperation> getOperations() {
		return observedAPI.getOperations();
	}

	@Override
	public Collection<ISubmodelElement> getSubmodelElements() {
		return observedAPI.getSubmodelElements();
	}

	@Override
	public void updateSubmodelElement(String idShortPath, Object newValue) {
		observedAPI.updateSubmodelElement(idShortPath, newValue);
		if (filter(idShortPath)) {
			String XMPPMessage = myMessages.get(0);
			sendXMPPmessage(serverEndpoint, Assetaddress, XMPPMessage);
		}
	}

	@Override
	public Object getSubmodelElementValue(String idShortPath) {
		return observedAPI.getSubmodelElementValue(idShortPath);
	}

	@Override
	public Object invokeOperation(String idShortPath, Object... params) {
		return observedAPI.invokeOperation(idShortPath, params);
	}

	@Override
	public Object invokeAsync(String idShortPath, Object... params) {
		return observedAPI.invokeAsync(idShortPath, params);
	}

	@Override
	public Object getOperationResult(String idShort, String requestId) {
		return observedAPI.getOperationResult(idShort, requestId);
	}
	
	public static String getCombinedMessage(String aasId, String submodelId, String elementPart) {
		elementPart = VABPathTools.stripSlashes(elementPart);
		return "(" + aasId + "," + submodelId + "," + elementPart + ")";
	}

	private boolean filter(String idShort) {
		idShort = VABPathTools.stripSlashes(idShort);
		return !useWhitelist || whitelist.contains(idShort);
	}
	
	private String getSubmodelId() {
		ISubmodel submodel = getSubmodel();
		return submodel.getIdentification().getId();
	}
	
	private String getAASId() {
		ISubmodel submodel = getSubmodel();
		IReference parentReference = submodel.getParent();
		if (parentReference != null) {
			List<IKey> keys = parentReference.getKeys();
			if (keys != null && keys.size() > 0) {
				return keys.get(0).getValue();
			}
		}
		return null;
	}
}
