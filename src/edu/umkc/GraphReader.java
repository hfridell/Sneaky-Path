package edu.umkc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphReader {

  private final static Integer INFINITY = 999999;
  String fileName = "CS404FS16SneakyPathInput1.txt";
  File file;
  Integer size;
  Integer start;
  Integer finish;

  private ArrayList<ArrayList<Integer>> pathMatrix;
  private ArrayList<ArrayList<Integer>> flowMatrix;

  public GraphReader(String fileName) {
    this.fileName = fileName;
    file = new File(this.fileName);
  }

  // Default Constructor uses the first sample file
  public GraphReader() {
    this("CS404FS16SneakyPathInput1.txt");
  }

  public void read() {
    try {
      Scanner in = new Scanner(file);
      String[] firstLine = in.nextLine().split(",");
      // Read in size info: size, start, end
      size = Integer.parseInt(firstLine[0].trim());
      start = Integer.parseInt(firstLine[1].trim());
      finish = Integer.parseInt(firstLine[2].trim());

      initializeMatrixs();

      // Read in Edge and Flow information:  Type(E/F), start, end, weight
      String line;
      while (in.hasNextLine()) {
        line = in.nextLine();
        // Skip Blank Lines
        if (line.isEmpty()){
          continue;
        }

        String[] nextLine = line.split(",");
        String type = nextLine[0].trim();

        // adjust for 0 based index
        int start = Integer.parseInt(nextLine[1].trim())-1;
        int end = Integer.parseInt(nextLine[2].trim())-1;
        int weight = Integer.parseInt(nextLine[3].trim());

        // Determine if Edge or Flow values
        if (type.contains("E")) {
          //paths[start][end] = weight;
          pathMatrix.get(start).set(end, weight);
        } else if (type.contains("F")) {
          //flows[start][end] = weight;
          flowMatrix.get(start).set(end, weight);

        } else {
          throw new IOException("File contained unexpected character");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Set default values to INFINITY
  // 0 values is set for weight to self
  private void initializeMatrixs() {
    pathMatrix = new ArrayList<>(size);
    flowMatrix = new ArrayList<>(size);
    for(int i = 0; i < size; i++) {
      pathMatrix.add(i, new ArrayList<>(size));
      flowMatrix.add(i, new ArrayList<>(size));
      for(int j = 0; j < size; j++) {
        if (i == j) {
          pathMatrix.get(i).add(j, 0);
          flowMatrix.get(i).add(j, 0);
        } else {
          pathMatrix.get(i).add(j, INFINITY);
          flowMatrix.get(i).add(j, INFINITY);
        }
      }
    }
  }


  // Helper functions
  public static Integer getINFINITY() {
    return INFINITY;
  }

  public Integer getSize() {
    return size;
  }

  public Integer getStart() {
    return start;
  }

  public Integer getFinish() {
    return finish;
  }

  public ArrayList<ArrayList<Integer>> getPathMatrix() {
    return pathMatrix;
  }

  public ArrayList<ArrayList<Integer>> getFlowMatrix() {
    return flowMatrix;
  }
}
