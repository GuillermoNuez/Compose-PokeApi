package com.gnuez.stibodxcodingchallenge

import com.gnuez.stibodxcodingchallenge.data.models.Pokemon
import com.gnuez.stibodxcodingchallenge.data.service.PokemonService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class APICallsTest {
    private lateinit var pokeApiService: PokemonService

    @Before
    fun setup() {
        pokeApiService = PokemonService.create()
    }

    @Test
    fun fetchPokemons() = runBlocking {
        val response = pokeApiService.getPokemons(20, 0)
        assertTrue("Expected Response is successful: ${response.code()}", response.isSuccessful)
        val pokemonData = response.body()
        assertNotNull("Expected Response body not null", pokemonData)
        val list: List<Pokemon> = pokemonData!!.results
        assertEquals("Expected number of Pokémon fetched", 21, list.size)
    }

    @Test
    fun fetchPokemonByName() = runBlocking {
        val response = pokeApiService.getPokemonByName("mew")
        assertTrue("Expected response is successful: ${response.code()}", response.isSuccessful)

        val pokemonData = response.body()
        assertNotNull("Expected body is not null", pokemonData)

        assertEquals("Expected Pokémon name", "mew", pokemonData!!.name)
    }

    @Test
    fun pokemonNotFound() = runBlocking {
        val response = pokeApiService.getPokemonByName("wrongName")

        assertFalse("Expected response is false", response.isSuccessful)
        assertEquals("Expected status code is 404", 404, response.code())

        val pokemonData = response.body()
        assertNull("Expected body should be null", pokemonData)
    }
}