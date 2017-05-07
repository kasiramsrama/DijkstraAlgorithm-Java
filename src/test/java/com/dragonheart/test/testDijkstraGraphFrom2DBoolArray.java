package com.dragonheart.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.dragonheart.dijkstra.*;

public class testDijkstraGraphFrom2DBoolArray {
	private boolean[][] boolMap;

	@Before
	public void setUp() throws Exception {
		int width = 7, height = 7;
		boolMap = new boolean[width][height];
		boolMap[0][0] = true; boolMap[1][0] = true; boolMap[2][0] = false; boolMap[3][0] = true; boolMap[4][0] = true; boolMap[5][0] = true; boolMap[6][0] = true;
		boolMap[0][1] = false; boolMap[1][1] = true; boolMap[2][1] = false; boolMap[3][1] = false; boolMap[4][1] = true; boolMap[5][1] = false; boolMap[6][1] = true;
		boolMap[0][2] = true; boolMap[1][2] = true; boolMap[2][2] = true; boolMap[3][2] = false; boolMap[4][2] = true; boolMap[5][2] = false; boolMap[6][2] = true;
		boolMap[0][3] = true; boolMap[1][3] = false; boolMap[2][3] = true; boolMap[3][3] = false; boolMap[4][3] = true; boolMap[5][3] = false; boolMap[6][3] = true;
		boolMap[0][4] = true; boolMap[1][4] = false; boolMap[2][4] = true; boolMap[3][4] = true; boolMap[4][4] = true; boolMap[5][4] = false; boolMap[6][4] = true;
		boolMap[0][5] = true; boolMap[1][5] = true; boolMap[2][5] = true; boolMap[3][5] = false; boolMap[4][5] = false; boolMap[5][5] = true; boolMap[6][5] = true;
		boolMap[0][6] = false; boolMap[1][6] = false; boolMap[2][6] = false; boolMap[3][6] = false; boolMap[4][6] = false; boolMap[5][6] = false; boolMap[6][6] = true;
	}

	@Test
	public final void testDijkstraMapFrom2DBoolArray_NoDiagonalMovement() {
		DijkstraGraph graph = dijkstraGraphFrom2DBoolArray(boolMap, 1.0);
		graph.setSource(6, 6, 0.0);
		graph.processGraph();
		List<Point> path = graph.getPathFrom(0, 0);
		Point temp = path.get(1);
		assertTrue(temp.X == 1 && temp.Y == 0);
		temp = path.get(2);
		assertTrue(temp.X == 1 && temp.Y == 1);
		temp = path.get(path.size() - 2);
		assertTrue(temp.X == 6 && temp.Y == 5);
	}

	@Test
	public final void testDijkstraMapFrom2DBoolArray_DiagonalMovement() {
		DijkstraGraph graph = dijkstraGraphFrom2DBoolArray(boolMap, 1.0, 1.0);
		graph.setSource(6, 6, 0.0);
		graph.processGraph();
		List<Point> path = graph.getPathFrom(0, 0);
		Point temp = path.get(1);
		assertTrue(temp.X == 1 && temp.Y == 0);
		temp = path.get(2);
		assertTrue(temp.X == 2 && temp.Y == 2);
		temp = path.get(path.size() - 2);
		assertTrue(temp.X == 5 && temp.Y == 5);
	}

}