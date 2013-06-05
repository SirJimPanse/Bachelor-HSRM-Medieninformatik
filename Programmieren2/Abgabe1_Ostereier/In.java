import java.io.*;

/*****************************************************
 Klasse zum Einlesen von Daten

 Folgende Klassen-Variable sind implementiert:
 private BufferedReader d


 Folgende Klassen-Methoden sind implementiert:
 int readInt() // liest ein int-Wert von Konsole ein
 void close()  // zum Schließen des BufferedReaders

 Folgende Instanz-Methode sind implementiert:
 void finalize()	// zum Schließen des BufferedReaders
                    // kann es nur einmal geben
                    // macht wahrscheinlich wenig Sinn da keine Instanz

 ******************************************************/

public class In{

	static private BufferedReader inputSteam = new  BufferedReader(new InputStreamReader(System.in));

	public static char readChar() {

		String s;

		if (inputSteam != null) { // Erzeugung überprüfen
			try {
					s = inputSteam.readLine();
					// wenn ein String eingegeben wurde, der mindestens ein Zeichen besitzt ...
					if (s != null && s.length() > 0) 
						// ... dann gibt das erste Zeichen des Strings zurueck
						return s.charAt(0); 
					else
						return '0';
			} catch (IOException e) {
					System.out.println(e);
					return '0';
			}
	     } else {

			System.out.println("Es konnte nichts eingelesen werden! Rückgabewert 0!");
			return '0';
		}
}

	
	public static int readInt() {

		int wert;

		if (inputSteam != null) { // Erzeugung überprüfen
			try {
				try {
					wert = Integer.parseInt(inputSteam.readLine());
				} catch(NumberFormatException e) {
						System.out.println(e);
						wert = 0;
				}
				// Eingabe-Streams wieder schließen

				return wert;

			 } catch (IOException e) {
					System.out.println(e);
					return 0;
			 }
	     } else {

			System.out.println("Es konnte nichts eingelesen werden! Rückgabewert 0!");
			return 0;
		}
	}

	public static long readLong() {

			long wert;

			if (inputSteam != null) { // Erzeugung überprüfen
				try {
					try {
						wert = Long.parseLong(inputSteam.readLine());
					} catch(NumberFormatException e) {
							System.out.println(e);
							wert = 0;
					}
					// Eingabe-Streams wieder schließen

					return wert;

				 } catch (IOException e) {
						System.out.println(e);
						return 0;
				 }
		     } else {

				System.out.println("Es konnte nichts eingelesen werden! Rückgabewert 0!");
				return 0;
			}
	}

	public static float readFloat() {

				float wert;

				if (inputSteam != null) { // Erzeugung überprüfen
					try {
						try {
							wert = Float.parseFloat(inputSteam.readLine());
						} catch(NumberFormatException e) {
								System.out.println(e);
								wert = 0;
						}
						// Eingabe-Streams wieder schließen

						return wert;

					 } catch (IOException e) {
							System.out.println(e);
							return 0;
					 }
			     } else {

					System.out.println("Es konnte nichts eingelesen werden! Rückgabewert 0!");
					return 0;
				}
	}

	public static double readDouble() {

				double wert;

				if (inputSteam != null) { // Erzeugung überprüfen
					try {
						try {
							wert = Double.parseDouble(inputSteam.readLine());
						} catch(NumberFormatException e) {
								System.out.println(e);
								wert = 0;
						}
						// Eingabe-Streams wieder schließen

						return wert;

					 } catch (IOException e) {
							System.out.println(e);
							return 0;
					 }
			     } else {

					System.out.println("Es konnte nichts eingelesen werden! Rückgabewert 0!");
					return 0;
				}
	}

	public static String readString() {

			String s;

			if (inputSteam != null) { // Erzeugung überprüfen
				try {
						s = inputSteam.readLine();
						return s;
				} catch (IOException e) {
						System.out.println(e);
						return null;
				}
		     } else {

				System.out.println("Es konnte nichts eingelesen werden! Rückgabewert 0!");
				return null;
			}
	}

	public static void close() {
		try {
			inputSteam.close();
		} catch (IOException e) {
				System.out.println(e);
		}
	}

	protected void finalize() throws Throwable {

		// Eine verwendetet temporärer Datei löschen 
		// Wenn die Datei nicht existiert oder tempfile null ist,
		// dann kann dies eine Ausnahme auslösen, s. throws
		inputSteam.close();
	}



}