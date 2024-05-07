package com.example.westwork5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.westwork5.ui.theme.Purple500
import com.example.westwork5.ui.theme.WestWork5Theme
import com.example.westwork5.view.SlideDeleteRecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WestWork5Theme {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val textState = remember {
                        mutableStateOf(TextFieldValue())
                    }
                    val dataList = remember {
                        mutableStateOf(mutableListOf(30,60,90))
                    }
                    val floatValue = remember {
                        mutableStateOf(mutableListOf<Float>())
                    }
                    val datasList = mutableListOf(2,3,4,5,6,7,8,9,10,11,12,13)

                    floatValue.value.clear()
                    dataList.value.forEachIndexed{index, value ->
                        floatValue.value.add(index = index, element = value.toFloat()/dataList.value.maxOrNull()!!.toFloat())
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("输入坐标\n（例：30,60,90）: ")
                        TextField(value = textState.value, onValueChange = { textState.value = it })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        dataList.value.clear()
                        floatValue.value.clear()
                        val newDataList = dataList.value.toMutableList()
                        val inputText = textState.value.text
                        val numbers = inputText.split(",").map { it.trim().toIntOrNull() }
                        newDataList.addAll(numbers.filterNotNull())
                        dataList.value = newDataList
                        dataList.value.forEachIndexed{index, value ->
                            floatValue.value.add(index = index, element = value.toFloat()/dataList.value.maxOrNull()!!.toFloat())
                        }
                        val newDataList2 = floatValue.value.toMutableList()
                        floatValue.value = newDataList2
                    }) {
                        Text("生成柱状图")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    BarGraph(
                        graphBarData = floatValue.value,
                        xAxisScaleData = datasList,
                        barData = dataList.value,
                        height = 400.dp,
                        roundType = BarType.TOP_CURVED,
                        barWidth = 20.dp,
                        barColor = Purple500,
                        barArrangement =Arrangement.SpaceEvenly
                    )

                    Button(onClick = {
                        startActivity(Intent(this@MainActivity, MyActivity::class.java))
                    }) {
                        Text("SlideDeleteRecyclerView")
                    }
                }
            }
        }
    }
}
