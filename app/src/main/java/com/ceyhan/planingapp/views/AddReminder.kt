@file:Suppress("DEPRECATION")

package com.ceyhan.planingapp.views

import android.icu.util.Calendar
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ceyhan.planingapp.R
import com.ceyhan.planingapp.models.Screen
import com.ceyhan.planingapp.models.daily.DailyToDo
import com.ceyhan.planingapp.models.reminder.ReminderModel
import com.ceyhan.planingapp.models.reminder.ReminderToDo
import com.ceyhan.planingapp.ui.theme.LocalCustomColors
import com.ceyhan.planingapp.util.BackDialog
import com.ceyhan.planingapp.util.CustomDatePickerDialog
import com.ceyhan.planingapp.util.CustomTimePickerDialog
import com.ceyhan.planingapp.viewModel.AddReminderViewModel
import com.ceyhan.planingapp.viewModel.ReminderViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddReminder(navController: NavController, baseViewModel: ReminderViewModel, viewModel: AddReminderViewModel) {
    val context = LocalContext.current
    val timeTextFieldSource = remember { MutableInteractionSource() }
    val dateTextFieldSource = remember { MutableInteractionSource() }
    val clickedTimePicker = remember { mutableStateOf(false) }
    val clickedDatePicker = remember { mutableStateOf(false) }

    val toDoList = remember { viewModel.toDoList }
    val descriptionText = remember { mutableStateOf("") }
    val toDoText = remember { mutableStateOf("") }
    val timeText = remember { mutableStateOf("") }
    val dateText = remember { viewModel.dateText }

    val toDoError = remember { mutableStateOf(false) }
    val timeError = remember { mutableStateOf(false) }
    val dateError = remember { mutableStateOf(false) }
    val backDialog = remember { mutableStateOf(false) }
    val selectedColor = remember { viewModel.selectedColor }

    if (timeTextFieldSource.collectIsPressedAsState().value) { clickedTimePicker.value = true }
    if (dateTextFieldSource.collectIsPressedAsState().value) { clickedDatePicker.value = true }

    if (clickedTimePicker.value) {
        CustomTimePickerDialog(
            onConfirm = { paramHour, paramMinute, time ->
                viewModel.hour = paramHour
                viewModel.minute = paramMinute
                timeText.value = time
                clickedTimePicker.value = false
            },
            onDismiss = { clickedTimePicker.value = false }
        )
        timeError.value = false
    }

    if (clickedDatePicker.value) {
        CustomDatePickerDialog(
            onConfirm = { dateMillis ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = dateMillis
                dateText.value = SimpleDateFormat("dd MMMM yyyy EEEE", Locale.getDefault()).format(calendar.time)
                viewModel.dateMillis = dateMillis
                clickedDatePicker.value = false
            },
            onDismiss = { clickedDatePicker.value = false }
        )
        dateError.value = false
    }

    Column(
        Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(top = 20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {
            IconButton(onClick = {backDialog.value = true}) { Icon(Icons.Default.ArrowBack,"back") }
        }

        Spacer(Modifier.padding(vertical = 10.dp))

        Text("Hatırlatıcı Ekle", fontSize = 30.sp, textAlign = TextAlign.Center)

        Spacer(Modifier.padding(vertical = 10.dp))

        TextField(
            value = dateText.value,
            onValueChange = {},
            label = { Text("Tarih") },
            interactionSource = dateTextFieldSource,
            readOnly = true,
            isError = dateError.value,
            supportingText = {
                if (dateError.value) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.textField_error),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (dateError.value) {
                    Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
                }
            }
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
                    if (toDoText.value.trim().isNotEmpty() && viewModel.hour != -1 && viewModel.minute != -1) {
                        val newReminderToDo = ReminderToDo(toDoText.value,descriptionText.value,viewModel.hour,viewModel.minute,timeText.value)
                        viewModel.toDoList.add(newReminderToDo)
                        toDoText.value = ""
                        descriptionText.value = ""
                        viewModel.hour = -1
                        viewModel.minute = -1
                        viewModel.dateMillis = -1
                        timeText.value = ""
                    }
                    else if (toDoText.value.trim().isEmpty()) {
                        toDoError.value = true
                    }
                    else if (viewModel.hour == -1 || viewModel.minute == -1) {
                        timeError.value = true
                    }
                }, modifier = Modifier.padding(end = 10.dp)) { Text(stringResource(R.string.add)) }
            }
        }

        Spacer(Modifier.padding(vertical = 10.dp))

        Row {
            SelectableColorCircle(0,selectedColor.intValue) { viewModel.selectedColor.intValue = it }
            Spacer(Modifier.padding(horizontal = 5.dp))
            SelectableColorCircle(1,selectedColor.intValue) { viewModel.selectedColor.intValue = it }
            Spacer(Modifier.padding(horizontal = 5.dp))
            SelectableColorCircle(2,selectedColor.intValue) { viewModel.selectedColor.intValue = it }
            Spacer(Modifier.padding(horizontal = 5.dp))
            SelectableColorCircle(3,selectedColor.intValue) { viewModel.selectedColor.intValue = it }
            Spacer(Modifier.padding(horizontal = 5.dp))
            SelectableColorCircle(4,selectedColor.intValue) { viewModel.selectedColor.intValue = it }
        }

        Spacer(Modifier.padding(vertical = 20.dp))

        Text(stringResource(R.string.preview), fontSize = 14.sp, color = Color.Gray)

        EditReminderCalendarView(ReminderModel(0,selectedColor.intValue,viewModel.dateMillis,dateText.value,toDoList)) { removedItem ->
            viewModel.toDoList.remove(removedItem)
        }

        Spacer(Modifier.padding(vertical = 10.dp))

        Button(onClick = {
            if (dateText.value.trim().isNotEmpty() && toDoList.isNotEmpty()) {
                viewModel.insertReminder {
                    baseViewModel.getReminders()
                    navController.popBackStack()
                }
            }
            else if (dateText.value.trim().isEmpty()) {
                dateError.value = true
            }
            else if (toDoList.isEmpty()) {
                Toast.makeText(context,context.getText(R.string.empty_todo_list_warning), Toast.LENGTH_LONG).show()
            }
        }) {
            Text(stringResource(R.string.finish))
        }

        Spacer(Modifier.padding(vertical = 5.dp))
    }

    BackHandler {
        backDialog.value = true
    }

    if (backDialog.value) {
        BackDialog(
            onDismiss = {
                backDialog.value = false
            },
            onConfirm = {
                viewModel.clear()
                navController.popBackStack()
                backDialog.value = false
            }
        )
    }
}

