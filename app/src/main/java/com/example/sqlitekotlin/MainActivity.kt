package com.example.sqlitekotlin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlitekotlin.databinding.ActivityMainBinding
import com.example.sqlitekotlin.databinding.LayoutNoteItemBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var notesRecyclerViewAdapter: RecyclerViewAdapter<Note>
    private lateinit var layoutNoteItemBinding : LayoutNoteItemBinding
    private lateinit var notes: List<Note>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val contentView = activityMainBinding.root
        setContentView(contentView)

        val rvNotes = activityMainBinding.rvNotes
        val btnAddNote = activityMainBinding.btnAddNote

        databaseHelper = DatabaseHelper(this)

        notes = databaseHelper.getAllNotes()

        val verticalLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        notesRecyclerViewAdapter = RecyclerViewAdapter(R.layout.layout_note_item, notes, true) {view, item, position ->
            layoutNoteItemBinding = LayoutNoteItemBinding.bind(view)
            val id = item.id
            val title = item.title
            val content = item.content
            layoutNoteItemBinding.tvNoteTitle.text = title
            layoutNoteItemBinding.tvNoteContent.text = content
            layoutNoteItemBinding.ivEditButton.setOnClickListener {
                val intent = Intent(this, EditNoteActivity::class.java).apply {
                    putExtra("note_id", id)
                }
                startActivity(intent)
            }
            layoutNoteItemBinding.ivDeleteButton.setOnClickListener {
                showDeleteNoteConfirmDialog(id)
            }
        }
        rvNotes.apply {
            layoutManager = verticalLinearLayoutManager
            setHasFixedSize(true)
            adapter = notesRecyclerViewAdapter
        }

        btnAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val newNotes = databaseHelper.getAllNotes()
        notesRecyclerViewAdapter.refreshData(newNotes)
    }

    private fun showDeleteNoteConfirmDialog(noteId: Int) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Note.")
        alertDialog.setMessage("Are you sure to delete this note?")
        alertDialog.setPositiveButton("Yes") { _, _ ->
            val isSuccessful = databaseHelper.deleteNoteById(noteId)
            val newNotes = databaseHelper.getAllNotes()
            notesRecyclerViewAdapter.refreshData(newNotes)
            if (isSuccessful) {
                Toast.makeText(this, "Note Deleted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete note!", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.setNegativeButton("No") { _, _ ->

        }
        alertDialog.create().show()
    }
}