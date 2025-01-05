@file:OptIn(ExperimentalMaterial3Api::class)

package com.ceyhan.planingapp.views

import com.ceyhan.planingapp.R
import android.icu.util.Calendar
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ceyhan.planingapp.models.DailyPlan
import com.ceyhan.planingapp.models.ToDo
import com.ceyhan.planingapp.ui.theme.PlaningAppTheme
import com.ceyhan.planingapp.util.BackDialog
import com.ceyhan.planingapp.viewModel.AddDailyPlanViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddDailyPlan(navController: NavController,viewModel: AddDailyPlanViewModel) {
    val context = LocalContext.current
    val timeTextFieldSource = remember { MutableInteractionSource() }
    val clickedTimePicker = remember { mutableStateOf(false) }

    val toDoList = remember { viewModel.toDoList }
    val titleText = remember { viewModel.title }
    val descriptionText = remember { mutableStateOf("") }
    val toDoText = remember { mutableStateOf("") }
    val timeText = remember { mutableStateOf("") }

    val hour = remember { viewModel.hour }
    val minute = remember { viewModel.minute }

    val titleError = remember { mutableStateOf(false) }
    val toDoError = remember { mutableStateOf(false) }
    val timeError = remember { mutableStateOf(false) }
    val backDialog = remember { mutableStateOf(false) }

    if (timeTextFieldSource.collectIsPressedAsState().value) { clickedTimePicker.value = true }

    if (clickedTimePicker.value) {
        CustomTimePickerDialog(
            Calendar.getInstance(),
            onConfirm = { paramHour, paramMinute ->
                viewModel.hour.intValue = paramHour
                viewModel.minute.intValue = paramMinute
                val customHour = if (hour.intValue < 10) {"0${hour.intValue}"} else {"${hour.intValue}"}
                val customMinute = if (minute.intValue < 10) {"0${minute.intValue}"} else {"${minute.intValue}"}
                timeText.value = "$customHour:$customMinute"
                clickedTimePicker.value = false
            },
            onDismiss = { clickedTimePicker.value = false }
        )
        timeError.value = false
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {
            IconButton(onClick = {backDialog.value = true}) { Icon(Icons.Default.ArrowBack,"back") }
        }

        Spacer(Modifier.padding(vertical = 10.dp))

        Text(stringResource(R.string.add_new_daily_plan), fontSize = 30.sp, textAlign = TextAlign.Center)

        Spacer(Modifier.padding(vertical = 10.dp))

        TextField(
            value = titleText.value,
            onValueChange = {
                viewModel.title.value = it
                titleError.value = false
                },
            label = {Text(stringResource(R.string.title))},
            isError = titleError.value,
            supportingText = {
                if (titleError.value) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.textField_error),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (titleError.value) {
                    Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences)
        )

        Spacer(Modifier.padding(vertical = 20.dp))

        Text(stringResource(R.string.add_todo_list), fontSize = 20.sp)

        Spacer(Modifier.padding(vertical = 10.dp))

        Row {
            Column(horizontalAlignment = Alignment.CenterHorizontally)  {
                TextField(
                    value = toDoText.value,
                    onValueChange = {
                        toDoText.value = it
                        toDoError.value = false
                        },
                    label = {Text(stringResource(R.string.title))},
                    modifier = Modifier.width(230.dp),
                    isError = toDoError.value,
                    supportingText = {
                        if (toDoError.value) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.textField_error),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    trailingIcon = {
                        if (toDoError.value) {
                            Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences))

                Spacer(Modifier.padding(vertical = 5.dp))

                TextField(
                    value = descriptionText.value,
                    onValueChange = {descriptionText.value = it},
                    label = {Text(stringResource(R.string.optional_description))},
                    modifier = Modifier.width(230.dp),
                    keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences)
                )
            }
            Spacer(Modifier.padding(horizontal = 5.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(
                    value = timeText.value,
                    onValueChange = {},
                    label = {Text(stringResource(R.string.time))},
                    interactionSource = timeTextFieldSource,
                    readOnly = true,
                    modifier = Modifier.width(120.dp),
                    isError = timeError.value,
                    supportingText = {
                        if (timeError.value) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.textField_error),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    trailingIcon = {
                        if (timeError.value) {
                            Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(Modifier.padding(vertical = 5.dp))

                Button(onClick = {
                    if (toDoText.value.trim().isNotEmpty() && hour.intValue != -1 && minute.intValue != -1) {
                        val newToDo = ToDo(toDoText.value,descriptionText.value,hour.intValue,minute.intValue,false)
                        viewModel.toDoList.add(newToDo)
                        toDoText.value = ""
                        descriptionText.value = ""
                        hour.intValue = -1
                        minute.intValue = -1
                        timeText.value = ""
                    }
                    else if (toDoText.value.trim().isEmpty()) {
                        toDoError.value = true
                    }
                    else if (hour.intValue == -1 || minute.intValue == -1) {
                        timeError.value = true
                    }
                }, modifier = Modifier.padding(end = 10.dp)) { Text(stringResource(R.string.add)) }
            }
        }
        Spacer(Modifier.padding(vertical = 10.dp))

        Text(stringResource(R.string.preview), fontSize = 14.sp, color = Color.Gray)

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        DailyPlanCard(DailyPlan(0,titleText.value,toDoList, date)) {}

        Button(onClick = {
            if (titleText.value.trim().isNotEmpty() && viewModel.toDoList.isNotEmpty()) {
                viewModel.insertDailyPlan{
                    navController.popBackStack()
                }
            }
            else if (titleText.value.trim().isEmpty()) {
                titleError.value = true
            }
            else if (viewModel.toDoList.isEmpty()) {
                Toast.makeText(context,context.getText(R.string.empty_todo_list_warning), Toast.LENGTH_LONG).show()
            }
        }) { Text(stringResource(R.string.finish)) }

        Spacer(Modifier.padding(vertical = 10.dp))
    }

    BackHandler {
        backDialog.value = true
    }

    if (backDialog.value) {
        BackDialog(onDismiss = {
            backDialog.value = false
        },
        onConfirm = {
            viewModel.clear()
            navController.popBackStack()
        })
    }
}

@Composable
fun CustomTimePickerDialog(currentTime: Calendar, onDismiss: () -> Unit, onConfirm: (hour: Int, minute: Int) -> Unit) {
    val timePickerState = rememberTimePickerState(initialHour = currentTime[Calendar.HOUR_OF_DAY], initialMinute = currentTime[Calendar.MINUTE], is24Hour = true)

    AlertDialog(
        onDismissRequest = {onDismiss()},
        dismissButton = { TextButton(onClick = {onDismiss()}) { Text(stringResource(R.string.cancel)) }},
        confirmButton = { TextButton(onClick = {onConfirm(timePickerState.hour,timePickerState.minute)}) { Text(stringResource(R.string.ok)) }},
        text = { TimePicker(state = timePickerState) }
    )
}

@Preview(showBackground = true)
@Composable
fun AddDailyPlanPreview() {
    PlaningAppTheme {
        AddDailyPlan(rememberNavController(), viewModel())
    }
}