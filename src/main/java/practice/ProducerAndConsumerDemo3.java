package practice;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: CyS2020
 * @date: 2021/2/18
 * 描述：生产者消费者，Lock + Condition
 */
public class ProducerAndConsumerDemo3 {

    private final int size = 10;

    private final Queue<Long> storage = new LinkedList<>();

    private final Lock lock = new ReentrantLock();

    private final Condition notFull = lock.newCondition();

    private final Condition notEmpty = lock.newCondition();

    public static void main(String[] args) {
        ProducerAndConsumerDemo3 demo3 = new ProducerAndConsumerDemo3();
        new Thread(demo3.new Producer()).start();
        new Thread(demo3.new Consumer()).start();
    }

    public class Producer implements Runnable {

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                lock.lock();
                try {
                    while (storage.size() == size) {
                        notFull.await();
                    }
                    long l = (long) (Math.random() * 1000);
                    storage.offer(l);
                    notEmpty.signalAll();
                    System.out.println("生产了资源" + l + "资源个数" + storage.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public class Consumer implements Runnable {

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                lock.lock();
                try {
                    while (storage.size() == 0) {
                        notEmpty.await();
                    }
                    Long l = storage.poll();
                    notFull.signalAll();
                    System.out.println("消费了资源" + l + "资源个数" + storage.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
