package com.example.datastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.datastore.ui.theme.DataStoreTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // Este es mi viewModel
    val myViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataStoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserNameScrean(innerPadding, myViewModel)
                }
            }
        }
    }
}

@Composable
fun UserNameScrean(paddingValues: PaddingValues, viewModel: UserViewModel) {

    val coroutineCtx = rememberCoroutineScope() // Hilo. Para lanzar hilos o funciones supend
    val context = LocalContext.current
    var fieldText by remember() { mutableStateOf("") }
    // recogemos la información de flujo con el collectAsState.
    // 1. apuntamos a viewModel
    // 2. llamamos a getData
    // 3. Parámetros necesarios: contexto y Preferences.Key correspondiente
    // 4. collectAsState para guardarlo como estado
    val userName = viewModel.getData(
        context,
        stringPreferencesKey("userName")
    ).collectAsState("")
    Column(
        Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("User: ${userName.value}")
        TextField(
            fieldText,
            { fieldText = it })
        Button({
            // Lanzas el hilo para realizar el set de forma asincrona (hilo)
            coroutineCtx.launch {
                viewModel.setData(
                    context,
                    stringPreferencesKey("userName"),
                    fieldText
                )
            }
        }) { Text("Guardar usuario!") }
    }

}