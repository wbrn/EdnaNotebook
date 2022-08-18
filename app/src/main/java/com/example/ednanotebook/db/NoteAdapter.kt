package com.example.ednanotebook.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ednanotebook.R
import com.example.ednanotebook.databinding.RcItemBinding
import com.example.ednanotebook.db.Note

class NoteAdapter : ListAdapter<Note, NoteAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: RcItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(note: Note) = with(binding){
            tvTitle.text = note.title
            tvTime.text = note.time
        }
        companion object{
            fun create(parent: ViewGroup): ItemHolder{
                return ItemHolder(RcItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}