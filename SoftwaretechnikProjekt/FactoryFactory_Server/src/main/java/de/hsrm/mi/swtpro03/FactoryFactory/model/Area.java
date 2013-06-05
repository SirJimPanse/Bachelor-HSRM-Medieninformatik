package de.hsrm.mi.swtpro03.FactoryFactory.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.AreaPositioningException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.ResizeMustNotDeleteMachinesException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Machine;

public class Area implements Serializable, Cloneable, Iterable<Machine> {

	private static Logger logger = Logger.getLogger("Area");
	private static final long serialVersionUID = -3660072665487684556L;

	private List<List<Machine>> area;
	
	private int width;
	private int height;

	public Area() {
		this.width  = 20;
		this.height = 20;
		this.area   = createArea(width,height);
	}

	public Area(int width, int height) {
		this.width  = width;
		this.height = height;
		this.area   = createArea(width,height);
	}
	
	/** Erstellt eine zweidimensionale Liste von Maschinen als der Area zugrunde liegenden Datenstruktur. **/
	private List<List<Machine>> createArea(int width, int height) {
		List<List<Machine>> init = new java.util.ArrayList<List<Machine>>();
		for (int i = 0; i < height; ++i){
			init.add(createRow(width));
		}
		return init;
	}

	/** Ändert die Größe aller Zeilen in-place (negative Werte sinnfrei) **/
	private void resizeRows(int deltaWidth) {
		if (0 < deltaWidth) {
			for (int j = 0; j < height; ++j) {
				List<Machine> lmao = area.get(j);
				for (int i = width; i < width + deltaWidth; ++i) {
					lmao.add(null);
				}
			}
		}
	}

	/* Initialisiert eine Zeile der Breite width mit Nullwerten */
	private List<Machine> createRow(int width) {
		List<Machine> temp = new java.util.ArrayList<Machine>();
		for (int j = 0; j < width; ++j){
			temp.add(null);
		}
		return temp;
	}
	
	/** Vergrößert die Factoryfläche. Wenn die Fläche verkleinert werden soll,
	 *  wird nicht die Fläche verkleinert, sondern nur die Breite und die Höhe
	 *  überschrieben. 
	 * @throws ResizeMustNotDeleteMachinesException falls das Grid so 
	 * verkleinert wird, dass Machinen außerhalb des gültigen Bereiches liegen. 
	 **/
	public List<List<Machine>> resizeDefaultArea(int x, int y) throws ResizeMustNotDeleteMachinesException {
		int deltaHeight = y - height;
		int deltaWidth  = x - width;
		List<List<Machine>> resize = area;
		
		if (resize == null) {
			area = createArea(x,y);
		} else {
			if(0 <= deltaWidth) {
				resizeRows(deltaWidth);
			}
			if (0 <= deltaHeight) {
				area.addAll(createArea(x,deltaHeight));
			}
			if (0 >= deltaHeight || 0 >= deltaWidth) {
				if (isGridShrinkable(x, y)) {
					deleteUnnecessaryBoxes(x,y);
				} else {
					logger.log(Level.WARNING, "User versucht Factory kleiner zu machen als die konvexe Hülle aller Maschinenpositionen.");
					throw new ResizeMustNotDeleteMachinesException();
				}
		}
		}
		
		this.width  = x;
		this.height = y;
		show();
		
		return resize;
	}
	
	private void deleteUnnecessaryBoxes(int x, int y) {
		List<Machine> list;
		while (area.size() > y) {
			area.remove(area.size()-1);
		}
		
		for (int i = 0; i < area.size(); i += 1) {
			list = area.get(i);
			while(list.size() > x) {
				list.remove(list.size()-1);
			}
		}
		
	}

	private boolean isGridShrinkable(int newWidth, int newHeight) {
		Iterator<Machine> mIter = iterator();
		Machine machine;
		Position position;
		
		while(mIter.hasNext()) {
			machine = mIter.next();
			position = machine.getPosition();
			
			if (position.getX() > newWidth-1 || position.getY() > newHeight-1) {
				return false;
			}
		}
		return true;
	}

	public Position neighbour(Position connection) {

		Direction direction = connection.getDirection();
		int x = connection.getX();
		int y = connection.getY();
		
		switch (direction) {
		case NORTH:
			return new Position(x, y - 1, direction);
		case EAST:
			return new Position(x + 1, y, direction);
		case SOUTH:
			return new Position(x, y + 1, direction);
		case WEST:
			return new Position(x - 1, y, direction);
		default:
			return null;
		}
	}
	
	public void connectNeighbours(Machine machine) throws AreaPositioningException {
		Position neighbourPos;
		Machine neighbour;
		
		if (!machine.getInput().isEmpty()) {
			for (Position actInputPosition : machine.getInput()) {
				neighbourPos = neighbour(actInputPosition);
				
				
				if (isInBounds(neighbourPos) && fieldIsEmpty(neighbourPos) == false) {
					
					neighbour = getMachine(neighbourPos);
					Direction connector = actInputPosition.getDirection().opposing();
					
					if (isInputConnectable(machine, neighbour, connector)) {
						machine.getPredecessors().add(neighbour);
					}
				}
			}
		}
		
		if (!machine.getOutput().isEmpty()) {
			for (Position actOutputPosition : machine.getOutput()) {
				neighbourPos = neighbour(actOutputPosition);
				
				if (isInBounds(neighbourPos) && fieldIsEmpty(neighbourPos) == false) {
					
					neighbour = getMachine(neighbourPos);
					Direction connector = actOutputPosition.getDirection().opposing();
					
					if (isOutputConnectable(machine, neighbour, connector)) {
						machine.getFollowers().add(neighbour);
					}
				}
			}
		}
	}

