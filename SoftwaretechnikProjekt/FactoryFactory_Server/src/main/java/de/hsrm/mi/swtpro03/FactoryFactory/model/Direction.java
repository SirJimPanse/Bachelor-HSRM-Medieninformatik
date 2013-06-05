package de.hsrm.mi.swtpro03.FactoryFactory.model;

/**
 * enum fuer die ausrichtung von maschinen und foerderbander
 *
 */

public enum Direction {
	
	NORTH, SOUTH, EAST, WEST;
	
	public Direction rotateRight() {
		switch(this) {
		case NORTH: return EAST;
		case EAST: return SOUTH;
		case SOUTH: return WEST;
		case WEST: return NORTH;
		default: return null;
		}
	}
	public Direction rotateLeft() {
		switch(this) {
		case NORTH: return WEST;
		case EAST: return NORTH;
		case SOUTH: return EAST;
		case WEST: return SOUTH;
		default: return null;
		}
	}
	
	public Direction opposing(){
		switch(this) {
		case NORTH: return SOUTH;
		case EAST: return WEST;
		case SOUTH: return NORTH;
		case WEST: return EAST;
		default: return null;
		}
	}
}
