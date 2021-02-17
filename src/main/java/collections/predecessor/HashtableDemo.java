package collections.predecessor;

import java.util.Hashtable;

/**
 * @author: CyS2020
 * @date: 2021/2/17
 * 描述：演示Hashtable方法
 */
public class HashtableDemo {

    public static void main(String[] args) {
        Hashtable<String, String> hashTable = new Hashtable<>();
        hashTable.put("学完以后跳槽涨薪幅度", "80%");
        System.out.println(hashTable.get("学完以后跳槽涨薪幅度"));
    }
}
