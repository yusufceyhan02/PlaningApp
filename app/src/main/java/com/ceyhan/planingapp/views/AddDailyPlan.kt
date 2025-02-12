@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("DEPRECATION")

package com.ceyhan.planingapp.views

import com.ceyhan.planingapp.R
import android.icu.util.Calendar
import android.widget.ProgressBar
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.models.daily.DailyToDo
import com.ceyhan.planingapp.ui.theme.PlaningAppTheme
import com.ceyhan.planingapp.util.BackDialog
import com.ceyhan.planingapp.util.CustomTimePickerDialog
import com.ceyhan.planingapp.viewModel.AddDailyPlanViewModel
import com.ceyhan.planingapp.viewModel.DailyPlanViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddDailyPlan(navController: NavController,baseViewModel: DailyPlanViewModel, viewModel: AddDailyPlanViewModel) {
    val context = LocalContext.current
    val progress = remember { viewModel.progress }

    val timeTextFieldSource = remember { MutableInteractionSource() }
    val clickedTimePicker = remember { mutableStateOf(false) }

    val toDoList = remember { viewModel.dailyToDoList }
    val titleText = remember { viewModel.title }
    val descriptionText = remember { mutableStateOf("") }
    val toDoText = remember { mutableStateOf("") }
    val timeText = remember { mutableStateOf("") }

    val titleError = remember { mutableStateOf(false) }
    val toDoError = remember { mutableStateOf(false) }
    val timeError = remember { mutableStateOf(false) }
    val backDialog = remember { mutableStateOf(false) }

    if (timeTextFieldSource.collectIsPressedAsState().value) { clickedTimePicker.value = true }

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

    if (!progress.value) {
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
                        if (toDoText.value.trim().isNotEmpty() && viewModel.hour != -1 && viewModel.minute != -1) {
                            val newDailyToDo = DailyToDo(toDoText.value,descriptionText.value,viewModel.hour,viewModel.minute,false)
                            viewModel.dailyToDoList.add(newDailyToDo)
                            toDoText.value = ""
                            descriptionText.value = ""
                            viewModel.hour = -1
                            viewModel.minute = -1
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

            Text(stringResource(R.string.preview), fontSize = 14.sp, color = Color.Gray)

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
            EditDailyPlanCard(DailyPlanModel(0,titleText.value,toDoList, date)) {
                viewModel.dailyToDoList.remove(it)
            }

            Button(
                onClick = {
                    if (titleText.value.trim().isNotEmpty() && viewModel.dailyToDoList.isNotEmpty()) {
                        viewModel.insertDailyPlan {
                            baseViewModel.getDailyPlans()
                            navController.popBackStack()
                        }
                    }
                    else if (titleText.value.trim().isEmpty()) {
                        titleError.value = true
                    }
                    else if (viewModel.dailyToDoList.isEmpty()) {
                        Toast.makeText(context,context.getText(R.string.empty_todo_list_warning), Toast.LENGTH_LONG).show()
                    }
                },
                enabled = !progress.value
            ) { Text(stringResource(R.string.finish)) }

            Spacer(Modifier.padding(vertical = 10.dp))
        }
    }
    else {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
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
            backDialog.value = false
        })
    }
}

@Composable
fun EditDailyPlanCard(dailyPlan: DailyPlanModel, deleteToDo: (todo: DailyToDo) -> Unit) {
    Card(Modifier.fillMaxWidth().padding(horizontal = 30.dp).padding(bottom = 20.dp)) {
        Column(Modifier.padding(10.dp)) {
            Text(dailyPlan.title, fontSize = 25.sp, modifier = Modifier.padding(start = 10.dp))
            Spacer(Modifier.padding(vertical = 5.dp))
            dailyPlan.dailyToDos.forEachIndexed { ix, todo ->
                EditDailyPlanToDo(todo) {
                    deleteToDo(it)
                }
            }
        }
    }
}

@Composable
fun EditDailyPlanToDo(dailyToDo: DailyToDo, deleteToDo: (todo: DailyToDo) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {deleteToDo(dailyToDo)}) {Icon(Icons.Default.Clear, contentDescription = null)}
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(dailyToDo.title, fontSize = 20.sp, modifier = Modifier.weight(1f,fill = false))
                Spacer(Modifier.padding(5.dp))
                Icon(painterResource(R.drawable.ic_clock), contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(Modifier.padding(1.dp))
                val hour = if (dailyToDo.hour < 10) {"0${dailyToDo.hour}"} else {"${dailyToDo.hour}"}
                val minute = if (dailyToDo.minute < 10) {"0${dailyToDo.minute}"} else {"${dailyToDo.minute}"}
                Text("${hour}:${minute}", fontSize = 14.sp)
            }
            if (dailyToDo.description.isNotEmpty()) {
                Text(dailyToDo.description)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddDailyPlanPreview() {
    PlaningAppTheme {
        AddDailyPlan(rememberNavController(), viewModel(),viewModel())
    }
}