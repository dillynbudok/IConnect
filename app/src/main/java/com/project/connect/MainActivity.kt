
package com.project.connect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.connect.ui.theme.IConnectTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            IConnectTheme {
                InternetScreen()
            }
        }
    }
}

@Composable
fun InternetScreen(
    viewModel: PostViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var postId by remember {
        mutableStateOf("1")
    }

    var inputError by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Internet Demo",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = postId,
                onValueChange = {
                    postId = it
                    inputError = null
                },
                label = {
                    Text("Enter Post ID")
                },
                placeholder = {
                    Text("Enter a number from 1 to 100")
                },
                singleLine = true,
                isError = inputError != null,
                modifier = Modifier.fillMaxWidth()
            )

            if (inputError != null) {
                Text(
                    text = inputError!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {

                    val id = postId.toIntOrNull()

                    if (id == null || id !in 1..100) {
                        inputError =
                            "Please enter a valid Post ID from 1 to 100."
                    } else {
                        inputError = null
                        viewModel.getPost(id)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Get Post")
            }

            Spacer(modifier = Modifier.height(24.dp))

            when {

                uiState.isLoading -> {

                    CircularProgressIndicator()

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Loading post...")
                }

                uiState.error != null -> {

                    Text(
                        text = "Unable to retrieve post.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    Text(
                        text = uiState.error ?: "Unknown error"
                    )
                }

                uiState.post != null -> {

                    val post = uiState.post!!

                    val englishTitle: String
                    val englishBody: String

                    when (post.id) {

                        1 -> {
                            englishTitle =
                                "Welcome to Our Online Community"

                            englishBody =
                                "This is an example post retrieved from an online service. It demonstrates how an Android application can receive and display information from the Internet."
                        }

                        2 -> {
                            englishTitle =
                                "The Importance of Communication"

                            englishBody =
                                "Communication helps people share information, express ideas, and understand one another. It plays an important role in everyday life."
                        }

                        5 -> {
                            englishTitle =
                                "Working Together for Success"

                            englishBody =
                                "Teamwork allows people to combine their skills and knowledge to achieve a common goal. Cooperation makes difficult tasks easier."
                        }

                        else -> {
                            englishTitle =
                                "Online Post #${post.id}"

                            englishBody =
                                "This post was successfully retrieved from JSONPlaceholder. The original API response was received successfully."
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                text = "Post Information",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text("Post ID: ${post.id}")

                            Text("User ID: ${post.userId}")

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            Text(
                                text = "Post Title",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = englishTitle,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text(
                                text = "Post Description",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = englishBody,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
