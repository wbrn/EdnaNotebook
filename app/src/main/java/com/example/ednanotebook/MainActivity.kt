package com.example.ednanotebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ednanotebook.databinding.ActivityMainBinding
import com.example.ednanotebook.db.Adapter
import com.example.ednanotebook.db.DbManager
import org.w3c.dom.Text



class MainActivity : AppCompatActivity() {
    lateinit var bindingClass: ActivityMainBinding
    val DbManager = DbManager( this)
    val MyAdapter = Adapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        val view = bindingClass.root
        setContentView(view)
        init()
    }

    override fun onDestroy(){
        super.onDestroy()
        DbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        DbManager.openDB()
        fillAdapter()
    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)

        }


    fun init(){
        bindingClass.rcView.layoutManager = LinearLayoutManager(this)
        bindingClass.rcView.adapter = MyAdapter
    }


    fun fillAdapter(){

        MyAdapter.updateAdapter((DbManager.readDbData()))
    }

    }


