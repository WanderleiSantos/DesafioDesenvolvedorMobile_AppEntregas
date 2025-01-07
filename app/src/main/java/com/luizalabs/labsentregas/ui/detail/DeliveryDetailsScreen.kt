package com.luizalabs.labsentregas.ui.detail

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
import com.luizalabs.labsentregas.ui.theme.LimeGreen
import com.luizalabs.labsentregas.util.BrazilStates
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DeliveryDetailsScreen(
    navController: NavController,
    viewModel: DeliveryDetailsViewModel = hiltViewModel()
) {

    val isEditable by viewModel.isEditable.collectAsState()

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

    LaunchedEffect(true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is DeliveryDetailsViewModel.DeliveryDetailsNavigationEvent.NavigateToHome -> {
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
                    text = "Detalhes da Entrega",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = buildAnnotatedString {
                    append("Entrega")
                    append(" #")
                    append(viewModel.deliveryId.toString())
                },
                style = MaterialTheme
                    .typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                fontSize = 18.sp,
                color = Color.DarkGray,
            )
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
                enabled = isEditable,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
            DeliveryTextField(
                value = customerCpf.value,
                onValueChange = { viewModel.onCustomerCpfChange(it) },
                isError = viewModel.customerCpfError.collectAsState().value,
                enabled = isEditable,
                label = {
                    Text(text = stringResource(R.string.cpf), color = Color.Gray, fontSize = 14.sp)
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
                    value = packageQuantity.value,
                    onValueChange = { viewModel.onPackageQuantityChange(it) },
                    isError = viewModel.packageQuantityError.collectAsState().value,
                    enabled = isEditable,
                    label = {
                        Text(
                            text = stringResource(R.string.quantidade_pacotes),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .width(180.dp)
                        .height(48.dp)
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
                    enabled = isEditable,
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
                    onValueChange = { viewModel.onZipCodeChange(it) },
                    isError = viewModel.zipCodeError.collectAsState().value,
                    enabled = isEditable,
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
                    enabled = isEditable,
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
                enabled = isEditable,
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
                enabled = isEditable,
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
                    enabled = isEditable,
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
                    enabled = isEditable,
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
                enabled = isEditable,
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


            if (isEditable) {
                Button(
                    onClick = {
                        if (viewModel.validateFields()) {
                            viewModel.onSaveClick()
                        }
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = LimeGreen)
                ) {
                    Text(
                        text = "Salvar Alterações",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.toggleEditMode() },
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CornflowerBlue)
                ) {
                    Text(
                        text = stringResource(R.string.editar_entrega),
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
    }
}