package de.hsrm.mi.swtpro03.FactoryFactory.model.machine;

import java.util.Collections;
import java.util.Map;

import org.python.util.PythonInterpreter;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.proxy.PositionerProxy;

public class Positioner extends Machine implements IScriptable{

	private static final String POSITIONER = "Positioner";
	private static final long serialVersionUID = 832674178690804553L;
	public static final int CLASS_ID = 3;

	private Machine origin;
	private String script;
	
	private Map<String, Machine> machineMap;

	public Positioner(int instanceID, String name, Position position,	int processingTime) {
		super(instanceID, name, position, processingTime);
		script = getDefaultScript();
	}
	
	public Positioner(int instanceID, Position position) {
		this(instanceID,POSITIONER, position, 1);
	}

	@Override
	public void take() throws ProductOverflowException {
		work();
		super.take();
	}

	@Override
	public void work() {
		if(script != ""){
			PositionerProxy proxy = new PositionerProxy(this);
			PythonInterpreter interpreter = new PythonInterpreter();
			interpreter.exec("from de.hsrm.mi.swtpro03.FactoryFactory.model import Product");
			interpreter.set("positioner", proxy);
			interpreter.exec(script);
			interpreter.exec("move()");
		}
	}
	
	public void init(){
		PositionerProxy proxy = new PositionerProxy(this);
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.exec("from de.hsrm.mi.swtpro03.FactoryFactory.model import Product");
		interpreter.set("positioner", proxy);
		interpreter.exec(script);
		
		interpreter.exec("init()");
		linkPositioner();
	}


	private void linkPositioner() {
		this.origin.getFollowers().add(this);
		this.predecessors.add(origin);
	}

	@Override
	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public String getScript() {
		return script;
	}

	public Machine getOrigin() {
		return origin;
	}

	public void setOrigin(String machineID) {
		this.origin = machineMap.get(machineID);
	}

	public void setMachineMap(Map<String, Machine> machineMap) {
		this.machineMap = machineMap;
	}

	public void addFollower(String machineId) {
		Machine machine = machineMap.get(machineId);
		this.followers.add(machine);
		machine.getPredecessors().add(this);
	}
	
	public void chooseDestination(String machineId){
		Machine newDestination = machineMap.get(machineId);
		int tempDestination = this.followers.indexOf(newDestination);
		if(this.followers.size()>1){
			Collections.swap(this.followers, 0, tempDestination);
		}
	}
	
	private String getDefaultScript() {
		return "def init():" +
				"\n\t positioner.setOrigin('5-2')" +
				"\n\t positioner.addFollower('5-3') " +
				"\n\t positioner.addFollower('5-5')" +
				"\n" +
				"def move():" +
				"\n\t for product in positioner.origin.products:" +
				"\n\t\t if product.weight > 3:" +
				"\n\t\t\t positioner.chooseDestination('5-3') " +
				"\n\t\t else:" +
				"\n\t\t\t positioner.chooseDestination('5-5') ";
	}

	@Override
	public void setPosition(Position position) {
		this.position = position;
		// Wenn irgendwann keine Vorgänger und Nachfolger IDs sondern 
		// Positionen angegeben werden, müssen input und output hier 
		// angepasst werden
	}
	
	@Override
	public int getClassId() {
		return CLASS_ID;
	}
	
}
