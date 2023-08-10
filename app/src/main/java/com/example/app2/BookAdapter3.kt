package com.example.app2

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
class BookAdapter3(private val context: Context, var bookList: List<BookDetails>) :
    RecyclerView.Adapter<BookAdapter3.BookViewHolder>() {

    private var filteredBookList: List<BookDetails> = bookList
    private val db = FirebaseDatabase.getInstance().getReference("borrowbook")
    val nodeName1 = MainActivity2.nodeName
    var onItemClick: ((BookDetails) -> Unit)? = null

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text1: TextView = itemView.findViewById(R.id.text1)
        val text3: TextView = itemView.findViewById(R.id.text3)
        val text4: TextView = itemView.findViewById(R.id.text4)
        val text5: TextView = itemView.findViewById(R.id.text5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item2, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredBookList.size
    }

    fun search(query: String) {
        filteredBookList = if (query.isEmpty()) {
            bookList.filter { it.nodeName == nodeName1 } // filter based on nodeName
        } else {
            bookList.filter { BookDetails ->
                BookDetails.nodeName == nodeName1 && (BookDetails.bookname?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true
                        ||    BookDetails.borrowername?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true)
            }
        }
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val bookDetails = filteredBookList[position]
        holder.text1.text = bookDetails.bookname

        // Format date as "d MMMM y"
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputDateFormat.parse(bookDetails.date)
        val outputDateFormat = SimpleDateFormat("d MMMM y", Locale.getDefault())
        val formattedDate = outputDateFormat.format(date)
        holder.text3.text = formattedDate

        holder.text4.text = bookDetails.currentid
        holder.text5.text = bookDetails.status

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(bookDetails)
            showBookDetailsDialog(bookDetails)
        }
    }


    fun setData(bookList: List<BookDetails>) {
        this.bookList = bookList
        filteredBookList = bookList.filter { it.nodeName == nodeName1 } // filter based on nodeName
        notifyDataSetChanged()
    }
    private fun showBookDetailsDialog(BookDetails: BookDetails) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.book_details_dialog1)
        val button3 = dialog.findViewById<Button>(R.id.button3)
        val button4 = dialog.findViewById<Button>(R.id.button4)
        val textView22 = dialog.findViewById<TextView>(R.id.textView22)
        val textView25 = dialog.findViewById<TextView>(R.id.textView25)
        val textView27 = dialog.findViewById<TextView>(R.id.textView27)
        val textView29 = dialog.findViewById<TextView>(R.id.textView29)



        textView22.text = BookDetails.status
        textView25.text = BookDetails.date
        textView27.text = BookDetails.dateborrow
        textView29.text = BookDetails.duedate


        val rootNode = FirebaseDatabase.getInstance().reference


        if (BookDetails.status == "pending") {
            // If the status is "pending", show the cancel button
            button3.visibility = View.VISIBLE
            button3.setOnClickListener {
                val db2 = FirebaseDatabase.getInstance().getReference("book")
                val db1 = FirebaseDatabase.getInstance().getReference("borrowbook")
                val dbRef = FirebaseDatabase.getInstance().getReference("users")
                val db3 = FirebaseDatabase.getInstance().getReference("review")
                val childRef = db3.child(BookDetails.currentid!!)

                // Add a ValueEventListener to db1 reference
                db1.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (borrowbookSnapshot in dataSnapshot.children) {
                            val borrowbook = borrowbookSnapshot.getValue(BookDetails::class.java)
                            if (borrowbook?.currentid == BookDetails.currentid) {
                                // If the borrowbook record matches the currentid, delete it
                                borrowbookSnapshot.ref.removeValue()
                                break
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors here
                    }
                })

                dialog.dismiss()
            }
        } else {
            // If the status is not "pending", hide the cancel button
            button3.visibility = View.GONE
        }

        dialog.show()
    }
            fun updateBookList(bookList: List<BookDetails>) {
                this.bookList = bookList
                this.filteredBookList = bookList
                notifyDataSetChanged()
            }

    fun fetchBookList() {
        db.orderByChild("nodeName").equalTo(nodeName1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val bookList = mutableListOf<BookDetails>()
                    for (postSnapshot in dataSnapshot.children) {
                        val bookDetails = postSnapshot.getValue(BookDetails::class.java)
                        if (bookDetails?.status in listOf("pending", "waiting", "borrowed") && bookDetails?.nodeName == nodeName1) {
                            bookList.add(bookDetails!!)
                        }
                    }
                    val sortedBookList = bookList.sortedByDescending { it.date }
                    updateBookList(sortedBookList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors
                }
            })
    }

            fun sortByAuthor() {
                bookList = bookList.sortedBy { it.bookname }
                notifyDataSetChanged()
            }

            fun sortByName() {
                bookList = bookList.sortedBy { it.borrowername }
                notifyDataSetChanged()
            }

            fun sortByDate() {
                bookList = bookList.sortedBy { it.dateborrow }
                notifyDataSetChanged()
            }

            fun sortByQuantity() {
                bookList = bookList.sortedByDescending { it.date }
                notifyDataSetChanged()
            }


        }

