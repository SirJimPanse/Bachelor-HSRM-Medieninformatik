package CamptureTheFlag.logik.inits;

import java.util.List;
import CamptureTheFlag.logik.models.Dialog;

/**
 * Erstellt Dialoge zur Nutzung bei den NPC
 * 
 * @author Simon
 */
public class DialogFactory {
	
	public  static List<Dialog> getDialoge() { return DialogFactory.getDialogeFromClass(); }  // Dialoge können später aus XML Files kommen
	private static List<Dialog> getDialogeFromClass() { return CamptureTheFlag.config.Dialoge.getDialoge(); }
	
	/*public Dialog getDialogFromXML(String npcName, String xmlFile) {
		
		Dialog result = null;
		DocumentBuilder builder = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
		try {
			
			factory.setNamespaceAware(true);
			builder = factory.newDocumentBuilder();
			Document daten = builder.parse("file:" + xmlFile);
			daten.getDocumentElement().normalize();
          	// remove whitespace
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPathExpression xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']"); 
			NodeList emptyTextNodes = (NodeList)xpathExp.evaluate(daten, XPathConstants.NODESET);
			for (int i = 0; i < emptyTextNodes.getLength(); i++) {
			    Node emptyTextNode = emptyTextNodes.item(i);
			    emptyTextNode.getParentNode().removeChild(emptyTextNode);
			}
			printElement(daten.getDocumentElement());
			
			NodeList nList = daten.getElementsByTagName("dialog");
			System.out.println(nList.getLength());
			

		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		
		return result;
	}*/
}