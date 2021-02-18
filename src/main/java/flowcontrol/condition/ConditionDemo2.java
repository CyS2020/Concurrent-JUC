package flowcontrol.condition;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: CyS2020
 * @date: 2021/2/18
 * 描述：演示用Condition实现生产者消费者模式
 */
public class ConditionDemo2 {

    private final int queueSize = 10;

    private final Queue<Integer> queue = new PriorityQueue<>(queueSize);

    private final Lock lock = new ReentrantLock();

    private final Condition notFull = lock.newCondition();

    private final Condition notEmpty = lock.newCondition();

    public static void main(String[] args) {
        ConditionDemo2 conditionDemo2 = new ConditionDemo2();
        Producer producer = conditionDemo2.new Producer();
        Consumer consumer = conditionDemo2.new Consumer();
        producer.start();
        consumer.start();
    }

    public class Consumer extends Thread{

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true){
                lock.lock();
                try{
                    while (queue.size() == 0){
                        System.out.println("队列空，等待数据");
                        notEmpty.await();
                    }
                    queue.poll();
                    notFull.signalAll();
                    System.out.println("从队列里取走了一个数据，队列占据空间 " + queue.size() + " 个元素");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public class Producer extends Thread{

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true){
                lock.lock();
                try{
                    while (queue.size() == queueSize){
                        System.out.println("队列满，等待消费");
                        notFull.await();
                    }
                    queue.offer(1);
                    notEmpty.signalAll();
                    System.out.println("从队列里添加了一个数据，队列剩余空间 " + (queueSize - queue.size()) + " 个元素");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
