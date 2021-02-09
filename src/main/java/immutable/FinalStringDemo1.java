package immutable;

/**
 * @author: CyS2020
 * @date: 2021/2/9
 * 描述：真假美猴王
 */
public class FinalStringDemo1 {

    public static void main(String[] args) {
        String a = "wukong2";
        // 编译期间已经知道b的准确的值，当做编译时期的常量来使用
        final String b = "wukong";
        String d = "wukong";
        // 编译时c会直接计算出为wukong2
        String c = b + 2;
        // e运行时才能确定，在堆上创建
        String e = d + 2;
        // a、c指向常量池，e指向堆
        System.out.println(a==c);
        System.out.println(a==e);
    }
}
