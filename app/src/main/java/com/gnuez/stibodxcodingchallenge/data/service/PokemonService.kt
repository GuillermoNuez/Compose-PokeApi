package com.gnuez.stibodxcodingchallenge.data.service
import com.gnuez.stibodxcodingchallenge.data.models.PokemonData
import com.gnuez.stibodxcodingchallenge.data.models.PokemonResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface PokemonService {
    @GET("pokemon")
    suspend fun getPokemons(@Query("limit") limit: Int, @Query("offset") offset: Int):  Response<PokemonResponse>

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): Response<PokemonData>

    @GET
    suspend fun getPokemonDetails(@Url url: String): Response<PokemonData>

    companion object {
        fun create(): PokemonService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(PokemonService::class.java)
        }
    }
}