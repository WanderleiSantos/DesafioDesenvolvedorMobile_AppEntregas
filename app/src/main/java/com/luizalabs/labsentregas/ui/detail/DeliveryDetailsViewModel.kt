package com.luizalabs.labsentregas.ui.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizalabs.labsentregas.core.data.local.DeliveryEntity
import com.luizalabs.labsentregas.core.data.remote.IBGEApiService
import com.luizalabs.labsentregas.domain.repository.DeliveryRepository
import com.luizalabs.labsentregas.ui.newdelivery.NewDeliveryViewModel.NewDeliveryEvent
import com.luizalabs.labsentregas.ui.newdelivery.NewDeliveryViewModel.NewDeliveryNavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryDetailsViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository,
    private val ibgeApiService: IBGEApiService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val deliveryId = savedStateHandle.get<Int>("deliveryId")

    private val _navigationEvent = MutableSharedFlow<DeliveryDetailsNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _isEditable = MutableStateFlow(false)
    val isEditable: StateFlow<Boolean> get() = _isEditable

    private val _uiState = MutableStateFlow<DeliveryDetailsEvent>(DeliveryDetailsEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _cityOptions = mutableStateOf<List<String>>(emptyList())
    val cityOptions: State<List<String>> get() = _cityOptions

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

    init {
        if (deliveryId != null) {
            loadDeliveryDetails(deliveryId)
        }

    }

    fun toggleEditMode() {
        _isEditable.value = !_isEditable.value
    }

    private fun loadDeliveryDetails(deliveryId: Int) {
        viewModelScope.launch {
            try {
                val delivery = deliveryRepository.getDeliveryById(deliveryId)

                customerName.value = delivery.customerName
                customerCpf.value = delivery.customerCpf
                zipCode.value = delivery.zipCode
                state.value = delivery.state
                city.value = delivery.city
                neighborhood.value = delivery.neighborhood
                street.value = delivery.street
                number.value = delivery.number
                complement.value = delivery.complement
                packageQuantity.value = delivery.packageQuantity.toString()
                deliveryDeadline.value = delivery.deliveryDeadline

                _uiState.value = DeliveryDetailsEvent.Success

                val cities = ibgeApiService.getCities(state.value).map { it.nome }
                _cityOptions.value = cities

            } catch (e: Exception) {
                _uiState.value = DeliveryDetailsEvent.Error
            }
        }
    }

    private fun clearFieldError(
        field: MutableStateFlow<String>,
        errorState: MutableStateFlow<Boolean>,
        newValue: String
    ) {
        field.value = newValue
        if (errorState.value) {
            errorState.value = false
        }
    }

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
        clearFieldError(city, stateError, "")
        viewModelScope.launch {
            _uiState.emit(DeliveryDetailsEvent.Loading)
            try {
                val cities = ibgeApiService.getCities(newValue).map { it.nome }
                _cityOptions.value = cities
                _uiState.emit(DeliveryDetailsEvent.Success)
            } catch (e: Exception) {
                _uiState.emit(DeliveryDetailsEvent.Error)
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

    fun onSaveClick() {
        if (!validateFields()) return
        viewModelScope.launch {
            try {

                val updatedDelivery = deliveryId?.let {
                    DeliveryEntity(
                        deliveryId = it,
                        customerName = customerName.value,
                        customerCpf = customerCpf.value,
                        zipCode = zipCode.value,
                        state = state.value,
                        city = city.value,
                        neighborhood = neighborhood.value,
                        street = street.value,
                        number = number.value,
                        complement = complement.value,
                        packageQuantity = packageQuantity.value.toInt(),
                        deliveryDeadline = deliveryDeadline.value
                    )
                }
                if (updatedDelivery != null) {
                    deliveryRepository.updateDelivery(updatedDelivery)
                }
                _uiState.value = DeliveryDetailsEvent.Success
                _navigationEvent.emit(DeliveryDetailsNavigationEvent.NavigateToHome)

            } catch (e: Exception) {
                _uiState.value = DeliveryDetailsEvent.Error
            }
        }
    }

    sealed class DeliveryDetailsNavigationEvent {
        object NavigateToHome : DeliveryDetailsNavigationEvent()
    }

    sealed class DeliveryDetailsEvent {
        object Nothing : DeliveryDetailsEvent()
        object Success : DeliveryDetailsEvent()
        object Error : DeliveryDetailsEvent()
        object Loading : DeliveryDetailsEvent()
    }
}