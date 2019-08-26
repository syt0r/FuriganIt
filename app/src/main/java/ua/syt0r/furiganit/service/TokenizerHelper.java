package ua.syt0r.furiganit.service;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import java.util.List;
import ua.syt0r.furiganit.Utils;

public class TokenizerHelper {

    public static String getTextWithFurigana(Tokenizer tokenizer, String text) {

        List<Token> tokens;
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ruby style=\"font-size:8vw;\">");
        tokens = tokenizer.tokenize(text);
        for (Token token : tokens){
            stringBuilder.append("<rb>").append(token.getSurface()).append("</rb><rt>");
            if (Utils.isKanji(token.getSurface().charAt(0)))
                stringBuilder.append(Utils.stringToHiragana(token.getReading()));
            stringBuilder.append("</rt>");
        }
        stringBuilder.append("</ruby>");

        return stringBuilder.toString();

    }

}
