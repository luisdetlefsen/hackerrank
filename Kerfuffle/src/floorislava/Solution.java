package floorislava;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author luisdetlefsen
 */
public class Solution {

    private final Scanner scanner = new Scanner(System.in);
    private final DecimalFormat df = new DecimalFormat("#.#####");

    private final double MAX_HOP_DISTANCE = 1d;
    private int MAX_HOPS = 5;

    public Solution() {
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

        return Double.parseDouble(df.format(v));
    }

    private double generateOut(double r) {
        double v = r / 4294967296d;
        return Double.parseDouble(df.format(v));
    }

    private double[] generatePostCoordinates(double w, double l, double s, int c) {
        double[] r = new double[c * 2];
        double ri = 1d;
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

    //w is the x axis, l is the y axis.
    private TreeMap<Integer, TreeMap<Integer, List<double[]>>> convertPostCoordsToSectors(double[] postsCoords) {
        final TreeMap<Integer, TreeMap<Integer, List<double[]>>> sectors = new TreeMap<>();
        //TODO: modify it so each sector can be of 0.5 or 0.2 of length
        for (int i = 0; i < postsCoords.length; i += 2) {
            double xi = postsCoords[i];
            double yi = postsCoords[i + 1];
            int xii = (int) xi;
            int yii = (int) yi;

            TreeMap<Integer, List<double[]>> yMap = sectors.get(xii);
            if (yMap == null) {
                yMap = new TreeMap();
                sectors.put(xii, yMap);
            }

            List<double[]> sector = yMap.get(yii);
            if (sector == null) {
                sector = new ArrayList<>();
                yMap.put(yii, sector);
            }
            sector.add(new double[]{xi, yi});
        }
        return sectors;
    }

    private List<double[]> getClosestCoords(TreeMap<Integer, TreeMap<Integer, List<double[]>>> allSectors, double x, double y) {
        List<double[]> r = new ArrayList<>();
        {
            TreeMap<Integer, List<double[]>> yy = allSectors.get((int) (x - 1));
            if (yy != null) {
                List<double[]> rr = yy.get((int) y);
                if (rr != null) {
                    r.addAll(rr);
                }
            }
        }

        {
            TreeMap<Integer, List<double[]>> yy = allSectors.get((int) (x - 1));
            if (yy != null) {
                List<double[]> rr = yy.get((int) (y + 1));
                if (rr != null) {
                    r.addAll(rr);
                }
            }
        }

        {
            TreeMap<Integer, List<double[]>> yy = allSectors.get((int) (x));
            if (yy != null) {
                List<double[]> rr = yy.get((int) (y + 1));
                if (rr != null) {
                    r.addAll(rr);
                }
            }
        }

        {
            TreeMap<Integer, List<double[]>> yy = allSectors.get((int) (x + 1));
            if (yy != null) {
                List<double[]> rr = yy.get((int) (y + 1));
                if (rr != null) {
                    r.addAll(rr);
                }
            }
        }
        {
            TreeMap<Integer, List<double[]>> yy = allSectors.get((int) (x + 1));
            if (yy != null) {
                List<double[]> rr = yy.get((int) (y));
                if (rr != null) {
                    r.addAll(rr);
                }
            }
        }

        return r;
    }

    private int findShortestPath(TreeMap<Integer, TreeMap<Integer, List<double[]>>> allSectors, double x, double y, int i, double l) {
//        System.out.println("Finding shortest path for: " + x + ", " + y+ " | current jumps: " + i);
        List<double[]> neighbours = getClosestCoords(allSectors, x, y);//allSectors.get(x).get(y);

        if (l - y <= MAX_HOP_DISTANCE) { //can it reach the other side?
//            if((i+1)==5 )
//                System.out.println("###################");
//            System.out.println("Reached the other side with jumps: " + (i+1));
            return i + 1;
        }

        if (i >= MAX_HOPS) {
//            System.out.println("Max hops reached. returning.");
            return Integer.MAX_VALUE;
        }

        int min = Integer.MAX_VALUE;
        for (int j = 0; j < neighbours.size(); j++) {
            double[] coord = neighbours.get(j);
            if (calculateDistanceBetweenPoints(x, y, coord[0], coord[1]) <= MAX_HOP_DISTANCE) {
                int hops = findShortestPath(allSectors, coord[0], coord[1], i + 1, l);
                if (hops < min) {
//                    System.out.println("New min found: " + hops);
                    min = hops;
                }
            }
        }
        return min;
    }

    public double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public int solve() {
        Integer[] in = readInput();
        double w = in[0];
        double l = in[1];
        double s = in[2];
        int c = in[3];

        double[] postCoords = generatePostCoordinates(w, l, s, c);

        TreeMap<Integer, TreeMap<Integer, List<double[]>>> allSectors = convertPostCoordsToSectors(postCoords);
        MAX_HOPS = c/2;
        List<double[]> postsNearEdge = new ArrayList<>();
        for (int i = 0; i < postCoords.length; i += 2) {
            if (postCoords[i + 1] <= MAX_HOP_DISTANCE) {
                postsNearEdge.add(new double[]{postCoords[i], postCoords[i + 1]});
            }
        }
//        for (int i = 0; i < w ; i++) {
//            postsNearEdge.addAll(getClosestCoords(allSectors, i, 0d));
//        }

        int d = Integer.MAX_VALUE;

        for (int i = 0; i < postsNearEdge.size(); i++) {
            int ii = 1;
            ii = findShortestPath(allSectors, postsNearEdge.get(i)[0], postsNearEdge.get(i)[1], ii, l);
            d = Math.min(d, ii);
        }
        if (d == Integer.MAX_VALUE) {
            d = -1;
        }
        System.out.println(d);
        return d;
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        s.solve();

    }

}
