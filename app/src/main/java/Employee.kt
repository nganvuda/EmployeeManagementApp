package com.example.employeemanagementsystem

import androidx.room.Entity
import androidx.room.PrimaryKey

// make a table named "employee_table"
@Entity(tableName = "employee_table")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // so Room knows to generate one
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val address: String,
    val designation: String,
    val salary: Double
)


