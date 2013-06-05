package de.hsrm.mi.swtpro03.FactoryFactory.controller;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.FactoryDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.SessionDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.FactoryManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.SessionManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.SimulationManager;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Session;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Consumer;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Machine;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Positioner;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Producer;
import de.hsrm.mi.swtpro03.FactoryFactory.service.SimulationService;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.FactoryMap;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.MachineMap;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.ProductMap;
import de.hsrm.mi.swtpro03.FactoryFactory.util.IMessagePublisher;

public class SimulationController {

	private static final int DELETE_SIMULATION_ERROR = -2;
	private static final int NEEDS_TO_BE_STOPPED_FIRST = -1;
	private static final int SIMULATION_DELETED = 0;
	
	private static final Logger LOGGER = Logger.getLogger("SimulationController");

	private FactoryManager factoryManager;
	private SessionManager sessionManager;
	private SimulationManager simulationManager;
	private Map<Integer, SimTimer> simulationMap;
	private Map<Integer, ProductMap> productMaps;
	private IMessagePublisher mqPublisher;
	
	private SimulationController(FactoryManager factoryManager, SimulationManager simulationManager, IMessagePublisher mqPublisher) {
		this.mqPublisher = mqPublisher;
		this.factoryManager = factoryManager;
		this.simulationManager = simulationManager;
		this.simulationMap = new java.util.HashMap<Integer, SimTimer>();
		this.productMaps = new java.util.HashMap<Integer, ProductMap>();
	}

	public SimulationController(FactoryManager factoryManager, SessionManager sessionManager, SimulationManager simulationManager, IMessagePublisher mqPublisher) {
		this(factoryManager, simulationManager, mqPublisher);
		this.sessionManager = sessionManager;
	}

	public void start() {
		startWebService();
	}

	private void startWebService() {
		final String URL = "http://0.0.0.0:8080/SimulationService";
		Endpoint.publish(URL, new SimulationService(this));
		LOGGER.log(Level.INFO, "SimulationService: " + URL + "?wsdl");
	}

	public void produce(final long sessionID, final int simulationId, long timeInMillis) {
		final Factory currentSimulation = getSimulationFactoryById(simulationId);

		if (currentSimulation != null) {
			TimerTask timertask = new TimerTask(){
				@Override
				public void run() {
					if(!currentSimulation.isStatsAlreadyShown()){
						tick(sessionID, currentSimulation);
					}
				}
			};
			SimTimer timer = new SimTimer();

			timer.schedule(timertask, new Date(), timeInMillis);

			simulationMap.put(currentSimulation.getFactoryID(), timer);
		}
	}

	private Factory getSimulationFactoryById(int simulationId) {
		Factory currentFactory = null;
		try {
			currentFactory = simulationManager.getSimulation(simulationId);
		} catch (FactoryDoesNotExistException e) {
			LOGGER.log(Level.WARNING, "Simulation mit der ID "+simulationId+" existiert nicht.");
		}
		return currentFactory;
	}

	public void cancelProduction(int simulationId) {
		LOGGER.log(Level.INFO,"Simulation angehalten");
		Timer timer = simulationMap.get(simulationId);
		if (timer != null) {
			timer.cancel();
			simulationMap.remove(timer);
		}
	}

	private void sendProductMessage(long sessionID, int simulationId) {
		mqPublisher.sendMessage(sessionID+"", simulationId+"", "-1", "updateProducts");
	}

	public ProductMap getProducts(int simulationId) {
		return productMaps.get(simulationId);
	}

	private void updateProductMap(int simulationId) {
		Factory currentSimulation = getSimulationFactoryById(simulationId);
		ProductMap pm = new ProductMap();
		if (currentSimulation != null) {
			Collection<Machine> machines = currentSimulation.getMachineMap().values();
			Product[] products = null;
			for (Machine m : machines) {
				m.writeCacheToProdudctList();
				products = m.getProducts().toArray(new Product[] {});
				pm.setAll(m.getPosition(), products);
			}
			productMaps.put(simulationId, pm);
		}
	}

