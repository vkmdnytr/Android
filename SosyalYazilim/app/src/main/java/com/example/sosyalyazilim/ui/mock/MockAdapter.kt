package com.example.sosyalyazilim.ui.mock

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sosyalyazilim.R
import com.example.sosyalyazilim.entity.model.data.MockResultItem
import kotlinx.android.synthetic.main.list_item_mock.view.*

class MockAdapter(private var mDatasource: List<MockResultItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_mock, parent, false)
        return MockVievHolder(v)

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


                val mockItemViewHolder = holder as MockVievHolder
                val mockItem = mDatasource?.get(position)
                 mockItemViewHolder.nameTextview.text = mockItem.name

    }
    override fun getItemCount() = mDatasource?.size ?: 0


    class MockVievHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var nameTextview: TextView=itemView.nameTextView

    }
}
