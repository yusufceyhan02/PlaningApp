package com.ceyhan.planingapp.util

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog

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

@Preview(showBackground = true)
@Composable
fun Diiew() {
    BackDialog(onDismiss = {}, onConfirm = {})
}