	public String createSimulation(long sessionID) {

		Factory currentFactory = null;
		Factory currentSimulation = null;
		String login = null;
		int factoryId = 0;
		try {
			currentFactory = this.getFactoryBySessionID(sessionID);
			LOGGER.log(Level.INFO, "klone Fabrik");			
			currentSimulation = currentFactory.clone(); 
			LOGGER.log(Level.INFO, "verbinde Maschinen");
			Collection<Machine> machines = currentSimulation.getMachineMap().values();
						
			Machine m = currentSimulation.getArea().globalConnections();
			if (m != null) {
				LOGGER.log(Level.WARNING, "Verbinden der Maschinen fehlgeschlagen!");	
				return "ERROR:" + Factory.getMachineID(m);
			}

			login = sessionManager.get(sessionID).getName();
			factoryId = createSimulation(sessionID, login, currentSimulation);
			for (Machine machine : machines) {
				if (machine instanceof Positioner) {
					((Positioner) machine).setMachineMap(currentSimulation.getMachineMap());
					((Positioner) machine).init();
				}
			}
		} catch (SessionDoesNotExistException e) {
			LOGGER.log(Level.INFO, e.getMessage());
			return "";
		}

		return "ID:" + factoryId;
	}

	private void tick(final long sessionID, final Factory currentFactory) {
		final Collection<Machine> machines = currentFactory.getMachineMap().values();
		final int factoryId = currentFactory.getFactoryID();
		
		LOGGER.log(Level.INFO, "Producers are producing. Factory: "+factoryId);
		
		Producer actProducer;
		long startTime = System.currentTimeMillis();
		if (currentFactory.isSimulation()) {
			for (Machine m : machines) {
				if (m instanceof Producer) {
					actProducer = (Producer) m;
					
					try {
						if(actProducer.isProducing()){
							actProducer.produce();
						}
					} catch (ProductOverflowException e) {
						finishSimulation(sessionID, currentFactory);
					}
					
				}else if (m instanceof Consumer){
					Consumer consumer = (Consumer) m;
					consumer.incEmptyCounter();
					if(consumer.isReadyToClear()){
						consumer.clear();
						consumer.resetEmptyCounter();
					}
				}
			}
			updateProductMap(factoryId);
		}
			sendProductMessage(sessionID,factoryId);
			long totalTime = System.currentTimeMillis() - startTime;
			LOGGER.log(Level.INFO, "Tick braucht "+totalTime+" Millisekunden");
		}

	private void finishSimulation(long sessionID, Factory factory) {
		int factoryId = factory.getFactoryID();
		Collection<Machine> machines = factory.getMachineMap().values();
		if (!factory.isStatsAlreadyShown()) {
			if(allfinished(machines)){
				createStatistics(sessionID, factory);
				cancelProduction(factoryId);								
				factory.setStatsAlreadyShown(true);
			}
		}
	}	

	private boolean allfinished(Collection<Machine> machines) {
		Producer actProducer;
		for (Machine m : machines) {
			if (m instanceof Producer) {
				actProducer = (Producer) m;
				if(actProducer.isProducing()){
					return false;
				}
			}
		}
		return true;
	}

	private Factory getFactoryBySessionID(long sessionId) {
		Factory factory = null;
		try {
			Session session = sessionManager.get(sessionId);
			int factoryId = session.getFactoryId();
			factory = factoryManager.getFactory(factoryId);
		} catch (SessionDoesNotExistException e) {
			LOGGER.log(Level.INFO, "getFactoryBySessionId " + e.getMessage());
		} catch (FactoryDoesNotExistException e) {
			LOGGER.log(Level.INFO, "getFactoryBySessionId " + e.getMessage());
		}
		return factory;
	}

	public FactoryMap getSimulations(long sessionId) {
		FactoryMap map = new FactoryMap();
		try {
			Session session = sessionManager.get(sessionId);
			String login = session.getName();
			map.setFactories(simulationManager.getSimulations(login));
		} catch (SessionDoesNotExistException e) {
			LOGGER.log(Level.INFO, "GetSimulations: " + e.getMessage());
		}
		return map;
	}

	private synchronized int createSimulation(long sessionID, String login,
			Factory factory) {
		return simulationManager.createFactory(login, factory, sessionID);
	}

	private String machineHeader(Machine machine) {
		return System.getProperty("line.separator") + "<<<"
				+ machine.getName() +  " " + machine.getInstanceID() + ">>>"
				+ System.getProperty("line.separator")
				+ System.getProperty("line.separator");
	}

