package com.example.sqlitekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sqlitekotlin.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var activityAddNoteBinding: ActivityAddNoteBinding
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddNoteBinding = ActivityAddNoteBinding.inflate(layoutInflater)
        val contentView = activityAddNoteBinding.root
        setContentView(contentView)

        databaseHelper = DatabaseHelper(this)

        val ivDoneButton = activityAddNoteBinding.ivDoneButton
        val etNoteTitle = activityAddNoteBinding.etNoteTitle
        val etNoteContent = activityAddNoteBinding.etNoteContent

        ivDoneButton.setOnClickListener {
            val title = etNoteTitle.text.toString().trim()
            val content = etNoteContent.text.toString().trim()
            val note = Note(id = 0 ,title = title, content = content)
            val isSuccessful = databaseHelper.addNote(note)
            finish()
            if (isSuccessful) {
                Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to save note!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}