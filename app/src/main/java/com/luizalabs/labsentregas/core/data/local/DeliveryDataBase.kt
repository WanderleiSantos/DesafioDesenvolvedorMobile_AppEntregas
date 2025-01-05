package com.luizalabs.labsentregas.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DeliveryEntity::class],
    version = 1
)
abstract class DeliveryDataBase : RoomDatabase() {
    abstract val deliveryDao: DeliveryDao
}