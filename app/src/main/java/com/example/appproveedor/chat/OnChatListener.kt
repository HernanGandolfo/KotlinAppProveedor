package com.example.appproveedor.chat

import com.example.appproveedor.entities.Message

interface OnChatListener {
    fun deleteMessage(message: Message)
}