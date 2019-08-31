package ua.syt0r.furiganit.utils;

public class Utils {

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

    public static String removeFurigana(String str){
        return str.replaceAll("(?s)<rt[^>]*>.*?</rt>","")
                .replaceAll("[(<ruby>)(</ruby>)(<rb>)(</rb>)]","");
    }

    public static String getReading(String str){
        str = str.replaceAll("[(<ruby>)(</ruby>)]","");
        String result = "";
        int rbBeginIndex = 0 , rbEndIndex = 0, rtBeginIndex = 0, rtEndIndex = -1;

        while (rbBeginIndex!=-1){

            rbBeginIndex = str.indexOf("<rb>",rtEndIndex);
            rbEndIndex = str.indexOf("</rb>",rbBeginIndex);
            rtBeginIndex = str.indexOf("<rt>",rbEndIndex);
            rtEndIndex = str.indexOf("</rt>",rtBeginIndex);

            if(rtEndIndex-rtBeginIndex<5)
                result+=str.substring(rbBeginIndex+4,rbEndIndex);
            else
                result+=str.substring(rtBeginIndex+4,rtEndIndex);

        }

        return result;

    }

}
