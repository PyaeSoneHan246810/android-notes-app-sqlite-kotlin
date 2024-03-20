package com.example.sqlitekotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


class DatabaseHelper(
    context: Context
): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT);"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val query = "DROP TABLE IF EXISTS $TABLE_NAME;"
        db?.execSQL(query)
        onCreate(db)
    }

    //CREATE (Add Note)
    fun addNote(note: Note): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        val result = db.insert(TABLE_NAME, null, cv)
        db.close()
        return result != -1L
    }

    //READ (Get All Notes)
    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME;"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
            val note = Note(id = id, title = title, content = content)
            notes.add(note)
        }
        cursor.close()
        db.close()
        return notes
    }

    //Read (Get Single Note by ID)
    fun getNoteById(noteId: Int): Note {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId;"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val note = Note(id = id, title = title, content = content)
        cursor.close()
        db.close()
        return note
    }

    //UPDATE (Edit Note)
    fun editNote(newNote: Note): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_TITLE, newNote.title)
            put(COLUMN_CONTENT, newNote.content)
        }
        val result = db.update(TABLE_NAME, cv, "$COLUMN_ID = ?", arrayOf(newNote.id.toString()))
        db.close()
        return result != -1
    }

    //DELETE (Delete Single Note by ID) {
    fun deleteNoteById(noteId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(noteId.toString()))
        db.close()
        return result != -1
    }
}