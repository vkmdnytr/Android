package com.example.sosyalyazilim.entity.model

import android.os.Parcel
import android.os.Parcelable

open class RecyclerViewBaseItem(var type: Int = 0) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<RecyclerViewBaseItem> = object : Parcelable.Creator<RecyclerViewBaseItem> {
            override fun createFromParcel(source: Parcel): RecyclerViewBaseItem = RecyclerViewBaseItem(source)
            override fun newArray(size: Int): Array<RecyclerViewBaseItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(type)
    }
}