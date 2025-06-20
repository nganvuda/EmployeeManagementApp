package com.example.employeemanagementsystem

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class EmployeeAdapter(
    private val context: Context,
    private var employees: List<Employee>
) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvInitials: TextView = itemView.findViewById(R.id.tvInitials)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDesignation: TextView = itemView.findViewById(R.id.tvDesignation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.employee_item, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employees[position]
        Log.d("EMPLOYEE: ", "EMPLOYEE NAME ${employee.firstName}")

        // Handle null-safe initials
        val initials = "${employee.firstName.firstOrNull() ?: ""}${employee.lastName.firstOrNull() ?: ""}"
        holder.tvInitials.text = initials.uppercase()

        holder.tvName.text = "${employee.firstName} ${employee.lastName}"
        holder.tvDesignation.text = employee.designation

        // Click to open Edit screen
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditEmployeeActivity::class.java)
            intent.putExtra("employeeId", employee.id)
            context.startActivity(intent)
        }

        // Long click to delete
        holder.itemView.setOnLongClickListener {
            Thread {
                val db = AppDatabase.getDatabase(context)
                db.employeeDao().deleteEmployeeById(employee.id)

                if (context is MainActivity) {
                    context.runOnUiThread {
                        Toast.makeText(context, "Employee deleted", Toast.LENGTH_SHORT).show()
                        context.loadEmployees()
                    }
                }
            }.start()
            true
        }
    }

    override fun getItemCount(): Int = employees.size

    fun updateEmployees(newList: List<Employee>) {
        employees = newList
        notifyDataSetChanged()
    }
}
