package ua.syt0r.furiganit;

import android.util.Log;

import java.util.List;

public class KuromojiHelper {

    public static boolean isKana(char c) {
        return (isHiragana(c) || isKatakana(c));
    }

    public static boolean isHiragana(char c) {
        return (('\u3041' <= c) && (c <= '\u309e'));
    }

    public static boolean isKatakana(char c) {
        return (isHalfWidthKatakana(c) || isFullWidthKatakana(c));
    }

    public static boolean isHalfWidthKatakana(char c) {
        return (('\uff66' <= c) && (c <= '\uff9d'));
    }

    public static boolean isFullWidthKatakana(char c) {
        return (('\u30a1' <= c) && (c <= '\u30fe'));
    }

    public static boolean isKanji(char c) {
        if (('\u4e00' <= c) && (c <= '\u9fa5')) {
            return true;
        }
        if (('\u3005' <= c) && (c <= '\u3007')) {
            return true;
        }
        return false;
    }

    public static char toHiragana(char c) {
        if (isFullWidthKatakana(c)) {
            return (char) (c - 0x60);
        } else if (isHalfWidthKatakana(c)) {
            return (char) (c - 0xcf25);
        }
        return c;
    }

    public static String stringToHiragana(String str){
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : str.toCharArray())
            stringBuilder.append(toHiragana(c));
        return stringBuilder.toString();
    }
    /*
    static String getFurigana(String input){
        Tokenizer tokenizer = new Tokenizer();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<ruby>");
        List<Token> tokens = tokenizer.tokenize(input);
        for (Token token : tokens){
            Log.wtf("sytor",token.getAllFeatures());
            stringBuffer.append("<rb>").append(token.getSurface()).append("</rb><rt>");
            if (isKanji(token.getSurface().charAt(0)))
                stringBuffer.append(stringToHiragana(token.getReading()));
            stringBuffer.append("</rt>");
        }
        stringBuffer.append("</ruby>");
        return stringBuffer.toString();
    }*/

}
