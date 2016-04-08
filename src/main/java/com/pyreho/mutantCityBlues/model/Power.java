package com.pyreho.mutantCityBlues.model;

import java.util.ArrayList;
import java.util.List;

public class Power {

	private final String name;
	
	private final List<Power> correlatedPowers;
	
	private final List<Power> connectedPowers;
	
	private int pointsInvested;

	public Power(String name) {
		this.name = name;
		this.correlatedPowers = new ArrayList<>();
		this.connectedPowers = new ArrayList<>();
		this.pointsInvested = 0;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == this) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof Power)) {
			return false;
		}
		Power power = (Power) o;
		if (name.equals(power.getName())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Implemented following Effective Java's (by Joshua Bloch) recommendations
	 */
	@Override
	public int hashCode(){
		int result = 17;
		result = 31 * result + name.hashCode();
		return result;
	}

	public String getName() {
		return name;
	}

	public List<Power> getCorrelatedPowers() {
		return correlatedPowers;
	}

	public List<Power> getConnectedPowers() {
		return connectedPowers;
	}

	public int getPointsInvested() {
		return pointsInvested;
	}
	
	public void addConnectedPower(Power power) {
		connectedPowers.add(power);
	}
	
	public void addCorrelatedPower(Power power) {
		correlatedPowers.add(power);
	}
}
