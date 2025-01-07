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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.luizalabs.labsentregas.R
import com.luizalabs.labsentregas.core.data.local.DeliveryEntity
import com.luizalabs.labsentregas.ui.navigation.NewDelivery
import com.luizalabs.labsentregas.ui.theme.CornflowerBlue

@Composable
fun HomeScreen(navController: NavController) {

    val deliveries = listOf(
        DeliveryEntity(
            deliveryId = 1,
            packageQuantity = 3,
            customerName = "John Doe",
            customerCpf = "123.456.789-10",
            deliveryDeadline = "2025-01-10",
            zipCode = "12345-678",
            state = "SP",
            city = "São Paulo",
            neighborhood = "Jardins",
            street = "Rua das Flores",
            number = "100",
            complement = "Apto 202"
        ),
        DeliveryEntity(
            deliveryId = 2,
            packageQuantity = 1,
            customerName = "Jane Smith",
            customerCpf = "987.654.321-00",
            deliveryDeadline = "2025-01-15",
            zipCode = "87654-321",
            state = "RJ",
            city = "Rio de Janeiro",
            neighborhood = "Copacabana",
            street = "Avenida Atlântica",
            number = "2000",
            complement = null
        )
    )

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
                    text = "Labs Entrega",
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
                                    .clickable { },
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
                                            append("Pedido")
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
                                    .clickable { },
                                tint = Color.LightGray,

                                )
                        }
                    }
                }
            }
        }
    }
}