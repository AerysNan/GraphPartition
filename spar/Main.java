package spar;

public class Main {
  public static void main(String[] args) {
    PartitionerRandom partitionerRandom = new PartitionerRandom("data.txt");
    int[] serverCounts = new int[] { 4, 64, 128, 256, 512, 1024 }, replicaCounts = new int[] { 0, 2 };
    for (int serverCount : serverCounts) {
      for (int replicaCount : replicaCounts) {
        System.out.println("Partition configuration: n=" + serverCount + " k=" + replicaCount);
        System.out.println("Average replica: " + partitionerRandom.partition(serverCount, replicaCount));
        System.out.println("Validity check: " + partitionerRandom.graph.checkValidity());
      }
    }
  }
}