package ua.syt0r.furiganit.utils

import com.atilika.kuromoji.ipadic.Tokenizer

fun isKana(c: Char): Boolean {
    return isHiragana(c) || isKatakana(c)
}

fun isHiragana(c: Char): Boolean {
    return '\u3041' <= c && c <= '\u309e'
}

fun isKatakana(c: Char): Boolean {
    return isHalfWidthKatakana(c) || isFullWidthKatakana(c)
}

fun isHalfWidthKatakana(c: Char): Boolean {
    return '\uff66' <= c && c <= '\uff9d'
}

fun isFullWidthKatakana(c: Char): Boolean {
    return '\u30a1' <= c && c <= '\u30fe'
}

fun isKanji(c: Char): Boolean {
    if ('\u4e00' <= c && c <= '\u9fa5') {
        return true
    }
    return if ('\u3005' <= c && c <= '\u3007') {
        true
    } else false
}

fun toHiragana(c: Char): Char {
    if (isFullWidthKatakana(c)) {
        return (c.toInt() - 0x60).toChar()
    } else if (isHalfWidthKatakana(c)) {
        return (c.toInt() - 0xcf25).toChar()
    }
    return c
}

fun stringToHiragana(str: String): String {
    val stringBuilder = StringBuilder()
    for (c in str.toCharArray())
        stringBuilder.append(toHiragana(c))
    return stringBuilder.toString()
}

fun removeFurigana(str: String): String {
    return str.replace("(?s)<rt[^>]*>.*?</rt>".toRegex(), "")
            .replace("[(<ruby>)(</ruby>)(<rb>)(</rb>)]".toRegex(), "")
}

fun getReading(str: String): String {

    var str = str
    str = str.replace("[(<ruby>)(</ruby>)]".toRegex(), "")
    var result = ""
    var rbBeginIndex = 0
    var rbEndIndex = 0
    var rtBeginIndex = 0
    var rtEndIndex = -1

    while (rbBeginIndex != -1) {

        rbBeginIndex = str.indexOf("<rb>", rtEndIndex)
        rbEndIndex = str.indexOf("</rb>", rbBeginIndex)
        rtBeginIndex = str.indexOf("<rt>", rbEndIndex)
        rtEndIndex = str.indexOf("</rt>", rtBeginIndex)

        if (rtEndIndex - rtBeginIndex < 5)
            result += str.substring(rbBeginIndex + 4, rbEndIndex)
        else
            result += str.substring(rtBeginIndex + 4, rtEndIndex)

    }

    return result

}

fun getTextWithFurigana(tokenizer: Tokenizer, text: String): String {

    val stringBuilder = StringBuilder()
    stringBuilder.append("<ruby style=\"font-size:8vw;\">")

    val tokens = tokenizer.tokenize(text)

    for (token in tokens) {
        stringBuilder.append("<rb>").append(token.surface).append("</rb><rt>")

        if (isKanji(token.surface[0]))
            stringBuilder.append(stringToHiragana(token.reading))

        stringBuilder.append("</rt>")
    }

    stringBuilder.append("</ruby>")

    return stringBuilder.toString()

}