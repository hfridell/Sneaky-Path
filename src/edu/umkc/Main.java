package edu.umkc;

public class Main {

  public static void main(String[] args) {
    Graph graph = new Graph();
    graph.readInput();

    graph.greedyFloydWarshal();

    System.out.println("My Distance:");
    graph.printMatrix(graph.paths);
    graph.buildEdgeTraffic();
    System.out.println("Shortest Paths:");
    graph.printShortestPaths();
    System.out.println("Path Traffic:");
    graph.printMatrix(graph.flows);

    System.out.println("Edge Traffic:");
    graph.printMatrix(graph.edgeTraffic);
  }
}
