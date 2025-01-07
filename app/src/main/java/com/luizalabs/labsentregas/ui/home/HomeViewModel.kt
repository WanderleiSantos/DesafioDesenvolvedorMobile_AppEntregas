package com.luizalabs.labsentregas.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizalabs.labsentregas.core.data.local.DeliveryEntity
import com.luizalabs.labsentregas.domain.repository.DeliveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository
) : ViewModel() {

    private val _deliveries = MutableStateFlow<List<DeliveryEntity>>(emptyList())
    val deliveries = _deliveries.asStateFlow()

    init {
        fetchDeliveries()
    }

    private fun fetchDeliveries() {
        viewModelScope.launch {
            _deliveries.value = deliveryRepository.getDeliveries()
        }
    }

    fun deleteDelivery(delivery: DeliveryEntity) {
        viewModelScope.launch {
            deliveryRepository.deleteDelivery(delivery)
            fetchDeliveries()
        }
    }
}