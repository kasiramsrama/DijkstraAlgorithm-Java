import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Graph {
	private Vector2D sourceNode;
	private ArrayList<Vector2D> listOfNodes;
	private ArrayList<Edge> listOfEdges;
	public ArrayList<Vector2D> allNodes() {
		return listOfNodes;
	}
	public Vector2D sourceVector() {
		return sourceNode;
	}
	public void sourceVector(Vector2D value) {
		for(int i = 0; i < listOfNodes.size(); i++) {
			if(listOfNodes.get(i) == value) {
				sourceNode = value;
				break;
			}
		}
	}

	public Graph() {
		listOfEdges = new ArrayList<Edge>();
		listOfNodes = new ArrayList<Vector2D>();
		sourceNode = null;
	}

	private void reset() {
		for(int i = 0; i < listOfNodes.size(); i++) {
			listOfNodes.get(i).visited = false;
			listOfNodes.get(i).aggregateCost = Vector2D.INFINITY;
			listOfNodes.get(i).edgeWithLowestCost = null;
		}
	}

	public void addEdge(Edge edge) {
		listOfEdges.add(edge);
		reset();
	}

	public void addEdge(Vector2D pointa, Vector2D pointb, int cost) {
		listOfEdges.add(new Edge(pointa, pointb, cost));
		reset();
	}

	public void addVector(Vector2D node) {
		listOfNodes.add(node);
		reset();
	}

	public void addVector(int x, int y, boolean deadend) {
		listOfNodes.add(new Vector2D(x, y, deadend));
		reset();
	}

	private ArrayList<Vector2D> getListOfVisitedNodes() {
		ArrayList<Vector2D> listOfVisitedNodes = new ArrayList<Vector2D>();
		for(Vector2D node : listOfNodes) {
			if(node.visited) {
				listOfVisitedNodes.add(node);
			}
		}
		return listOfVisitedNodes;
	}

	private boolean moreVisitedNodes() {
		return getListOfVisitedNodes().size() < listOfNodes.size();
	}

	private PriorityQueue<Edge> getConnectedEdges(Vector2D startnode) {
		PriorityQueue<Edge> connectedEdges = new PriorityQueue<Edge>();
		for(int i = 0; i < listOfEdges.size(); i++) {
			if(listOfEdges.get(i).getOtherVector(startnode) != null && !listOfEdges.get(i).getOtherVector(startnode).visited) {
				connectedEdges.add(listOfEdges.get(i));
			}
		}
		return connectedEdges;
	}

	private void performCalculationForAllNodes() {
		Vector2D currentNode = sourceNode;
		currentNode.visited = true;
		do {
			Vector2D nextBestNode = null;
			for(Vector2D visitedNode : getListOfVisitedNodes()) {
				PriorityQueue<Edge> connectedEdges = getConnectedEdges(visitedNode);
				while(connectedEdges.size() > 0) {
					Edge connectedEdge = connectedEdges.remove();
					if(connectedEdge.getOtherVector(visitedNode).aggregateCost == Vector2D.INFINITY || (visitedNode.aggregateCost + connectedEdge.cost) < connectedEdge.getOtherVector(visitedNode).aggregateCost) {
						connectedEdge.getOtherVector(visitedNode).aggregateCost = visitedNode.aggregateCost + connectedEdge.cost;
						connectedEdge.getOtherVector(visitedNode).edgeWithLowestCost = connectedEdge;
					}
					if(nextBestNode == null || connectedEdge.getOtherVector(visitedNode).aggregateCost < nextBestNode.aggregateCost) {
						nextBestNode = connectedEdge.getOtherVector(visitedNode);
					}
				}
			}
			currentNode = nextBestNode;
			currentNode.visited = true;
		} while(moreVisitedNodes());
	}

	public boolean calculateShortestPath() {
		boolean unreachable = false;
		if(sourceNode == null) {
			return false;
		}
		reset();
		sourceNode.aggregateCost = 0;
		performCalculationForAllNodes();
		if(unreachable) {
			return false;
		}
		return true;
	}

	public ArrayList<Vector2D> retrieveShortestPath(Vector2D targetnode) {
		ArrayList<Vector2D> shortestPath = new ArrayList<Vector2D>();
		if(targetnode != null) {
			Vector2D currentNode = targetnode;
			shortestPath.add(currentNode);
			while(currentNode.edgeWithLowestCost != null) {
				currentNode = currentNode.edgeWithLowestCost.getOtherVector(currentNode);
				shortestPath.add(currentNode);
			}
		}
		Collections.reverse(shortestPath);
		return shortestPath;
	}
}
