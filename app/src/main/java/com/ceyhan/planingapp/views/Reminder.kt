@file:OptIn(ExperimentalFoundationApi::class)

package com.ceyhan.planingapp.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.ceyhan.planingapp.R
import com.ceyhan.planingapp.models.Screen
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.models.reminder.ReminderModel
import com.ceyhan.planingapp.models.reminder.ReminderToDo
import com.ceyhan.planingapp.ui.theme.LocalCustomColors
import com.ceyhan.planingapp.util.AddFloatingActionButton
import com.ceyhan.planingapp.viewModel.ReminderViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun Reminder(navController: NavController, viewModel: ReminderViewModel) {
    val reminderList = remember { viewModel.reminderList }
    val process = remember { viewModel.process }
    val firstStart = remember { viewModel.firstStart }

    if (firstStart.value) {
        ReminderFirstStart()
    }

    Scaffold (floatingActionButton = { AddFloatingActionButton { navController.navigate(route = Screen.ADD_REMINDER.name) } }) { innerPadding ->
        Column(Modifier.fillMaxSize().consumeWindowInsets(innerPadding), horizontalAlignment = Alignment.CenterHorizontally) {
            if (!process.value) {
                if (reminderList.isNotEmpty()) {
                    Text(stringResource(R.string.options_info), fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 10.dp, bottom = 2.dp))
                    Column(modifier = Modifier.verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                        reminderList.forEach { reminder ->
                            Spacer(Modifier.padding(vertical = 5.dp))
                            ReminderCalendarView(reminder,viewModel,navController)
                        }
                    }
                }
                else {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Henüz hatırlatıcı eklemediniz!\n+ butonuna tıklayarak ekleyebilirsiniz.", textAlign = TextAlign.Center, color = Color.Gray)
                    }
                }
            }
            else {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
fun ReminderCalendarView(reminder: ReminderModel, viewModel: ReminderViewModel, navController: NavController) {
    val longClick = remember { mutableStateOf(false) }

    if (longClick.value) {
        ReminderOptions (
            dismiss = {
                longClick.value = false
            },
            edit = {
                longClick.value = false
                val type = object: TypeToken<ReminderModel>() {}.type
                val gsonReminder = Gson().toJson(reminder,type)
                navController.navigate(route = "${Screen.ADD_REMINDER.name}?gsonReminder=$gsonReminder")
            },
            delete = {
                longClick.value = false
                viewModel.deleteReminder(reminder)
            }
        )
    }

    GetReminderColors(reminder.colorsIndex) { color, hoverColor ->
        Column(Modifier.padding(horizontal = 30.dp).combinedClickable(onClick = {}, onLongClick = {
            longClick.value = true
        })) {
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
                }
                Spacer(Modifier.padding(vertical = 5.dp))
            }
        }
    }
}

@Composable
fun ReminderOptions(dismiss: () -> Unit, edit: () -> Unit, delete: () -> Unit) {
    Dialog(onDismissRequest = {dismiss()}) {
        Column {
            ElevatedButton(
                onClick = {
                    edit()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Düzenle") }

            ElevatedButton(
                onClick = {
                    delete()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Sil") }
        }
    }
}

@Composable
fun ReminderFirstStart() {

}

@Composable
fun Circle(modifier: Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Box(Modifier.padding(top = 30.dp), contentAlignment = Alignment.Center) {
            Box(Modifier.size(20.dp).clip(CircleShape).background(LocalCustomColors.current.circleColor))
            Box(Modifier.size(17.dp).clip(CircleShape).background(MaterialTheme.colorScheme.background))
        }
        Box(Modifier.height(30.dp).width(3.dp).background(LocalCustomColors.current.lineColor,RoundedCornerShape(5.dp)))
    }
}

@Composable
fun GetReminderColors(index: Int, colors: @Composable (color: Color, hoverColor: Color) ->  Unit) {
    when (index) {
        0 -> colors(LocalCustomColors.current.blue,LocalCustomColors.current.blueHover)
        1 -> colors(LocalCustomColors.current.green,LocalCustomColors.current.greenHover)
        2 -> colors(LocalCustomColors.current.orange,LocalCustomColors.current.orangeHover)
        3 -> colors(LocalCustomColors.current.red,LocalCustomColors.current.redHover)
        4 -> colors(LocalCustomColors.current.gray,LocalCustomColors.current.grayHover)
    }
}