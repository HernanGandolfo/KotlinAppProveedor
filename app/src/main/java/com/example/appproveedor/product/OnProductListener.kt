package com.example.appproveedor.product

import com.example.appproveedor.entities.Product

interface OnProductListener {
    fun onClick(product: Product)
    fun onLongClick(product: Product)
}