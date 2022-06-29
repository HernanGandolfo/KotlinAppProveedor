package com.example.appproveedor.order

import com.example.appproveedor.entities.Order


interface OnOrderListener {
    fun onStartChat(order: Order)
    fun onStatusChange(order: Order)
}