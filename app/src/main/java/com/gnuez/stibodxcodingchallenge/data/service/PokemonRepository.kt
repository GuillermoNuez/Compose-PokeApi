package com.gnuez.stibodxcodingchallenge.data.service

import com.gnuez.stibodxcodingchallenge.data.models.PokemonData

interface PokemonRepository {
    suspend fun fetchPokemons(limit: Int, offset: Int): Result<List<PokemonData>>
    suspend fun fetchPokemonByName(name: String): Result<PokemonData?>
}