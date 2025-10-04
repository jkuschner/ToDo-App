package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.ui.theme.ToDoAppTheme
import java.util.concurrent.atomic.AtomicInteger

data class Task (
    val id: Int,
    var description: String,
    var isCompleted: Boolean = false
)
// set initial value to 4 b/c we have 0-3 in sample tasks
private val nextTaskId = AtomicInteger(4)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                }
            }
        }
    }
}

@Composable
fun ToDoApp() {
    val taskList = remember {
        mutableStateListOf(
            Task(0, "Finish 411A Assignment", false),
            Task(1, "take out the trash", false),
            Task(2, "dig for gold", true),
            Task(3, "retire at an early age", false)
        )
    }

    // state to hold the current user input for a new task
    var newTaskDescription by remember {mutableStateOf("")}


    // define task actions
    val toggleTaskCompletion: (Int) -> Unit = {taskId ->
        // search list for index of task that matches taskId
        val index = taskList.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val oldTask = taskList[index]
            val updatedTask = oldTask.copy(isCompleted = !oldTask.isCompleted)
            taskList[index] = updatedTask
        }
    }

    val deleteTask: (Int) -> Unit = {taskId ->
        taskList.removeAll {it.id == taskId}
    }

    val addTask: (String) -> Unit = {description ->
        val newTask = Task(
            id = nextTaskId.getAndIncrement(),
            description = description,
            isCompleted = false
        )
        taskList.add(newTask)
        newTaskDescription = "" // reset the new task description to be blank
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F7FA)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // title
            TitleBar()
            // text input for new tasks
            TaskInput(
                description = newTaskDescription,
                onTitleChange = { newTaskDescription = it },
                onAddTaskClick = {
                    if (newTaskDescription.isNotBlank()) {
                        addTask(newTaskDescription.trim())
                    }
                }
            )
        }
    }
}

@Composable
fun TitleBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "TODO App",
            fontSize = 28.sp,
            color = Color(0xFF333333),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TaskInput(description: String,
              onTitleChange: (String) -> Unit,
              onAddTaskClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // field where user inputs task text
        TextField(
            value = description,
            onValueChange = onTitleChange,
            placeholder = { Text("add a new Task here") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )

        //button to add the task
        Button(
            onClick = onAddTaskClick,
            enabled = description.isNotBlank(), // disable add task button if there is no descriptio
            modifier = Modifier.height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF007AFF)
            )
        ) {
            Text(text = "Add")
        }
    }
}

@Preview(showSystemUi = true) // Use showSystemUi=true to see it in a full phone frame
@Composable
fun Preview_HeaderAssembly() {
    // Use a Column to mimic the main structure
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA))) {
        TitleBar()
        TaskInput(
            description = "Design system check...",
            onTitleChange = {},
            onAddTaskClick = {}
        )
        // Add a placeholder section to visually simulate the scrollable list below
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Task List Starts Here...",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray
        )
    }
}
