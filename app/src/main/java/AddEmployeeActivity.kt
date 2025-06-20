package com.example.employeemanagementsystem

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class AddEmployeeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.title = "EmployeeManagementSystem"

        // Initialize db
        val db = AppDatabase.getDatabase(applicationContext)

        // input fields from layout
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etDesignation = findViewById<EditText>(R.id.etDesignation)
        val etSalary = findViewById<EditText>(R.id.etSalary)

        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            // Read inputs
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val designation = etDesignation.text.toString().trim()
            val salary = etSalary.text.toString().toDoubleOrNull() ?: 0.0

            // validation
            if (firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(this, "Both first and last name are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create employee and save to DB
            Thread {
                val newEmployee = Employee(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    phone = phone,
                    address = address,
                    designation = designation,
                    salary = salary
                )

                db.employeeDao().insertEmployee(newEmployee)

                runOnUiThread {
                    Toast.makeText(this, "Employee added successfully!", Toast.LENGTH_SHORT).show()
                    finish() // go back to main activity
                }
            }.start()
        }
    }
}
