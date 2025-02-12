@file:OptIn(ExperimentalFoundationApi::class)

package com.ceyhan.planingapp.views

import android.icu.util.Calendar
import androidx.compose.foundation.ExperimentalFoundationApi
import com.ceyhan.planingapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ceyhan.planingapp.models.weekly.WeeklyPlanModel
import com.ceyhan.planingapp.models.weekly.WeeklyToDo
import com.ceyhan.planingapp.ui.theme.PlaningAppTheme
import com.ceyhan.planingapp.util.CustomTimePickerDialog
import com.ceyhan.planingapp.viewModel.WeeklyPlanViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.util.UUID

@Composable
fun WeeklyPlan(viewModel: WeeklyPlanViewModel) {
    val weeklyPlans = remember { viewModel.weeklyPlanList }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.weekly_plan_info), fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 10.dp, bottom = 2.dp))

        LazyColumn {
            items(7) { index ->
                val weeklyPlan = weeklyPlans.find { it.uid == index}?: run {
                    WeeklyPlanModel(index,listOf<WeeklyToDo>())
                }
                WeeklyPlanCard(weeklyPlan) { newWeeklyPlanModel ->
                    viewModel.insertOrUpdateWeeklyPlan(newWeeklyPlanModel)
                }
            }
        }
    }
}

@Composable
fun WeeklyPlanCard(weeklyPlanModel: WeeklyPlanModel, updateModel: (newWeeklyPlanModel: WeeklyPlanModel) -> Unit) {
    val longClick = remember { mutableStateOf(false) }

    if (longClick.value) {
        WeeklyPlanEditDialog(weeklyPlanModel, dismiss = { longClick.value = false }, confirm = { updateModel(it) })
    }

    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.padding(top = 10.dp)) {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        longClick.value = true
                    })
        ) {
            Column(Modifier.fillMaxWidth().padding(30.dp)) {
                if (weeklyPlanModel.weeklyToDos.isNotEmpty()) {
                    weeklyPlanModel.weeklyToDos.forEach {
                        Spacer(Modifier.padding(vertical = 5.dp))
                        WeeklyPlanToDo(it)
                    }
                }
                else {
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.free_time), color = Color.Gray)
                    }
                }
            }
        }
        Text(getWeeklyDay(weeklyPlanModel.uid), fontSize = 25.sp, fontFamily = FontFamily(Font(R.font.super_bubble)), color = Color(0xFFFFA500))
    }
}

@Composable
fun WeeklyPlanToDo(weeklyToDo: WeeklyToDo) {
    Row {
        Icon(painterResource(R.drawable.ic_point), contentDescription = "point", Modifier.padding(top = 6.dp).size(12.dp))
        Spacer(Modifier.padding(horizontal = 2.dp))
        Text(weeklyToDo.title, modifier = Modifier.weight(1f,fill = false))
        Spacer(Modifier.padding(horizontal = 5.dp))
        Icon(painterResource(R.drawable.ic_clock), contentDescription = null, modifier = Modifier.size(14.dp))
        Spacer(Modifier.padding(1.dp))
        Text(weeklyToDo.time, fontSize = 14.sp)
    }
}

