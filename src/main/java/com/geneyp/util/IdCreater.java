package com.gym.util;

public class IdCreater {

    private final static String STR = "0123456789abcdefghijklmnopqrstuvwxyz";
    private final static int PIXLEN = STR.length();
    private static volatile int pixOne = 0;
    private static volatile int pixTwo = 0;
    private static volatile int pixThree = 0;
    private static volatile int pixFour = 0;

    final public synchronized static String create15() {
        StringBuilder sb = new StringBuilder(15);// 创建一个StringBuilder
        sb.append(Long.toHexString(System.currentTimeMillis()));// 先添加当前时间的毫秒值的16进制
        pixFour++;
        if (pixFour == PIXLEN) {
            pixFour = 0;
            pixThree++;
            if (pixThree == PIXLEN) {
                pixThree = 0;
                pixTwo++;
                if (pixTwo == PIXLEN) {
                    pixTwo = 0;
                    pixOne++;
                    if (pixOne == PIXLEN) {
                        pixOne = 0;
                    }
                }
            }
        }
        return sb.append(STR.charAt(pixOne)).append(STR.charAt(pixTwo)).append(STR.charAt(pixThree))
                .append(STR.charAt(pixFour)).toString();
    }

    public static void main(String[] args) {
        IdCreater.create15();
        IdCreater.create15();
        IdCreater.create15();
        IdCreater.create15();
    }
}
