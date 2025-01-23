package com.ceyhan.planingapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ceyhan.planingapp.models.Screen
import com.ceyhan.planingapp.ui.theme.LocalCustomColors
import com.ceyhan.planingapp.util.AddFloatingActionButton

@Composable
fun Reminder(navController: NavController) {
    Scaffold (floatingActionButton = { AddFloatingActionButton { navController.navigate(route = Screen.ADD_REMINDER.name) } }) { innerPadding ->
        Column(modifier = Modifier.fillMaxWidth().padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally) {
            ImageCard()
            Spacer(Modifier.height(10.dp))
            ImageCard()
        }
    }
}

@Composable
fun ImageCard() {
    Column(Modifier.padding(horizontal = 30.dp)) {
        Box {
            Box(Modifier.fillMaxWidth().padding(top = 20.dp).height(40.dp).background(LocalCustomColors.current.blue, RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)), contentAlignment = Alignment.Center) {
                Text("15 ocak 2025 Pazartesi", color = Color.White)
            }
            Row(Modifier.fillMaxWidth()) {
                Circle(Modifier.padding(start = 10.dp))
                Spacer(Modifier.weight(1f))
                Circle(Modifier.padding(end = 10.dp))
            }
        }
        Column(Modifier.fillMaxWidth().height(200.dp).background(LocalCustomColors.current.blueHover, RoundedCornerShape(bottomEnd = 5.dp, bottomStart = 5.dp))) {

        }
    }
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