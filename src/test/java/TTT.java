/**
 * 测试用
 * @author luojie
 * @since 2018-12-27
 */
public class TTT {
    public static void main(String[] args) {
//        splitTest();

//        maxPlusTest();

//        String s = tryCatchFinallyTest();
//        System.out.println("result = " + s);
    }

    /**
     * finally中return时，try、catch中的return不会执行。
     *
     * @return
     */
    private static String tryCatchFinallyTest(){
        try {
            System.out.println("try");
//            int n = 1 / 0;
            return "try";
        } catch (Exception e) {
            System.out.println("exception");
            return "exception";
        } finally {
            System.out.println("finally");
            // 程序在此处return
            return "finally";
        }
    }

    private static void maxPlusTest(){
        int a = Integer.MAX_VALUE;
        String x = Integer.toString(a, 2);
        System.out.println(x + "\t" + x.length());
        String x1 = Integer.toString(a + 1, 2);
        System.out.println(x1 + "\t" + x1.length());
        System.out.println(a + 1);
    }

    /**
     * split 最后的空值会被省略，中间空值会保留
     */
    private static void splitTest(){
        String str = "a,b,c,,,";
        String[] arr = str.split(",");
        for(String s : arr){
            System.out.println(s);
        }
        // "a,b,c,,," arr.length == 3
        System.out.println(arr.length);
    }
}
