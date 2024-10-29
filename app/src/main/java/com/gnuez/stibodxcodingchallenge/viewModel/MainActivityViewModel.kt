package com.gnuez.stibodxcodingchallenge.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnuez.stibodxcodingchallenge.data.models.PokemonData
import com.gnuez.stibodxcodingchallenge.data.service.PokemonRepository
import com.gnuez.stibodxcodingchallenge.data.service.PokemonRepositoryImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class MainActivityViewModel : ViewModel() {
    private val repository: PokemonRepository = PokemonRepositoryImpl()

    private val _pokemonList = MutableStateFlow<List<PokemonData>>(emptyList())
    val pokemonList: StateFlow<List<PokemonData>> = _pokemonList

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _noResult = MutableStateFlow(false)
    val noResult: StateFlow<Boolean> = _noResult

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var fetchJob: Job? = null

    fun fetchPokemons(limit: Int, offset: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                resetState()
                val result = repository.fetchPokemons(limit, offset)
                if (result.isSuccess) {
                    val list = result.getOrNull() ?: emptyList()
                    _pokemonList.value = list
                } else {
                    showUnexpectedError()
                }
            } catch (e: IOException) {
                showNoConexionError()
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    showUnexpectedError()
                }
            }
            _loading.value = false
        }
    }

    fun fetchPokemonByName(name: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                resetState()
                val result = repository.fetchPokemonByName(name)
                val data = result.getOrNull()
                if (result.isSuccess && data != null) {
                    _pokemonList.value = listOf(data)
                } else {
                    if (result.isFailure) {
                        showUnexpectedError()
                    } else {
                        _noResult.value = true
                        _pokemonList.value = emptyList()
                    }
                }
            } catch (e: IOException) {
                showNoConexionError()
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    showUnexpectedError()
                }
            }
            _loading.value = false
        }
    }

    private fun showUnexpectedError() {
        _errorMessage.value = "An unexpected error occurred.\nPlease try again."
    }

    private fun showNoConexionError() {
        _errorMessage.value = "No internet connection.\nPlease check your settings."
    }

    private fun resetState() {
        _errorMessage.value = ""
        _noResult.value = false
        _loading.value = true
    }
}