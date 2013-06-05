package de.hsrm.mi.swtpro03.FactoryFactory.model;

import java.io.Serializable;

/**
 * x und y zusammengefasst halt
 * Bei Fragen wenden sie sich an die Autoren
 * @author steven und tobias
 *
 */
public class Position implements Serializable{

	private static final long serialVersionUID = -2792403638668317729L;
	private int x;
	private int y;
	private Direction direction;

	public Position() {
		x = -1;
		y = -1;
		this.direction = Direction.NORTH;
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
		this.direction = Direction.NORTH;
	}
	
	public Position(int x, int y, Direction direction){
		this.direction = direction;
		this.x = x;
		this.y = y;
	}
	
	public Position(Position p, Direction newDirection){
		this.direction = newDirection;
		this.x = p.getX();
		this.y = p.getY();
	}
	
	
	
	public Position(Position p){
		this.direction = p.getDirection();
		this.x = p.getX();
		this.y = p.getY();
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public String toString(){
		return "x: " + x + " y: " + y + " direction: " + direction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		if (direction != other.direction) {
			return false;
		}
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

}
