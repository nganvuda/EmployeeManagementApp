package com.example.employeemanagementsystem

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var employeeAdapter: EmployeeAdapter
    // Room db instance
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.title = "EmployeeManagementSystem"

        // 1. Initialize database
        db = AppDatabase.getDatabase(applicationContext)

        // 2. Set up RecyclerView and adapter
        val recyclerView = findViewById<RecyclerView>(R.id.rvEmployees)
        recyclerView.layoutManager = LinearLayoutManager(this)
        employeeAdapter = EmployeeAdapter(this, listOf())
        recyclerView.adapter = employeeAdapter

        // 3. Add button AddEmployeeActivity
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener {
            val intent = Intent(this, AddEmployeeActivity::class.java)
            startActivity(intent)
        }
    }

    // Refresh list when activity resumes
    override fun onResume() {
        super.onResume()
        loadEmployees()
    }

    // get all employees and update the adapter
    fun loadEmployees() {
        Thread {
            try {
                val employeeList = db.employeeDao().getAllEmployees()
                Log.d("DEBUG", "Loaded ${employeeList.size} employees")
                runOnUiThread {
                    employeeAdapter.updateEmployees(employeeList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error loading employees", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
