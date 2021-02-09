package immutable;

/**
 * @author: CyS2020
 * @date: 2021/2/9
 * 描述：
 */
public class FinalStringDemo2 {

    public static void main(String[] args) {
        String a = "wukong2";
        final String b = getDashixiong();
        String c = b + 2;
        System.out.println(a==c);
    }

    private static String getDashixiong() {
        return "wukong";
    }
}
