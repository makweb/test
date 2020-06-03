package ru.skillbranch.skillarticles.ui.custom.markdown

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.isEmpty
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.doOnLayout
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.data.repositories.MarkdownElement
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.extensions.groupByBounds
import ru.skillbranch.skillarticles.extensions.setPaddingOptionally
import ru.skillbranch.skillarticles.ui.custom.ShimmerDrawable
import kotlin.properties.Delegates

class MarkdownContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private lateinit var elements: List<MarkdownElement>
    private var layoutManager: LayoutManager = LayoutManager()

    var textSize by Delegates.observable(14f) { _, old, value ->
        if (value == old) return@observable
        this.children.forEach {
            it as IMarkdownView
            it.fontSize = value
        }
    }
    var isLoading by Delegates.observable(false) { _, _, newValue ->
        if (!newValue) hideShimmer()
        else showShimmer()
    }
    private val defaultSpace = context.dpToIntPx(8)
    private val lineHeight = context.dpToIntPx(14)
    private val miniLineHeight = context.dpToIntPx(12)

    private val shimmerDrawable by lazy {
        ShimmerDrawable.Builder()
            .addShape(
                ShimmerDrawable.Shape.TextRow(
                    width - 4 * defaultSpace,
                    6,
                    lineHeight = lineHeight,
                    lineSpace = defaultSpace,
                    cornerRadius = defaultSpace,
                    offset = 2 * defaultSpace to 2 * defaultSpace
                )
            )
            .addShape(
                ShimmerDrawable.Shape.ImagePlaceholder(
                    width - 4 * defaultSpace,
                    16 / 9f,
                    borderWidth = defaultSpace,
                    cornerRadius = defaultSpace,
                    offset = 2 * defaultSpace to defaultSpace
                )
            )
            .addShape(
                ShimmerDrawable.Shape.TextRow(
                    width - 14 * defaultSpace,
                    1,
                    lineHeight = miniLineHeight,
                    lineSpace = defaultSpace / 2,
                    cornerRadius = defaultSpace,
                    offset = 7 * defaultSpace to defaultSpace
                )
            )
            .addShape(
                ShimmerDrawable.Shape.TextRow(
                    width - 4 * defaultSpace,
                    4,
                    lineHeight = lineHeight,
                    lineSpace = defaultSpace,
                    cornerRadius = defaultSpace,
                    offset = 2 * defaultSpace to 2 * defaultSpace
                )
            )
            .addShape(
                ShimmerDrawable.Shape.Rectangle(
                    width - 4 * defaultSpace,
                    context.dpToIntPx(56),
                    cornerRadius = defaultSpace,
                    offset = 2 * defaultSpace to defaultSpace
                )
            )
            .addShape(
                ShimmerDrawable.Shape.TextRow(
                    width - 4 * defaultSpace,
                    4,
                    lineHeight = lineHeight,
                    lineSpace = defaultSpace,
                    offset = 2 * defaultSpace to 2 * defaultSpace
                )
            )
            .itemPatternCount(4)
            .setBaseColor(context.getColor(R.color.color_gray_light))
            .setHighlightColor(context.getColor(R.color.color_divider))
            .build()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var usedHeight = paddingTop
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)

        children.forEach {
            measureChild(it, widthMeasureSpec, heightMeasureSpec)
            usedHeight += it.measuredHeight
        }

        usedHeight += paddingBottom
        setMeasuredDimension(width, if (usedHeight < minimumHeight) minimumHeight else usedHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var usedHeight = paddingTop
        val bodyWidth = right - left - paddingLeft - paddingRight
        val left = paddingLeft
        val right = paddingLeft + bodyWidth

        children.forEach {
            if (it is MarkdownTextView) {
                it.layout(
                    left - paddingLeft / 2,
                    usedHeight,
                    r - paddingRight / 2,
                    usedHeight + it.measuredHeight
                )
            } else {
                it.layout(
                    left,
                    usedHeight,
                    right,
                    usedHeight + it.measuredHeight
                )
            }
            usedHeight += it.measuredHeight
        }
    }

    fun setContent(content: List<MarkdownElement>) {
        elements = content
        var index = 0
        content.forEach {
            when (it) {
                is MarkdownElement.Text -> {
                    val tv = MarkdownTextView(context, textSize).apply {
                        setPaddingOptionally(
                            left = defaultSpace,
                            right = defaultSpace
                        )
                        setLineSpacing(fontSize * 0.5f, 1f)
                    }

                    MarkdownBuilder(context)
                        .markdownToSpan(it)
                        .run {
                            tv.setText(this, TextView.BufferType.SPANNABLE)
                        }

                    addView(tv)
                }

                is MarkdownElement.Image -> {
                    val iv = MarkdownImageView(
                        context,
                        textSize,
                        it.image.url,
                        it.image.text,
                        it.image.alt
                    )
                    addView(iv)
                    layoutManager.attachToParent(iv, index)
                    index++
                }

                is MarkdownElement.Scroll -> {
                    val sv = MarkdownCodeView(
                        context,
                        textSize,
                        it.blockCode.text
                    )
                    addView(sv)
                    layoutManager.attachToParent(sv, index)
                    index++
                }
            }
        }
    }

    fun renderSearchResult(searchResult: List<Pair<Int, Int>>) {
        children.forEach { view ->
            view as IMarkdownView
            view.clearSearchResult()
        }

        if (searchResult.isEmpty()) return

        val bounds = elements.map { it.bounds }
        val result = searchResult.groupByBounds(bounds)

        children.forEachIndexed { index, view ->
            view as IMarkdownView
            //search for child with markdown element offset
            view.renderSearchResult(result[index], elements[index].offset)
        }
    }

    fun renderSearchPosition(
        searchPosition: Pair<Int, Int>?
    ) {
        searchPosition ?: return
        val bounds = elements.map { it.bounds }

        val index = bounds.indexOfFirst { (start, end) ->
            val boundRange = start..end
            val (startPos, endPos) = searchPosition
            startPos in boundRange && endPos in boundRange
        }

        if (index == -1) return
        val view = getChildAt(index)
        view as IMarkdownView
        view.renderSearchPosition(searchPosition, elements[index].offset)
    }

    fun clearSearchResult() {
        children.forEach { view ->
            view as IMarkdownView
            view.clearSearchResult()
        }
    }

    fun setCopyListener(listener: (String) -> Unit) {
        children.filterIsInstance<MarkdownCodeView>()
            .forEach { it.copyListener = listener }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = SavedState(super.onSaveInstanceState())
        state.layout = layoutManager
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if (state is SavedState) layoutManager = state.layout
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>?) {
        //save children manually without markdown text view
        children.filter { it !is MarkdownTextView }
            .forEach { it.saveHierarchyState(layoutManager.container) }
        //save only markdownContentView
        dispatchFreezeSelfOnly(container)
    }

    private fun hideShimmer() {
        (foreground as? ShimmerDrawable)?.stop()
        foreground = null
    }

    private fun showShimmer() {
        doOnLayout{
            foreground = shimmerDrawable
            shimmerDrawable.start()
        }
    }

    private class LayoutManager() : Parcelable {
        var ids: MutableList<Int> = mutableListOf()
        var container: SparseArray<Parcelable> = SparseArray()

        constructor(parcel: Parcel) : this() {
            ids = parcel.createIntArray()!!.toMutableList()
            Log.e("MarkdownContentView", "parcel: $ids.");
//            ids = arr.toMutableList()
            container =
                parcel.readSparseArray<Parcelable>(this::class.java.classLoader) as SparseArray<Parcelable>
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            Log.e("MarkdownContentView", "write parcel: ${ids.size}.");
            parcel.writeIntArray(ids.toIntArray())
            parcel.writeSparseArray(container)
        }

        fun attachToParent(view: View, index: Int) {
            if (container.isEmpty()) {
                view.id = ViewCompat.generateViewId()
                ids.add(view.id)
            } else {
                view.id = ids[index]
                view.restoreHierarchyState(container)
            }
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<LayoutManager> {
            override fun createFromParcel(parcel: Parcel): LayoutManager = LayoutManager(parcel)
            override fun newArray(size: Int): Array<LayoutManager?> = arrayOfNulls(size)

        }
    }

    private class SavedState : BaseSavedState, Parcelable {
        lateinit var layout: LayoutManager

        constructor(superState: Parcelable?) : super(superState)

        @Suppress("UNCHECKED_CAST")
        constructor(src: Parcel) : super(src) {
            //restore state from parcel
            layout = src.readParcelable(LayoutManager::class.java.classLoader)!!
        }

        override fun writeToParcel(dst: Parcel, flags: Int) {
            //write state to parcel
            super.writeToParcel(dst, flags)
            dst.writeParcelable(layout, flags)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel) = SavedState(parcel)

            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }
}