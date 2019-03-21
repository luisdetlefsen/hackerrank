package floorislava;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * Solution2 has too many options. This class is the same as Solution2 without
 * all the custom options
 *
 * @author luisdetlefsen
 */
public class SolutionSmall {

    private final Scanner scanner = new Scanner(System.in);

    private final double MAX_HOP_DISTANCE = 1.0d;
    private int MAX_HOPS = 0;

    Integer shortestPathFound = Integer.MAX_VALUE; //so threads can use it

    final Map<Integer, Map<Integer, Integer>> pathsTraveledWeight = new HashMap<>();

    private Integer[] readInput() {
        String[] input = scanner.nextLine().split(" ");
        Integer[] result = new Integer[4];
        result[0] = Integer.parseInt(input[0]); //width
        result[1] = Integer.parseInt(input[1]); //length
        result[2] = Integer.parseInt(input[2]); //seed
        result[3] = Integer.parseInt(input[3]); //count
        return result;
    }

    private double generateR(double r) {
        double v = (1664525d * r + 1013904223d) % 4294967296d;
        return Math.round(v * 10000.0) / 10000.0;
    }

    private double generateOut(double r) {
        double v = r / 4294967296d;
        return Math.round(v * 10000.0) / 10000.0;
    }

    public double[] generatePostCoordinates(double w, double l, double s, int c) {
        double[] r = new double[c * 2];
        double ri = s;
        double oi;
        for (int i = 0; i < c * 2; i++) {
            ri = generateR(ri);
            oi = generateOut(ri);

            if (i % 2 == 0) {
                r[i] = oi * w;
            } else {
                r[i] = oi * l;
            }
        }

        return r;
    }

    private boolean pathTraveledAlready(Node from, Node to, Integer i) {
        Map<Integer, Integer> pt1 = pathsTraveledWeight.get(from.id);
        if (pt1 != null) {
            Integer pt2 = pt1.get(to.id);
            if (pt2 != null) {
                if (pt2 > i) {
                    pt1.replace(to.id, i);
                } else {
                    return true;
                }
            } else {
                pt1.put(to.id, i);
            }
        } else {
            Map<Integer, Integer> ptt = new HashMap<>();
            ptt.put(to.id, i);
            pathsTraveledWeight.put(from.id, ptt);
        }
        return false;
    }

    private int findShortestPath(final Node n, Integer i, int l, final HashSet<Integer> nodesToIgnore) {
        if (l - n.y <= MAX_HOP_DISTANCE) { //can it reach the other side?
            return i + 1;
        }

        if (i >= shortestPathFound || i >= MAX_HOPS) {
            return Integer.MAX_VALUE;
        }

        int min = Integer.MAX_VALUE;
        for (Node ni : n.nodes) {
            if (pathTraveledAlready(n, ni, i)) {
                continue;
            }

            if (nodesToIgnore.contains(ni.id)) {
                continue;
            }

            nodesToIgnore.add(ni.id);

            min = findShortestPath(ni, i + 1, l, nodesToIgnore);
            if (min < shortestPathFound) {
                shortestPathFound = min;
            }
            nodesToIgnore.remove(ni.id);
        }

        return min;

    }

    public int solve() {
        Integer[] in = readInput();
        final double w = in[0];
        final double l = in[1];
        double s = in[2];
        int c = in[3];

        shortestPathFound = Integer.MAX_VALUE;
        MAX_HOPS = (int) Math.round(Math.sqrt(c)) + (int) Math.cbrt(c) + 15;

        double[] postCoords = generatePostCoordinates(w, l, s, c);

        final List<Node> startingNodes = convertPostCoordsToNodes(postCoords, l);

        startingNodes.parallelStream().forEach((n) -> {
            int hops = 1;

            HashSet<Integer> nodesToIgnore = new HashSet<>();
            nodesToIgnore.add(n.id);
            hops = findShortestPath(n, hops, (int) l, nodesToIgnore);
        });

        if (shortestPathFound == Integer.MAX_VALUE) {
            shortestPathFound = -1;
        }
        System.out.println(shortestPathFound);

        return shortestPathFound;
    }

    // Enter the X coordinate, then the Y coordinate, and get a List of node ids that are in that sector
    private TreeMap<Integer, TreeMap<Integer, List<Integer>>> convertPostCoordsToSectors(double[] postsCoords) {
        final TreeMap<Integer, TreeMap<Integer, List<Integer>>> sectors = new TreeMap<>();
        for (int i = 0, j = 0; i < postsCoords.length; i += 2, j++) {
            double xi = postsCoords[i];
            double yi = postsCoords[i + 1];
            int xii = (int) xi;
            int yii = (int) yi;

            TreeMap<Integer, List<Integer>> yMap = sectors.get(xii);
            if (yMap == null) {
                yMap = new TreeMap();
                sectors.put(xii, yMap);
            }

            List<Integer> sector = yMap.get(yii);
            if (sector == null) {
                sector = new ArrayList<>();
                yMap.put(yii, sector);
            }
            sector.add(j);
        }
        return sectors;
    }

    private List<Integer> getNodesInSector(final TreeMap<Integer, TreeMap<Integer, List<Integer>>> allSectors, double x, double y) {
        int xx = (int) x;
        int yy = (int) y;
        List<Integer> r = new ArrayList<>();
        for (int i = xx - 1; i <= xx + 1; i++) {
            if (xx < 0) {
                continue;
            }
            for (int j = yy - 1; j <= yy + 1; j++) {
                if (yy < 0) {
                    continue;
                }
                TreeMap<Integer, List<Integer>> r1 = allSectors.get(i);
                if (r1 == null) {
                    continue;
                }
                List<Integer> r2 = r1.get(j);
                if (r2 == null) {
                    continue;
                }
                r.addAll(r2);
            }
        }
        return r;
    }

    private List<Node> convertPostCoordsToNodes(double[] postsCoords, double l) {
        final List<Node> startingNodes = new ArrayList<>();
        final TreeMap<Integer, TreeMap<Integer, List<Integer>>> sectors = convertPostCoordsToSectors(postsCoords);

        final Map<Integer, Node> allNodes = new HashMap<>();
        for (int i = 0, j = 0; i < postsCoords.length; i += 2, j++) {
            Node node = new Node();
            node.id = j;
            node.x = postsCoords[i];
            node.y = postsCoords[i + 1];

            List<Integer> nodesNearSector = getNodesInSector(sectors, node.x, node.y);
            List<Node> tmpNodes = new ArrayList<>();
            nodesNearSector.forEach((nns) -> {
                Node nns1 = allNodes.get(nns);
                if (!(nns1 == null)) {
                    tmpNodes.add(allNodes.get(nns));
                }
            });

            Iterator<Node> it = tmpNodes.iterator();
            while (it.hasNext()) {
                Node n = it.next();
                if (calculateDistanceBetweenPoints(n.x, n.y, node.x, node.y) <= MAX_HOP_DISTANCE) {
                    node.nodes.add(n);
                    n.nodes.add(node);
                }
            }
            allNodes.put(node.id, node);

            if (node.y <= MAX_HOP_DISTANCE) {
                startingNodes.add(node);
            }
        }

        allNodes.values().forEach((ns) -> {
            Collections.sort(ns.nodes, (Node o1, Node o2) -> (int) (100 * (o2.y - o1.y)));
        });

        return startingNodes;
    }

    public double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public static void main(String[] args) {
        SolutionSmall s = new SolutionSmall();
        s.solve();
    }

}
//
//private class Node {
//
//    public Integer id;
//    public double x, y;
//    public List<Node> nodes = new ArrayList<>();
//
//}
