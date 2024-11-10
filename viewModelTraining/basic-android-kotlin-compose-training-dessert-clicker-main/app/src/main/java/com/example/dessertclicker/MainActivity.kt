package com.example.dessertclicker

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.core.content.ContextCompat.startActivity
import com.example.dessertclicker.ui.theme.DessertClickerTheme
import com.example.dessertclicker.ui.theme.DessertViewModel
import com.example.dessertclicker.data.DessertUiState

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate Called")
        setContent {
            DessertClickerTheme {
                DessertClickerApp() // This is just the app screen that shows up
            }
        }
    }
}

private fun shareSoldDessertsInformation(intentContext: Context, dessertsSold: Int, revenue: Int) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, intentContext.getString(R.string.share_text, dessertsSold, revenue))
        type = "text/plain"
    }
    try {
        startActivity(intentContext, Intent.createChooser(sendIntent, null), null)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(intentContext, "Sharing is not available right now", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun DessertClickerApp(viewModel: DessertViewModel = viewModel()) {
    val uiState by viewModel.dessertUiState.collectAsState()
    DessertClickerApp(
        uiState = uiState,
        onDessertClicked = viewModel::onDessertClicked
    )
}

@Composable
private fun DessertClickerApp(
    uiState: DessertUiState,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            val intentContext = LocalContext.current
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary) // Use Material3 color scheme
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.app_name), color = Color.White)
                IconButton(
                    onClick = {
                        shareSoldDessertsInformation(
                            intentContext = intentContext,
                            dessertsSold = uiState.dessertsSold,
                            revenue = uiState.revenue
                        )
                    }
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Share")
                }
            }
        }
    ) { paddingValues ->
        DessertClickerScreen(
            revenue = uiState.revenue,
            dessertsSold = uiState.dessertsSold,
            dessertImageId = uiState.currentDessertImageId,
            onDessertClicked = onDessertClicked,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun DessertClickerScreen(
    revenue: Int,
    dessertsSold: Int,
    @DrawableRes dessertImageId: Int,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.bakery_back),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(dessertImageId),
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .align(Alignment.Center)
                        .clickable { onDessertClicked() },
                    contentScale = ContentScale.Crop,
                )
            }
            Column(
                modifier = modifier
                    .background(Color.White).width(500.dp).height(100.dp),
            ) {
                Text("Desserts Sold: $dessertsSold")
                Text("Revenue: $$revenue", textAlign = TextAlign.End)
            }
        }
    }
}

@Preview
@Composable
fun DessertClickerPreview() {
    DessertClickerTheme {
        DessertClickerApp(
            uiState = DessertUiState(),
            onDessertClicked = {}
        )
    }
}
