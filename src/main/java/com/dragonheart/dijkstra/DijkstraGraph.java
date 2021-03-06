package com.dragonheart.dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class DijkstraGraph {
	private ArrayList<DijkstraPoint[]> listOfConnections;
	private ArrayList<DijkstraPoint> sourcePoints;
	private HashMap<DijkstraPoint, PointData> listOfPoints;
	private int totalVisited = 0;

	private class PointData {
		public Double aggregateCost;
		public DijkstraPoint pointWithLowestCost;
		public boolean visited;

		private PointData() {
			this.aggregateCost = null;
			this.pointWithLowestCost = null;
			this.visited = false;
		}
	}

	public DijkstraGraph() {
		this.listOfConnections = new ArrayList<DijkstraPoint[]>();
		this.listOfPoints = new HashMap<DijkstraPoint, PointData>();
		this.sourcePoints = new ArrayList<DijkstraPoint>();
	}

	protected void clear() {
		this.listOfConnections = new ArrayList<DijkstraPoint[]>();
		this.listOfPoints = new HashMap<DijkstraPoint, PointData>();
		this.sourcePoints = new ArrayList<DijkstraPoint>();
	}

	public void addPoint(DijkstraPoint point) {
		listOfPoints.put(point, new PointData());
	}

	public void addConnection(DijkstraPoint pointA, DijkstraPoint pointB) {
		DijkstraPoint[] connection = {pointA, pointB};
		DijkstraPoint[] otherConnection = {pointB, pointA};
		if(!listOfConnections.contains(connection) && !listOfConnections.contains(otherConnection)) {
			listOfConnections.add(connection);
		}
	}

	public void removeConnection(DijkstraPoint pointA, DijkstraPoint pointB) {
		DijkstraPoint[] connection = {pointA, pointB};
		listOfConnections.remove(connection);
		DijkstraPoint[] otherConnection = {pointB, pointA};
		listOfConnections.remove(otherConnection);
	}

	/**
	 * Add a Point onto the list of sources with a starting cost
	 * @param point is a Point that is the source you want to add
	 * @param cost is a Double with what this source starts at
	 */
	public void addSource(DijkstraPoint point, Double cost) {
		if(listOfPoints.containsKey(point)) {
			listOfPoints.get(point).aggregateCost = cost;
			sourcePoints.add(point);
		}
	}

	/**
	 * Add a Point onto the list
	 * @param point is a Point that is the source you want to add
	 */
	public void addSource(DijkstraPoint point) {
		if(listOfPoints.containsKey(point)) {
			sourcePoints.add(point);
		}
	}

	/**
	 * Add a list of Points to the list of sources all with the specified starting cost
	 * @param points is a List<Point> containing the points you want to add to your sources
	 * @param cost is a Double with what these sources start at
	 */
	public void addSources(List<DijkstraPoint> points, Double cost) {
		for(DijkstraPoint point : points) {
			addSource(point, cost);
		}
	}

	/**
	 * Sets your source Point to this Point and cost
	 * @param point is the Point that is the source you want
	 * @param cost is a Double with what this source starts at
	 */
	public void setSource(DijkstraPoint point, Double cost) {
		sourcePoints = new ArrayList<DijkstraPoint>();
		addSource(point, cost);
	}

	/**
	 * Sets your source Point to this Point
	 * @param point is the Point that is the source you want
	 */
	public void setSource(DijkstraPoint point) {
		sourcePoints = new ArrayList<DijkstraPoint>();
		addSource(point, listOfPoints.get(point).aggregateCost);
	}

	/**
	 * Sets your source Points to these Points and cost
	 * @param points is a List<Point> containing the points you want to set your sources to
	 * @param cost is a Double with what these sources start at
	 */
	public void setSources(List<DijkstraPoint> points, Double cost) {
		sourcePoints = new ArrayList<DijkstraPoint>();
		addSources(points, cost);
	}

	private DijkstraPoint getOtherPoint(DijkstraPoint basepoint, DijkstraPoint[] connection) {
		if(basepoint == connection[0]) {
			return connection[1];
		} else if (basepoint == connection[1]) {
			return connection[0];
		} else {
			return null;
		}
	}

	private List<DijkstraPoint> getListOfVisitedPoints() {
		ArrayList<DijkstraPoint> listOfVisitedPoints = new ArrayList<DijkstraPoint>();
		for(Map.Entry<DijkstraPoint, PointData> point : listOfPoints.entrySet()) {
			if(point.getValue().visited) {
				listOfVisitedPoints.add(point.getKey());
			}
		}
		return listOfVisitedPoints;
	}

	private Queue<DijkstraPoint> getConnectedPoints(DijkstraPoint startpoint) {
		Queue<DijkstraPoint> connectedPoints = new LinkedList<DijkstraPoint>();
		for(DijkstraPoint[] connection : listOfConnections) {
			DijkstraPoint otherPoint = getOtherPoint(startpoint, connection);
			if(otherPoint != null && !listOfPoints.get(otherPoint).visited) {
				connectedPoints.add(otherPoint);
			}
		}
		return connectedPoints;
	}

	private DijkstraPoint getNextBestPoint() {
		DijkstraPoint nextBestPoint = null;
		for(DijkstraPoint visitedPoint : getListOfVisitedPoints()) {
			Queue<DijkstraPoint> connectedPoints = getConnectedPoints(visitedPoint);
			while(connectedPoints.size() > 0) {
				DijkstraPoint otherPoint = connectedPoints.remove();
				if(listOfPoints.get(otherPoint).aggregateCost == null ||
						(listOfPoints.get(visitedPoint).aggregateCost + otherPoint.costToEnter()) < listOfPoints.get(otherPoint).aggregateCost) {
					listOfPoints.get(otherPoint).aggregateCost = listOfPoints.get(visitedPoint).aggregateCost + otherPoint.costToEnter();
					listOfPoints.get(otherPoint).pointWithLowestCost = visitedPoint;
				}
				if(nextBestPoint == null || listOfPoints.get(otherPoint).aggregateCost < listOfPoints.get(nextBestPoint).aggregateCost) {
					nextBestPoint = otherPoint;
				}
			}
		}
		return nextBestPoint;
	}

	private void performCalculationForAllPoints() {
		DijkstraPoint currentPoint = null;
		do {
			currentPoint = getNextBestPoint();
			if(currentPoint != null) {
				listOfPoints.get(currentPoint).visited = true;
			}
			totalVisited++;
		} while(totalVisited < listOfPoints.size());
	}

	/**
	 * processes the whole graph
	 * @return If no sourcePoints return false, otherwise return true
	 */
	public boolean processGraph() {
		if(sourcePoints.isEmpty()) {
			return false;
		}
		for(PointData point : listOfPoints.values()) {
			point.visited = false;
			point.pointWithLowestCost = null;
		}
		totalVisited = 0;
		for(DijkstraPoint point : sourcePoints) {
			listOfPoints.get(point).visited = true;
			totalVisited++;
		}
		for(PointData point : listOfPoints.values()) {
			if(!point.visited) {
				point.aggregateCost = null;
			}
		}
		performCalculationForAllPoints();
		return true;
	}

	/**
	 * processes the whole graph while keeping any pre-existing aggregateCosts
	 * @param multiplierforaggregatecosts will be a value that the aggregateCost of all points will be multiplied by
	 * @return If no sourcePoints return false, otherwise return true
	 */
	public boolean processGraph(double multiplierforaggregatecosts) {
		if(sourcePoints.isEmpty()) {
			return false;
		}
		totalVisited = 0;
		for(PointData point : listOfPoints.values()) {
			point.pointWithLowestCost = null;
			point.visited = false;
			if(sourcePoints.contains(point)) {
				point.visited = true;
				totalVisited++;
			}
		}
		for(PointData point : listOfPoints.values()) {
			point.aggregateCost = point.aggregateCost * multiplierforaggregatecosts;
		}
		performCalculationForAllPoints();
		return true;
	}

	/**
	 * Gets the path from the target to the closest source
	 * @param targetpoint is the Point you want the path to go from
	 * @return List<Point> with the path to the closest source
	 */
	public List<DijkstraPoint> getPathFrom(DijkstraPoint targetpoint) {
		ArrayList<DijkstraPoint> shortestPath = new ArrayList<DijkstraPoint>();
		if(targetpoint != null) {
			DijkstraPoint currentPoint = targetpoint;
			shortestPath.add(currentPoint);
			while(!sourcePoints.contains(currentPoint)) {
				if(listOfPoints.get(currentPoint).pointWithLowestCost != null) {
					currentPoint = listOfPoints.get(currentPoint).pointWithLowestCost;
					shortestPath.add(currentPoint);
				} else {
					ArrayList<DijkstraPoint> connectedPoints = new ArrayList<DijkstraPoint>();
					for(DijkstraPoint[] connection : listOfConnections) {
						DijkstraPoint otherPoint = getOtherPoint(currentPoint, connection);
						if(otherPoint != null && !shortestPath.contains(otherPoint)) {
							connectedPoints.add(otherPoint);
						}
					}
					if(connectedPoints.isEmpty()) {
						break;
					}
					DijkstraPoint newPoint = connectedPoints.get(0);
					connectedPoints.remove(0);
					for(DijkstraPoint point : connectedPoints) {
						if(listOfPoints.get(point).aggregateCost < listOfPoints.get(newPoint).aggregateCost) {
							newPoint = point;
						}
					}
					currentPoint = newPoint;
					shortestPath.add(currentPoint);
				}
			}
		}
		return shortestPath;
	}
}