	private boolean isOutputConnectable(Machine machine, Machine neighbour, Direction connectorDirection) {
		
		boolean notConnected 			= !machine.getFollowers().contains(neighbour);
		boolean isNotSelf 				= machine != neighbour;
		boolean neighbourInputsMatch 	= neighbour.hasMatchingInput(connectorDirection);
		
		return notConnected && isNotSelf && neighbourInputsMatch;
	}

	private boolean isInputConnectable(Machine machine, Machine neighbour, Direction connectorDirection) {
		
		boolean notConnected 			= !machine.getPredecessors().contains(neighbour);
		boolean isNotSelf 				= machine != neighbour;
		boolean neighbourOutputsMatch 	= neighbour.hasMatchingOutput(connectorDirection);
		
		return notConnected && isNotSelf && neighbourOutputsMatch;
	}
	
	
	/** Überprüft, ob die Position p mit der Breite und Höhe der Area verträglich sind. **/
	private boolean isInBounds(Position p) { return p.getX() < width && p.getY() < height && p.getX() >= 0 && p.getY() >= 0; }
	

	/** Gibt die Maschine an Position p zurück (oder null falls p nicht mit Area verträglich ist). 
	 * @throws AreaPositioningException **/
	public Machine getMachine(Position position) throws AreaPositioningException {
		if (isInBounds(position)){
			return area.get(position.getY()).get(position.getX());
		} else throw new AreaPositioningException();
	}
	
	/** Gibt die zuvor an Position p platzierte Maschine zurück und ersetzt diese mit der Maschine m.
	 *  Falls auf p innerhalb der Area nicht zugegriffen werden kann, wird eine Exception geworfen. 
	 *  Falls auf p schon eine Maschine ist, wird ebenfalls eine Exception geworfen. **/
	public Machine setMachine(Machine newm, Position p) throws AreaPositioningException{
		if (isInBounds(p)) {
			if (newm != null) {
				newm.setPosition(p);
			}
			Machine m = area.get(p.getY()).get(p.getX());
			if (m != null) {
				throw new AreaPositioningException();
			}
			return area.get(p.getY()).set(p.getX(), newm);
		} else throw new AreaPositioningException();
	}

	public Machine globalConnections() {
		for (Machine actMachine : this) {
			try {
				connectNeighbours(actMachine);
			} catch (AreaPositioningException e) {
				return actMachine;
			}
		}
		return null;
	}
	
	public boolean fieldIsEmpty(Position position) {
		try {
			return getMachine(position) == null;
		} catch (Exception e) { return false; } 
	}

	public boolean moveMachine(Machine machine, int newX, int newY) throws AreaPositioningException {
		Position oldMachinePosition = machine.getPosition();
		Position newMachinePosition = new Position(newX,newY,oldMachinePosition.getDirection());
		if (fieldIsEmpty(newMachinePosition)) {
			delete(oldMachinePosition);
			setMachine(machine,newMachinePosition);
			return true;
		}
		return false;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void show() { System.out.println(toString()); }
	
	@Override 
	public String toString() {
		Machine m = null;
		List<Product> products = null;
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				try {
					m = getMachine(new Position(x,y));
					if (m != null) {
						products = m.getProducts();
						if (!products.isEmpty()) {
							sb.append(products.size()+" ");
						} else {
							machineToChar(m, sb);
							
						}
						continue;
					}
					sb.append("· ");
				} catch (AreaPositioningException ape) { /* sollte besser nicht passieren */ }
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	private void machineToChar(Machine m, StringBuilder sb) {
		if (m.getPosition().getDirection() == Direction.NORTH) {
			sb.append("^ ");
		}
		else if (m.getPosition().getDirection() == Direction.EAST) {
			sb.append("> ");
		}
		else if (m.getPosition().getDirection() == Direction.SOUTH) {
			sb.append("v ");
		}
		else if (m.getPosition().getDirection() == Direction.WEST) {
			sb.append("< ");
		}
		else {
			sb.append("X ");
		}
	}

	@Override
	/* der Iterator geht feldweise durch, nicht maschinenweise (vielfeldige Maschinen erscheinen mehrfach)! */
	public Iterator<Machine> iterator() {
		return new Iterator<Machine>() {
			int currentX = 0;
			int currentY = 0;
			
			@Override
			public boolean hasNext() {
				while (currentY < height) {
					while (currentX < width) {
						if (area.get(currentY).get(currentX) != null) {
							++currentX;
							return true;
						}
						++currentX;
					}
					currentX = 0;
					++currentY;
				}
				return false;
			}

			@Override
			public Machine next() {
				return area.get(currentY).get(currentX-1);
			}

			@Override
			public void remove() {}
		};
	}
	
	@Override
	public Object clone() { throw new UnsupportedOperationException(); }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + height;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Area))
			return false;
		Area other = (Area) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	/**@return true wenn es eine Maschine gab, false wenn nicht
	 * @throws AreaPositioningException wenn out of bounds */
	public boolean delete(Position p) throws AreaPositioningException {
		if (isInBounds(p)) {
			if (area.get(p.getY()).get(p.getX()) == null) {
				return false;
			}
			area.get(p.getY()).set(p.getX(), null);
			return true;
		}
		throw new AreaPositioningException("Dieses Feld gibt es gar nicht.");
	}
}