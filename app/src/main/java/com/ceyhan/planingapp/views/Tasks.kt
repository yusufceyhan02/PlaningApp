@file:OptIn(ExperimentalFoundationApi::class)

package com.ceyhan.planingapp.views

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ceyhan.planingapp.R
import com.ceyhan.planingapp.models.task.TaskModel
import com.ceyhan.planingapp.ui.theme.PlaningAppTheme
import com.ceyhan.planingapp.util.AddFloatingActionButton
import com.ceyhan.planingapp.viewModel.TaskViewModel
import org.w3c.dom.Text

@Composable
fun Tasks(viewModel: TaskViewModel) {
    val progress = remember { viewModel.process }
    val taskList = remember { viewModel.taskList }
    val addButton = remember { mutableStateOf(false) }

    if (addButton.value) {
        AddTaskDialog(null,viewModel) {
            addButton.value = false
        }
    }

    Scaffold(floatingActionButton = { AddFloatingActionButton { addButton.value = true } }) { contentPadding ->
        Column(Modifier.fillMaxSize().consumeWindowInsets(contentPadding), horizontalAlignment = Alignment.CenterHorizontally) {
            if (!progress.value) {
                if (taskList.isNotEmpty()) {
                    Text(stringResource(R.string.options_info), fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 10.dp, bottom = 2.dp))
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        taskList.forEach { task ->
                            TaskToDo(viewModel,task)
                        }
                    }
                }
                else {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Henüz görev eklemediniz!\n+ butonuna tıklayarak ekleyebilirsiniz.", textAlign = TextAlign.Center, color = Color.Gray)
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
fun TaskToDo(viewModel: TaskViewModel, task: TaskModel) {
    val selected = remember { mutableStateOf(task.selected) }

    val longClick = remember { mutableStateOf(false) }
    val edit = remember { mutableStateOf(false) }

    if (edit.value) {
        AddTaskDialog(task,viewModel) {
            edit.value = false
        }
    }

    if (longClick.value) {
        TaskOptions(task, dismiss = { longClick.value = false }, edit = {
            longClick.value = false
            edit.value = true
        }, delete = {
            viewModel.deleteTask(task)
        })
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().combinedClickable(onClick = {}, onLongClick = {
        longClick.value = true
    })) {
        Checkbox(checked = selected.value, onCheckedChange = {
            selected.value = !selected.value
            viewModel.selectTask(task)
        })
        Column {
            if (selected.value) {
                Text(task.title, fontSize = 22.sp, color = Color.Gray, style = TextStyle(textDecoration = TextDecoration.LineThrough))
                if (task.description.isNotEmpty()) { Text(task.description, fontSize = 17.sp, color = Color.Gray, style = TextStyle(textDecoration = TextDecoration.LineThrough)) }
            }
            else {
                Text(task.title, fontSize = 20.sp)
                if (task.description.isNotEmpty()) { Text(task.description, fontSize = 15.sp) }
            }
        }
    }
}

@Composable
fun AddTaskDialog(task: TaskModel?, viewModel: TaskViewModel, dismiss: () -> Unit) {
    var id = 0

    val dialogTitle = remember { mutableStateOf("Görev Ekle") }
    val dialogButtonText = remember { mutableStateOf("Ekle") }

    val titleText = remember { viewModel.title }
    val descriptionText = remember { viewModel.description }

    val titleError = remember { mutableStateOf(false) }

    task?.let {
        id = it.uid
        viewModel.title.value = it.title
        viewModel.description.value = it.description
        dialogTitle.value = "Görevi Düzenle"
        dialogButtonText.value = "Düzenle"
        titleError.value = false
    } ?: run {
        id = 0
        viewModel.title.value = ""
        viewModel.description.value = ""
        dialogTitle.value = "Görev Ekle"
        dialogButtonText.value = "Ekle"
        titleError.value = false
    }

    Dialog(onDismissRequest = {}) {
        Card {
            Row(Modifier.fillMaxWidth().padding(top = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(dialogTitle.value, fontSize = 20.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(start = 30.dp).weight(1f))
                IconButton(onClick = {dismiss()}, modifier = Modifier.padding(end = 5.dp)) { Icon(Icons.Default.Clear, contentDescription = "close") }
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.padding(vertical = 5.dp))

                OutlinedTextField(
                    value = titleText.value,
                    onValueChange = {
                        titleError.value = false
                        titleText.value = it
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

                OutlinedTextField(
                    value = descriptionText.value,
                    onValueChange = {descriptionText.value = it},
                    label = {Text(stringResource(R.string.optional_description))},
                    keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences)
                )

                Button(
                    onClick = {
                        if (titleText.value.trim().isNotEmpty()) {
                            viewModel.insertOrUpdateTask(id) {
                                dismiss()
                            }
                        }
                        else {
                            titleError.value = true
                        }
                    },
                    modifier = Modifier.padding(vertical = 15.dp)
                ) { Text(dialogButtonText.value) }
            }
        }
    }
}

@Composable
fun TaskOptions(task: TaskModel, dismiss: () -> Unit, edit: () -> Unit, delete: () -> Unit) {
    val context = LocalContext.current

    Dialog(onDismissRequest = {dismiss()}) {
        Column {
            ElevatedButton(
                onClick = {
                    if (!task.selected) {
                        edit()
                    }
                    else {
                        Toast.makeText(context,"Tamamlanan görevler düzenlenemez!", Toast.LENGTH_LONG).show()
                        dismiss()
                    }
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