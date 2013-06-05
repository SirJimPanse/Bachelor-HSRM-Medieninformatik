package CamptureTheFlag.logik;

import java.io.Serializable;

/** 
 * Tupelklasse, die in Java leider fehlt und die wir einige Male brauchen.
 * @author dgens
 *
 * @param <E>
 * @param <T>
 */
public final class P<E,T> extends Object implements Serializable {
	
	/** p-Operator **/
	public static <E,T> P<E,T> p(E x, T y) { return new P<E,T>(x,y); }
	
	private static final long serialVersionUID = -4473193529338854070L;
	final public E x; final public T y;
	
	public P(E x, T y) { this.x = x; this.y = y; }
	@Override
	public int hashCode() { return (this.x.hashCode()*17) ^ this.y.hashCode(); } // 17 am meisten zufällig
	@Override
	public boolean equals(Object o) { return o instanceof P ? ((P<?,?>)o).x.equals(this.x) && ((P<?,?>)o).y.equals(this.y) : false; }
	@Override
	public String toString() { return "(" + this.x + "," + this.y + ")"; }
}
