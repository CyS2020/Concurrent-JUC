package collections.predecessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: CyS2020
 * @date: 2021/2/17
 * 描述：演示Collections.synchronizedList(new ArrayList<E>())
 */
public class SynList {

    public static void main(String[] args) {
        List<Object> list = Collections.synchronizedList(new ArrayList<>());
        list.add(5);
        System.out.println(list.get(0));
    }
}
