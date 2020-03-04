package ru.skillbranch.skillarticles.extensions

import android.text.Layout

/**
 * Get the line height of a line.
 */
fun Layout.getLineHeight(line: Int): Int {
    return getLineTop(line.inc()) - getLineTop(line)
}

/**
 * Returns the top of the Layout after removing the extra padding applied by  the Layout.
 */
fun Layout.getLineTopWithoutPadding(line: Int): Int {
    var lineTop = getLineTop(line)
    val l = getLineLeft(line)
    val s = getLineStart(line)
    val e = getLineEnd(line)
    val w = getLineWidth(line)
    val vw = getLineVisibleEnd(line)
    val v = getLineForVertical(200)

    val lh =  getLineBottom(line) - getLineTop(line)
    val lha =  getLineDescent(line) - getLineAscent(line)
    val p = getPrimaryHorizontal(s)
    val leading = paint.fontMetrics.leading
    val text = text.toString().substring(s,e)
    if (line == 0) {
        lineTop -= topPadding
    }
    bottomPadding
    return lineTop
}

/**
 * Returns the bottom of the Layout after removing the extra padding applied by the Layout.
 */
fun Layout.getLineBottomWithoutPadding(line: Int): Int {
    var lineBottom = getLineBottomWithoutSpacing(line)
    if (line == lineCount.dec()) {
        lineBottom -= bottomPadding
    }
    return lineBottom
}

/**
 * Get the line bottom discarding the line spacing added.
 */
fun Layout.getLineBottomWithoutSpacing(line: Int): Int {
    val lineBottom = getLineBottom(line)
    val isLastLine = line == lineCount.dec()
    val hasLineSpacing = spacingAdd != 0f

    var lineTop = getLineTop(line)
    var lineBot = getLineBottom(line)
    val l = getLineLeft(line)
    val s = getLineStart(line)
    val e = getLineEnd(line)
    val w = getLineWidth(line)
    val vw = getLineVisibleEnd(line)
    val v = getLineForVertical(200)

    val lh =  getLineBottom(line) - getLineTop(line)
    val lha =  getLineDescent(line) - getLineAscent(line)
    val p = getPrimaryHorizontal(s)
    val leading = paint.fontMetrics.leading
    val texta = text.toString().substring(s,e)

    val lc = lineCount
    if(line!=0 && line != lineCount.dec()){
        val sa = getLineStart(line.dec())
        val ea = getLineEnd(line.dec())
        val prevLh = getLineBottom(line.dec()) - getLineTop(line.dec())
        val prevText = text.toString().substring(sa,ea)
        val sb = getLineStart(line.inc())
        val eb = getLineEnd(line.inc())
        val lastLh =getLineBottom(line.inc()) - getLineTop(line.inc())
        val lastText = text.toString().substring(sb,eb)
    }

    return if (!hasLineSpacing || isLastLine) {
        lineBottom + spacingAdd.toInt()
    } else {
        lineBottom - spacingAdd.toInt()
    }
}