@Composable
fun EditReminderCalendarView(reminder: ReminderModel, removeToDo: (item: ReminderToDo) -> Unit) {

    GetReminderColors(reminder.colorsIndex) { color, hoverColor ->
        Column(Modifier.padding(horizontal = 30.dp)) {
            Box {
                Box(Modifier.fillMaxWidth().padding(top = 20.dp).height(40.dp).background(color, RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)), contentAlignment = Alignment.Center) {
                    Text(reminder.dateText, color = Color.White)
                }
                Row(Modifier.fillMaxWidth()) {
                    Circle(Modifier.padding(start = 10.dp))
                    Spacer(Modifier.weight(1f))
                    Circle(Modifier.padding(end = 10.dp))
                }
            }
            Column(Modifier.fillMaxWidth().heightIn(min = 80.dp).background(hoverColor, RoundedCornerShape(bottomEnd = 5.dp, bottomStart = 5.dp)).padding(horizontal = 10.dp), verticalArrangement = Arrangement.Center) {
                reminder.reminderToDos.forEach { todo ->
                    Spacer(Modifier.padding(vertical = 5.dp))
                    Row(Modifier.fillMaxWidth()) {
                        Row(Modifier.weight(1f)) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                            Spacer(Modifier.padding(horizontal = 2.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(todo.title, modifier = Modifier.weight(1f,false))
                                    Spacer(Modifier.padding(horizontal = 3.dp))
                                    Icon(painterResource(R.drawable.ic_clock), contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(Modifier.padding(horizontal = 1.dp))
                                    Text(todo.time, fontSize = 14.sp)
                                }
                                if (todo.description.isNotEmpty()) {Text(todo.description, fontSize = 12.sp)}
                            }
                        }
                        IconButton(
                            onClick = {
                                removeToDo(todo)
                            },
                            modifier = Modifier.size(30.dp)
                        ) { Icon(Icons.Default.Clear, contentDescription = "clear") }
                    }
                }
                Spacer(Modifier.padding(vertical = 5.dp))
            }
        }
    }
}

@Composable
fun SelectableColorCircle(index: Int, selectedIndex: Int, select: (index: Int) -> Unit) {
    GetReminderColors(index) { color, hoverColor ->
        if (selectedIndex == index) {
            Box(Modifier.size(32.dp).clip(CircleShape).background(color = color).border(width = 2.dp, color = LocalCustomColors.current.reverseColor, shape = CircleShape), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "check"
                )
            }
        }
        else {
            Box(Modifier.size(32.dp).clip(CircleShape).background(color = color).clickable {
                select(index)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddReminderPreview() {
    AddReminder(rememberNavController(),viewModel(),viewModel())
}