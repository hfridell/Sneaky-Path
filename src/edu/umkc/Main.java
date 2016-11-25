package edu.umkc;

import java.util.ArrayList;

public class Main {

  public static void main(String[] args) {
    GraphReader reader = new GraphReader("test");
    reader.read();

    Graph original = new Graph(reader.getPathMatrix(), reader.getFlowMatrix());
    System.out.println("-----------------------------------------------------------------------------");
    System.out.println("Original:");
    original.printMatrix();
    System.out.println("-----------------------------------------------------------------------------");

    System.out.println("All pairs shortest paths:");
    original.floydWarshal();
    original.printMatrix();
    System.out.println("-----------------------------------------------------------------------------");
    System.out.println("Actual shortest paths:");
    original.printShortestPaths();
    System.out.println("-----------------------------------------------------------------------------");
    System.out.println("Original flow Matrix:");
    Graph.printMatrix(original.flowMatrix);
    System.out.println("-----------------------------------------------------------------------------");
    System.out.println("Edge Traffic:");
    original.printEdgeTraffic();
    System.out.println("-----------------------------------------------------------------------------");


  }
}
