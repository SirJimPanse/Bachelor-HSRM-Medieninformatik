package de.hsrm.mi.swtpro03.FactoryFactory.model.machine;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.python.util.PythonInterpreter;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.proxy.ChangerProxy;

public class Changer extends Machine implements IScriptable{
	private static final String CHANGER = "Changer";
	private static Logger logger = Logger.getLogger(CHANGER);
	private static final long serialVersionUID = -1851511672600933349L;
	public static final int CLASS_ID = 1;
	private String inputProductType;
	private String script;
	
	public Changer(int instanceID, String name, Position position,int processingTime) {	
		super(instanceID, name, position, processingTime);
		
		Position inputPos  = new Position(position, position.getDirection().opposing());
		Position inputPos1 = new Position(position, position.getDirection().rotateLeft());
		Position inputPos2 = new Position(position, position.getDirection().rotateRight());
		
		this.getOutput().add(position);
		this.getInput().add(inputPos);
		this.getInput().add(inputPos1);
		this.getInput().add(inputPos2);
		setInputProductType("Obst");
		script = getDefaultScript();
	}
	
	public Changer(int instanceID, Position position){
		this(instanceID, CHANGER, position, 1);
	}

	@Override
	public void work(){
		if(script != ""){
			ChangerProxy changerProxy = new ChangerProxy(this);
			PythonInterpreter interpreter = new PythonInterpreter();
			interpreter.exec("from de.hsrm.mi.swtpro03.FactoryFactory.model import Product");
			interpreter.set("changer", changerProxy);
			interpreter.exec(script);
			interpreter.exec("change()");
		}
	}

	@Override
	public void take() throws ProductOverflowException{
		logger.log(Level.INFO, this.getName() + " verarbeitet Produkte");
		if(this.getProducts().size() > 0){
			this.work();
		}
		super.take();
	}
	
	public String getInputProductType() {
		return inputProductType;
	}

	public void setInputProductType(String inputProductType) {
		this.inputProductType = inputProductType;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
	public String getScript() {
		return script;
	}
	
	private String getDefaultScript() {
		return "def change(): " +
				"\n\t newName = 'Changed-Kiste'" +
				"\n\t newWeight = 0." +
				"\n\t newPrice = 0." +
				"\n\t temp = list(changer.products)" +
				"\n\t inputProductName = 'Kiste'" +
				"\n\t for act in temp:" +
				"\n\t\t if act.name == inputProductName:" +
				"\n\t\t\t newWeight += act.weight" +
				"\n\t\t\t newPrice += act.price+20" +
				"\n\t\t\t changer.products.remove(act)" +
				"\n\t changedProduct = Product(newName, newWeight, newPrice, 'Changed-Default');" +
				"\n\t changer.products += [changedProduct]";
	}

	@Override
	public int getClassId() {
		return CLASS_ID;
	}
}