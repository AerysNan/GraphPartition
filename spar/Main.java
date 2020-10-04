package spar;

public class Main {
  public static void main(String[] args) {
    PartitionerRandom partitionerRandom = new PartitionerRandom("data.txt");
    // int[] serverCounts = new int[] { 4, 8, 16, 32, 64, 128, 256, 512 },
    // replicaCounts = new int[] { 0, 2 };
    int[] serverCounts = new int[] { 128 }, replicaCounts = new int[] { 0 };
    for (int serverCount : serverCounts) {
      for (int replicaCount : replicaCounts) {
        System.out.println("Partition configuration: n=" + serverCount + " k=" + replicaCount);
        System.out.println("Average replica: " + partitionerRandom.partition(serverCount, replicaCount));
        System.out.println("Imbalance Ratio: " + partitionerRandom.imBalanceRatio());
        System.out.println("Validity check: " + (partitionerRandom.checkValidity() ? "passed" : "failed"));
        System.out.println("Redundancy check: " + (partitionerRandom.checkRedundancy() ? "passed" : "failed"));
      }
    }
    PartitionerSpar partitionerSpar = new PartitionerSpar("data.txt");
    for (int serverCount : serverCounts) {
      for (int replicaCount : replicaCounts) {
        System.out.println("Partition configuration: n=" + serverCount + " k=" + replicaCount);
        System.out.println("Average replica: " + partitionerSpar.partition(serverCount, replicaCount));
        System.out.println("Imbalance Ratio: " + partitionerSpar.imBalanceRatio());
        System.out.println("Validity check: " + (partitionerSpar.checkValidity() ? "passed" : "failed"));
        System.out.println("Redundancy check: " + (partitionerSpar.checkRedundancy() ? "passed" : "failed"));
      }
    }
  }
}