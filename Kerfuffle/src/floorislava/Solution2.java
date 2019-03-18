package floorislava;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

/**
 *
 * @author luisdetlefsen
 */
public class Solution2 {
//0.067s, 0.02s   || 0.017
    private Scanner scanner = new Scanner(System.in);
    private final DecimalFormat df = new DecimalFormat("#.####");
    private final boolean debug = false;

    private final double MAX_HOP_DISTANCE = 1.0d;
    private final int MAX_HOPS = 262;
    private final boolean ROUND_DECIMALS = false;
    private final boolean ROUND_EXPERIMENTAL = true; //it is more accurate

    private final boolean OPTIMIZATION_MOVE_UP_ONLY = false; //doesn't work in all cases
    private final boolean OPTIMIZATION_USE_MAGIC = false; //magic is not real
    private final boolean OPTIMIZATION_REMEMBER_PATHS_VISITED = true;
    int shortestPathFound = Integer.MAX_VALUE;

//    long t1 = w* 100_000_000_000_000l+ i * 10_000_000l + i2;
    private final HashSet<Long> pathsTraveled = new HashSet<>();

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

    private int findShortestPath(final Node n, int i, int l, final HashSet<Integer> nodesToIgnore) {
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
                Long pt = generatePathTraveled(n.id, ni.id, i + 1);
                if (debug) {
                    System.out.println("Traveled " + pt);
                }
                if (pathsTraveled.contains(pt)) {
                    continue;
                }

                pathsTraveled.add(pt);
            }

            if (nodesToIgnore.contains(ni.id)) {
                continue;
            }
            
            if (OPTIMIZATION_MOVE_UP_ONLY && ni.y < n.y) { //Search only nodes that are closer to the other edge
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

        double[] postCoords = generatePostCoordinates(w, l, s, c);

        List<Node> startingNodes = convertPostCoordsToNodes(postCoords, l);
        if (debug) {
            System.out.println("Finding shortest path from starting nodes...");
        }

        for (Node n : startingNodes) {
            int hops = 1;
            HashSet<Integer> nodesToIgnore = new HashSet<>();
            nodesToIgnore.add(n.id);
            hops = findShortestPath(n, hops, (int) l, nodesToIgnore);
        }
        if (debug) {
            System.out.println("Completed");
        }

        if (shortestPathFound == Integer.MAX_VALUE) {
            shortestPathFound = -1;
        }
        System.out.println(shortestPathFound);
        return shortestPathFound;
    }

    private List<Node> convertPostCoordsToNodes(double[] postsCoords, double l) {
        if (debug) {
            System.out.println("Converting post coords to nodes...");
        }

        List<Node> startingNodes = new ArrayList<>();
        List<Node> endingNodes = new ArrayList<>(); //not used. Just to remind me that there are ending nodes

        List<Node> allNodes = new ArrayList<>();
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

            Iterator<Node> it = allNodes.iterator();
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
                        

//                        throw new RuntimeException("Could not insert node");
//                        System.err.println("COULD NOT INSERT NODE!");
                    }
                }
            }
            allNodes.add(node);

            if (node.y <= MAX_HOP_DISTANCE) {
                startingNodes.add(node);
            }
        }
        if (debug) {
            System.out.println("Completed");
        }

        if (OPTIMIZATION_USE_MAGIC) {
            for (int i = 0; i < allNodes.size(); i++) {
                for (Node n : allNodes) {

                    if (!n.magic.isReal) {
                        n.magic.isReal = n.hasMagic();
                    }
                    if (debug) {
                        n.print();
                    }
                }
            }
        }

        System.out.println("=============");
        return startingNodes;
    }

    public double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    private Long generatePathTraveled(int id1, int id2, int w) {
        return w * 100_000_000_000_000l + id1 * 10_000_000l + id2;
    }

}

class Node implements Comparable<Node> {

    public int id;
    public double x, y;
    public TreeSet<Node> nodes = new TreeSet<>();
    public Magic magic = new Magic();

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

    @Override
    public int hashCode() {
//        int hash = 7;
//        hash = 67 * hash + this.id;
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        return this.id == other.id;
    }

    public boolean equals(Node o) {
        Node n = (Node) o;
        return this.id == n.id;
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
