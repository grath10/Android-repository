package com.example.dell.app;

public class Demo {
    public static void main(String[] args) {
        try {
            byte[] arr = "天气预报".getBytes("GBK");
            System.out.println(getJineiMa("天气预报"));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("1234");
    }

    public static String getJineiMa(String chineseName){
        StringBuffer sb = new StringBuffer();
        try {
            char[]ch = chineseName.toCharArray();
            for (char c : ch){
                if (isChinese(c)){
                    byte[]by = String.valueOf(c).getBytes("GBK");
                    for (byte b : by){
                        sb.append(Integer.toHexString(b & 0xff));
                    }
                    System.out.println(sb);
                }else{
                    byte b = (byte) c;
                    sb.append(Integer.toHexString(b & 0xff));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString().toUpperCase().trim();
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
}
