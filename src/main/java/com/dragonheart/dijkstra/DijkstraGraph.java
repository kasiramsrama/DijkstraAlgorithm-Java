package com.dragonheart.dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class DijkstraGraph {
	private ArrayList<Edge> listOfEdges;
	private ArrayList<Point> listOfPoints, sourcePoints;

	public DijkstraGraph() {
		this.listOfEdges = new ArrayList<Edge>();
		this.listOfPoints = new ArrayList<Point>();
		this.sourcePoints = new ArrayList<Point>();
	}

	public void addPoint(Point point) {
		listOfPoints.add(point);
	}

	public void addEdge(Edge edge) {
		listOfEdges.add(edge);
	}

	/**
	 * Add a Point onto the list of sources with a starting cost
	 * @param point is a Point that is the source you want to add
	 * @param cost is a Double with what this source starts at
	 */
	public void addSource(Point point, Double cost) {
		if(listOfPoints.contains(point)) {
			point.aggregateCost = cost;
			sourcePoints.add(point);
		}
	}

	/**
	 * Add a Point onto the list found by the xyz coord with a starting cost
	 * @param x
	 * @param y
	 * @param z
	 * @param cost is a Double with what this source starts at
	 */
	public void addSource(int x, int y, int z, Double cost) {
		for(Point point : listOfPoints) {
			if(point.X == x && point.Y == y && point.Z == z) {
				addSource(point, cost);
			}
		}
	}

	/**
	 * Add a Point onto the list found by the xy coord with a starting cost
	 * @param x
	 * @param y
	 * @param cost is a Double with what this source starts at
	 */
	public void addSource(int x, int y, Double cost) {
		addSource(x, y, 0, cost);
	}

	/**
	 * Add a Point onto the list
	 * @param point is a Point that is the source you want to add
	 */
	public void addSource(Point point) {
		if(listOfPoints.contains(point)) {
			sourcePoints.add(point);
		}
	}

	/**
	 * Add a Point onto the list found by the xyz coord
	 * @param x
	 * @param y
	 * @param z
	 */
	public void addSource(int x, int y, int z) {
		for(Point point : listOfPoints) {
			if(point.X == x && point.Y == y && point.Z == z) {
				addSource(point);
			}
		}
	}

	/**
	 * Add a Point onto the list found by the xy coord
	 * @param x
	 * @param y
	 */
	public void addSource(int x, int y) {
		addSource(x, y, 0);
	}

	/**
	 * Add a list of Points to the list of sources all with the specified starting cost
	 * @param points is a List<Point> containing the points you want to add to your sources
	 * @param cost is a Double with what these sources start at
	 */
	public void addSources(List<Point> points, Double cost) {
		for(Point point : points) {
			addSource(point, cost);
		}
	}

	/**
	 * Sets your source Point to this Point and cost
	 * @param point is the Point that is the source you want
	 * @param cost is a Double with what this source starts at
	 */
	public void setSource(Point point, Double cost) {
		sourcePoints = new ArrayList<Point>();
		addSource(point, cost);
	}

	/**
	 * Sets your source Point to a point determined by an xyz coord at cost 
	 * @param x
	 * @param y
	 * @param z
	 * @param cost is a Double with what this source starts at
	 */
	public void setSource(int x, int y, int z, Double cost) {
		sourcePoints = new ArrayList<Point>();
		addSource(x, y, z, cost);
	}

	/**
	 * Sets your source Point to a point determined by an xy coord at cost
	 * @param x
	 * @param y
	 * @param cost is a Double with what this source starts at
	 */
	public void setSource(int x, int y, Double cost) {
		setSource(x, y, 0, cost);
	}

	/**
	 * Sets your source Point to this Point
	 * @param point is the Point that is the source you want
	 */
	public void setSource(Point point) {
		sourcePoints = new ArrayList<Point>();
		addSource(point, point.aggregateCost);
	}

	/**
	 * Sets your source Point to a point determined by an xyz coord
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setSource(int x, int y, int z) {
		sourcePoints = new ArrayList<Point>();
		addSource(x, y, z);
	}

	/**
	 * Sets your source Point to a point determined by an xy coord
	 * @param x
	 * @param y
	 */
	public void setSource(int x, int y) {
		setSource(x, y, 0);
	}

	/**
	 * Sets your source Points to these Points and cost
	 * @param points is a List<Point> containing the points you want to set your sources to
	 * @param cost is a Double with what these sources start at
	 */
	public void setSources(List<Point> points, Double cost) {
		sourcePoints = new ArrayList<Point>();
		addSources(points, cost);
	}

	private List<Point> getListOfVisitedPoints() {
		ArrayList<Point> listOfVisitedPoints = new ArrayList<Point>();
		for(Point point : listOfPoints) {
			if(point.isVisited()) {
				listOfVisitedPoints.add(point);
			}
		}
		return listOfVisitedPoints;
	}

	private PriorityQueue<Edge> getConnectedEdges(Point startpoint) {
		PriorityQueue<Edge> connectedEdges = new PriorityQueue<Edge>();
		for(Edge edge : listOfEdges) {
			Point otherPoint = edge.getOtherPoint(startpoint);
			if(otherPoint != null && !otherPoint.isVisited()) {
				connectedEdges.add(edge);
			}
		}
		return connectedEdges;
	}

	private Point getNextBestPoint() {
		Point nextBestPoint = null;
		for(Point visitedPoint : getListOfVisitedPoints()) {
			PriorityQueue<Edge> connectedEdges = getConnectedEdges(visitedPoint);
			while(connectedEdges.size() > 0) {
				Edge connectedEdge = connectedEdges.remove();
				Point otherPoint = connectedEdge.getOtherPoint(visitedPoint);
				if(otherPoint.aggregateCost == null ||
						(visitedPoint.aggregateCost + connectedEdge.cost) < otherPoint.aggregateCost) {
					otherPoint.aggregateCost = visitedPoint.aggregateCost + connectedEdge.cost;
					otherPoint.edgeWithLowestCost = connectedEdge;
				}
				if(nextBestPoint == null || otherPoint.aggregateCost < nextBestPoint.aggregateCost) {
					nextBestPoint = otherPoint;
				}
			}
		}
		return nextBestPoint;
	}

	private void performCalculationForAllPoints() {
		Point currentPoint = null;
		do {
			currentPoint = getNextBestPoint();
			currentPoint.setVisited();
		} while(Point.TotalVisited < listOfPoints.size());
	}

	/**
	 * processes the whole graph
	 * @return If no sourcePoints return false, otherwise return true
	 */
	public boolean processGraph() {
		if(sourcePoints.isEmpty()) {
			return false;
		}
		for(Point point : listOfPoints) {
			point.resetVisited();
			point.edgeWithLowestCost = null;
		}
		Point.TotalVisited = 0;
		for(Point point : sourcePoints) {
			point.setVisited();
		}
		for(Point point : listOfPoints) {
			if(!point.isVisited()) {
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
		Point.TotalVisited = 0;
		for(Point point : listOfPoints) {
			point.edgeWithLowestCost = null;
			point.resetVisited();
			if(sourcePoints.contains(point)) {
				point.setVisited();
			}
		}
		for(Point point : listOfPoints) {
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
	public List<Point> getPathFrom(Point targetpoint) {
		ArrayList<Point> shortestPath = new ArrayList<Point>();
		if(targetpoint != null) {
			Point currentPoint = targetpoint;
			shortestPath.add(currentPoint);
			while(!sourcePoints.contains(currentPoint)) {
				if(currentPoint.edgeWithLowestCost != null) {
					currentPoint = currentPoint.edgeWithLowestCost.getOtherPoint(currentPoint);
					shortestPath.add(currentPoint);
				} else {
					ArrayList<Point> connectedPoints = new ArrayList<Point>();
					for(Edge edge : listOfEdges) {
						Point otherPoint = edge.getOtherPoint(currentPoint);
						if(otherPoint != null && !shortestPath.contains(otherPoint)) {
							connectedPoints.add(otherPoint);
						}
					}
					if(connectedPoints.isEmpty()) {
						break;
					}
					Point newPoint = connectedPoints.get(0);
					connectedPoints.remove(0);
					for(Point point : connectedPoints) {
						if(point.aggregateCost < newPoint.aggregateCost) {
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
