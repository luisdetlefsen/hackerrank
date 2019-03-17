package floorislava;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

/**
 *
 * @author luisdetlefsen
 */
public class Solution2 {

    private Scanner scanner = new Scanner(System.in);
    private final DecimalFormat df = new DecimalFormat("#.##########");
    private final boolean debug = false;

    private final double MAX_HOP_DISTANCE = 1.0d;
    final private int MAX_HOPS = 143;
    private final boolean ROUND_DECIMALS = true;

    public Solution2() {
        df.setRoundingMode(RoundingMode.DOWN);
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
        return ROUND_DECIMALS ? Double.parseDouble(df.format(v)) : v;
    }

    private double generateOut(double r) {
        double v = r / 4294967296d;
        return ROUND_DECIMALS ? Double.parseDouble(df.format(v)) : v;
    }

    private double[] generatePostCoordinates(double w, double l, double s, int c) {
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

//        for (int i = 0; i < r.length; i+=2) {
//            
//            System.out.print(r[i]+", "+r[i+1]);
//            System.out.println("");
//        }
//        r = new double[]{0.7094, 1.4772,1.5127, 2.8195,0.1516, 1.4781,2.3243, 2.2248,0.0495, 2.5570,0.7514, 1.6895,1.7721, 3.3477,0.7052, 3.9234,2.5827, 1.3075,2.0478, 2.1258,0.6478, 0.4181,0.2116, 1.2755,0.3894, 2.8290,1.7487, 1.5172,0.7888, 3.0036,2.7921, 0.7091};
        return r;
    }

    int shortestPathFound = Integer.MAX_VALUE;

    private int findShortestPath(Node n, int i, int l, List<Node> nodesToIgnore) {
        if (debug) {
            System.out.println("===Finding shortest path from node");
            n.print();
        }
        if (l - n.y <= MAX_HOP_DISTANCE) { //can it reach the other side?
            return i + 1;
        }

        if (i > shortestPathFound || i > MAX_HOPS) {
            return Integer.MAX_VALUE;
        }

//        for(Node ni: nodesToIgnore){
//            if(ni.id == n.id)
//                return Integer.MAX_VALUE;
//        }
        int min = Integer.MAX_VALUE;
        for (Node ni : n.nodes) {

            if (nodesToIgnore.contains(ni)) {
                continue;
            }
            nodesToIgnore.add(ni);
            min = findShortestPath(ni, i + 1, l, nodesToIgnore);
            if (min < shortestPathFound) {
                shortestPathFound = min;
            }
            nodesToIgnore.remove(ni);
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

        List<Node> startingNodes = convertPostCoordsToNodes(postCoords);
        if (debug) {
            System.out.println("Finding shortest path from starting nodes...");
        }

        for (Node n : startingNodes) {
            int hops = 1;
            List<Node> nodesToIgnore = new ArrayList<>();
            nodesToIgnore.add(n);
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

    private List<Node> convertPostCoordsToNodes(double[] postsCoords) {
        if (debug) {
            System.out.println("Converting post coords to nodes...");
        }

        List<Node> startingNodes = new ArrayList<>();

        List<Node> allNodes = new ArrayList<>();
        for (int i = 0, j = 0; i < postsCoords.length; i += 2, j++) {
            Node node = new Node();
            node.id = j;
            node.x = postsCoords[i];
            node.y = postsCoords[i + 1];

            
            Iterator<Node> it = allNodes.iterator();
            while (it.hasNext()) {
                Node n = it.next();
//            for (int ii=0; ii< allNodes.size(); ii++) {
                if (calculateDistanceBetweenPoints(n.x, n.y, node.x, node.y) <= MAX_HOP_DISTANCE) {
                    boolean b2 = node.nodes.add(n);
                    boolean b1 = n.nodes.add(node); 
                    
                    if(!b1 || !b2)
                        System.err.println("ALSKDFJLASDJFLKASFJKLSDF");
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
        return startingNodes;
    }

    public double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

}

class Node implements Comparable<Node> {

    public int id;
    public double x, y;
    public TreeSet<Node> nodes = new TreeSet<>();
            
//            new TreeSet<>(new Comparator<Node>() {
//        @Override
//        public int compare(Node o1, Node o2) {
//            Node n1 = (Node)o1;
//            Node n2 = (Node)o2;
//            return (int)(n1.y-n2.y);
//        }
//    });

    @Override
    public int compareTo(Node o) {
//        return this.id - o.id;


        if (this.y == o.y ) return 0;

        return this.y - o.y < 0d? -1 : +1;       
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        return hash;
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
    
   
    public boolean equals(Node o){
        Node n = (Node)o;
        return this.id == n.id;
    }
    
   
    public void print() {
        System.out.println(id +"(" + x + "," + y + ")");
        for (Node n : nodes) {
            System.out.println("    "+n.id+"(" + n.x + "," + n.y + ")");
        }
    }

}
