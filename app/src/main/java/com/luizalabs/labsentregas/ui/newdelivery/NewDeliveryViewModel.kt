package com.luizalabs.labsentregas.ui.newdelivery

import androidx.lifecycle.ViewModel
import com.luizalabs.labsentregas.core.data.local.DeliveryDataBase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewDeliveryViewModel @Inject constructor(
    private val deliveryDataBase: DeliveryDataBase
): ViewModel() {
}