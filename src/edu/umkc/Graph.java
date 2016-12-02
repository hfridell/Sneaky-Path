package edu.umkc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
  private final static Integer INFINITY = 999999;
  int size;
  // 2-D arrays
  ArrayList<ArrayList<Integer>> flowMatrix;
  Integer[][] original;
  ArrayList<ArrayList<Integer>> adjacencyMatrix;
  ArrayList<ArrayList<Integer>> nextMatrix;
  ArrayList<ArrayList<Integer>> edgeTraffic;
  Integer[][] minPathCosts;
  Integer[][] maxPathCosts;
  Double[][] avgPathCosts;
  // 3-D array
  ArrayList<ArrayList<ArrayList<Integer>>> shortestPaths;

  public Graph(ArrayList<ArrayList<Integer>> adjacencyMatrix) {
    this.size = adjacencyMatrix.size();
    this.adjacencyMatrix = adjacencyMatrix;
    original = new Integer[size][size];
    for (int i = 0; i < size; i++) {
      original[i] = adjacencyMatrix.get(i).toArray(original[i]);
    }
  }

  public Graph(ArrayList<ArrayList<Integer>> adjacencyMatrix, ArrayList<ArrayList<Integer>> flowMatrix) {
    this(adjacencyMatrix);
    this.flowMatrix = flowMatrix;
  }

  // Print given matrix to standard out, Infinity values are aliased to NA
  public static void printMatrix(ArrayList<ArrayList<Integer>> matrix) {
    for (List<Integer> col : matrix) {
      for (Integer x : col) {
        if (x.equals(INFINITY)) {
          System.out.format("%8s,", "NA");
        } else {
          System.out.format("%8d,", x);
        }
      }
      System.out.println();
    }
  }

  public void floydWarshall() {
    // https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm#Path_reconstruction
    // Build matrix for path reconstruction
    nextMatrix = new ArrayList<>(size);
    for (Integer i = 0; i < size; i++) {
      nextMatrix.add(i, new ArrayList<>(size));
      for (Integer j = 0; j < size; j++) {
        nextMatrix.get(i).add(j, j);
      }
    }

    // Floyd Warshall
    for (int k = 0; k < size; k++) {
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          int iK = adjacencyMatrix.get(i).get(k);
          int kJ = adjacencyMatrix.get(k).get(j);
          int iJ = adjacencyMatrix.get(i).get(j);

          // If new path is better
          if ((iK + kJ) < iJ) {
            adjacencyMatrix.get(i).set(j, (iK + kJ));
            nextMatrix.get(i).set(j, nextMatrix.get(i).get(k));
          }
        }
      }
    }
  }

  // Build an individual Path
  public ArrayList<Integer> path(int start, int end) {
    // if no path exits return null
    if (nextMatrix.get(start).get(end) == null) {
      return null;
    }

    ArrayList<Integer> path = new ArrayList<>();
    int current = start;
    // add offset to display 1 based array
    path.add(current + 1);
    // move through matrix until we reach the end
    while (current != end) {
      current = nextMatrix.get(current).get(end);
      path.add(current + 1);
    }

    return path;
  }

  public void buildShortestPathMatrix() {
    // Don't rebuild matrix if work has already been done
    if (shortestPaths == null) {
      initializeShortestPaths();
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          shortestPaths.get(i).add(j, path(i, j));
        }
      }
    }
  }

  public void buildEdgeTraffic() {
    if (edgeTraffic == null) {
      initializeEdgeTraffic();
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          ArrayList<Integer> path = shortestPaths.get(i).get(j);
          // For each hop in the path add the flow from i to j
          for (int hop = 1; hop < path.size(); hop++) {
            // -1 for offset
            int current = path.get(hop - 1) - 1;
            int next = path.get(hop) - 1;
            // if i,j value is not set assign value
            if (edgeTraffic.get(current).get(next) == null) {
              edgeTraffic.get(current).set(next, flowMatrix.get(i).get(j));
            } else {
              // i,j already has value, add to it
              int newValue = flowMatrix.get(i).get(j);
              newValue += edgeTraffic.get(current).get(next);
              edgeTraffic.get(current).set(next, newValue);
            }
          }
        }
      }
    }
  }

  // initialize Edge Traffic to 0 unless path isn't in adjacency
  private void initializeEdgeTraffic() {
    edgeTraffic = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      edgeTraffic.add(i, new ArrayList<>(size));
      for (int j = 0; j < size; j++) {
        // if path doesn't exist set to null
        if (original[i][j].equals(INFINITY)) {
          edgeTraffic.get(i).add(j, INFINITY);
        } else {
          edgeTraffic.get(i).add(j, 0);
        }
      }
    }
  }

  public void pathCosts() {
    maxPathCosts = new Integer[size][size];
    minPathCosts = new Integer[size][size];
    avgPathCosts = new Double[size][size];

    // Calculate Min, Max, Average paths
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        int max = -1;
        int min = INFINITY;
        ArrayList<Integer> pathCost = new ArrayList<>();
        // if path doesn't exist set min/max/avg to null and move to next
        if (shortestPaths.get(i).get(j) == null) {
          maxPathCosts[i][j] = null;
          minPathCosts[i][j] = null;
          avgPathCosts[i][j] = null;
          continue;
        }

        ArrayList<Integer> path = shortestPaths.get(i).get(j);
        for (int hop = 1; hop < path.size(); hop++) {
          // -1 for offset
          int current = path.get(hop - 1) - 1;
          int next = path.get(hop) - 1;

          // set min/max based on new value
          if (original[current][next] > max) {
            max = original[current][next];
          }
          if (original[current][next] < min) {
            min = original[current][next];
          }
          // add cost to running total
          pathCost.add(original[current][next]);
        }
        maxPathCosts[i][j] = max;
        minPathCosts[i][j] = min;
        avgPathCosts[i][j] = pathAvg(pathCost);
      }
    }
  }

  // calculate avg in path
  private double pathAvg(ArrayList<Integer> path) {
    double sum = 0;
    for (int hop : path)
      sum += hop;
    return sum / path.size();
  }

  private void initializeShortestPaths() {
    shortestPaths = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      shortestPaths.add(i, new ArrayList<>(size));
    }
  }

  public void printPath(int x, int y) {
    // if costs haven't been calculated evaluate now
    if (maxPathCosts == null)
      pathCosts();

    ArrayList<Integer> path = path(x, y);
    System.out.println(Arrays.toString(
        path.toArray()));

    System.out.println("Min path cost: " + minPathCosts[x][y]);
    System.out.println("Max path cost: " + maxPathCosts[x][y]);
    System.out.println("Avg path cost: " + avgPathCosts[x][y]);
  }

  // Print the default adjacency matrix, alias infinity as NA
  public void printMatrix() {
    for (List<Integer> col : adjacencyMatrix) {
      for (Integer x : col) {
        if (x.equals(INFINITY)) {
          System.out.format("%8s,", "NA");
        } else {
          System.out.format("%8d,", x);
        }
      }
      System.out.println();
    }
  }

  // build and print edge traffic, infinity is aliased as NA, null as 0
  public void printEdgeTraffic() {
    buildEdgeTraffic();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        Integer value = edgeTraffic.get(i).get(j);
        if (i == j && value == null) {
          System.out.format("%8s,", "0");
        } else if (value.equals(INFINITY)) {
          System.out.format("%8s,", "NA");
        } else {
          System.out.format("%8d,", value);
        }
      }
      System.out.println();
    }
    System.out.println();
  }


  public void printShortestPaths() {
    buildShortestPathMatrix();
    for (ArrayList<ArrayList<Integer>> col : shortestPaths) {
      for (ArrayList<Integer> path : col) {
        System.out.format("%15s,", Arrays.toString(path.toArray()));
      }
      System.out.println();
    }
  }

  // infinity is aliased as NA, null as 0
  public void printMinPaths() {
    if (minPathCosts == null)
      pathCosts();

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        ;
        if (i == j) {
          System.out.format("%8s,", "0");
        } else if (minPathCosts[i][j] == null) {
          System.out.format("%8s,", "NA");
        } else {
          System.out.format("%8d,", minPathCosts[i][j]);
        }
      }
      System.out.println();
    }
    System.out.println();
  }

  // Evaluate path costs if needed, infinity is aliased as NA, null as 0
  public void printMaxPaths() {
    if (maxPathCosts == null)
      pathCosts();

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (i == j) {
          System.out.format("%8s,", "0");
        } else if (maxPathCosts[i][j] == null) {
          System.out.format("%8s,", "NA");
        } else {
          System.out.format("%8d,", maxPathCosts[i][j]);
        }
      }
      System.out.println();
    }
    System.out.println();
  }

  // Evaluate path costs if needed, infinity is aliased as NA, null as 0
  public void printAvgPaths() {
    if (avgPathCosts == null)
      pathCosts();

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (i == j) {
          System.out.format("%8s,", "0");
        } else if (avgPathCosts[i][j] == null) {
          System.out.format("%8s,", "NA");
        } else {
          System.out.format("%8.1f,", avgPathCosts[i][j]);
        }
      }
      System.out.println();
    }
    System.out.println();
  }
}