	private void createStatistics(long sessionID, Factory factory) {

		int factoryID = factory.getFactoryID();
		Collection<Machine> machines = factory.getMachineMap().values();
		String s = "";

		for (Machine act : machines) {
			if (act instanceof Producer || act instanceof Consumer) {
				s += statisticsString(act);
			}
		}		
		mqPublisher.sendMessage(sessionID+"", factoryID+"", s, "statistic");		
	}

	private String statisticsString(Machine machine) {

		String string = machineHeader(machine);

		for (String key : machine.getStatisticsList().keySet()) {
			string += "Produkt: " + key;
			string += System.getProperty("line.separator");
			string += " - Anzahl: "
					+ machine.getStatisticsList().get(key).getCount();
			string += System.getProperty("line.separator");
			string += " - Gewicht: "
					+ machine.getStatisticsList().get(key).getWeight();
			string += System.getProperty("line.separator");
			string += " - Preis: "
					+ machine.getStatisticsList().get(key).getPrice();
			string += System.getProperty("line.separator");
			string += System.getProperty("line.separator");
		}

		return string;
	}

	public void addMemberToSimulation(long sessionID, int simulationId,
			String login) {
		try {
			simulationManager.addMemberToMemberAccess(simulationId, login);
			Session session = sessionManager.get(sessionID);
			Factory simulation = getSimulationFactoryById(simulationId);
			mqPublisher.sendMessage(session.getSessionID()+"", login, simulation.getFactoryID()+" - "+simulation.getName(), "added-Simulation");
			LOGGER.log(Level.INFO, "addMemberToMemberAccess - sendMessage: " + session.getName() + login + simulation.getName() + "added-Simulation");
		} catch (SessionDoesNotExistException e) {
			LOGGER.log(Level.INFO, "addMemberToMemberAccess " + e.getMessage());
		}
	}

	/**
	 * 
	 * @param sessionId
	 * @param simulationId
	 * @return 0 if simulation has been deleted, -1 if simulation has to be
	 *         stopped first, -2 if any other error occurred
	 */
	public int deleteSimulation(long sessionId, int simulationId) {
		try {
			Session session = sessionManager.get(sessionId);
			String login = session.getName();
			boolean simulationIsRunning = isRunning(simulationId);
			boolean noOtherUsers = simulationManager.deleteSimulation(login,
					simulationId, simulationIsRunning);

			if (!simulationIsRunning) {
				return SIMULATION_DELETED; // Deleted
			} else if (noOtherUsers) {
				return NEEDS_TO_BE_STOPPED_FIRST; // has to be stopped first
			}
		} catch (SessionDoesNotExistException e) {
			LOGGER.log(Level.INFO, "deleteFactory " + e.getMessage());
		}
		return DELETE_SIMULATION_ERROR; // error
	}

	private boolean isRunning(int simulationId) {
		SimTimer timer = simulationMap.get(simulationId);
		return (timer != null && timer.isRunning());
	}

	public int getAreaWidth(int simulationId) {
		Factory simulation = getSimulationFactoryById(simulationId);
		return simulation.getArea().getWidth();
	}

	public int getAreaHeight(int simulationId) {
		Factory simulation = getSimulationFactoryById(simulationId);
		return simulation.getArea().getHeight();
	}

	public MachineMap getMachineMap(int simulationId) {
		Factory factory = getSimulationFactoryById(simulationId);
		MachineMap machineMap = new MachineMap();
		if (factory != null) {
			for (String machineID : factory.getMachineMap().keySet()) {
				Position position = factory.getMachineMap().get(machineID)
						.getPosition();
				machineMap.addMachineToMap(machineID, position);
			}
		}
		return machineMap;
	}

	private class SimTimer extends Timer {
		private boolean isRunning;

		public SimTimer() {
			super();
			isRunning = false;
		}

		@Override
		public void schedule(TimerTask task, long delay, long period) {
			super.schedule(task, delay, period);
			isRunning = true;
		}

		public boolean isRunning() {
			return isRunning;
		}

		@Override
		public void cancel() {
			super.cancel();
			isRunning = false;
		}
	}
}
