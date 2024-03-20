package com.example.sqlitekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Toast
import com.example.sqlitekotlin.databinding.ActivityEditNoteBinding

class EditNoteActivity : AppCompatActivity() {
    private lateinit var activityEditNoteBinding: ActivityEditNoteBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var nodeId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditNoteBinding = ActivityEditNoteBinding.inflate(layoutInflater)
        val contentView = activityEditNoteBinding.root
        setContentView(contentView)

        val etNoteTitle = activityEditNoteBinding.etNoteTitle
        val etNoteContent = activityEditNoteBinding.etNoteContent
        val ivDoneButton = activityEditNoteBinding.ivDoneButton

        databaseHelper = DatabaseHelper(this)

        nodeId =  intent.getIntExtra("note_id", -1)
        if (nodeId == -1){
            finish()
            return
        }

        val note = databaseHelper.getNoteById(noteId = nodeId)
        val id = note.id
        val title = note.title
        val content = note.content

        etNoteTitle.setText(title)
        etNoteContent.setText(content)

        ivDoneButton.setOnClickListener {
            val editedTitle = etNoteTitle.text.toString().trim()
            val editedContent = etNoteContent.text.toString().trim()
            val editedNote = Note(id = id, title = editedTitle, content = editedContent)
            val isSuccessful = databaseHelper.editNote(editedNote)
            finish()
            if (isSuccessful) {
                Toast.makeText(this, "Note Edited!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to edit note!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}