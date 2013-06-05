package tools;

import javax.jms.JMSException;

import com.fasterxml.jackson.core.JsonProcessingException;

import entities.Hobby;
import entities.Schnorchel;

public class App {

	public static void main(String args[]){
		App app = new App();
		app.start();
	}

	private void start() {
		String destinationName = "FactoryFactory";
		Schnorchel schnorchi = createASchnorchel();
		OurPublisher pubi = new OurPublisher(destinationName, ConnectionType.TOPIC);
		OurListener listi = new OurListener(destinationName, ConnectionType.TOPIC);
		
		try {
			listi.start();
		} catch (JMSException e) {
			System.out.println("Listener Error : " + e.getMessage());
		}
		
		try {
			pubi.sendASchnorchel(schnorchi);
		} catch (JsonProcessingException e) {
			System.out.println("Publisher Json Error: " + e.getMessage());
		} catch (JMSException e) {
			System.out.println("Publisher JMS Error: " + e.getMessage());
		}
				
	}

	private Schnorchel createASchnorchel(){
		// Datenobjekt fuellen
		Schnorchel schnorch = new Schnorchel();
		schnorch.setAlter(17);
		schnorch.setName("Heliodor");
		schnorch.getHobbies().add(new Hobby("reiten", false));
		schnorch.getHobbies().add(new Hobby("schwimmen", false));
		schnorch.getHobbies().add(new Hobby("lesen", true));
		schnorch.getFaehigkeiten().put("huepfen", true);
		schnorch.getFaehigkeiten().put("weidomieren", false);
		schnorch.getFaehigkeiten().put("folloppen", true);

		System.out.println("Ausgangsobjekt:");
		System.out.println(schnorch);
		
		return schnorch;
	}
}
