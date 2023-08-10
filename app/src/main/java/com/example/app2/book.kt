package com.example.app2

import android.os.Parcel
import android.os.Parcelable

data class book(
    val bookauthor: String = "",
    val bookcourse: String = "",
    var bookcatalog: String = "",
    var bookname: String = "",
    var book_image_url: String = "",
    var bookdatepublish: String = "",
    var bookquantity: String =  "",
    var bookyear: String = "",
    var description: String = "",
    var totalbookborrowed: String = "",
    var totaldays: String = "",
    var totalreview: String ="",
    var date: String ="",
    var childno: String = "",
    var averagereview: String = "",
    var subjects: List<String> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bookauthor)
        parcel.writeString(bookcourse)
        parcel.writeString(bookcatalog)
        parcel.writeString(bookname)
        parcel.writeString(book_image_url)
        parcel.writeString(bookdatepublish)

        parcel.writeString(bookquantity)
        parcel.writeString(bookyear)
        parcel.writeString(description)
        parcel.writeString(totalbookborrowed)
        parcel.writeString(totaldays)
        parcel.writeString(totalreview)
        parcel.writeString(date)
        parcel.writeString(childno)
        parcel.writeString(averagereview)
        parcel.writeStringList(subjects)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<book> {
        override fun createFromParcel(parcel: Parcel): book {
            return book(parcel)
        }

        override fun newArray(size: Int): Array<book?> {
            return arrayOfNulls(size)
        }
    }
}
