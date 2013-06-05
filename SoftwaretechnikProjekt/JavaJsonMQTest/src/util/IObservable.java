package util;

public interface IObservable {
	public void addObserver(IObserver ob);
	public void removeObserver(IObserver ob);
	public void notifyObservers();
	public void notifyObservers(Object o);
	
}
