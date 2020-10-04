package spar;

import java.util.Set;
import java.util.HashSet;

class User {
  int id;
  int master;
  Set<Integer> slaves;

  public User(int id) {
    this.id = id;
    slaves = new HashSet<>();
  }
}
