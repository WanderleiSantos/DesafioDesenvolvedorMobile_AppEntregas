package com.luizalabs.labsentregas.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luizalabs.labsentregas.ui.theme.CornflowerBlue
import java.util.Calendar

@Composable
fun DatePickerTextField(
    label: @Composable (() -> Unit)? = null,
    selectedDate: String,
    onDateChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors().copy(
        focusedIndicatorColor = CornflowerBlue,
        unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.4f),
        unfocusedTextColor = Color.DarkGray,
        focusedTextColor = Color.DarkGray,
    ),
    fontSize: TextUnit = 16.sp,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val initialYear = calendar.get(Calendar.YEAR)
    val initialMonth = calendar.get(Calendar.MONTH)
    val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                onDateChange(formattedDate)
                showDialog = false
            },
            initialYear,
            initialMonth,
            initialDay
        ).show()
    }

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
                textStyle = TextStyle(fontSize = fontSize),
                value = selectedDate,
                onValueChange = {},
                modifier = modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable(enabled = enabled) { if (enabled) showDialog = true },
                enabled = enabled,
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "",
                        modifier = Modifier.clickable { if (enabled) showDialog = true }
                    )
                },
                colors = colors,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDatePickerTextField() {
    var selectedDate by remember { mutableStateOf("10/01/2025") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(48.dp)) {
        DatePickerTextField(
            label = { Text("Data Limite") },
            selectedDate = selectedDate,
            onDateChange = { selectedDate = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
