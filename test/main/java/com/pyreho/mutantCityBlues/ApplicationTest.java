package com.pyreho.mutantCityBlues;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.pyreho.mutantCityBlues.Application;
import com.pyreho.mutantCityBlues.model.Power;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ApplicationTest {
	
	@Test
	public void powersMapIsNotEmpty() {
		assertFalse(Application.getPowerGraph().isEmpty());
	}
	
	@Test
	public void powersConnectionsAreConsistent() {
		for (Power power : Application.getPowerGraph().values()) {
			for (Power connectedPower : power.getConnectedPowers()) {
				assertTrue(connectedPower.getConnectedPowers().contains(power));
			}
		}
	}
	
	@Test
	public void powersCorrelationsAreConsistent() {
		for (Power power : Application.getPowerGraph().values()) {
			for (Power correlatedPower : power.getCorrelatedPowers()) {
				assertTrue(correlatedPower.getCorrelatedPowers().contains(power));
			}
		}
	}
	
	@Test
	public void findShortestPathsCorrectly() {
		final List<Power> shortestPathBetweenHealingAndFlight = new LinkedList<>();
		shortestPathBetweenHealingAndFlight.add(Application.getPowerFromGraph("healing"));
		shortestPathBetweenHealingAndFlight.add(Application.getPowerFromGraph("regeneration"));
		shortestPathBetweenHealingAndFlight.add(Application.getPowerFromGraph("strenght"));
		shortestPathBetweenHealingAndFlight.add(Application.getPowerFromGraph("flight"));
		assertEquals(shortestPathBetweenHealingAndFlight,Application.findShortestPath(Application.getPowerFromGraph("healing"), Application.getPowerFromGraph("flight")));

		final List<Power> shortestPathBetweenKineticEnergyDispersalAndRegeneration = new LinkedList<>();
		shortestPathBetweenKineticEnergyDispersalAndRegeneration.add(Application.getPowerFromGraph("kinetic energy dispersal"));
		shortestPathBetweenKineticEnergyDispersalAndRegeneration.add(Application.getPowerFromGraph("limb extension"));
		shortestPathBetweenKineticEnergyDispersalAndRegeneration.add(Application.getPowerFromGraph("strenght"));
		shortestPathBetweenKineticEnergyDispersalAndRegeneration.add(Application.getPowerFromGraph("regeneration"));
		assertEquals(shortestPathBetweenKineticEnergyDispersalAndRegeneration,Application.findShortestPath(Application.getPowerFromGraph("kinetic energy dispersal"), Application.getPowerFromGraph("regeneration")));
	}
	
	@Test
	public void calculatesPathCostsCorrectly() {
		final List<Power> shortestPathBetweenHealingAndFlight = new LinkedList<>();
		shortestPathBetweenHealingAndFlight.add(Application.getPowerFromGraph("healing"));
		shortestPathBetweenHealingAndFlight.add(Application.getPowerFromGraph("regeneration"));
		shortestPathBetweenHealingAndFlight.add(Application.getPowerFromGraph("strenght"));
		shortestPathBetweenHealingAndFlight.add(Application.getPowerFromGraph("flight"));
		assertEquals(12,Application.minimumCostOfPath(shortestPathBetweenHealingAndFlight));

		final List<Power> shortestPathBetweenKineticEnergyDispersalAndRegeneration = new LinkedList<>();
		shortestPathBetweenKineticEnergyDispersalAndRegeneration.add(Application.getPowerFromGraph("kinetic energy dispersal"));
		shortestPathBetweenKineticEnergyDispersalAndRegeneration.add(Application.getPowerFromGraph("limb extension"));
		shortestPathBetweenKineticEnergyDispersalAndRegeneration.add(Application.getPowerFromGraph("strenght"));
		shortestPathBetweenKineticEnergyDispersalAndRegeneration.add(Application.getPowerFromGraph("regeneration"));
		assertEquals(16,Application.minimumCostOfPath(shortestPathBetweenKineticEnergyDispersalAndRegeneration));
	}
	
	@BeforeClass
	public static void setUp() {
		Application.main(null);
	}	
}
