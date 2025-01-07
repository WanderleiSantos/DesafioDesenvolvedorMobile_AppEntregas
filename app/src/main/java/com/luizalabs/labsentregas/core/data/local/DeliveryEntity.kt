package com.luizalabs.labsentregas.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeliveryEntity(

    @PrimaryKey(autoGenerate = true)
    val deliveryId: Int = 0,

    val packageQuantity: Int,
    val deliveryDeadline: String,
    val customerName: String,
    val customerCpf: String,

    val zipCode: String,
    val state: String,
    val city: String,
    val neighborhood: String,
    val street: String,
    val number: String,
    val complement: String?
)