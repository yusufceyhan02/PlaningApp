@file:OptIn(ExperimentalFoundationApi::class)

package com.ceyhan.planingapp.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import com.ceyhan.planingapp.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.models.Screen
import com.ceyhan.planingapp.models.daily.DailyToDo
import com.ceyhan.planingapp.ui.theme.PlaningAppTheme
import com.ceyhan.planingapp.util.AddFloatingActionButton
import com.ceyhan.planingapp.viewModel.DailyPlanViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun DailyPlan(navController: NavController, viewModel: DailyPlanViewModel) {
    val dailyPlans = remember { viewModel.dailyPlans }
    val process = remember { viewModel.process }
    val firstStart = remember { viewModel.firstStart }

    if (firstStart.value) {
        DailyPlanFirstStart()
    }

    Scaffold (floatingActionButton = { AddFloatingActionButton { navController.navigate(route = Screen.ADD_DAILY_PLAN.name) } }) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding), horizontalAlignment = Alignment.CenterHorizontally) {
            if (!process.value) {
                if (dailyPlans.isNotEmpty()) {
                    Text(stringResource(R.string.options_info), fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 10.dp, bottom = 2.dp))
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        dailyPlans.forEach { dailyPlan ->
                            DailyPlanCard(dailyPlan,viewModel,navController) { toDoIndex ->
                                val newDailyPlan = dailyPlan.apply {
                                    dailyToDos[toDoIndex].also {
                                        val newValue = !it.selected
                                        it.selected = newValue
                                    }
                                }
                                viewModel.updateDailyPlan(newDailyPlan)
                            }
                        }
                    }
                }
                else {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Henüz günlük plan eklemediniz!\n+ butonuna tıklayarak ekleyebilirsiniz.", textAlign = TextAlign.Center, color = Color.Gray)
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
fun DailyPlanCard(dailyPlan: DailyPlanModel, viewModel: DailyPlanViewModel, navController: NavController, selectedCheckBox: (index: Int) -> Unit) {
    val options = remember { mutableStateOf(false) }

    if (options.value) {
        DailyPlanOptions(
            dismiss = { options.value = false },
            edit = {
                options.value = false
                val type = object: TypeToken<DailyPlanModel>() {}.type
                val gsonDailyPlan = Gson().toJson(dailyPlan,type)
                navController.navigate(route = "${Screen.ADD_DAILY_PLAN.name}?gsonDaily=$gsonDailyPlan")
            },
            delete = {
                options.value = false
                viewModel.deleteDailyPlan(dailyPlan)
            }
        )
    }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(bottom = 20.dp)
            .combinedClickable(onClick = {}, onLongClick = {
                options.value = true
            })) {
        Column(Modifier.padding(10.dp)) {
            Text(dailyPlan.title, fontSize = 25.sp, modifier = Modifier.padding(start = 10.dp))
            Spacer(Modifier.padding(vertical = 5.dp))
            dailyPlan.dailyToDos.forEachIndexed { ix, todo ->
                DailyPlanToDo(todo) {
                    selectedCheckBox(ix)
                }
            }
        }
    }
}

@Composable
fun DailyPlanToDo(dailyToDo: DailyToDo, selectedCheckBox: () -> Unit) {
    val selected = remember { mutableStateOf(dailyToDo.selected) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = selected.value,
            onCheckedChange = {
            selected.value = !selected.value
            selectedCheckBox()
        })
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

@Composable
fun DailyPlanOptions(dismiss: () -> Unit, edit: () -> Unit, delete: () -> Unit) {
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
fun DailyPlanFirstStart() {

}

@Preview(showBackground = true)
@Composable
fun DailyPlanPreview() {
    PlaningAppTheme {
        DailyPlan(rememberNavController(), viewModel<DailyPlanViewModel>())
    }
}