package com.gnuez.stibodxcodingchallenge.data.models

import java.io.Serializable

data class PokemonResponse(
    val results: List<Pokemon>
)

data class Pokemon(
    val name: String,
    val url: String
)

data class PokemonData(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val types: List<Types>,
    val moves: List<Moves>,
    val base_experience: Int
): Serializable

data class Moves(
    val move: Move
): Serializable

data class Move(
    val name: String
): Serializable

data class Sprites(
    val front_default: String
): Serializable

data class Types(
    val type: Type,
): Serializable

data class Type(
    val name: String,
): Serializable