package com.ceyhan.planingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ceyhan.planingapp.models.BottomNavItem
import com.ceyhan.planingapp.models.Screen
import com.ceyhan.planingapp.models.weekly.WeekDays
import com.ceyhan.planingapp.models.weekly.WeeklyPlanModel
import com.ceyhan.planingapp.ui.theme.PlaningAppTheme
import com.ceyhan.planingapp.viewModel.AddDailyPlanViewModel
import com.ceyhan.planingapp.viewModel.AddReminderViewModel
import com.ceyhan.planingapp.viewModel.DailyPlanViewModel
import com.ceyhan.planingapp.viewModel.WeeklyPlanViewModel
import com.ceyhan.planingapp.views.AddDailyPlan
import com.ceyhan.planingapp.views.AddReminder
import com.ceyhan.planingapp.views.DailyPlan
import com.ceyhan.planingapp.views.Reminder
import com.ceyhan.planingapp.views.Tasks
import com.ceyhan.planingapp.views.WeeklyPlan

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaningAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val dailyPlanViewModel = viewModel<DailyPlanViewModel>()
    val addDailyPlanViewModel = viewModel<AddDailyPlanViewModel>()
    val weeklyPlanViewModel = viewModel<WeeklyPlanViewModel>()
    val addReminderViewModel = viewModel<AddReminderViewModel>()

    Scaffold (bottomBar = { MainBottomBar(navController) }) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.DAILY_PLAN.name, modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            composable(route = Screen.DAILY_PLAN.name) {
                dailyPlanViewModel.init()
                DailyPlan(navController,dailyPlanViewModel)
            }
            composable(route = Screen.ADD_DAILY_PLAN.name) {
                AddDailyPlan(navController,addDailyPlanViewModel)
            }
            composable(route = Screen.WEEKLY_PLAN.name) {
                weeklyPlanViewModel.init()
                WeeklyPlan(weeklyPlanViewModel)
            }
            composable(route = Screen.REMINDER.name) {
                Reminder(navController)
            }
            composable(route = Screen.ADD_REMINDER.name) {
                AddReminder(navController,addReminderViewModel)
            }
            composable(route = Screen.TASKS.name) {
                Tasks()
            }
        }
    }
}

@Composable
fun MainBottomBar(navController: NavController) {
    val selectedIx = remember { mutableIntStateOf(0) }
    val navItemList = listOf(
        BottomNavItem(stringResource(R.string.daily_plan), Icons.Default.Check, Screen.DAILY_PLAN.name),
        BottomNavItem(stringResource(R.string.weekly_plan), Icons.Default.Menu, Screen.WEEKLY_PLAN.name),
        BottomNavItem(stringResource(R.string.reminder), Icons.Default.DateRange, Screen.REMINDER.name),
        BottomNavItem(stringResource(R.string.tasks), Icons.Default.Star, Screen.TASKS.name)

    )
    NavigationBar {
        navItemList.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = selectedIx.intValue == index,
                onClick = {
                    selectedIx.intValue = index
                    navController.navigate(route = navItem.route)
                },
                icon = {Icon(imageVector = navItem.icon,contentDescription = null)},
                label = {Text(navItem.label, textAlign = TextAlign.Center)}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PlaningAppTheme {
        MainScreen()
    }
}