package com.example.myapplication.ui.home


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.database.dao.BlogDao
import com.example.myapplication.model.Blog
import com.example.myapplication.service.BlogService.BlogLocalService
import com.example.myapplication.service.BlogService.BlogRemoteService
import com.example.myapplication.service.BlogService.api.BlogRemoteRaw
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LocalPostFragment : Fragment() {
    val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    val myBlogService: BlogLocalService by inject()
    val myBlogRemoteService: BlogRemoteService by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mRecyclerview = view.findViewById<RecyclerView>(R.id.myRecycler)
//        var myPosts = listOf(
//            Blog(title = "hello", body = "first blog post"),
//            Blog(title = "hello 22", body = " this is another blog post, not my first blog post"),
//            Blog(title = "bye", body = "i have written a lot since my first blog post")
//        )

        var theTitle = view.findViewById<EditText>(R.id.myPostTitle)
        var theBody = view.findViewById<EditText>(R.id.myPostBody)
        var theSubmit = view.findViewById<Button>(R.id.submitPost)
        theSubmit.setOnClickListener {
            var blog = Blog(title = theTitle.text.toString(), body = theBody.text.toString())
            myBlogService.addBlog(blog)
                .flatMap {
                    myBlogRemoteService.addBlog(blog)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(scopeProvider)
                .subscribe({
                    Log.d("MainActivity", "inserted blog post successfully, $it")
                }, {
                    Log.e("MainActivity", "couldn insert blog post successfully")
                })
        }

        var theAdapter = MyAdapter()
        myBlogService.fetchAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe {
                theAdapter.blogItems = it
            }


        mRecyclerview.adapter = theAdapter
        mRecyclerview.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_post, container, false)
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
