package edu.umkc;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.RejectedExecutionException;

public class Graph {
  private final int inf = Integer.MAX_VALUE / 2;
  int size;
  int start;
  int finish;
  //private ArrayList<Integer> nodes;
  int[][] paths;
  int[][] nextPath;
  int[][] edgeTraffic;
  int[][] flows;
  int[][][] shortestPaths;


  public void buildEdgeTraffic() {
    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        ArrayList<Integer> shortPath = getShortestPath(i, j);
        if (shortPath != null) {
          // Convert to array
          int[] path = new int[shortPath.size()];
          for (int x = 0; x < shortPath.size(); x++) {
            if (shortPath.get(x) != null) {
              path[x] = shortPath.get(x);
            }
          }
          shortestPaths[i][j] = path;
          for (int hop = 1; hop < path.length; hop++) {
            int current = path[hop - 1];
            int next = path[hop];
            edgeTraffic[current][next] += flows[current][next];
          }
        }
      }
    }
  }

  public ArrayList<Integer> getShortestPath() {
    return getShortestPath(start, finish);
  }

  public ArrayList<Integer> getShortestPath(int start, int finish) {

    if (nextPath[start][finish] == inf) {
      return null;
    }
    ArrayList<Integer> path = new ArrayList<>();
    path.add(start);
    while (start != finish) {
      start = nextPath[start][finish];
      path.add(start);
    }
    return path;
  }

  public void floydWarshal() {
    for (int k = 1; k <= size; k++) {
      for (int i = 1; i <= size; i++) {
        for (int j = 1; j <= size; j++) {
          if (isPathBetter(k, i, j)) {
            paths[i][j] = sumPaths(k, i, j);
            nextPath[i][j] = nextPath[i][k];
          }
        }
      }
    }
  }

  private void setupArrays() {
    paths = new int[size + 1][size + 1];
    nextPath = new int[size + 1][size + 1];
    flows = new int[size + 1][size + 1];
    edgeTraffic = new int[size + 1][size + 1];
    shortestPaths = new int[size + 1][size + 1][];
    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        if (i == j) {
          paths[i][j] = 0;
        } else {
          paths[i][j] = inf;
        }
        nextPath[i][j] = j;
      }
    }
  }

  private int sumPaths(int k, int i, int j) {
    return paths[i][k] + paths[k][j];
  }

  private boolean isPathBetter(int k, int i, int j) {
    return paths[i][k] + paths[k][j] < paths[i][j];
  }

  public void readInput() {
    //String fileName = getFileName();
    String fileName = "CS404FS16SneakyPathInput1.txt";
    File file = new File(fileName);

    try {
      Scanner in = new Scanner(file);
      String[] firstLine = in.nextLine().split(",");
      // Read in size info: size, start, end
      size = Integer.parseInt(firstLine[0].trim());
      start = Integer.parseInt(firstLine[1].trim());
      finish = Integer.parseInt(firstLine[2].trim());

      // initialize arrays
      setupArrays();


      // Read in Edge and Flow information:  Type(E/F), start, end, weight
      while (in.hasNext()) {
        String[] nextLine = in.nextLine().split(",");
        String type = nextLine[0].trim();
        int start = Integer.parseInt(nextLine[1].trim());
        int end = Integer.parseInt(nextLine[2].trim());
        int weight = Integer.parseInt(nextLine[3].trim());

        // Determine if Edge or Flow values
        if (type.equals("E")) {
          paths[start][end] = weight;
        } else if (type.equals("F")) {
          flows[start][end] = weight;
        } else {
          throw new RejectedExecutionException("File contained unexpected character");
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private String getFileName() {
    Scanner input = new Scanner(System.in);
    System.out.print("Enter input file name: ");
    return input.nextLine();
  }

  public void printMatrix(int[][] matrix) {

    for (int i = 1; i < matrix.length; i++) {
      for (int j = 1; j < matrix[i].length; j++) {
        System.out.format("%4d", matrix[i][j]);
      }
      System.out.println();
    }
  }

  public void printShortestPaths() {

    for (int i = 1; i < shortestPaths.length; i++) {
      for (int j = 1; j < shortestPaths[i].length; j++) {
        System.out.format("%15s", Arrays.toString(shortestPaths[i][j]));
      }
      System.out.println();
    }
  }
}