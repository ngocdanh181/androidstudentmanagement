package com.example.studentmanager

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.studentmaneger.Student
import com.example.studentmaneger.StudentDao
import com.example.studentmaneger.StudentDatabase
import com.example.stydentmaneger.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private val db: StudentDatabase by lazy {
        Room.databaseBuilder(applicationContext, StudentDatabase::class.java, "student_db").build()
    }
    private val studentDao: StudentDao by lazy { db.studentDao() }
    private lateinit var student: Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initializeStudent()
        setupDeleteButton()
        setupUpdateButton()
    }

    private fun initializeStudent() {
        student = intent.getSerializableExtra("student") as? Student ?: return
        findViewById<TextView>(R.id.detail_mssv).text = student.mssv
        findViewById<TextView>(R.id.detail_fullname).text = student.fullName
        findViewById<TextView>(R.id.detail_date).text = student.dateOfBirth
        findViewById<TextView>(R.id.detail_country).text = student.country
    }

    private fun setupDeleteButton() {
        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            deleteStudent()
        }
    }

    private fun setupUpdateButton() {
        findViewById<Button>(R.id.btn_update).setOnClickListener {
            updateStudentDetails()
        }
    }

    private fun deleteStudent() {
        lifecycleScope.launch(Dispatchers.IO) {
            studentDao.deleteStudent(student)
            finishWithResult()
        }
    }

    private fun updateStudentDetails() {
        lifecycleScope.launch(Dispatchers.IO) {
            val mssv = findViewById<EditText>(R.id.detail_mssv).text.toString()
            val fullName = findViewById<EditText>(R.id.detail_fullname).text.toString()
            val dateOfBirth = findViewById<EditText>(R.id.detail_date).text.toString()
            val country = findViewById<EditText>(R.id.detail_country).text.toString()

            student.apply {
                this.mssv = mssv
                this.fullName = fullName
                this.dateOfBirth = dateOfBirth
                this.country = country
            }

            studentDao.updateStudent(student)
            finishWithResult()
        }
    }

    private fun finishWithResult() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }
}
