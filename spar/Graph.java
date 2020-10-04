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

  public void addEdge(User user1, User user2) {
    map.computeIfAbsent(user1.id, x -> new HashSet<>()).add(user2.id);
    map.computeIfAbsent(user2.id, x -> new HashSet<>()).add(user1.id);
  }

  public double averageReplicaNumber() {
    return (double) users.values().stream().mapToInt(x -> x.slaves.size()).sum() / users.size();
  }

  public boolean checkValidity() {
    for (User user : users.values()) {
      for (int id : map.get(user.id)) {
        User neighbour = users.get(id);
        if (user.master != neighbour.master && !user.slaves.contains(neighbour.master)
            && !neighbour.slaves.contains(user.master))
          return false;
      }
    }
    return true;
  }
}