package com.example.myapplication.ui.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Blog
import com.example.myapplication.service.BlogService.BlogStore
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject


class RemotePostsFragment : Fragment() {
    val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    val myBlogStore: BlogStore by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mRecyclerview = view.findViewById<RecyclerView>(R.id.myRemoteViewRecycler)


        var theAdapter = MyAdapter()
        myBlogStore.getBlogs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({ bloglist ->
                theAdapter.blogItems = bloglist
            }, {
                Log.e("RemotePostFragment", "failed got message ${it.message}")
            })


        mRecyclerview.adapter = theAdapter
        mRecyclerview.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remote_posts, container, false)
    }

    class BlogViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var blogTitle = view.findViewById<TextView>(R.id.blogTitle)
        var blogBody = view.findViewById<TextView>(R.id.blogBody)

        fun bindTo(blogTitleText: String, blogBodyText: String) {
            blogTitle.text = blogTitleText
            blogBody.text = blogBodyText
        }
    }

    class MyAdapter : RecyclerView.Adapter<BlogViewHolder>() {
        var blogItems: List<Blog> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
            var view: View = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, parent, false);
            return BlogViewHolder(view)
        }

        override fun getItemCount(): Int {
            return blogItems.size
        }

        override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
            var blogPost = blogItems[position]
            holder.bindTo(blogPost.title, blogPost.body)
        }
    }

}
