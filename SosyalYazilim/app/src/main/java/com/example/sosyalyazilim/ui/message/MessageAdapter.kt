package com.example.sosyalyazilim.ui.message

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sosyalyazilim.R
import com.example.sosyalyazilim.entity.model.RecyclerViewBaseItem
import com.example.sosyalyazilim.entity.model.data.socket.ActionItem
import com.example.sosyalyazilim.entity.model.data.socket.LogItem
import com.example.sosyalyazilim.entity.model.data.socket.MessageItem
import hu.agta.rxwebsocket.entities.SocketMessageEvent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class MessageAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mUsernameColors: IntArray = context.resources.getIntArray(R.array.username_colors)
    private val mMessages = ArrayList<RecyclerViewBaseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var layout =-1

                layout = R.layout.list_item_message
                val v = LayoutInflater
                        .from(parent.context)
                        .inflate(layout, parent, false)
                return MessageViewHolder(v)

    }
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val message = mMessages[position]
        when (message.type) {
            MessageItem.MESSAGE_TYPE ->( viewHolder as MessageViewHolder).setBinding(message as MessageItem)
        }
    }
    override fun getItemCount(): Int {
        return mMessages.size
    }
    override fun getItemViewType(position: Int): Int {
        return mMessages[position].type
    }


    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mUsernameView: TextView?= itemView.findViewById<View>(R.id.usernameTextView) as TextView
        private var mMessageView: TextView?= itemView.findViewById<View>(R.id.messageTextView) as TextView
        fun setBinding(message: MessageItem) {
            mUsernameView?.text = message.userName
            mUsernameView?.setTextColor(getUsernameColor( message.userName))
            mMessageView?.text = message.message
        }
        private fun getUsernameColor(username: String): Int {
            var hash = 7
            var i = 0
            val len = username.length
            while (i < len) {
                hash = username.codePointAt(i) + (hash shl 5) - hash
                i++
            }
            val index = abs(hash % mUsernameColors.size)
            return mUsernameColors[index]
        }
    }



    fun addMessage(event: SocketMessageEvent,message: String,user:String) {
        mMessages.add(MessageItem(user,message))
        notifyItemInserted(mMessages.size - 1)
    }
    fun reMoveMessage(index:Int) {
        mMessages.removeAt(index)
        notifyDataSetChanged()

//        Fixme  ALL DELETE
//        mMessages.clear()
//        notifyDataSetChanged()

    }
}