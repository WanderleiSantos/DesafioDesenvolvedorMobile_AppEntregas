package com.luizalabs.labsentregas.ui.newdelivery

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizalabs.labsentregas.core.data.local.DeliveryEntity
import com.luizalabs.labsentregas.core.data.remote.IBGEApiService
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
    private val deliveryRepository: DeliveryRepository,
    private val ibgeApiService: IBGEApiService
) : ViewModel() {

    private val _cityOptions = mutableStateOf<List<String>>(emptyList())
    val cityOptions: State<List<String>> get() = _cityOptions

    private val _uiState = MutableStateFlow<NewDeliveryEvent>(NewDeliveryEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NewDeliveryNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    val packageQuantity = MutableStateFlow("")
    val packageQuantityError = MutableStateFlow(false)

    val deliveryDeadline = MutableStateFlow("")
    val deliveryDeadlineError = MutableStateFlow(false)

    val customerName = MutableStateFlow("")
    val customerNameError = MutableStateFlow(false)

    val customerCpf = MutableStateFlow("")
    val customerCpfError = MutableStateFlow(false)

    val zipCode = MutableStateFlow("")
    val zipCodeError = MutableStateFlow(false)

    val state = MutableStateFlow("")
    val stateError = MutableStateFlow(false)

    val city = MutableStateFlow("")
    val cityError = MutableStateFlow(false)

    val neighborhood = MutableStateFlow("")
    val neighborhoodError = MutableStateFlow(false)

    val street = MutableStateFlow("")
    val streetError = MutableStateFlow(false)

    val number = MutableStateFlow("")
    val numberError = MutableStateFlow(false)

    val complement = MutableStateFlow<String?>(null)

    fun onPackageQuantityChange(newValue: String) {
        clearFieldError(packageQuantity, packageQuantityError, newValue)
    }

    fun onDeliveryDeadlineChange(newValue: String) {
        clearFieldError(deliveryDeadline, deliveryDeadlineError, newValue)
    }

    fun onCustomerNameChange(newValue: String) {
        clearFieldError(customerName, customerNameError, newValue)
    }

    fun onCustomerCpfChange(newValue: String) {
        clearFieldError(customerCpf, customerCpfError, newValue)
    }

    fun onZipCodeChange(newValue: String) {
        clearFieldError(zipCode, zipCodeError, newValue)
    }

    fun onComplementChange(newValue: String?) {
        complement.value = newValue
    }

    fun onStateChange(newValue: String) {
        clearFieldError(state, stateError, newValue)
        viewModelScope.launch {
            _uiState.emit(NewDeliveryEvent.Loading)
            try {
                val cities = ibgeApiService.getCities(newValue).map { it.nome }
                _cityOptions.value = cities
                _uiState.emit(NewDeliveryEvent.Success)
            } catch (e: Exception) {
                _uiState.emit(NewDeliveryEvent.Error)
            }
        }
    }

    fun onCityChange(newValue: String) {
        clearFieldError(city, cityError, newValue)
    }

    fun onNeighborhoodChange(newValue: String) {
        clearFieldError(neighborhood, neighborhoodError, newValue)
    }

    fun onStreetChange(newValue: String) {
        clearFieldError(street, streetError, newValue)
    }

    fun onNumberChange(newValue: String) {
        clearFieldError(number, numberError, newValue)
    }

    fun validateFields(): Boolean {
        var isValid = true

        if (packageQuantity.value.isBlank()) {
            packageQuantityError.value = true
            isValid = false
        }
        if (deliveryDeadline.value.isBlank()) {
            deliveryDeadlineError.value = true
            isValid = false
        }
        if (customerName.value.isBlank()) {
            customerNameError.value = true
            isValid = false
        }
        if (customerCpf.value.isBlank()) {
            customerCpfError.value = true
            isValid = false
        }
        if (zipCode.value.isBlank()) {
            zipCodeError.value = true
            isValid = false
        }
        if (state.value.isBlank()) {
            stateError.value = true
            isValid = false
        }
        if (city.value.isBlank()) {
            cityError.value = true
            isValid = false
        }
        if (neighborhood.value.isBlank()) {
            neighborhoodError.value = true
            isValid = false
        }
        if (street.value.isBlank()) {
            streetError.value = true
            isValid = false
        }
        if (number.value.isBlank()) {
            numberError.value = true
            isValid = false
        }

        return isValid
    }

    private fun clearFieldError(field: MutableStateFlow<String>, errorState: MutableStateFlow<Boolean>, newValue: String) {
        field.value = newValue
        if (errorState.value) {
            errorState.value = false
        }
    }

    fun onAddClick() {

        if (!validateFields()) return

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