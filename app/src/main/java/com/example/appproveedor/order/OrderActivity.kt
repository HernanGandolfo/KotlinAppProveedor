package com.example.appproveedor.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appproveedor.Constants
import com.example.appproveedor.R
import com.example.appproveedor.chat.ChatFragment
import com.example.appproveedor.databinding.ActivityOrderBinding
import com.example.appproveedor.entities.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase


class OrderActivity : AppCompatActivity(), OnOrderListener, OrderAux {

    private lateinit var binding: ActivityOrderBinding

    private lateinit var adapter: OrderAdaper

    private lateinit var orderSelected: Order



    private val aValues: Array<String> by lazy {
        resources.getStringArray(R.array.status_value)
    }

    private val aKeys: Array<Int> by lazy {
        resources.getIntArray(R.array.status_key).toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFirestore()

    }

    private fun setupRecyclerView() {
        adapter = OrderAdaper(mutableListOf(), this)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrderActivity)
            adapter = this@OrderActivity.adapter
        }
    }

    private fun setupFirestore(){
        val db = FirebaseFirestore.getInstance()

        db.collection(Constants.COLL_REQUESTS)
            .orderBy(Constants.PROP_DATE, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (document in it){
                    val order = document.toObject(Order::class.java)
                    order.id = document.id
                    adapter.add(order)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al consultar los datos.", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    override fun onStartChat(order: Order) {
        orderSelected = order

        val fragment = ChatFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerMain, fragment)
            .addToBackStack(null)
            .commit()
    }

    //Actualiza cambios
    override fun onStatusChange(order: Order) {
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.COLL_REQUESTS)
            .document(order.id)
            .update(Constants.PROP_STATUS, order.status)
            .addOnSuccessListener {
                Toast.makeText(this, "Orden actualizada.", Toast.LENGTH_SHORT).show()
                //notifyClient(order)

            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar orden.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getOrderSelected(): Order = orderSelected
}