package com.luizalabs.labsentregas.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.luizalabs.labsentregas.R
import com.luizalabs.labsentregas.core.data.local.DeliveryEntity
import com.luizalabs.labsentregas.ui.navigation.NewDelivery
import com.luizalabs.labsentregas.ui.theme.CornflowerBlue

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val deliveries by viewModel.deliveries.collectAsState(emptyList())

    var deliveryToDelete by remember { mutableStateOf<DeliveryEntity?>(null) }

    Box() {
        Image(
            painter = painterResource(id = R.drawable.ic_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .statusBarsPadding(),
            ) {
                Text(
                    text = "Magalu Entregas",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = {
                        navController.navigate(NewDelivery)
                    },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonColors(
                        contentColor = Color.White,
                        containerColor = CornflowerBlue,
                        disabledContainerColor = CornflowerBlue,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("+ Nova Entrega")
                }
            }

            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = Color.White)
                    .fillMaxSize()
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(all = 24.dp)
                ) {
                    items(count = deliveries.size) {
                        val delivery = deliveries[it]
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .height(80.dp)
                                    .weight(1f)
                                    .clickable {
                                        navController.navigate("Details/${delivery.deliveryId}")
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.box_delivery),
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)

                                ) {
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Entrega")
                                            append(" #")
                                            append(delivery.deliveryId.toString())
                                        },
                                        style = MaterialTheme
                                            .typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        fontSize = 14.sp,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = delivery.customerName,
                                        style = MaterialTheme
                                            .typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = buildAnnotatedString {
                                            append(delivery.city)
                                            append(", ")
                                            append(delivery.neighborhood)
                                            append(", ")
                                            append(delivery.street)
                                            append(", ")
                                            append(delivery.state)
                                        },
                                        style = MaterialTheme
                                            .typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                                        fontSize = 10.sp,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = delivery.deliveryDeadline,
                                        style = MaterialTheme
                                            .typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                                        fontSize = 10.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { deliveryToDelete = delivery },
                                tint = Color.LightGray,
                            )
                        }
                    }
                }
            }
        }

        if (deliveryToDelete != null) {
            AlertDialog(
                onDismissRequest = { deliveryToDelete = null },
                title = { Text("Confirmar Exclusão") },
                text = {
                    Text("Você tem certeza que deseja excluir a entrega ${deliveryToDelete?.deliveryId}?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            deliveryToDelete?.let { viewModel.deleteDelivery(it) }
                            deliveryToDelete = null
                        }
                    ) {
                        Text("Excluir")
                    }
                },
                dismissButton = {
                    Button(onClick = { deliveryToDelete = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}