package com.example.employeemanagementsystem
import androidx.room.*

@Dao
interface EmployeeDao {
    @Insert
    fun insertEmployee(employee: Employee)

    @Query("SELECT * FROM employee_table ORDER BY firstName ASC")
    fun getAllEmployees(): List<Employee>

    @Query("SELECT * FROM employee_table WHERE id = :id")
    fun getEmployeeById(id: Int): Employee

    @Update
    fun updateEmployee(employee: Employee)

    @Query("DELETE FROM employee_table WHERE id = :id")
    fun deleteEmployeeById(id: Int)

}
