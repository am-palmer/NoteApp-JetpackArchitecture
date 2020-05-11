package amichaelpalmer.kotlin.noteappjetpack

import amichaelpalmer.kotlin.noteappjetpack.data.Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackarchitecturedemo.R

class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteViewHolder>(DIFF_CALLBACK) {
    private var notes: List<Note> = ArrayList()
    private var listener: OnItemLongTapListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote: Note = getItem(position)
        holder.getTitle.text = currentNote.getTitle
        holder.getDescription.text = currentNote.getDescription
        holder.getPriority.text = currentNote.getPriority.toString()
    }

    fun getNoteAtPosition(position: Int): Note {
        return getItem(position)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.getTitle == newItem.getTitle && oldItem.getDescription == newItem.getDescription
                        && oldItem.getPriority == newItem.getPriority
            }
        }
    }

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.text_view_title)
        private val description: TextView = view.findViewById(R.id.text_view_description)
        private val priority: TextView = view.findViewById(R.id.text_view_priority)

        val getTitle get() = title
        val getDescription get() = description
        val getPriority get() = priority

        init {
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemLongTap(getItem(position))
                }
                true
            }
        }

    }

    interface OnItemLongTapListener {
        fun onItemLongTap(note: Note)
    }

    fun setOnItemLongTapListener(listener: OnItemLongTapListener) {
        this.listener = listener
    }
}