package com.dragonheart.dijkstra;

/**
 * "Edge" is a class made to represent the connection between 2 nodes in a dijkstra map
 * @author Akhier Dragonheart
 */
public class Edge implements Comparable<Edge>{
	//The three that will matter
	public Point A, B;
	public Double cost;

	//Id is purely for debugging as seen with toString being the only place it is called
	public final int edgeId;
	private static int edgeIdCount = -1;

	public Edge(Point a, Point b, Double cost) {
		this.A = a;
		this.B = b;
		this.cost = cost;
		this.edgeId = ++edgeIdCount;
	}

	/**
	 * Returns the other Point on the Edge or null if given a Point not on the Edge
	 * @param basepoint
	 * @return either null if given a Point not on the Edge or the other Point of the given one
	 */
	public Point getOtherPoint(Point basepoint) {
		if(basepoint == A) {
			return B;
		} else if (basepoint == B) {
			return A;
		} else {
			return null;
		}
	}

	@Override
	public int compareTo(Edge otheredge) {
		return cost.compareTo(otheredge.cost);
	}
}
