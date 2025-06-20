package com.example.employeemanagementsystem

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class EditEmployeeActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var employeeId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_employee)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.title = "EmployeeManagementSystem"

        db = AppDatabase.getDatabase(applicationContext)

        employeeId = intent.getIntExtra("employeeId", -1)

        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etDesignation = findViewById<EditText>(R.id.etDesignation)
        val etSalary = findViewById<EditText>(R.id.etSalary)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnSave.isEnabled = false // initially disabled

        // Enabled only when both names are not blank
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val valid = etFirstName.text.toString().trim().isNotEmpty() &&
                        etLastName.text.toString().trim().isNotEmpty()
                btnSave.isEnabled = valid
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        etFirstName.addTextChangedListener(watcher)
        etLastName.addTextChangedListener(watcher)

        // Preload data into fields
        if (employeeId != -1) {
            Thread {
                val employee = db.employeeDao().getEmployeeById(employeeId)
                runOnUiThread {
                    etFirstName.setText(employee.firstName)
                    etLastName.setText(employee.lastName)
                    etEmail.setText(employee.email)
                    etPhone.setText(employee.phone)
                    etAddress.setText(employee.address)
                    etDesignation.setText(employee.designation)
                    etSalary.setText(employee.salary.toString())
                    btnSave.isEnabled = true
                }
            }.start()
        }

        // Save edits
        btnSave.setOnClickListener {
            val updated = Employee(
                id = employeeId,
                firstName = etFirstName.text.toString().trim(),
                lastName = etLastName.text.toString().trim(),
                email = etEmail.text.toString().trim(),
                phone = etPhone.text.toString().trim(),
                address = etAddress.text.toString().trim(),
                designation = etDesignation.text.toString().trim(),
                salary = etSalary.text.toString().toDoubleOrNull() ?: 0.0
            )

            Thread {
                db.employeeDao().updateEmployee(updated)
                runOnUiThread {
                    Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }

        // Delete with confirmation
        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to remove this employee from database?")
                .setPositiveButton("Yes") { _, _ ->
                    Thread {
                        db.employeeDao().deleteEmployeeById(employeeId)
                        runOnUiThread {
                            Toast.makeText(this, "Employee deleted", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }.start()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
