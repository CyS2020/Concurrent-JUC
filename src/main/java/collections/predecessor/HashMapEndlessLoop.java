package collections.predecessor;

import java.util.HashMap;

/**
 * @author: CyS2020
 * @date: 2021/2/17
 * 描述：演示HashMap在多线程下造成死循环的情况-1.7 此处是1.8只分析原理
 * 并不会出现死循环，如想看死循环则需要下载jdk1.7，transfer方法里打断点，if(rehash)
 */
public class HashMapEndlessLoop {

    private static HashMap<Integer, String> map = new HashMap<>(2, 1.5f);

    public static void main(String[] args) {
        map.put(5, "C");
        map.put(7, "B");
        map.put(3, "A");
        new Thread(() -> {
            map.put(15, "D");
            System.out.println(map);
        }, "Thread1").start();

        new Thread(() -> {
            map.put(1, "E");
            System.out.println(map);
        }, "Thread2").start();
    }

}
