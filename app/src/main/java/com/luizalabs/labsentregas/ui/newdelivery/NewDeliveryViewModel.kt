package com.luizalabs.labsentregas.ui.newdelivery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizalabs.labsentregas.core.data.local.DeliveryEntity
import com.luizalabs.labsentregas.domain.repository.DeliveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewDeliveryViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewDeliveryEvent>(NewDeliveryEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NewDeliveryNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _packageQuantity = MutableStateFlow("")
    val packageQuantity = _packageQuantity.asStateFlow()

    private val _deliveryDeadline = MutableStateFlow("")
    val deliveryDeadline = _deliveryDeadline.asStateFlow()

    private val _customerName = MutableStateFlow("")
    val customerName = _customerName.asStateFlow()

    private val _customerCpf = MutableStateFlow("")
    val customerCpf = _customerCpf.asStateFlow()

    private val _zipCode = MutableStateFlow("")
    val zipCode = _zipCode.asStateFlow()

    private val _state = MutableStateFlow("")
    val state = _state.asStateFlow()

    private val _city = MutableStateFlow("")
    val city = _city.asStateFlow()

    private val _neighborhood = MutableStateFlow("")
    val neighborhood = _neighborhood.asStateFlow()

    private val _street = MutableStateFlow("")
    val street = _street.asStateFlow()

    private val _number = MutableStateFlow("")
    val number = _number.asStateFlow()

    private val _complement = MutableStateFlow<String?>(null)
    val complement = _complement.asStateFlow()

    fun onPackageQuantityChange(newValue: String) {
        _packageQuantity.value = newValue
    }

    fun onDeliveryDeadlineChange(newValue: String) {
        _deliveryDeadline.value = newValue
    }

    fun onCustomerNameChange(newValue: String) {
        _customerName.value = newValue
    }

    fun onCustomerCpfChange(newValue: String) {
        _customerCpf.value = newValue
    }

    fun onZipCodeChange(newValue: String) {
        _zipCode.value = newValue
    }

    fun onStateChange(newValue: String) {
        _state.value = newValue
    }

    fun onCityChange(newValue: String) {
        _city.value = newValue
    }

    fun onNeighborhoodChange(newValue: String) {
        _neighborhood.value = newValue
    }

    fun onStreetChange(newValue: String) {
        _street.value = newValue
    }

    fun onNumberChange(newValue: String) {
        _number.value = newValue
    }

    fun onComplementChange(newValue: String?) {
        _complement.value = newValue
    }

    fun onAddClick() {
        viewModelScope.launch {
            _uiState.value = NewDeliveryEvent.Loading
            try {

                val entity = DeliveryEntity(
                    packageQuantity = packageQuantity.value.toInt(),
                    deliveryDeadline = deliveryDeadline.value,
                    customerName = customerName.value,
                    customerCpf = customerCpf.value,
                    zipCode = zipCode.value,
                    state = state.value,
                    city = city.value,
                    neighborhood = neighborhood.value,
                    street = street.value,
                    number = number.value,
                    complement = complement.value
                )


                deliveryRepository.insertDelivery(entity)

                _uiState.value = NewDeliveryEvent.Success
                _navigationEvent.emit(NewDeliveryNavigationEvent.NavigateToHome)

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = NewDeliveryEvent.Error
            }
        }
    }

    sealed class NewDeliveryNavigationEvent {
        object NavigateToHome : NewDeliveryNavigationEvent()
    }

    sealed class NewDeliveryEvent {
        object Nothing : NewDeliveryEvent()
        object Success : NewDeliveryEvent()
        object Error : NewDeliveryEvent()
        object Loading : NewDeliveryEvent()
    }
}