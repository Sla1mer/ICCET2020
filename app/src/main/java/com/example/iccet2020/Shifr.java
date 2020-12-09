package com.example.iccet2020;

public class Shifr {
    public String hifr_zezarya(String text)
    {
        byte random = (byte) (Math.random()*54+1);
        char[] chars = text.toCharArray();
        StringBuilder text2 = new StringBuilder();
        text2.append((char) random);
        for (int b = 0; chars.length > b; b++)
        {
            chars[b] = (char) (chars[b] + random);
            text2.append(chars[b]);
        }
        System.out.println(text2);
        return text2.toString();
    }

    public String dehifator(String Text)
    {
        char[] chars = Text.toCharArray();
        StringBuilder test2 = new StringBuilder();
        byte random = (byte) chars[0];
        for (byte b = 0; chars.length > b; b++)
        {
            chars[b] = (char) (chars[b] - random);
            test2.append(chars[b]);
        }
        System.out.println(test2);
        return test2.toString();
    }

    public String hifr_zezaryaEmail(String text)
    {
        char[] chars = text.toCharArray();
        StringBuilder text2 = new StringBuilder();
        text2.append((char) 67);
        for (int b = 0; chars.length > b; b++)
        {
            chars[b] = (char) (chars[b] + 67);
            text2.append(chars[b]);
        }
        System.out.println(text2);
        return text2.toString();
    }

    public String dehifatorEmail(String Text)
    {
        char[] chars = Text.toCharArray();
        StringBuilder test2 = new StringBuilder();
        for (byte b = 0; chars.length > b; b++)
        {
            chars[b] = (char) (chars[b] - 67);
            test2.append(chars[b]);
        }
        System.out.println(test2);
        return test2.toString();
    }
}
