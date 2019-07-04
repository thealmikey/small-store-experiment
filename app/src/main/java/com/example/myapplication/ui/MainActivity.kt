package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.dao.BlogDao
import com.example.myapplication.model.Blog
import com.example.myapplication.service.BlogService.api.BlogRemoteRaw
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        var bottomNav = findViewById<BottomNavigationView>(R.id.navigationView)
        NavigationUI.setupWithNavController(bottomNav, navController)
    }

}
