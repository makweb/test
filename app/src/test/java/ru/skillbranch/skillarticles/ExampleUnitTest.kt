package ru.skillbranch.skillarticles

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.SpannableString
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import ru.skillbranch.skillarticles.data.longText
import ru.skillbranch.skillarticles.data.repositories.MarkdownParser
import ru.skillbranch.skillarticles.data.repositories.clearContent
import ru.skillbranch.skillarticles.extensions.*
import ru.skillbranch.skillarticles.ui.custom.markdown.MarkdownImageView
import ru.skillbranch.skillarticles.ui.custom.markdown.SearchBgHelper
import ru.skillbranch.skillarticles.ui.custom.spans.HeaderSpan
import ru.skillbranch.skillarticles.ui.custom.spans.SearchSpan

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    companion object {
        lateinit var mockContext: Context
        @BeforeClass
        @JvmStatic
        fun setupMocks() {
            mockkStatic("ru.skillbranch.skillarticles.extensions.ContextKt")
            mockContext = mockk<Context>(relaxUnitFun = true, relaxed = true) {
                every { attrValue(R.attr.colorSecondary) } returns Color.RED
                every { dpToIntPx(56) } returns 112
                every { dpToIntPx(8) } returns 16
                every { dpToIntPx(4) } returns 8
                every { dpToIntPx(1) } returns 2
                every { dpToPx(8) } returns 16f
                every { dpToPx(4) } returns 8f
                every { attrValue(R.attr.colorSurface) } returns Color.RED
                every { attrValue(R.attr.colorOnSurface) } returns Color.WHITE
                every { attrValue(R.attr.colorOnBackground) } returns Color.BLACK
                every { getColor(R.color.color_divider) } returns Color.LTGRAY
//                every { getApplicationContext()} returns this
//                every { getPackageManager()} returns mockk(relaxed = true){
//                    every { getApplicationInfo() } returns mockk(relaxed = true)
//                }
            }
            mockkStatic("com.bumptech.glide.Glide")
            every { Glide.with(mockContext) } returns mockk(relaxed = true) {
                every { load(any<String>()) } returns mockk<RequestBuilder<Drawable>>(relaxed = true) {
                    every { transform(any()) } returns mockk(relaxed = true)
                }
            }


//            every { mockContext.dpToPx(any())} returns 0f
        }
    }

    @Test
    fun group_by_bounds() {
        val query = "background"
        val expectedResult: List<List<Pair<Int, Int>>> = listOf(
            listOf(25 to 35, 92 to 102, 153 to 163),
            listOf(220 to 230),
            listOf(239 to 249),
            listOf(330 to 340),
            listOf(349 to 359),
            listOf(421 to 431),
            listOf(860 to 870, 954 to 964),
            listOf(1084 to 1094),
            listOf(1209 to 1219, 1355 to 1365, 1795 to 1805),
            listOf(),
            listOf(
                2115 to 2125,
                2357 to 2367,
                2661 to 2671,
                2807 to 2817,
                3314 to 3324,
                3348 to 3358,
                3423 to 3433,
                3623 to 3633,
                3711 to 3721,
                4076 to 4086
            ),
            listOf(),
            listOf(5766 to 5776, 5897 to 5907, 5939 to 5949),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf()
        )
        val rawContent = MarkdownParser.parse(longText)

        val bounds = rawContent.map { it.bounds }

        val searchResult = rawContent.clearContent()
            .indexesOf(query)
            .map { it to it + query.length }

        val result = searchResult.groupByBounds(bounds)

        assertEquals(expectedResult, result)


    }


    /* @Test
     fun test2() {
         val mockContext = mock(Context::class.java)
         `when`(mockContext.attrValue(R.attr.colorSecondary)).thenReturn(Color.RED)
 //        `when`(mockContext.attrValue(R.attr.colorSecondary)).thenReturn(Color.RED)
         val spySearchBgHelper = Mockito.spy(SearchBgHelper(mockContext))
     }*/

    @Test
    fun test3() {

        val headerSpan = mockk<HeaderSpan>() {
            every { firstLineBounds } returns 0..26
            every { lastLineBounds } returns 48..69
            every { topExtraPadding } returns 24
            every { bottomExtraPadding } returns 28
        }
//        every { headerSpan getProperty "firstLineBounds" } returns 0..22


        val str =
            "Header1 for first line and for second line also header for third line\nsimple text on line"

        /*mockkStatic("ru.skillbranch.skillarticles.extensions.ContextKt")
        val mockContext = mockk<Context>()
        every { mockContext.attrValue(R.attr.colorSecondary) } returns Color.RED
        every { mockContext.dpToIntPx(4) } returns 8
        every { mockContext.dpToIntPx(1) } returns 2
        every { mockContext.dpToPx(8) } returns 16f*/

        val mockSpannable = mockk<SpannableString>() {
            every { getSpans(any(), any(), HeaderSpan::class.java) } returns arrayOf(headerSpan)
            every { getSpans(any(), any(), SearchSpan::class.java) } returns arrayOf(SearchSpan())
            every { length } returns str.length
        }

        val mockCanvas = mockk<Canvas>()
        val mockLayout = mockk<Layout>() {
            every { getLineForOffset(any()) } answers {
                when (firstArg<Int>()) {
                    in 0..26 -> 0
                    in 26..48 -> 1
                    in 48..69 -> 2
                    else -> 3
                }
            }
            every { getPrimaryHorizontal(any()) } answers {
                when (firstArg<Int>()) {
                    0 -> 0f
                    8 -> 226f
                    23 -> 524f
                    54 -> 172f
                    70 -> 0f
                    84 -> 76f
                    else -> 0f
                }
            }
            every { getLineTop(any()) } answers {
                when (firstArg<Int>()) {
                    0 -> 0
                    1 -> 97
                    2 -> 170
                    else -> 271
                }
            }
            every { getLineBottom(any()) } answers {
                when (firstArg<Int>()) {
                    0 -> 90
                    1 -> 163
                    2 -> 264
                    else -> 304
                }
            }
            every { getLineLeft(any()) } returns 0f
            every { getLineRight(any()) } answers {
                when (firstArg<Int>()) {
                    0 -> 617f
                    1 -> 489f
                    else -> 640f
                }
            }
            every { bottomPadding } returns 0
            every { topPadding } returns 0
            every { lineCount } returns 4
            every { spacingAdd } returns 7f
        }
        val mockDrawable = mockk<Drawable>(relaxed = true)

        mockkConstructor(SearchBgHelper::class, recordPrivateCalls = true)
        every { anyConstructed<SearchBgHelper>() getProperty "drawableLeft" } returns mockDrawable
        every { anyConstructed<SearchBgHelper>() getProperty "drawableMiddle" } returns mockDrawable
        every { anyConstructed<SearchBgHelper>() getProperty "drawableRight" } returns mockDrawable
        every { anyConstructed<SearchBgHelper>() getProperty "drawable" } returns mockDrawable

        //singleline search
        every { mockSpannable.getSpanStart(any<SearchSpan>()) } returns 0
        every { mockSpannable.getSpanEnd(any<SearchSpan>()) } returns 8

        SearchBgHelper(mockContext).draw(mockCanvas, mockSpannable, mockLayout)

        //multiline search
        every { mockSpannable.getSpanStart(any<SearchSpan>()) } returns 23
        every { mockSpannable.getSpanEnd(any<SearchSpan>()) } returns 54

        SearchBgHelper(mockContext).draw(mockCanvas, mockSpannable, mockLayout)

        verifyAll {
            //singleline search
            mockDrawable.setBounds(-8, 24, 234, 83)
            mockDrawable.draw(mockCanvas)

            //multiline search
            mockDrawable.setBounds(516, 24, 625, 83)
            mockDrawable.draw(mockCanvas)
            mockDrawable.setBounds(-8, 97, 497, 156)
            mockDrawable.draw(mockCanvas)
            mockDrawable.setBounds(-8, 170, 180, 229)
            mockDrawable.draw(mockCanvas)
        }
        /*sb.append(
            "Header1 for first line\nfor second line\nfor third line\nsimple text on line",
            headerSpan,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        sb.setSpan(SearchSpan(), 8, 11, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append(
            "simple text on line"
        )*/


    }

    @Test
    fun test4() {
        val expectedWidth = 740
        val expectedHeight = 415
        /*val spyMarkdownImageView = spyk( MarkdownImageView(mockContext, 14f, "Any", "title", "altTitle"), recordPrivateCalls = true){
            every { addView(any<ImageView>()) } answers {
                this@spyk.iv_image = mockk(relaxed = true)
            }

        }*/

        mockkConstructor(MarkdownImageView::class, recordPrivateCalls = true)

        every { anyConstructed<MarkdownImageView>() getProperty "iv_image" } returns mockk<ImageView>(relaxed = true)

        /*every { anyConstructed<MarkdownImageView> getProperty "iv_image"} returns mockk<ImageView>(relaxed = true){
            every {measuredWidth} returns expectedWidth
            every {measuredHeight} returns expectedHeight
        }*/
        /*every { spyMarkdownImageView getProperty "iv_image"} returns mockk<ImageView>(relaxed = true){
            every {measuredWidth} returns expectedWidth
            every {measuredHeight} returns expectedHeight
        }*/
        /*every { spyMarkdownImageView getProperty "tv_title" } returns mockk<View>(){
            every {measuredWidth} returns expectedWidth
            every {measuredHeight} returns 32
        }
        every { spyMarkdownImageView getProperty "tv_alt" } returns mockk<View>(){
            every {measuredWidth} returns expectedWidth
            every {measuredHeight} returns 32 + 2*16
        }
        */
        MarkdownImageView(mockContext, 14f, "Any", "title", "altTitle").onMeasure(700, 700)


        verifyAll {
            //singleline search
            anyConstructed<MarkdownImageView>()["setMeasuredDimension"](0, 16)
        }
        /*sb.append(
            "Header1 for first line\nfor second line\nfor third line\nsimple text on line",
            headerSpan,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        sb.setSpan(SearchSpan(), 8, 11, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append(
            "simple text on line"
        )*/


    }
}
