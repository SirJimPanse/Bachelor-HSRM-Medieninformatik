package entities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schnorchel {
    private int alter;
    private String name;
    private List<Hobby> hobbies = new ArrayList<Hobby>();
    private Map<String, Boolean> faehigkeiten = new HashMap<String,Boolean>();

    public int getAlter() {
		return alter;
	}

	public void setAlter(int alter) {
		this.alter = alter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Hobby> getHobbies() {
		return hobbies;
	}

	public void setHobbies(List<Hobby> hobbies) {
		this.hobbies = hobbies;
	}

	public Map<String, Boolean> getFaehigkeiten() {
		return faehigkeiten;
	}

	public void setFaehigkeiten(Map<String, Boolean> faehigkeiten) {
		this.faehigkeiten = faehigkeiten;
	}

	@Override
    public String toString()
    {
    	String hob = "";
    	for (Hobby h: hobbies) {
    		hob += " " + h.hobbyname;
    	}
    	String fk = "";
    	for (String f: faehigkeiten.keySet()) {
    		fk += " "+f;
    	}
    	
    	Hobby[] h = (Hobby[]) hobbies.toArray(new Hobby[hobbies.size()]);
        return String.format("Ich bin ein %d Jahre alter Schnorchel und heisse %s. Meine Hobbies sind %s, ich kann %s", 
            alter, name, hob, fk);
    }

}
