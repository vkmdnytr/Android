package com.example.sosyalyazilim.ui.message

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.example.sosyalyazilim.R
import com.example.sosyalyazilim.entity.model.RecyclerViewBaseItem
import com.example.sosyalyazilim.entity.model.data.socket.MessageItem
import com.example.sosyalyazilim.entity.rest.Urls
import com.example.sosyalyazilim.ui.login.LoginActivity
import com.example.sosyalyazilim.ui.mock.MockActivity
import hu.agta.rxwebsocket.RxWebSocket
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_socket.*
import java.util.ArrayList


class MessageFragment : Fragment() {
    companion object {
        private val REQUEST_LOGIN = 0
    }

    private var rxWebSocket: RxWebSocket? = null
    private var mAdapter: MessageAdapter? = null
    private val mMessages = ArrayList<RecyclerViewBaseItem>()
    private var reciveMessage:String? = null
    private var mUsername: String? = null
    private var isSocketConnect:Boolean=false


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let {
             mAdapter= MessageAdapter(it)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initSocketProcess()
        isSingInUser()

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_socket, container, false)
    }
    override fun onDestroy() {
        super.onDestroy()
        disConnect()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { mAdapter = MessageAdapter(it) }
        messageList?.layoutManager = LinearLayoutManager(context)
        messageList?.adapter = mAdapter



        mInputMessageView.setOnEditorActionListener(TextView.OnEditorActionListener { v, id, event ->
            if (id == R.id.send || id == EditorInfo.IME_NULL) {
                attemptSend()
                return@OnEditorActionListener true
            }
            false
        })
        mInputMessageView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
              isSingInUser()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        sendButtons.setOnClickListener { attemptSend() }
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_main, menu)
    }
    var menuItem:MenuItem?=null
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        menuItem = item
        val id=item.itemId
        if (id == R.id.action_connect) {
            if(isSocketConnect){
                item.title="DisConnect"
                disConnect()
            }else{
                item.title="Connect"
                connect()
            }
            return true
        }
        if (id == R.id.action_mockList) {
            goToMockListActivity()
            return true
        }


        return super.onOptionsItemSelected(item)
    }
    private fun goToMockListActivity() {
        val intent = Intent(activity, MockActivity::class.java)
        startActivity(intent)
    }

    //LISTEN USERNAME FROM LOGIN ACTIVITY
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK != resultCode) {
            activity!!.finish()
            return
        }

        mUsername = data!!.getStringExtra("userName")
        val numUsers = data.getIntExtra("id", 1)
        connect()
    }

    private fun removeMessage(username: String) {
        for (i in mMessages.indices.reversed()) {
            val message = mMessages.get(i)
            if (message.type == MessageItem.MESSAGE_TYPE) {
                val message = message as MessageItem
                if (message.userName == username) {
                    mMessages.removeAt(i)
                    mAdapter?.reMoveMessage(i)
                }

            }
        }
    }
    private fun attemptSend() {

        val message = mInputMessageView.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus()
            return
        }
        senMessageForSocket(message)
        mInputMessageView.setText("")
    }
    private fun goToLoginForUser() {
        mUsername = null
        val intent = Intent(activity, LoginActivity::class.java)
        startActivityForResult(intent, REQUEST_LOGIN)
    }
    private fun isSingInUser(): Boolean {

        if (mUsername == null) {
            goToLoginForUser()
            return false
        } else {
            return true
        }
    }


    //SOCKET LISTENER
    @SuppressLint("CheckResult")
    private fun initSocketProcess() {
        rxWebSocket = RxWebSocket(Urls.SOSYALYAZILIM_SOCKET_URL)

        rxWebSocket?.apply {


            this.onOpen()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        isSocketConnect=true
                        menuItem?.title="DisConnect"
                        messageList?.let { Snackbar.make(it, "WebSocket opened.", Snackbar.LENGTH_SHORT).show() } }
                            , { it.printStackTrace()
                    })


            this.onClosed()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        isSocketConnect=false
                        menuItem?.title="Connect"
                        messageList?.let { Snackbar.make(it, "WebSocket closed.", Snackbar.LENGTH_SHORT).show() } }, { it.printStackTrace() })

            this.onClosing()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        messageList?.let { Snackbar.make(it, "WebSocket is closing.", Snackbar.LENGTH_SHORT).show()
                            isSocketConnect=false
                            menuItem?.title="Connect"
                        } }, { it.printStackTrace() })

            this.onTextMessage()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ socketMessageEvent ->
                        reciveMessage?.let { mUsername?.let { it1 -> mAdapter?.addMessage(socketMessageEvent, it, it1)
                            mMessages.add(MessageItem(it1,it))
                        isSocketConnect=true
                        } } }, { it.printStackTrace() })

            this.onFailure()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { isSocketConnect=false
                                  menuItem?.title="Connect"
                                 Snackbar.make(messageList as View, "WebSocket failure! " + it.exception.message, Snackbar.LENGTH_SHORT).show() }

        }


    }
    private fun disConnect(){
        removeMessage(mUsername!!)

        rxWebSocket?.close()
                ?.subscribeOn(Schedulers.computation())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { success ->
                    messageList?.let {
                        Snackbar.make(it, "WebSocket close initiated. With result: " + success!!,
                                Snackbar.LENGTH_SHORT).show()
                    }
                }

    }
    private fun connect(){
        rxWebSocket?.connect()

    }
    private fun senMessageForSocket(message: String) {
        rxWebSocket?.apply {
            this.sendMessage(message)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { success ->
                                messageList?.let {
                                    reciveMessage = message
                                    Snackbar.make(it, "Message sent result: $success", Snackbar.LENGTH_SHORT).show()
                                }
                            },
                            { throwable -> messageList?.let { Snackbar.make(it, "Message error: $throwable", Snackbar.LENGTH_SHORT).show() } }
                    )
        }
    }
}

