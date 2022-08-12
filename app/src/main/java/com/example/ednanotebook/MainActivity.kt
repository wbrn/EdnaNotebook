package com.example.ednanotebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ednanotebook.databinding.ActivityMainBinding
import com.example.ednanotebook.db.Adapter
import com.example.ednanotebook.db.DbManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.w3c.dom.Text



class MainActivity : AppCompatActivity() {
    lateinit var bindingClass: ActivityMainBinding
    val DbManager = DbManager( this)
    val MyAdapter = Adapter(ArrayList(), this)
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        val view = bindingClass.root
        setContentView(view)
        init()
        initSearchView()
    }

    override fun onDestroy(){
        super.onDestroy()
        DbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        DbManager.openDB()
        fillAdapter("")
    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)

        }


    fun init(){
        bindingClass.rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(bindingClass.rcView)
        bindingClass.rcView.adapter = MyAdapter
    }


    fun initSearchView(){
    bindingClass.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(text: String?): Boolean {
//            val list = DbManager.readDbData(text!!)
//            MyAdapter.updateAdapter(list)
                fillAdapter(text!!)
            return true
        }
    })

    }



    private fun fillAdapter(text:String){

      job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch{

            val list = DbManager.readDbData(text)
            MyAdapter.updateAdapter(list)
            if(list.size > 0){

                bindingClass.tvNoElements.visibility = View.GONE
            } else{
                bindingClass.tvNoElements.visibility = View.VISIBLE}
        }


    }

    private fun getSwapMg(): ItemTouchHelper{
        return ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                MyAdapter.removeItem(viewHolder.adapterPosition, DbManager)

            }

        })
    }


    }


