package edu.umkc;

import java.util.ArrayList;

public class Main {

  public static void main(String[] args) {
    Graph graph = new Graph();
    graph.readInput();

    graph.floydWarshal();
    ArrayList<Integer> shortestPath = graph.getShortestPath(2,1);
    if (shortestPath != null) {
      for (int i : shortestPath) {
        System.out.print(i + ", ");
      }
      System.out.println();
    } else {
      System.out.println("Not Path found.");
    }
    System.out.println("My Distance:");
    graph.printMatrix(graph.paths);
    graph.buildEdgeTraffic();

    System.out.println("Shortest Paths:");
    graph.printShortestPaths();
    System.out.println("Edge Traffic:");
    graph.printMatrix(graph.edgeTraffic);
  }
}
