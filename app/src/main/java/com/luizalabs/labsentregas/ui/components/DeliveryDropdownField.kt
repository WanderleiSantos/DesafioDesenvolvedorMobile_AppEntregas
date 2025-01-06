package com.luizalabs.labsentregas.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luizalabs.labsentregas.ui.theme.CornflowerBlue

@Composable
fun DeliveryDropdownField(
    selectedValue: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    options: List<String>,
    placeholder: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors().copy(
        focusedIndicatorColor = CornflowerBlue,
        unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.4f),
        unfocusedTextColor = Color.DarkGray,
        focusedTextColor = Color.DarkGray,
    ),
    fontSize: TextUnit = 16.sp,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        label?.let {
            Row {
                Spacer(modifier = Modifier.size(4.dp))
                it()
            }
        }
        Spacer(modifier = Modifier.size(8.dp))

        Box {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = enabled,
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
                placeholder = placeholder,
                isError = isError,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            expanded = true
                        }
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = colors,

            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 264.dp)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        },
                        text = { Text(text = option) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDeliveryDropdownField() {
    var selectedValue by remember { mutableStateOf("RJ") }
    val options = listOf("RJ", "MG", "SP")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DeliveryDropdownField(
            selectedValue = selectedValue,
            onValueChange = { selectedValue = it },
            options = options,
            label = { Text("UF") },
        )
    }
}
