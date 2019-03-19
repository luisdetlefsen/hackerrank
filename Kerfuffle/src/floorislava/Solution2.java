package floorislava;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import static misc.Performance.*;

/**
 *
 * @author luisdetlefsen
 */
public class Solution2 {

    private Scanner scanner = new Scanner(System.in);
    private final DecimalFormat df = new DecimalFormat("#.####");
    private final boolean debug = false;
    private final boolean printPerformance = false;

    private final double MAX_HOP_DISTANCE = 1.0d;
    private int MAX_HOPS = 0;
    private final boolean ROUND_DECIMALS = false;
    private final boolean ROUND_EXPERIMENTAL = true; //it is more accurate

    private final boolean OPTIMIZATION_MOVE_UP_ONLY = false; //doesn't work in all cases
    private final boolean OPTIMIZATION_USE_MAGIC = false; //magic is not real
    private final boolean OPTIMIZATION_REMEMBER_PATHS_VISITED = true;
    int shortestPathFound = Integer.MAX_VALUE;
    private final boolean PRINT_TOTAL_ITERATIONS = false;

    private int totalIterations = 0;

    final Map<Integer, Map<Integer, Integer>> pathsTraveledWeight = new HashMap<>();

    public Solution2() {
        df.setRoundingMode(RoundingMode.HALF_UP);
    }

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

