package spar;

import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

class PartitionerSpar extends PartitionerBasic {
  public PartitionerSpar(String path) {
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
    int master = candidates.get(random.nextInt(candidates.size()));
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
    graph.addEdge(user1Id, user2Id);
    if (user1.master == user2.master)
      return;
    int remainGain = remainAsIsGain(user1, user2), moveUser1ToUser2Gain = moveMasterReplicaGain(user1, user2),
        moveUser2ToUser1Gain = moveMasterReplicaGain(user2, user1);
    int[] gains = new int[] { remainGain, moveUser1ToUser2Gain, moveUser2ToUser1Gain };
    Arrays.sort(gains);
    for (int i = 0; i < 3; i++) {
      if (gains[i] == remainGain) {
        remainAsIs(user1, user2);
        return;
      } else if (gains[i] == moveUser1ToUser2Gain) {
        if (i != 2 && servers.get(user1.master).size() <= servers.get(user2.master).size()
            && gains[i + 1] - gains[i] < imBalanceRatio())
          continue;
        moveMasterReplica(user1, user2);
        return;
      } else {
        if (i != 2 && servers.get(user2.master).size() <= servers.get(user1.master).size()
            && gains[i + 1] - gains[i] < imBalanceRatio())
          continue;
        moveMasterReplica(user2, user1);
        return;
      }
    }
  }

  private void remainAsIs(User user1, User user2) {
    user1.slaves.add(user2.master);
    user2.slaves.add(user1.master);
  }

  private void moveMasterReplica(User user1, User user2) {
    // create user1's slave on user1's old master server
    for (int neighbourId : graph.map.get(user1.id)) {
      User neighbour = graph.users.get(neighbourId);
      if (user1.master == neighbour.master) {
        user1.slaves.add(user1.master);
        break;
      }
    }
    // create neighbours' slave on user1's new master server
    for (int neighbourId : graph.map.get(user1.id)) {
      User neighbour = graph.users.get(neighbourId);
      if (neighbour.master != user2.master)
        neighbour.slaves.add(user2.master);
    }
    // remove user1's slave on user1's new master server
    if (user1.slaves.contains(user2.master) && user1.slaves.size() > k)
      user1.slaves.remove(user2.master);
    // remove neighbours' slave on user1's old master server
    for (int neighbourId : graph.map.get(user1.id)) {
      User neighbour = graph.users.get(neighbourId);
      if (!neighbour.slaves.contains(user1.master))
        continue;
      boolean redundant = true;
      for (int secondOrderNeighbourId : graph.map.get(neighbourId)) {
        if (secondOrderNeighbourId == user1.id)
          continue;
        User secondOrderNeighbour = graph.users.get(secondOrderNeighbourId);
        if (secondOrderNeighbour.master == user1.master && secondOrderNeighbour.master != neighbour.master) {
          redundant = false;
          break;
        }
      }
      if (redundant && neighbour.slaves.size() > k)
        neighbour.slaves.remove(user1.master);
    }
    // move master
    servers.get(user1.master).remove(user1.id);
    servers.get(user2.master).add(user1.id);
    user1.master = user2.master;
  }

  private int remainAsIsGain(User user1, User user2) {
    int result = 0;
    if (!user1.slaves.contains(user2.master))
      result++;
    if (!user2.slaves.contains(user1.master))
      result++;
    return result;
  }

  // move user1's master to user2's master server
  private int moveMasterReplicaGain(User user1, User user2) {
    int result = 0;
    // create user1's slave on user1's old master server
    for (int neighbourId : graph.map.get(user1.id)) {
      User neighbour = graph.users.get(neighbourId);
      if (user1.master == neighbour.master && !user1.slaves.contains(user1.master)) {
        result++;
        break;
      }
    }
    // create neighbours' slave on user1's new master server
    for (int neighbourId : graph.map.get(user1.id)) {
      User neighbour = graph.users.get(neighbourId);
      if (neighbour.master == user2.master || neighbour.slaves.contains(user2.master))
        continue;
      result++;
    }
    // remove user1's slave on user1's new master server
    if (user1.slaves.contains(user2.master) && user1.slaves.size() > k)
      result--;
    // remove neighbours' slave on user1's old master server
    for (int neighbourId : graph.map.get(user1.id)) {
      User neighbour = graph.users.get(neighbourId);
      if (!neighbour.slaves.contains(user1.master))
        continue;
      boolean redundant = true;
      for (int secondOrderNeighbourId : graph.map.get(neighbourId)) {
        if (secondOrderNeighbourId == user1.id)
          continue;
        User secondOrderNeighbour = graph.users.get(secondOrderNeighbourId);
        if (secondOrderNeighbour.master == user1.master && secondOrderNeighbour.master != neighbour.master) {
          redundant = false;
          break;
        }
        if (redundant && neighbour.slaves.size() > k)
          result--;
      }
    }
    return result;
  }
}
