package ru.skillbranch.skillarticles.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.data.local.entities.CategoryData
import ru.skillbranch.skillarticles.databinding.ItemCategoryDialogBinding

class CategoryAdapter(private val listener: (String, Boolean) -> Unit) :
    ListAdapter<CategoryDataItem, CategoryVH>(CategoryDiffCallback()) {
    LayoutInflater.from(parent.context).inflate(R.layout.item_category_dialog, parent, false)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH = CategoryVH(
        ItemCategoryDialogBinding.inflate(),
        listener
    )

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.bind(getItem(position))
    }
}

class CategoryVH(val containerView: View, val listener: (String, Boolean) -> Unit) :
    RecyclerView.ViewHolder(containerView) {

    fun bind(item: CategoryDataItem) {
        //remove listener
       /* ch_select.setOnCheckedChangeListener(null)
        //bind data
        ch_select.isChecked = item.isChecked
        Glide.with(containerView.context)
            .load(item.icon)
            .apply(RequestOptions.circleCropTransform())
            .override(iv_icon.width)
            .into(iv_icon)
        tv_category.text = item.title
        tv_count.text = "${item.articlesCount}"

        //set listeners
        ch_select.setOnCheckedChangeListener { _, checked -> listener(item.categoryId, checked) }
        itemView.setOnClickListener { ch_select.toggle() }*/
    }

}

class CategoryDiffCallback() : DiffUtil.ItemCallback<CategoryDataItem>() {
    override fun areItemsTheSame(oldItem: CategoryDataItem, newItem: CategoryDataItem): Boolean =
        oldItem.categoryId == newItem.categoryId

    override fun areContentsTheSame(oldItem: CategoryDataItem, newItem: CategoryDataItem): Boolean =
        oldItem == newItem
}

data class CategoryDataItem(
    val categoryId: String,
    val icon: String,
    val title: String,
    val articlesCount: Int = 0,
    val isChecked: Boolean = false
)

fun CategoryData.toItem(checked:Boolean = false) = CategoryDataItem(categoryId, icon, title, articlesCount, checked)