import org.python.core.Py;
import org.python.util.PythonInterpreter;

public class TRechnerImpl implements TRechner {
	
	private PythonInterpreter pyinterp; 
	
	public TRechnerImpl() {
		this.pyinterp = new PythonInterpreter();
	}
	
	public String eval(String evstr) {
		try {
			return Double.toString(Py.py2double(this.pyinterp.eval(evstr)));
		} catch (Exception e) {
			return "Fehler: Falsche Eingabe.. " + e.getMessage();
		}
	}
	
	/** Test **/
	public static void main(String[] args) {
		System.out.println(new TRechnerImpl().eval(args[0]));
	}
}
