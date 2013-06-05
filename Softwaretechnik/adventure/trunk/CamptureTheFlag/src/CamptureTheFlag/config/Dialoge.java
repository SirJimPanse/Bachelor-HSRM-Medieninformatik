package CamptureTheFlag.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import CamptureTheFlag.logik.models.Dialog;
import CamptureTheFlag.logik.models.Erwiderung;
import CamptureTheFlag.logik.entities.gegenstaende.Doughnut;
import CamptureTheFlag.logik.entities.gegenstaende.Schein;
import CamptureTheFlag.logik.entities.gegenstaende.Schluessel;
import CamptureTheFlag.logik.entities.gegenstaende.Spicker;

/**
 * Eine Klasse um die Dialogerstellung zu kapseln (anstatt diese aus einem XML File mit JAXB auszulesen beispielsweise).
 * 
 * @author dgens
 */
public class Dialoge {
	
	/** Ein Listenkonstruktor mit varargs (machts bisschen lesbarer) **/
	public static <T> List<T> l(T... array) { return new ArrayList<T>(Arrays.asList(array)); }
	
	public static List<Dialog> getDialoge() {
		
		final int KEY_BESENKAMMER = 3;
		final int KEY_PRUEFUNGSZIMMER = 4;
		
		/*
		 * Lisa
		 * führt Dialog ohne Nutzen
		 */
		Dialog lisa = new Dialog("Hallo wie gehts dir?");
		lisa.setErwiderungen(l(
			new Erwiderung("Schlecht", 
				new Dialog("Oh wieso?",	l(
								new Erwiderung("Weil!", new Dialog("Oh..")),
								new Erwiderung("Hörst du nicht zu?", lisa),
								new Erwiderung("Ach uninteressant..", new Dialog("Naja dann nicht.."))
				))
	        ),
			new Erwiderung("Naja...", new Dialog("Na dann...")),
	        new Erwiderung("Gut!",  new Dialog("Cool!"))
		));
		
		
		/*
		 * Homer
		 * fragt nach einem Doughnut
		 * Dieser ist TIBUT für KEY_BESENKAMMER
		 */
		Schluessel keyBesenkammer = new Schluessel("Homer's Schlüssel", "schluessel.png", "Ein kleiner verbogener Schlüssel - ob der zu was taugt?", KEY_BESENKAMMER);
		Dialog homer = new Dialog("Hey Du, gib mir einen Doughnut!");
		homer.setErwiderungen(l(
			new Erwiderung("Nagut Homer, ich bin eh satt", new Doughnut(null, null, null), new Dialog("Ah .. du bist so gut zu mir. Hier nimm das.", keyBesenkammer)),
			new Erwiderung("Nein Homer, hol Dir doch selbst einen.", 
				new Dialog("Ganz schön gemein!", l(
					new Erwiderung("Du bist sowieso zu dick!", homer))))
		));
		
		/*
		 * Meister Berdux
		 * Gibt Schlüssel für Prüfungszimmer
		 */
		Schluessel keyPruefungszimmer = new Schluessel("Schlüssel zum Prüfungszimmer", "schluessel.png", "Mit diesem Schlüssel ist man zu den Prüfungen zugelassen", KEY_PRUEFUNGSZIMMER);
		Dialog berdux = new Dialog("Hast du Dich für alle Prüfungen im QIS angemeldet?");
		berdux.setErwiderungen(l(
			new Erwiderung("Aber natürlich!", new Dialog("Sehr gut. Mit diesem Schlüssel kommst du ins Prüfungszimmer.", keyPruefungszimmer)),
			new Erwiderung("Öhm, nein - wie geht das denn?", 
				new Dialog("Dazu geht man online ins QIS und meldet sich an.", l(
					new Erwiderung("OK, Danke. Dann mache ich das gleich!", berdux))))
		));
		
		
		/*
		 * Meister Barth
		 * Prüft in ADS
		 * Richtige Antwort liefert SCHEIN_ADS
		 */
		Schein scheinADS = new Schein("Schein ADS", "schein_ads.png", "Erfolgreich die Prüfung ADS bestanden.");
		Dialog barth = new Dialog("Was ist der coolste Baum?");
		barth.setErwiderungen(l(
			new Erwiderung("2-3-4-Baum", barth),
			new Erwiderung("Rot-Schwarz-Baum", new Dialog("Richtig, Prüfung bestanden", scheinADS)),
			new Erwiderung("Binärbaum", barth),
			new Erwiderung("Spicker einsetzen",  new Spicker(null, null, null), new Dialog("Richtig, Prüfung bestanden", scheinADS))
		));
		
		
		/*
		 * Meister Weitz
		 * Prüft in Rechnernetze
		 * Richtige Antwort liefert SCHEIN_NETZ
		 */
		Schein scheinNetz = new Schein("Schein Rechnernetze", "schein_netze.png", "Erfolgreich die Prüfung Rechnernetze bestanden.");
		Dialog weitz = new Dialog("Was bedeutet DNS?");
		weitz.setErwiderungen(l(
			new Erwiderung("Datum-Nummer-System", weitz),
			new Erwiderung("Dynamischer Neurotransmittarisches System", weitz),
			new Erwiderung("Domain Name System", new Dialog("Richtig, Prüfung bestanden", scheinNetz))
		));
		

		// alle Dialoge verfühgbar machen
		return l(lisa, homer, berdux, barth, weitz);
	}
}