@Composable
fun WeeklyPlanEditDialog(weeklyPlanModel: WeeklyPlanModel, dismiss: () -> Unit, confirm: (newWeeklyPlanModel: WeeklyPlanModel) -> Unit) {
    val clickedTimePicker = remember { mutableStateOf(false) }
    val timeTextFieldSource = remember { MutableInteractionSource() }

    val timeText = remember { mutableStateOf("") }
    val titleText = remember { mutableStateOf("") }

    val titleError = remember { mutableStateOf(false) }
    val timeError = remember { mutableStateOf(false) }

    val list = remember { mutableStateListOf<WeeklyToDo>() }
    if (list.isEmpty()) { list.addAll(weeklyPlanModel.weeklyToDos) }

    val coroutineScope = rememberCoroutineScope()
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        list.apply {
            list.add(to.index, removeAt(from.index))
        }
    })

    if (timeTextFieldSource.collectIsPressedAsState().value) { clickedTimePicker.value = true }

    if (clickedTimePicker.value) {
        CustomTimePickerDialog(
            onConfirm = { paramHour, paramMinute, time ->
                timeText.value = time
                clickedTimePicker.value = false
            },
            onDismiss = { clickedTimePicker.value = false }
        )
        timeError.value = false
    }

    Dialog(onDismissRequest = {}) {
        Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
            Card(Modifier.fillMaxWidth().padding(vertical = 15.dp)) {
                Column(Modifier.padding(horizontal = 15.dp).padding(top = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (list.isNotEmpty()) {
                        LazyColumn(
                            state = state.listState,
                            modifier = Modifier.reorderable(state).detectReorderAfterLongPress(state).heightIn(max = 300.dp)
                        ) {
                            items(list, key = {UUID.randomUUID()}) { item ->
                                ReorderableItem(state, key = {UUID.randomUUID()}) {
                                    Row(Modifier.fillMaxWidth()) {
                                        Icon(Icons.Default.Menu, contentDescription = null, modifier = Modifier.padding(vertical = 1.dp))
                                        Spacer(Modifier.padding(horizontal = 2.dp))
                                        Row(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 1.dp)) {
                                                Icon(painterResource(R.drawable.ic_clock), contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(Modifier.padding(horizontal = 1.dp))
                                                Text(item.time, fontSize = 14.sp)
                                            }
                                            Spacer(Modifier.padding(horizontal = 5.dp))
                                            Text(item.title, modifier = Modifier.weight(1f,false))
                                        }
                                        IconButton(
                                            onClick = {
                                                list.remove(item)
                                            }
                                        ) { Icon(Icons.Default.Clear, contentDescription = "clear") }
                                    }
                                }
                            }
                        }
                        Text("Basılı tutup sürükleyerek listeyi düzenleyebilirsiniz.", fontSize = 11.sp, color = Color.Gray)
                    }
                    else {
                        Column(Modifier.padding(horizontal = 15.dp).padding(top = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(stringResource(R.string.free_time), color = Color.Gray)
                        }
                    }

                    Spacer(Modifier.padding(vertical = 10.dp))

                    OutlinedTextField(
                        value = titleText.value,
                        onValueChange = {
                            titleText.value = it
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

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = timeText.value,
                            onValueChange = {},
                            label = {Text(stringResource(R.string.time))},
                            interactionSource = timeTextFieldSource,
                            readOnly = true,
                            modifier = Modifier.padding(start = 5.dp).width(120.dp),
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
                        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                            Button(
                                onClick = {
                                    if (titleText.value.trim().isNotEmpty() && timeText.value.trim().isNotEmpty()) {
                                        val toDo = WeeklyToDo(titleText.value.trim().toString(),timeText.value.trim().toString())
                                        list.add(toDo)
                                        titleText.value = ""
                                        timeText.value = ""
                                        coroutineScope.launch {
                                            state.listState.animateScrollToItem(index = list.lastIndex)
                                        }
                                    }
                                    else if (titleText.value.trim().isEmpty()) {
                                        titleError.value = true
                                    }
                                    else if (timeText.value.trim().isEmpty()) {
                                        timeError.value = true
                                    }
                                }
                            ) { Text(stringResource(R.string.add)) }
                        }
                    }

                    Spacer(Modifier.padding(vertical = 5.dp))

                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = {dismiss()}) { Text(stringResource(R.string.cancel)) }
                        TextButton(
                            onClick = {
                                val newWeeklyPlanModel = WeeklyPlanModel(weeklyPlanModel.uid,list)
                                confirm(newWeeklyPlanModel)
                                dismiss()
                            }
                        ) { Text(stringResource(R.string.save)) }
                    }
                }
            }
            Text(getWeeklyDay(weeklyPlanModel.uid), fontSize = 25.sp, fontFamily = FontFamily(Font(R.font.super_bubble)), color = Color(0xFFFFA500))
        }
    }
}

@Composable
fun getWeeklyDay(id: Int): String {
    return when(id) {
        0 -> stringResource(R.string.monday)
        1 -> stringResource(R.string.tuesday)
        2 -> stringResource(R.string.wednesday)
        3 -> stringResource(R.string.thursday)
        4 -> stringResource(R.string.friday)
        5 -> stringResource(R.string.saturday)
        6 -> stringResource(R.string.sunday)
        else -> stringResource(R.string.error)
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyPlanPreview() {
    PlaningAppTheme {
        //WeeklyPlan(rememberNavController())
    }
}