package com.luizalabs.labsentregas.ui.newdelivery

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.luizalabs.labsentregas.R
import com.luizalabs.labsentregas.ui.components.DatePickerTextField
import com.luizalabs.labsentregas.ui.components.DeliveryDropdownField
import com.luizalabs.labsentregas.ui.components.DeliveryTextField
import com.luizalabs.labsentregas.ui.navigation.Home
import com.luizalabs.labsentregas.ui.theme.CornflowerBlue
import com.luizalabs.labsentregas.util.BrazilStates
import com.luizalabs.labsentregas.util.CepVisualTransformation
import com.luizalabs.labsentregas.util.CpfVisualTransformation
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NewDeliveryScreen(
    navController: NavController,
    viewModel: NewDeliveryViewModel = hiltViewModel()
) {
    val loading = remember { mutableStateOf(false) }

    val errorMessage = remember { mutableStateOf<String?>(null) }
    val uiState = viewModel.uiState.collectAsState()

    val packageQuantity = viewModel.packageQuantity.collectAsStateWithLifecycle()
    val deliveryDeadline = viewModel.deliveryDeadline.collectAsStateWithLifecycle()
    val customerName = viewModel.customerName.collectAsStateWithLifecycle()
    val customerCpf = viewModel.customerCpf.collectAsStateWithLifecycle()
    val zipCode = viewModel.zipCode.collectAsStateWithLifecycle()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val city = viewModel.city.collectAsStateWithLifecycle()
    val neighborhood = viewModel.neighborhood.collectAsStateWithLifecycle()
    val street = viewModel.street.collectAsStateWithLifecycle()
    val number = viewModel.number.collectAsStateWithLifecycle()
    val complement = viewModel.complement.collectAsStateWithLifecycle()

    when (uiState.value) {
        is NewDeliveryViewModel.NewDeliveryEvent.Error -> {
            loading.value = false
            errorMessage.value = "Failed"
        }

        is NewDeliveryViewModel.NewDeliveryEvent.Loading -> {
            loading.value = true
            errorMessage.value = null
        }

        else -> {
            loading.value = false
            errorMessage.value = null
        }
    }

    LaunchedEffect(true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is NewDeliveryViewModel.NewDeliveryNavigationEvent.NavigateToHome -> {
                    navController.navigate(Home)
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.popBackStack() },
                    tint = Color.DarkGray,
                )
                Text(
                    text = stringResource(R.string.nova_entrega),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            DeliveryTextField(
                value = customerName.value,
                onValueChange = {
                    viewModel.onCustomerNameChange(it)
                },
                isError = viewModel.customerNameError.collectAsState().value,
                label = {
                    Text(
                        text = stringResource(R.string.cliente),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
            DeliveryTextField(
                value = customerCpf.value,
                onValueChange = { if (it.length < 12) viewModel.onCustomerCpfChange(it) },
                isError = viewModel.customerCpfError.collectAsState().value,
                label = {
                    Text(text = stringResource(R.string.cpf), color = Color.Gray, fontSize = 14.sp)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                visualTransformation = CpfVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DeliveryTextField(
                    value = packageQuantity.value,
                    onValueChange = { viewModel.onPackageQuantityChange(it) },
                    isError = viewModel.packageQuantityError.collectAsState().value,
                    label = {
                        Text(
                            text = stringResource(R.string.quantidade_pacotes),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .width(180.dp)
                        .height(48.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                DatePickerTextField(
                    label = {
                        Text(
                            text = stringResource(R.string.data_limite),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    selectedDate = deliveryDeadline.value,
                    onDateChange = { viewModel.onDeliveryDeadlineChange(it) },
                    isError = viewModel.deliveryDeadlineError.collectAsState().value,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DeliveryTextField(
                    value = zipCode.value,
                    onValueChange = { if (it.length < 9) viewModel.onZipCodeChange(it) },
                    isError = viewModel.zipCodeError.collectAsState().value,
                    visualTransformation = CepVisualTransformation(),
                    label = {
                        Text(
                            text = stringResource(R.string.cep),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .width(230.dp)
                        .height(48.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                DeliveryDropdownField(
                    selectedValue = state.value,
                    onValueChange = { viewModel.onStateChange(it) },
                    isError = viewModel.stateError.collectAsState().value,
                    label = {
                        Text(
                            text = stringResource(R.string.uf),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    options = BrazilStates.states,
                    placeholder = { Text("...") },
                )
            }
            DeliveryDropdownField(
                selectedValue = city.value,
                onValueChange = { viewModel.onCityChange(it) },
                isError = viewModel.cityError.collectAsState().value,
                label = {
                    Text(
                        text = stringResource(R.string.cidade),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                options = viewModel.cityOptions.value,
                placeholder = { Text("...") },
            )
            DeliveryTextField(
                value = neighborhood.value,
                onValueChange = { viewModel.onNeighborhoodChange(it) },
                isError = viewModel.neighborhoodError.collectAsState().value,
                label = {
                    Text(
                        text = stringResource(R.string.bairro),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DeliveryTextField(
                    value = street.value,
                    onValueChange = { viewModel.onStreetChange(it) },
                    isError = viewModel.streetError.collectAsState().value,
                    label = {
                        Text(
                            text = stringResource(R.string.rua),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                DeliveryTextField(
                    value = number.value,
                    onValueChange = { viewModel.onNumberChange(it) },
                    isError = viewModel.numberError.collectAsState().value,
                    label = {
                        Text(
                            text = stringResource(R.string.n_mero),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier.height(48.dp)
                )
            }
            DeliveryTextField(
                value = complement.value ?: "",
                onValueChange = { viewModel.onComplementChange(it) },
                label = {
                    Text(
                        text = stringResource(R.string.complemento),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = {
                    if (viewModel.validateFields()) {
                        viewModel.onAddClick()
                    }
                },
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CornflowerBlue)
            ) {
                Box {
                    AnimatedContent(targetState = loading.value,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f) togetherWith
                                    fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f)
                        }
                    ) { target ->
                        if (target) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 32.dp)
                                    .size(24.dp)
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.cadastrar_entrega),
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}