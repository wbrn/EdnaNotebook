package com.example.ednanotebook.db

import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ednanotebook.EditActivity
import com.example.ednanotebook.R
import com.example.ednanotebook.databinding.RcItemBinding





class Adapter(listMain:ArrayList<ListItem>, contextM: Context) : RecyclerView.Adapter<Adapter.Holder>() {

//    inner class ViewHolder(val binding: RcItemBinding) : RecyclerView.ViewHolder(binding.root)

    var listArray = listMain
    var context = contextM





    class Holder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
         val tvTitle = itemView.findViewById<TextView>(R.id.tv_Title)!!
        val context = contextV

        fun setData(item:ListItem){

            tvTitle.text = item.title
            itemView.setOnClickListener{
                val intent = Intent(context, EditActivity::class.java).apply {

                    putExtra(MyIntentConstants.I_TITLE_KEY, item.title)
                    putExtra(MyIntentConstants.I_DESC_KEY, item.desc)
                    putExtra(MyIntentConstants.I_URI_KEY, item.uri)


                }
                context.startActivity(intent)
            }



        }


    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            RcItemBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val inflater = LayoutInflater.from(parent.context)
            return  Holder(inflater.inflate(R.layout.rc_item, parent, false), context)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {

            holder.setData(listArray.get(position))

        }

        override fun getItemCount(): Int {
            return listArray.size
        }

    fun updateAdapter(listItems:List<ListItem>){

        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    }