package com.gnuez.stibodxcodingchallenge.viewModel

import android.content.Context
import android.util.Log
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

class SharedViewModel(
) : ViewModel() {
    private val repository: PokemonRepository = PokemonRepositoryImpl()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    private val _pokemonList = MutableStateFlow<List<PokemonData>>(emptyList())
    val pokemonList: StateFlow<List<PokemonData>> = _pokemonList

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _noResult = MutableStateFlow(false)
    val noResult: StateFlow<Boolean> = _noResult

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var fetchJob: Job? = null

    private val favList: MutableList<Int> = mutableListOf()

    private val _isFavourite = MutableStateFlow(false)
    val isFavourite: StateFlow<Boolean> = _isFavourite

    private val _pokemonDetails =  MutableStateFlow<PokemonData?>(null)
    val pokemonDetails: StateFlow<PokemonData?> = _pokemonDetails

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

    fun saveFavorites(context: Context) {
        val favListString = favList.joinToString(",")
        val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)

        Log.d("Debug", "Saving: " +favListString )
        sharedPreferences.edit().putString("fav_list", favListString).apply()
    }

    fun loadFavorites(context: Context) {
        val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favListString = sharedPreferences.getString("fav_list", "")

        if (!favListString.isNullOrEmpty()) {
            val newFavorites = favListString
                .split(",")
                .mapNotNull { it.toIntOrNull() }

            favList.addAll(newFavorites)
        }


        Log.d("Debug", "Loaded: " +favList.toString() )
    }




    fun fetchPokemonDetails(name: String) {
        viewModelScope.launch {
            try {
                val result = repository.fetchPokemonByName(name)
                if (result.isSuccess) {
                    _loading.value = false
                    _pokemonDetails.value = result.getOrNull()
                }
            } catch (e: Exception) {
                _loading.value = false
                Log.e("PokemonViewModel", "Error fetching Pokemon details", e)
            }

        }
    }

    fun favClicked(id: Int) {
        if (favList.contains(id)) {
            favList.remove(id)
            _isFavourite.value = false
        } else {
            favList.add(id)
            _isFavourite.value = true
        }
    }

    fun checkIfFav(id: Int) {
        _isFavourite.value = favList.contains(id)
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