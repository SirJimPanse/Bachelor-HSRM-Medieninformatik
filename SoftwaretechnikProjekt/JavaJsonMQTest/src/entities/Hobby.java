package entities;


public class Hobby {
	public Hobby() {}
    public Hobby(String hn, boolean ish)
    {
        isSuperhobby = ish;
        hobbyname = hn;
    }
    public boolean isSuperhobby = false;
    public String hobbyname = "";
}
