package spar;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Set;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;

abstract class PartitionerBasic {
  int n, k;
  Graph graph;
  Random random;
  List<Point> data;
  List<Set<Integer>> servers;

  public PartitionerBasic(String path) {
    data = new ArrayList<>();
    random = new Random();
    try {
      File file = new File("data.txt");
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        String[] chunks = scanner.nextLine().split(" ");
        data.add(new Point(Integer.parseInt(chunks[0]), Integer.parseInt(chunks[1])));
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("Exception: data file not found in path " + path);
    }
  }

  void initialize(int n, int k) {
    this.n = n;
    this.k = k;
    graph = new Graph();
    servers = new ArrayList<>();
    for (int i = 0; i < n; i++)
      servers.add(new HashSet<>());
  }

  public double partition(int n, int k) {
    initialize(n, k);
    for (Point point : data) {
      placeNode(point.x);
      placeNode(point.y);
      adjustEdge(point.x, point.y);
    }
    return graph.averageReplicaNumber();
  }

  abstract void placeNode(int userId);

  abstract void adjustEdge(int user1Id, int user2Id);
}