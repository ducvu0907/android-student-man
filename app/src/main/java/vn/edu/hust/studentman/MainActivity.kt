package vn.edu.hust.studentman

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private val students = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003"),
    StudentModel("Phạm Thị Dung", "SV004"),
    StudentModel("Đỗ Minh Đức", "SV005"),
    StudentModel("Vũ Thị Hoa", "SV006"),
    StudentModel("Hoàng Văn Hải", "SV007"),
    StudentModel("Bùi Thị Hạnh", "SV008"),
    StudentModel("Đinh Văn Hùng", "SV009"),
    StudentModel("Nguyễn Thị Linh", "SV010"),
    StudentModel("Phạm Văn Long", "SV011"),
    StudentModel("Trần Thị Mai", "SV012"),
    StudentModel("Lê Thị Ngọc", "SV013"),
    StudentModel("Vũ Văn Nam", "SV014"),
    StudentModel("Hoàng Thị Phương", "SV015"),
    StudentModel("Đỗ Văn Quân", "SV016"),
    StudentModel("Nguyễn Thị Thu", "SV017"),
    StudentModel("Trần Văn Tài", "SV018"),
    StudentModel("Phạm Thị Tuyết", "SV019"),
    StudentModel("Lê Văn Vũ", "SV020")
  )

  private lateinit var studentAdapter: StudentAdapter
  private var deletedStudent: StudentModel? = null
  private var deletedStudentPosition: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    studentAdapter = StudentAdapter(students, ::onEditClick, ::onDeleteClick)

    findViewById<RecyclerView>(R.id.recycler_view_students).apply {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddStudentDialog()
    }
  }

  private fun showAddStudentDialog() {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null)
    val nameInput = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
    val idInput = dialogView.findViewById<EditText>(R.id.edit_text_student_id)

    AlertDialog.Builder(this)
      .setTitle("Add New Student")
      .setView(dialogView)
      .setPositiveButton("Add") { dialog, _ ->
        val name = nameInput.text.toString().trim()
        val id = idInput.text.toString().trim()
        if (name.isNotEmpty() && id.isNotEmpty()) {
          val newStudent = StudentModel(name, id)
          students.add(newStudent)
          studentAdapter.notifyItemInserted(students.size - 1)
        }
        dialog.dismiss()
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  private fun onEditClick(student: StudentModel, position: Int) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_student, null)
    val nameInput = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
    val idInput = dialogView.findViewById<EditText>(R.id.edit_text_student_id)

    nameInput.setText(student.studentName)
    idInput.setText(student.studentId)

    AlertDialog.Builder(this)
      .setTitle("Edit Student")
      .setView(dialogView)
      .setPositiveButton("Save") { dialog, _ ->
        val name = nameInput.text.toString().trim()
        val id = idInput.text.toString().trim()
        if (name.isNotEmpty() && id.isNotEmpty()) {
          students[position] = StudentModel(name, id)
          studentAdapter.notifyItemChanged(position)
        }
        dialog.dismiss()
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  private fun onDeleteClick(student: StudentModel, position: Int) {
    AlertDialog.Builder(this)
      .setTitle("Delete Student")
      .setMessage("Are you sure you want to delete this student?")
      .setPositiveButton("Delete") { dialog, _ ->
        deletedStudent = student
        deletedStudentPosition = position
        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        val snackbar = Snackbar.make(findViewById(R.id.recycler_view_students),
          "Student deleted",
          Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
          deletedStudent?.let {
            students.add(deletedStudentPosition, it)
            studentAdapter.notifyItemInserted(deletedStudentPosition)
          }
        }
        snackbar.show()

        dialog.dismiss()
      }
      .setNegativeButton("Cancel", null)
      .show()
  }
}
