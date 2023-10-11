package com.example.vma_du1_todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vma_du1_todolist.databinding.ActivityNewItemBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class NewItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select time")
                .build()

        binding = ActivityNewItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            var good = true
            if (binding.titleEditText.text.isEmpty()) {
                binding.titleEditText.error = "Title required"
                good = false
            } else {
                binding.titleEditText.error = null
            }
            if (binding.dateEditText.text.isEmpty()) {
                binding.dateEditText.error = "Date required"
                good = false
            } else {
                binding.dateEditText.error = null
            }
            if (binding.timeEditText.text.isEmpty()) {
                binding.timeEditText.error = "Time required"
                good = false
            } else {
                binding.timeEditText.error = null
            }
            if (good) {
                val newItem = Item(
                    binding.titleEditText.text.toString(),
                    datePicker.selection!! - (2 * 3600000) + (timePicker.hour * 3600000) + (timePicker.minute * 60000),
                )
                DataSource.data.add(newItem)
                finish()
            }

        }

        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.dateEditText.setOnClickListener {
            datePicker.show(supportFragmentManager, "date")
            datePicker.addOnPositiveButtonClickListener {
                val dateFormat = Item.Format.dateFormat
                binding.dateEditText.setText(dateFormat.format(datePicker.selection))
            }

        }

        binding.timeEditText.setOnClickListener {
            timePicker.show(supportFragmentManager, "time")
            timePicker.addOnPositiveButtonClickListener {
                binding.timeEditText.setText(
                    Item.Format.formatTime(
                        timePicker.hour,
                        timePicker.minute
                    )
                )
            }

        }
    }

}