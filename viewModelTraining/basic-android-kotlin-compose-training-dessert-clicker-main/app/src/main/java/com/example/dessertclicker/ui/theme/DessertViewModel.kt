package com.example.dessertclicker.ui.theme

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.DessertUiState
import com.example.dessertclicker.data.Datasource.dessertList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {

    private val _dessertUiState = MutableStateFlow(DessertUiState())
    val dessertUiState: StateFlow<DessertUiState> = _dessertUiState.asStateFlow()

    fun onDessertClicked() {
        _dessertUiState.update { currentState ->
            val newDessertsSold = currentState.dessertsSold + 1
            val nextDessertIndex = determineDessertIndex(newDessertsSold)
            currentState.copy(
                dessertsSold = newDessertsSold,
                revenue = currentState.revenue + currentState.currentDessertPrice,
                currentDessertImageId = dessertList[nextDessertIndex].imageId,
                currentDessertPrice = dessertList[nextDessertIndex].price,
                currentDessertIndex = nextDessertIndex
            )
        }
    }

    private fun determineDessertIndex(dessertsSold: Int): Int {
        var index = 0
        for (i in dessertList.indices) {
            if (dessertsSold >= dessertList[i].startProductionAmount) {
                index = i
            } else {
                break
            }
        }
        return index
    }
}
