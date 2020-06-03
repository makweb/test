package ru.skillbranch.skillarticles.utils

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.ui.custom.ShimmerDrawable


class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val view = View(this)
        val view = LinearLayout(this)
            .apply { orientation = LinearLayout.VERTICAL }
        addContentView(
            view,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        val inflater = LayoutInflater.from(this)
        val ch = inflater.inflate(R.layout.item_article, view, false)
        ch.foreground = ShimmerDrawable.fromView(ch) { v ->
            when {
                v.width == v.height && v.width <= 80 -> ShimmerDrawable.Shape.Round(
                    v.width,
                    offset = v.left to v.top
                )
                v is TextView && v.lineCount > 1 && v.id != R.id.tv_title -> ShimmerDrawable.Shape.TextRow(
                    v.width,
                    v.lineCount,
                    v.textSize.toInt(),
                    v.lineHeight - v.textSize.toInt(),
                    offset = v.left to v.top + v.firstlineTop
                )
                else -> ShimmerDrawable.Shape.Rectangle(v.width, v.height, offset = v.left to v.top)
            }
        }
            ShimmerDrawable.Builder().build().apply {
                setShimmerAngle(-15f)
                setShimmerDuration(1800)
                start()
            }
        view.addView(ch)
    }
}

fun test() {

    val path = Path()
    path.moveTo(-50f, 0f)
    path.cubicTo(-40f, -60f, 40f, -60f, 50f, 0f)
    path.close()
    val m = Matrix()
    val hornL = Path()
    hornL.addRoundRect(RectF(-2f, -60f, 2f, 0f), 2f, 2f, Path.Direction.CW)
    m.setRotate(30f)
    hornL.transform(m)
    path.op(hornL, Path.Op.UNION)

    val hornR = Path()
    hornR.set(hornL)
    m.setScale(-1f, 1f)
    hornR.transform(m)
    path.op(hornR, Path.Op.UNION)

    val eye = Path()
    eye.addCircle(0f, -30f, 4f, Path.Direction.CW)
    m.reset()
    m.setRotate(50f)
    eye.transform(m)
    path.op(eye, Path.Op.DIFFERENCE)

    val eyeR = Path()
    eyeR.set(eye)
    m.setScale(-1f, 1f)
    eyeR.transform(m)
    path.op(eye, Path.Op.DIFFERENCE)

    path.offset(50f, 60f)
//    canvas.drawPath(path, Paint().apply { color = Color.RED })
}

private val TextView.firstlineTop: Int
    get() = totalPaddingTop + baseline + paint.fontMetrics.ascent.toInt() + context.dpToIntPx(3)
