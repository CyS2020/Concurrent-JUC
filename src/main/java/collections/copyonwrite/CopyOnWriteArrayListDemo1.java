package collections.copyonwrite;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: CyS2020
 * @date: 2021/2/17
 * 描述：演示CopyOnWriteArrayList可以再迭代的过程中修改数组内容
 * 但是ArrayList不行
 */
public class CopyOnWriteArrayListDemo1 {

    public static void main(String[] args) {
        //List<String>  list = new ArrayList<>();
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()){
            System.out.println("list is "+list);
            String next = iterator.next();
            System.out.println(next);

            if (next.equals("2")){
                list.remove("5");
            }
            if (next.equals("3")){
                list.add("3 found");
            }
        }
    }
}
