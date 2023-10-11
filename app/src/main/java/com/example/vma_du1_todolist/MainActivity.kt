package com.example.vma_du1_todolist

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vma_du1_todolist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val COUNT_TAG = "COUNT"
    private val TITLE_TAG = "TITLE"
    private val DATE_TAG = "DATE"
    private val DONE_TAG = "DONE"
    private var requestCode = 0
    private val pendingIntents = mutableListOf<PendingIntent>()

    private lateinit var alarmManager: AlarmManager
    private lateinit var adapter: TodoAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val todos = DataSource.data
        adapter = TodoAdapter(todos, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        preferences = getSharedPreferences("todo", Context.MODE_PRIVATE)
        setUpDataSource()

        binding.deleteAllButton.setOnClickListener {
            DataSource.data.removeIf { it.done }
            adapter.notifyDataSetChanged()
            setAlarms()
        }
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, NewItemActivity::class.java)
            this.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        setAlarms()

    }

    fun setAlarms() {
        for (pendingIntent in pendingIntents) {
            alarmManager.cancel(pendingIntent)
        }
        pendingIntents.clear()
        for (item in DataSource.data) {
            if (!item.done && item.date > System.currentTimeMillis()) {
                setAlarm(item.date, item.title)
            }

        }
    }


    override fun onPause() {
        super.onPause()
        val editor = preferences.edit()
        val size = DataSource.data.size
        val data = DataSource.data
        editor.putInt(COUNT_TAG, size)
        for (i in 0 until size) {
            editor.putString("$TITLE_TAG$i", data[i].title)
            editor.putLong("$DATE_TAG$i", data[i].date)
            editor.putBoolean("$DONE_TAG$i", data[i].done)
        }
        editor.apply()
    }

    private fun setUpDataSource() {
        DataSource.data.clear()
        val size = preferences.getInt(COUNT_TAG, 0)
        for (i in 0 until size) {
            val title = preferences.getString("$TITLE_TAG$i", "")
            val date = preferences.getLong("$DATE_TAG$i", 0L)
            val done = preferences.getBoolean("$DONE_TAG$i", false)
            val item =
                Item(
                    title = title!!,
                    date = date,
                    done = done,
                )
            DataSource.data.add(item)
        }

    }

    private fun setAlarm(time: Long, title: String) {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("MESSAGE", title)
        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0)
        pendingIntents.add(pendingIntent)
        alarmManager.setExact(AlarmManager.RTC, time, pendingIntent)
        requestCode++

    }
}