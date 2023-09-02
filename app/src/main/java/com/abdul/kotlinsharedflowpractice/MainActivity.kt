package com.abdul.kotlinsharedflowpractice

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abdul.kotlinsharedflowpractice.ui.theme.KotlinSharedFlowPracticeTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinSharedFlowPracticeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    FlowsPractice()
                }
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun FlowsPractice() {
    //consumer1
    GlobalScope.launch(Dispatchers.Main) {
        val result = producer()
        result.collect {
            Log.d("soha1", "Item - $it")
        }
    }

    //consumer2
    GlobalScope.launch(Dispatchers.Main) {
        val result = producer()
        result.collect {
           delay(2500) //2.5 seconds
            Log.d("soha2", "Item - $it")
        }
    }
}

//producer
@OptIn(DelicateCoroutinesApi::class)
private fun producer(): Flow<Int> {
    //normal flow (cold flow)
    /* return flow {
         val list = listOf<Int>(1,2,3,4,5)
         list.forEach {
             delay(1000) //1 second
             emit(it)
         }
     }*/

    //shared flow (hot flow)
   // val mutableSharedFlow = MutableSharedFlow<Int>()
    val mutableSharedFlow = MutableSharedFlow<Int>(replay = 2) //It works as a buffer
    GlobalScope.launch {
        val list = listOf<Int>(1, 2, 3, 4, 5)
        list.forEach {
            mutableSharedFlow.emit(it)
            delay(1000) //1 second
        }
    }
    return mutableSharedFlow
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinSharedFlowPracticeTheme {
        Greeting("Android")
    }
}