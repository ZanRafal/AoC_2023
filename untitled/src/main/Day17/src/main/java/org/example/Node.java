package org.example;

public class Node implements Comparable<Node> {

    int x, y, cost, direction, steps;

    public Node(int x, int y, int cost, int direction, int steps) {
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.direction = direction;
        this.steps = steps;
    }

    @Override
    public int compareTo(Node o) {
        return this.cost - o.cost;
    }
}
