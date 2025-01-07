package com.luizalabs.labsentregas.core.data.repository

import com.luizalabs.labsentregas.core.data.local.DeliveryDataBase
import com.luizalabs.labsentregas.core.data.local.DeliveryEntity
import com.luizalabs.labsentregas.domain.repository.DeliveryRepository
import javax.inject.Inject

class DeliveryRepositoryImpl @Inject constructor(
    private val deliveryDataBase: DeliveryDataBase
) : DeliveryRepository {
    override suspend fun insertDelivery(delivery: DeliveryEntity) {
        deliveryDataBase.deliveryDao.insertDelivery(delivery)
    }

    override suspend fun getDeliveries(): List<DeliveryEntity> {
        return deliveryDataBase.deliveryDao.getDeliveries()
    }

    override suspend fun getDeliveryById(deliveryId: Int): DeliveryEntity {
        return deliveryDataBase.deliveryDao.getDeliveryById(deliveryId)
    }
}