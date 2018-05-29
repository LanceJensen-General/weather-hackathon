package com.att.cdo.weather.quadtree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import com.att.cdo.trait.Logging;
import com.att.cdo.util.ReflectionUtils;
import com.att.cdo.weather.spatial.ImmutablePoint;
import com.att.cdo.weather.spatial.Point;

public class QuadTreeTest implements Logging {
	
	private static QuadTree quadTree;
	private static Point treeCenter;

	@BeforeClass
	public static void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, SpaceTimeViolation {
		List<Point> gridPoints = getGridPoints();
		
		quadTree = new QuadTree();
		Random random = new Random();
		while(!gridPoints.isEmpty()) {
			Point randomPoint = gridPoints.get(random.nextInt(gridPoints.size()));
			gridPoints.remove(randomPoint);
			quadTree.addPoint(randomPoint);
		}
		QuadNode root = (QuadNode) ReflectionUtils.getObjectField(quadTree, "root");
		SquareBounds bounds = (SquareBounds) ReflectionUtils.getObjectField(root, "areaBounds");
		double x = (Double) ReflectionUtils.getObjectField(bounds, "centerX");
		double y = (Double) ReflectionUtils.getObjectField(bounds, "centerY");
		treeCenter = new ImmutablePoint(x,y);
	}

	@Test
	public void test() {
		// Given
		int requiredNeighbors = 10;
		List<Point> nearestPoints = getNearestPointsTo(treeCenter,requiredNeighbors);
		// When
		List<NeighborDistance> aproximatedNearestPoints = quadTree.findNearestNeighbors(treeCenter, requiredNeighbors, true);
		// Then
		int neighborsFound = 0;
		getLogger().info("Finding the nearest neighbors to " + treeCenter.toString());
		for(NeighborDistance distanceMeasure : aproximatedNearestPoints) {
			Point nearbyPoint = distanceMeasure.getNeighbor();
			if(nearestPoints.contains(nearbyPoint)) {
				int rank = nearestPoints.indexOf(nearbyPoint) +1;
				getLogger().info("Found the " + rank + " closest point: " + nearbyPoint.toString() + ".");
				neighborsFound++;
			} else {
				getLogger().warning("Quadtree returned point that was not a nearest neighbor: " + nearbyPoint.toString() + ".");
			}
		}
		getLogger().info("Printing truth set of nearest points:");
		for(Point currentPoint : nearestPoints) {
			getLogger().info(new NeighborDistance(treeCenter,currentPoint).toString());
		}
		assertTrue(neighborsFound >= requiredNeighbors-1);
	}

	private static List<Point> getGridPoints() {
		List<Point> gridPoints = new ArrayList<Point>(121);
		
		for(int x = -5; x < 6; x++) {
			for(int y = -5; y < 6; y++) {
				gridPoints.add(new ImmutablePoint(x,y));
			}
		}
		return gridPoints;
	}

	private List<Point> getNearestPointsTo(Point pointOfIntrest, int requiredNeighbors) {
		List<Point> gridPoints = getGridPoints();
		List<NeighborDistance> distances = new ArrayList<NeighborDistance>(121);
		for(Point gridPoint : gridPoints) {
			distances.add(new NeighborDistance(pointOfIntrest, gridPoint));
		}
		Collections.sort(distances);
		List<Point> nearestPoints = new ArrayList<Point>(requiredNeighbors);
		for(int neighborIndex = 0; neighborIndex < requiredNeighbors; neighborIndex++) {
			nearestPoints.add(distances.get(neighborIndex).getNeighbor());
		}
		return nearestPoints;
	}

}
