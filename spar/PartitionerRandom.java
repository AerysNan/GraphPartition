package spar;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

class PartitionerRandom extends PartitionerBasic {
  public PartitionerRandom(String path) {
    super(path);
  }

  @Override
  public void placeNode(int userId) {
    if (graph.containsNode(userId))
      return;
    User user = new User(userId);
    List<Integer> candidates = new ArrayList<>();
    int minCount = Integer.MAX_VALUE;
    for (Set<Integer> set : servers)
      minCount = Math.min(minCount, set.size());
    for (int i = 0; i < n; i++)
      if (servers.get(i).size() == minCount)
        candidates.add(i);
    int master = random.nextInt(candidates.size());
    user.master = master;
    servers.get(master).add(user.id);
    while (user.slaves.size() < k) {
      int choice = random.nextInt(n);
      if (choice == user.master)
        continue;
      user.slaves.add(choice);
    }
    graph.addNode(user);
  }

  @Override
  public void adjustEdge(int user1Id, int user2Id) {
    User user1 = graph.users.get(user1Id), user2 = graph.users.get(user2Id);
    graph.addEdge(user1, user2);
    if (user1.master == user2.master)
      return;
    user1.slaves.add(user2.master);
    user2.slaves.add(user1.master);
  }
}
