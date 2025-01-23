package com.ceyhan.planingapp.views

import com.ceyhan.planingapp.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.models.Screen
import com.ceyhan.planingapp.models.daily.DailyToDo
import com.ceyhan.planingapp.ui.theme.PlaningAppTheme
import com.ceyhan.planingapp.util.AddFloatingActionButton
import com.ceyhan.planingapp.viewModel.DailyPlanViewModel

@Composable
fun DailyPlan(navController: NavController, viewModel: DailyPlanViewModel) {
    val dailyPlans = remember { viewModel.dailyPlans }

    Scaffold (floatingActionButton = { AddFloatingActionButton { navController.navigate(route = Screen.ADD_DAILY_PLAN.name) } }) { innerPadding ->
        Column(Modifier.verticalScroll(rememberScrollState()).padding(innerPadding)) {
            dailyPlans.forEach { dailyPlan ->
                DailyPlanCard(dailyPlan) { toDoIndex ->
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
}

@Composable
fun DailyPlanCard(dailyPlan: DailyPlanModel, selectedCheckBox: (index: Int) -> Unit) {
    Card(Modifier.fillMaxWidth().padding(horizontal = 30.dp).padding(bottom = 20.dp)) {
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

@Preview(showBackground = true)
@Composable
fun DailyPlanPreview() {
    PlaningAppTheme {
        DailyPlan(rememberNavController(), viewModel<DailyPlanViewModel>())
    }
}