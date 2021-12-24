package utils;
import domain.*;

/**
 * @author Jia Long Ji Qiu
 */

public class Pair implements Comparable<Pair> {
    double first;
    Item second;

    public Pair(double first, Item second) {
        this.first = first;
        this.second = second;
    }

    public double getFirst() {
        return this.first;
    }

    public Item getSecond() {
        return this.second;
    }

    public void setFirst(float first) {
        this.first = first;
    }

    public void setSecond(Item second) {
        this.second = second;
    }

    public int compareTo(Pair p) {
        if (this.first > p.first) return 1;
        else if (this.first < p.first) return -1;
        else {
            if (this.second.getId() > p.second.getId()) return 1;
            else if (this.second.getId() < p.second.getId()) return -1;
            return 0;
        }
    }
}
