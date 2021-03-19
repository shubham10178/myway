package com.fluper.seeway.panels.chat.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.databinding.AdapterRecieveChatBinding
import com.fluper.seeway.databinding.AdapterSendChatBinding



const val SENDER=1
const val RECIEVER=2

class ChatAdapter :RecyclerView.Adapter<ChatAdapter.ChatViewHolder>()  {


    private lateinit var binding: ViewDataBinding

    inner class ChatViewHolder(item: View): RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        if(viewType== SENDER){
             binding= DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_send_chat,parent,false) as AdapterSendChatBinding
        }else{
             binding= DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_recieve_chat,parent,false) as AdapterRecieveChatBinding
        }
        return ChatViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {


    }


    override fun getItemViewType(position: Int): Int {
        return if( position % 2==SENDER ) SENDER else RECIEVER;
    }
}