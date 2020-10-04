package spar;

import java.util.Set;
import java.util.Map;
import java.util.Random;
import java.util.HashSet;
import java.util.HashMap;

class Graph {
  Random random;
  Map<Integer, User> users;
  Map<Integer, Set<Integer>> map;

  public Graph() {
    random = new Random();
    users = new HashMap<>();
    map = new HashMap<>();
  }

  public void addNode(User user) {
    users.put(user.id, user);
  }

  public boolean containsNode(int id) {
    return users.containsKey(id);
  }

  public void addEdge(int user1Id, int user2Id) {
    map.computeIfAbsent(user1Id, x -> new HashSet<>()).add(user2Id);
    map.computeIfAbsent(user2Id, x -> new HashSet<>()).add(user1Id);
  }

  public double averageReplicaNumber() {
    return (double) users.values().stream().mapToInt(x -> x.slaves.size()).sum() / users.size();
  }
}