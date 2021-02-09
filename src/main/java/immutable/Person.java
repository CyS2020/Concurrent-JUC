package immutable;

/**
 * @author: CyS2020
 * @date: 2021/2/9
 * 描述：不可变的对象，演示其他类无法修改这个对象，public也不行
 */
public class Person {

    public final int age = 18;

    public final String name = "Alice";

    public String bag = "computer";
}
