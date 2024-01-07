package com.example.studentmaneger

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.studentmaneger.Student
import com.example.studentmaneger.StudentDao
import com.example.studentmaneger.StudentDatabase
import com.example.stydentmaneger.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsertStudentActivity : AppCompatActivity() {

    private lateinit var db: StudentDatabase
    private lateinit var studentDao: StudentDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_student)

        setupAutoCompleteCountries()
        setupDatePicker()
        setupSaveButton()

        initializeDatabase()
    }

    private fun setupAutoCompleteCountries() {
        val countries = resources.getStringArray(R.array.countries)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countries)
        val autoCountry = findViewById<AutoCompleteTextView>(R.id.autoCountry)
        autoCountry.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        val datePickerButton = findViewById<ImageButton>(R.id.btn_calendar)
        datePickerButton.setOnClickListener {
            val datePicker = DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    val dateEditText = findViewById<EditText>(R.id.edt_date)
                    dateEditText.setText("${dayOfMonth}/${month + 1}/${year}")
                }, 2023, 12, 31)
            datePicker.show()
        }
    }

    private fun setupSaveButton() {
        val saveButton = findViewById<Button>(R.id.btn_save)
        saveButton.setOnClickListener {
            val student = createStudentFromInput()
            insertStudentIntoDatabase(student)
            setResultAndFinish()
        }
    }

    private fun createStudentFromInput(): Student {
        val country = findViewById<AutoCompleteTextView>(R.id.autoCountry).text.toString()
        val date = findViewById<EditText>(R.id.edt_date).text.toString()
        val mssv = findViewById<EditText>(R.id.edt_mssv).text.toString()
        val name = findViewById<EditText>(R.id.edt_fullname).text.toString()
        return Student(mssv, name, date, country)
    }

    private fun insertStudentIntoDatabase(student: Student) {
        lifecycleScope.launch(Dispatchers.IO) {
            studentDao.insertStudent(student)
        }
    }

    private fun setResultAndFinish() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    private fun initializeDatabase() {
        db = Room.databaseBuilder(applicationContext, StudentDatabase::class.java, "student_db").build()
        studentDao = db.studentDao()
    }
}
