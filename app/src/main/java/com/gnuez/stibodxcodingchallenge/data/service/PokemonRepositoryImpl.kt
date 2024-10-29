package com.gnuez.stibodxcodingchallenge.data.service

import com.gnuez.stibodxcodingchallenge.data.models.PokemonData
import retrofit2.Response
import java.io.IOException

class PokemonRepositoryImpl : PokemonRepository {

    private val pokemonService = PokemonService.create()

    override suspend fun fetchPokemons(limit: Int, offset: Int): Result<List<PokemonData>> {
        return try {
            val response = pokemonService.getPokemons(limit, offset)

            if (response.isSuccessful) {
                val data = response.body()?.results

                val pokemonListData = mutableListOf<PokemonData>()

                if (data != null) {
                    for (p in data) {
                        val detailsResponse: Response<PokemonData> =
                            pokemonService.getPokemonDetails(p.url)
                        if (detailsResponse.isSuccessful) {
                            detailsResponse.body()?.let { pokemonData ->
                                pokemonListData += pokemonData
                            } ?: run {
                                return Result.failure(IOException("No data found for ${p.name}"))
                            }
                        } else {
                            return Result.failure(IOException("Error fetching details for ${p.name}, code: ${detailsResponse.code()}"))
                        }
                    }
                }
                Result.success(pokemonListData)
            } else {
                Result.failure(IOException("Error fetching data, code: ${response.code()}"))
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun fetchPokemonByName(name: String): Result<PokemonData?> {
        return try {
            val response = pokemonService.getPokemonByName(name)

            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                if (response.body() == null) {
                    Result.success(null)
                } else {
                    Result.failure(IOException("Error fetching data, code: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}