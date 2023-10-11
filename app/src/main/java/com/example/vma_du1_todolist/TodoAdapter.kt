package com.example.vma_du1_todolist

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.vma_du1_todolist.databinding.RecyclerviewItemBinding


class TodoAdapter(private var todos: MutableList<Item>, private val context: Context?) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(val binding: RecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerviewItemBinding.inflate(layoutInflater, parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.binding.apply {
            titleTextView.text = todos[position].title
            datetimeTextView.text =
                Item.Format.dateTimeFormat.format(todos[position].date)
            if (todos[position].date <= System.currentTimeMillis() && !todos[position].done) {
                datetimeTextView.setTextColor(Color.RED)
            } else {
                datetimeTextView.setTextColor(titleTextView.textColors)
            }

            if (todos[position].done) {
                titleTextView.paintFlags = titleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                datetimeTextView.paintFlags =
                    datetimeTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                titleTextView.paintFlags =
                    titleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                datetimeTextView.paintFlags =
                    datetimeTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            if (todos[position].done) {
                checkButton.icon =
                    ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_cancel_24)

            } else {
                checkButton.icon =
                    ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_check_24)
            }
            checkButton.setOnClickListener {
                todos[position].done = !todos[position].done
                notifyItemChanged(position)
                (context as MainActivity).setAlarms()
            }
            deleteButton.setOnClickListener {
                todos.remove(todos[position])
                notifyDataSetChanged()
                (context as MainActivity).setAlarms()
            }

        }
    }

    override fun getItemCount() = todos.size

}