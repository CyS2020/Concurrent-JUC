package practice;

/**
 * @author: CyS2020
 * @date: 2021/1/25
 * 描述：演示哲学家就餐问题导致的死锁
 * 修复死锁的策略：
 * 服务员检查(避免策略)
 * 改变一个哲学家拿叉子的书序(避免策略)
 * 餐票(避免策略)
 * 领导调节(检测与恢复策略)
 */
public class Philosopher implements Runnable {

    private final Object leftChopstick;

    private final Object rightChopstick;

    public Philosopher(Object leftChopstick, Object rightChopstick) {
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
    }

    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] chopsticks = new Object[philosophers.length];
        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new Object();
        }
        for (int i = 0; i < philosophers.length; i++) {
            Object leftChopstick = chopsticks[i];
            Object rightChopstick = chopsticks[(i + 1) % chopsticks.length];
            if (i == philosophers.length - 1) {
                philosophers[i] = new Philosopher(rightChopstick, leftChopstick);
            } else {
                philosophers[i] = new Philosopher(leftChopstick, rightChopstick);
            }
            new Thread(philosophers[i], "哲学家" + (i + 1) + "号").start();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                doAction("thinking");
                synchronized (leftChopstick) {
                    doAction("pick up left chopstick");
                    synchronized (rightChopstick) {
                        doAction("pick up right chopstick-eating");
                        doAction("put down right chopstick");
                    }
                    doAction("put down left chopstick");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAction(String action) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " " + action);
        Thread.sleep((long) (Math.random() * 10));
    }

}