        if (ROUND_EXPERIMENTAL) {
            return Math.round(v * 10000.0) / 10000.0;
        }
        return ROUND_DECIMALS ? Double.parseDouble(df.format(v)) : v;
    }

    private double generateOut(double r) {
        double v = r / 4294967296d;

        if (ROUND_EXPERIMENTAL) {
            return Math.round(v * 10000.0) / 10000.0;
        }
        return ROUND_DECIMALS ? Double.parseDouble(df.format(v)) : v;
    }

    public double[] generatePostCoordinates(double w, double l, double s, int c) {
        if (printPerformance) {
            startCounting();
        }
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

        if (printPerformance) {
            System.out.println("Generate post coordinates: " + stopCountingStr());
        }
        return r;
    }

    private int findShortestPath(final Node n, int i, int l, final HashSet<Integer> nodesToIgnore) {
        totalIterations++;
        if (debug) {
            System.out.println("===Finding shortest path from node " + n.id);
            n.print();
        }

        if (l - n.y <= MAX_HOP_DISTANCE) { //can it reach the other side?
            if (debug) {
                System.out.println("Reached the other side in " + (i + 1) + " hops.");
            }
            return i + 1;
        }

        if (i >= shortestPathFound || i >= MAX_HOPS) {
            return Integer.MAX_VALUE;
        }

        int min = Integer.MAX_VALUE;
        for (Node ni : n.nodes) {
            if (OPTIMIZATION_REMEMBER_PATHS_VISITED) {
                Map<Integer, Integer> pt1 = pathsTraveledWeight.get(n.id);
                if (pt1 != null) {
                    Integer pt2 = pt1.get(ni.id);
                    if (pt2 != null) {
                        if (pt2 > i) {
                            pt1.replace(ni.id, i);
                        } else {
                            continue;
                        }
                    } else {
                        pt1.put(ni.id, i);
                    }

                } else {
                    Map<Integer, Integer> ptt = new HashMap<>();
                    ptt.put(ni.id, i);
                    pathsTraveledWeight.put(n.id, ptt);
                }
            }

            if (nodesToIgnore.contains(ni.id)) {
                continue;
            }

            if (OPTIMIZATION_MOVE_UP_ONLY && ni.y < n.y - 0.5d) { //Search only nodes that are closer to the other edge
                continue;
            }

            if (OPTIMIZATION_USE_MAGIC && !ni.hasMagic()) {
                continue;
            }

            nodesToIgnore.add(ni.id);
            if (debug) {
                System.out.println("Going from node " + n.id + " to node " + ni.id);
            }
            min = findShortestPath(ni, i + 1, l, nodesToIgnore);
            if (min < shortestPathFound) {
                shortestPathFound = min;
            }
            nodesToIgnore.remove(ni.id);
        }
        if (debug) {
            System.out.println("Returning from node " + n.id + ". Distance found: " + min);
        }
        return min;

    }

    public int solve() {
        Integer[] in = readInput();
        double w = in[0];
        double l = in[1];
        double s = in[2];
        int c = in[3];

        MAX_HOPS = (int) Math.round(Math.sqrt(c)) + (int) Math.cbrt(c) + 15;
//MAX_HOPS = 265;
//        System.out.println("Max hops: " + MAX_HOPS);

        double[] postCoords = generatePostCoordinates(w, l, s, c);

        final List<Node> startingNodes = convertPostCoordsToNodes(postCoords, l);
        if (debug) {
            System.out.println("Finding shortest path from starting nodes...");
        }

        if (printPerformance) {
            startCounting();
        }
        for (Node n : startingNodes) {
            int hops = 1;
            HashSet<Integer> nodesToIgnore = new HashSet<>();
            nodesToIgnore.add(n.id);
            hops = findShortestPath(n, hops, (int) l, nodesToIgnore);
        }
        if (printPerformance) {
            System.out.println("Solving for shortest path: " + stopCountingStr());
        }
        if (debug) {
            System.out.println("Completed");
        }

        if (shortestPathFound == Integer.MAX_VALUE) {
            shortestPathFound = -1;
        }
        System.out.println(shortestPathFound);
        if (PRINT_TOTAL_ITERATIONS) {
            System.out.println("Iterations: " + totalIterations);
        }
        return shortestPathFound;
    }

    // Enter the X coordinate, then the Y coordinate, and get a List of node ids that are in that sector
    private TreeMap<Integer, TreeMap<Integer, List<Integer>>> convertPostCoordsToSectors(double[] postsCoords) {
        final TreeMap<Integer, TreeMap<Integer, List<Integer>>> sectors = new TreeMap<>();
        //TODO: modify it so each sector can be of 0.5 or 0.2 of length
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
            sector.add(j); //add the id of the node. 
        }
        return sectors;
    }

    private List<Integer> getNodesInSector(final TreeMap<Integer, TreeMap<Integer, List<Integer>>> allSectors, double x, double y) {
        int xx = (int) x;
        int yy = (int) y;
        List<Integer> r = new ArrayList<>();
        for (int i = xx - 1; i <= xx + 1; i++) {
            for (int j = yy - 1; j <= yy + 1; j++) {
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
        if (debug) {
            System.out.println("Converting post coords to nodes...");
        }
        if (printPerformance) {
            startCounting();
        }

        List<Node> startingNodes = new ArrayList<>();
        List<Node> endingNodes = new ArrayList<>(); //not used. Just to remind me that there are ending nodes

        if (printPerformance) {
            startCounting();
        }
        TreeMap<Integer, TreeMap<Integer, List<Integer>>> sectors = convertPostCoordsToSectors(postsCoords);
        if (printPerformance) {
            System.out.println("Converted to sectors: " + stopCountingStr());
        }

        Map<Integer, Node> allNodes = new HashMap<>();
        for (int i = 0, j = 0; i < postsCoords.length; i += 2, j++) {
            Node node = new Node();
            node.id = j;
            node.x = postsCoords[i];
            node.y = postsCoords[i + 1];

            if (l - node.y <= MAX_HOP_DISTANCE) {
                if (debug) {
                    System.out.println("Found magic in node " + node.id);
                }
                node.magic.isReal = true;
            }

            List<Integer> nodesNearSector = getNodesInSector(sectors, node.x, node.y);
            List<Node> tmpNodes = new ArrayList<>();
            for (Integer nns : nodesNearSector) {
                Node nns1 = allNodes.get(nns);
                if (nns1 == null) {
                    continue;
                }
                tmpNodes.add(allNodes.get(nns));
            }

            Iterator<Node> it = tmpNodes.iterator();
            boolean first = true;
            while (it.hasNext()) {
                Node n = it.next();
                if (calculateDistanceBetweenPoints(n.x, n.y, node.x, node.y) <= MAX_HOP_DISTANCE) {
                    boolean b2 = node.nodes.add(n);
                    boolean b1 = n.nodes.add(node);

                    if (OPTIMIZATION_USE_MAGIC && first) {
                        first = false;
                        node.magic = n.magic;
                        if (l - node.y <= MAX_HOP_DISTANCE) {
                            node.magic.isReal = true;
                            for (Node nn : n.nodes) {
                                nn.magic.isReal = true;
                            }
                        }

                    }

                    if (debug && (!b1 || !b2)) {
                        if (!b1) { //There is already a node with the same coordinates. It happens.
                            System.err.println("Could not add node " + node.id + " to node " + n.id);
                        } else {
                            System.err.println("Could not add node " + n.id + " to node " + node.id);
                        }

                        if (debug) {
                            n.print();
                            node.print();
                        }

                    }
                }
            }
            allNodes.put(node.id, node);

            if (node.y <= MAX_HOP_DISTANCE) {
                startingNodes.add(node);
            }
        }
        if (debug) {
            System.out.println("Completed");
        }

        if (OPTIMIZATION_USE_MAGIC) {
            for (int i = 0; i < allNodes.size(); i++) {
                for (Node n : allNodes.values()) {

                    if (!n.magic.isReal) {
                        n.magic.isReal = n.hasMagic();
                    }
                    if (debug) {
                        n.print();
                    }
                }
            }
        }
        if (debug) {
            System.out.println("=============");
        }
        if (printPerformance) {
            System.out.println("Converting post coords to nodes: " + stopCountingStr());
        }
        return startingNodes;
    }

    public double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    private Long generatePathTraveled(int id1, int id2) {
        return id1 * 10_000_000l + id2;
    }

}

class Node implements Comparable<Node> {

    public Integer id;
    public double x, y;
    public TreeSet<Node> nodes = new TreeSet<>();
    public Magic magic = new Magic(); //Magic is not real :( Now it is used to identify the posts closer to the edges.

    public boolean hasMagic() {
        if (magic.isReal) {
            return true;
        }
        for (Node n : nodes) {
            if (n.magic.isReal) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Node o) {
        if (this.y == o.y && this.x == o.x) {
            return 0;
        }

        return this.y - o.y < 0d ? 1 : -1;
    }

    public void print() {
        System.out.println(id + "(" + x + "," + y + ")" + (hasMagic()));
        for (Node n : nodes) {
            System.out.println("    " + n.id + "(" + n.x + "," + n.y + ")" + n.hasMagic());
        }
    }

}

class Magic {

    public boolean isReal = false;
}
