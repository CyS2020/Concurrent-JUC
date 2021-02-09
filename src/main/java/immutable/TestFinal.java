package immutable;

/**
 * @author: CyS2020
 * @date: 2021/2/9
 * 描述：测试final能否被修改
 */
public class TestFinal {

    public static void main(String[] args) {
        final Person person = new Person();
        person.bag = "book";
    }
}
