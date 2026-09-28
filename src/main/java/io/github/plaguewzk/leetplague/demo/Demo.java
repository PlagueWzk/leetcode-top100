package io.github.plaguewzk.leetplague.demo;

/**
 * Created on 2026/9/14 16:05
 *
 * @author PlagueWzk
 */

public class Demo {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "main当前线程");
        Thread interuptedThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": 开始启动");
            try {
                Thread.sleep(10000);
            }
            catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": 线程被中断");
            }
            System.out.println(Thread.currentThread().getName() + ": 线程结束");
        });
        interuptedThread.start();
        System.out.println(
                "interuptedThread.isInterrupted() = " + interuptedThread.isInterrupted());
        System.out.println("Thread.interrupted() = " + Thread.interrupted());
        interuptedThread.interrupt();
        System.out.println(
                "interuptedThread.isInterrupted() = " + interuptedThread.isInterrupted());
        System.out.println("Thread.interrupted() = " + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "main当前线程");
    }
}
