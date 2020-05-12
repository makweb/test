package ru.skillbranch.skillarticles.utils

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.skillarticles.data.models.CommentItemData
import ru.skillbranch.skillarticles.ui.article.CommentVH
import ru.skillbranch.skillarticles.ui.article.CommentsAdapter
import ru.skillbranch.skillarticles.viewmodels.article.ArticleViewModel
import ru.skillbranch.skillarticles.viewmodels.base.ViewModelFactory

class TestActivity : AppCompatActivity() {
    val viewModel by viewModels<ArticleViewModel> {
        ViewModelFactory(
            owner = this,
            params = "0"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_test)
        val test_rv = RecyclerView(this).apply {
            id = 999
        }
        setContentView(test_rv)
        test_rv.adapter = CommentsAdapter({})
        test_rv.layoutManager = LinearLayoutManager(this@TestActivity)

        viewModel.observeList(this){
            Log.e("TestActivity", "submit: ${it.lastKey}");
            (test_rv.adapter as PagedListAdapter<CommentItemData, CommentVH>).submitList(it)
        }

//        (test_rv.layoutManager as LinearLayoutManager).scrollToPosition(40)
    }
}
