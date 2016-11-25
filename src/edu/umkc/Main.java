package edu.umkc;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Main {

  final static boolean writeToFile = true;
  final static boolean runAllFiles = true;
  final static String[] files = {
      "CS404FS16SneakyPathInput1.txt",
      "CS404FS16SneakyPathInputN10b.txt",
      "CS404FS16SneakyPathInputN10c.txt",
      "CS404FS16SneakyPathInputN15a.txt",
      "CS404FS16SneakyPathInputN15b.txt",
      "CS404FS16SneakyPathInputN20a.txt",
      "CS404FS16SneakyPathInputN25a.txt",
      "CS404FS16SneakyPathInputN30a.txt",
      "CS404FS16SneakyPathInputN35a.txt",
      "CS404FS16SneakyPathInputN50a.txt",
      "CS404FS16SneakyPathInputN75a.txt"
  };

  static Instant start;
  static Instant end;

  public static void main(String[] args) {
    if (runAllFiles){

    } else {
      String fileName = getFileName();
      evaluateFile(fileName);
    }
  }

  private static void evaluateFile(String fileName){
    if (writeToFile) {
      try {
        System.setOut(new PrintStream(new BufferedOutputStream(
            new FileOutputStream("Output_"+fileName)), true));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        System.exit(0);
      }
    }

    Instant totalStart = Instant.now();
    System.out.println("-----------------------------------------------------------------------------");
    System.out.println("Hank Fridell");
    System.out.println("-----------------------------------------------------------------------------");
    start = Instant.now();
    System.out.println("Parsing file: " + fileName);
    GraphReader reader = new GraphReader(fileName);
    reader.read();
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    int startNode = reader.start-1;
    int finishNode = reader.finish-1;
    System.out.println("-----------------------------------------------------------------------------");

    start = Instant.now();
    Graph original = new Graph(reader.getPathMatrix(), reader.getFlowMatrix());
    System.out.println("Original:");
    original.printMatrix();
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    System.out.println("-----------------------------------------------------------------------------");

    start = Instant.now();
    System.out.println("All pairs shortest paths:");
    original.floydWarshal();
    original.printMatrix();
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    System.out.println("-----------------------------------------------------------------------------");

    start = Instant.now();
    System.out.println("Actual shortest paths:");
    original.printShortestPaths();
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    System.out.println("-----------------------------------------------------------------------------");

    start = Instant.now();
    System.out.println("Original flow Matrix:");
    Graph.printMatrix(original.flowMatrix);
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    System.out.println("-----------------------------------------------------------------------------");

    start = Instant.now();
    System.out.println("Edge Traffic:");
    original.printEdgeTraffic();
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    System.out.println("-----------------------------------------------------------------------------");

    start = Instant.now();
    Graph sneaky = new Graph(original.edgeTraffic);
    sneaky.floydWarshal();
    System.out.println("All pairs sneaky paths:");
    sneaky.printMatrix();
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    System.out.println("-----------------------------------------------------------------------------");

    start = Instant.now();
    System.out.println("Actual sneaky paths:");
    sneaky.printShortestPaths();
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    System.out.println("-----------------------------------------------------------------------------");

    Instant totalEnd = Instant.now();
    System.out.println("Total Running time: " + Duration.between(totalStart, totalEnd));
    System.out.println("-----------------------------------------------------------------------------");

    start = Instant.now();
    System.out.println("Minimum cars seen:");
    sneaky.printMinPaths();
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    System.out.println("-----------------------------------------------------------------------------");

    start = Instant.now();
    System.out.println("Maximum cars seen:");
    sneaky.printMaxPaths();
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    System.out.println("-----------------------------------------------------------------------------");

    start = Instant.now();
    System.out.println("Average cars seen:");
    sneaky.printAvgPaths();
    end = Instant.now();
    System.out.println("Elapsed Time: " + Duration.between(start, end));
    System.out.println("-----------------------------------------------------------------------------");

    System.out.println("Specified Shortest Path: " + reader.start + "," + reader.finish);
    original.printPath(startNode, finishNode);
    System.out.println("-----------------------------------------------------------------------------");

    System.out.println("Specified Sneaky Path: "+ reader.start + "," + reader.finish);
    sneaky.printPath(startNode, finishNode);
    System.out.println("-----------------------------------------------------------------------------");
    System.out.println("-----------------------------------------------------------------------------");
  }

  private static String getFileName() {
    Scanner input = new Scanner(System.in);
    System.out.println("Enter input file name: ");
    String fileName = input.nextLine().trim();
    return fileName;
  }
}
