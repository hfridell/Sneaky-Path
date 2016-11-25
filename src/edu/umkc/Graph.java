package edu.umkc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
  private final static Integer INFINITY = 99999;
  ArrayList<ArrayList<Integer>> flowMatrix;
  Integer[][] original;
  ArrayList<ArrayList<Integer>> adjacencyMatrix;
  ArrayList<ArrayList<Integer>> nextMatrix;
  ArrayList<ArrayList<Integer>> edgeTraffic;
  ArrayList<ArrayList<ArrayList<Integer>>> shortestPaths;
  int size;

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

  public void floydWarshal() {
    // Build matrix for path reconstruction
    nextMatrix = new ArrayList<>(size);
    for (Integer i = 0; i < size; i++) {
      nextMatrix.add(i, new ArrayList<>(size));
      for (Integer j = 0; j < size; j++) {
        nextMatrix.get(i).add(j, j);
      }
    }

    //initializeAllPairsShortestPath();
    // Floyd Warshal
    for (int k = 0; k < size; k++) {
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          int iK = adjacencyMatrix.get(i).get(k);
          int kJ = adjacencyMatrix.get(k).get(j);
          int iJ = adjacencyMatrix.get(i).get(j);

          if ((iK + kJ) < iJ) {
            adjacencyMatrix.get(i).set(j, (iK + kJ));
            nextMatrix.get(i).set(j, nextMatrix.get(i).get(k));
          }
        }
      }
    }
  }

  public ArrayList<Integer> path(int start, int end) {
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
          for (int hop = 1; hop < path.size(); hop++) {
            // -1 for offset
            int current = path.get(hop - 1) - 1;
            int next = path.get(hop) - 1;
            if (edgeTraffic.get(current).get(next) == null) {
              edgeTraffic.get(current).set(next, flowMatrix.get(i).get(j));
            } else {
              int newValue = flowMatrix.get(i).get(j);
              newValue += edgeTraffic.get(current).get(next);
              edgeTraffic.get(current).set(next, newValue);
            }
          }
        }
      }
    }
  }

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

  public void printMatrix() {
    for (List<Integer> col : adjacencyMatrix) {
      for (Integer x : col) {
        if (x.equals(INFINITY)) {
          System.out.format("%8s", "NA");
        } else {
          System.out.format("%8d", x);
        }
      }
      System.out.println();
    }
  }

  public static void printMatrix(ArrayList<ArrayList<Integer>> matrix) {
    for (List<Integer> col : matrix) {
      for (Integer x : col) {
        System.out.format("%8d", x);
      }
      System.out.println();
    }
  }

  public void printEdgeTraffic() {
    buildEdgeTraffic();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        Integer value = edgeTraffic.get(i).get(j);
        if (i == j && value == null) {
          System.out.format("%8s", "0");
        } else if (value.equals(INFINITY)) {
          System.out.format("%8s", "NA");
        } else {
          System.out.format("%8d", value);
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
        System.out.format("%15s", Arrays.toString(path.toArray()));
      }
      System.out.println();
    }
  }

  private void initializeShortestPaths() {
    shortestPaths = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      shortestPaths.add(i, new ArrayList<>(size));
    }
  }
}