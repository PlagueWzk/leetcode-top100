package io.github.plaguewzk.leetplague.demo.atomic;

/**
 * Created on 2026/9/23 10:03
 *
 * @author PlagueWzk
 */
public class Quota {
    private int remaining = 10;

    public synchronized boolean tryAcquire1(int amount) {
        if (remaining <= 0) {
            return false;
        }
        if (remaining - amount < 0) {
            return false;
        }
        remaining -= amount;
        return true;
    }

    public boolean tryAcquire2(int amount) {
        while (true) {
            int tmp = remaining;
            if (tmp <= 0) {
                return false;
            }
            if (tmp - amount < 0) {
                return false;
            }
            if (compareAndSet(tmp, amount)) {
                return true;
            }
        }
    }
    private synchronized boolean compareAndSet(int expect, int update) {
        if (remaining != expect) {
            return false;
        }
        remaining -= update;
        return true;
    }
}
