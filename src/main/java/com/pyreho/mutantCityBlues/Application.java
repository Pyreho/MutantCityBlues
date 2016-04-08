package com.pyreho.mutantCityBlues;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pyreho.mutantCityBlues.model.Power;


public class Application {
	
	private final static Map<String, Power> POWER_GRAPH = new HashMap<>();
	
	public static void main(String[] args) {
		XSSFSheet spreadsheet = null;
		try {
		    spreadsheet = new XSSFWorkbook(new FileInputStream(new File("src/main/resources/Powers.xlsx"))).getSheetAt(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int rowNumber = 0;
		XSSFRow currentRow = spreadsheet.getRow(rowNumber);
		while (currentRow != null && currentRow.getCell(0, Row.RETURN_BLANK_AS_NULL) != null) {
			final String powerName = currentRow.getCell(0).toString();
			final Power currentPower = new Power(powerName);
			POWER_GRAPH.put(powerName, currentPower);
			rowNumber++;
			currentRow = spreadsheet.getRow(rowNumber);
		}
		
		rowNumber = 0;
		currentRow = spreadsheet.getRow(rowNumber);
		while (currentRow != null && currentRow.getCell(0, Row.RETURN_BLANK_AS_NULL) != null) {
			final Power currentPower = POWER_GRAPH.get(currentRow.getCell(0).toString());
			if (currentRow.getCell(1, Row.RETURN_BLANK_AS_NULL) != null) {
				final String[] connectedPowers = currentRow.getCell(1).toString().split(";");
				for (String power : connectedPowers) {
					currentPower.addConnectedPower(POWER_GRAPH.get(power));
				}
			}
			if (currentRow.getCell(2, Row.RETURN_BLANK_AS_NULL) != null) {
				final String[] correlatedPowers = currentRow.getCell(2).toString().split(";");
				for (String power : correlatedPowers) {
					currentPower.addCorrelatedPower(POWER_GRAPH.get(power));
				}
			}
			rowNumber++;
			currentRow = spreadsheet.getRow(rowNumber);
		}
		
		final List<Power> shortestPath = findShortestPath(new Power("force field"), new Power("command mammals"));
		for (Power power : shortestPath) {
			System.out.println(power.getName());
		}
		System.out.println("The cost of buying the those two abilities only (leapfrogging everything else) is: " + minimumCostOfPath(shortestPath));
	}
	
	public static Map<String, Power> getPowerGraph() {
		return POWER_GRAPH;
	}
	
	public static Power getPowerFromGraph(String name) {
		return getPowerGraph().get(name);
	}
	
	/**
	 * Cost of path assuming you leapfrog all abilities in between the first and last powers
	 * of the list, and buy only one point from those two.
	 * 
	 * @param powers list of powers of which you want to buy the two extremes and leapfrog
	 * 			everything in between
	 * @return cost of buying one point in the first and last abilities of the list provided
	 * 			as parameter. Returns -1 if the list provided is not valid.
	 */
	public static int minimumCostOfPath(List<Power> powers) {
		//Cost starts at 8 because we have to buy the first and last abilities of the list
		int accumulatedCost = 8;
		Iterator<Power> iterator = powers.iterator();
		Power currentPower = iterator.next();
		while (iterator.hasNext()) {
			Power nextPower = iterator.next();
			if (currentPower.getCorrelatedPowers().contains(nextPower)) {
				accumulatedCost += 4;
			} else if (!currentPower.getConnectedPowers().contains(nextPower)) {
				//Powers in the list are not connected!
				return -1;
			}
			if (iterator.hasNext()) {
				// For leapfrogging
				accumulatedCost += 2;
				currentPower = nextPower;
			}
		}
		
		return accumulatedCost;
	}
	
	/**
	 * Finds the shortest path between two powers
	 * 
	 * @param power1 First power in the chain
	 * @param power2 Last power in the chain
	 * @return A list including the first power (parameter 1), all nodes between it and the
	 * 			second power (parameter 2) and the second power (parameter 2) itself. They are presented
	 * 			in that exact order.
	 */
	public static List<Power> findShortestPath(Power power1, Power power2) {
		final Map<Power,Integer> distances = new HashMap<>();
		final Map<Power,List<Power>> previousNodes = new HashMap<>();
		final Set<Power> unvisitedPowers = new HashSet<>();
		for (Power power : Application.POWER_GRAPH.values()) {
			distances.put(power, Integer.MAX_VALUE);
			previousNodes.put(power, new LinkedList<Power>());
			unvisitedPowers.add(power);
		}
		Power currentPower = POWER_GRAPH.get(power1.getName());
		distances.put(currentPower, 0);
		while (!currentPower.equals(power2)) {
			updateNeighbours(distances, previousNodes, currentPower, currentPower.getConnectedPowers());
			updateNeighbours(distances, previousNodes, currentPower, currentPower.getCorrelatedPowers());
			unvisitedPowers.remove(currentPower);
			currentPower = POWER_GRAPH.get(power2.getName());;
			for (Power newPower : unvisitedPowers) {
				if (distances.get(newPower) < distances.get(currentPower)) {
					currentPower = newPower;
				}
			}
		}
		previousNodes.get(currentPower).add(currentPower);
		return previousNodes.get(currentPower);
	}

	private static void updateNeighbours(Map<Power, Integer> distances, Map<Power, List<Power>> previousNodes, Power currentPower, List<Power> neighbours) {
		for (Power neighbour : neighbours) {
			final int newTentativeValue = distances.get(currentPower) + 2;
			if (newTentativeValue < distances.get(neighbour)) {
				distances.put(neighbour, newTentativeValue);
				previousNodes.get(neighbour).clear();
				previousNodes.get(neighbour).addAll(previousNodes.get(currentPower));
				previousNodes.get(neighbour).add(currentPower);
			}
		}
	}

}
