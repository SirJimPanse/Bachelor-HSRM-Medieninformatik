package util;

public interface IObserver {
	public void update(IObservable ob);
	public void update(IObservable ob, Object o);
}
