@file:OptIn(ExperimentalMaterial3Api::class)

package com.ceyhan.planingapp.util

import android.icu.util.Calendar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ceyhan.planingapp.R

@Composable
fun AddFloatingActionButton(onclick: () -> Unit) {
    FloatingActionButton(
        onClick = onclick
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}

@Composable
fun BackDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = { TextButton(onClick = { onConfirm() }) { Text("Devam et") } },
        dismissButton = { TextButton(onClick = { onDismiss() }) { Text("iptal") } },
        title = { Text("Geri dönmek istediğinize emin misiniz?") },
        text = {Text("Yaptığınız tüm değişiklikler kaybolacak!")})
}

@Composable
fun CustomTimePickerDialog(onDismiss: () -> Unit, onConfirm: (hour: Int, minute: Int, time: String) -> Unit) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(initialHour = currentTime[Calendar.HOUR_OF_DAY], initialMinute = currentTime[Calendar.MINUTE], is24Hour = true)

    AlertDialog(
        onDismissRequest = {onDismiss()},
        dismissButton = { TextButton(onClick = {onDismiss()}) { Text(stringResource(R.string.cancel)) } },
        confirmButton = { TextButton(onClick = {
            val hour = timePickerState.hour
            val minute = timePickerState.minute
            val customHour = if (hour < 10) {"0${hour}"} else {"$hour"}
            val customMinute = if (minute < 10) {"0${minute}"} else {"$minute"}
            val time = "$customHour:$customMinute"
            onConfirm(hour,minute,time)
        }) { Text(stringResource(R.string.ok)) }},
        text = { TimePicker(state = timePickerState) }
    )
}

@Composable
fun CustomDatePickerDialog(onDismiss: () -> Unit, onConfirm: (dateMillis: Long) -> Unit) {
    val currentTime = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = currentTime.timeInMillis)

    DatePickerDialog(
        onDismissRequest = {onDismiss()},
        dismissButton = { TextButton(onClick = {onDismiss()}) { Text(stringResource(R.string.cancel)) } },
        confirmButton = { TextButton(onClick = {
            onConfirm(datePickerState.selectedDateMillis!!)
        }) { Text(stringResource(R.string.ok)) } }
    ) {
        DatePicker(state = datePickerState)
    }
}
