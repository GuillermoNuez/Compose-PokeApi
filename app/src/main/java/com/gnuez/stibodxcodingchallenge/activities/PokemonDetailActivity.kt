package com.gnuez.stibodxcodingchallenge.activities

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.gnuez.stibodxcodingchallenge.R
import com.gnuez.stibodxcodingchallenge.data.models.PokemonData
import com.gnuez.stibodxcodingchallenge.viewModel.SharedViewModel

@Composable
fun DetailScreen(
    pokemon: PokemonData,
    viewModel: SharedViewModel,
    context: android.content.Context
) {
    val isFavorite by viewModel.isFavourite.collectAsState()



    val gradientColors = listOf(Color(0xFF48B684), Color(0xFF216379))

    val latoBoldFont = FontFamily(Font(R.font.lato_bold))

    var expanded: Boolean by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
            )
        }
        Surface(
            modifier = Modifier.fillMaxSize(), color = Color.Transparent
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = pokemon.name.replaceFirstChar { it.uppercase() },
                            color = colorResource(id = R.color.mint_background),
                            fontSize = 25.sp,
                            fontFamily = latoBoldFont,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )

                        HorizontalDivider(
                            color = colorResource(id = R.color.mint_background),
                            modifier = Modifier.padding(horizontal = 60.dp, vertical = 0.dp)
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(
                            top = 0.dp, start = 20.dp, end = 20.dp, bottom = 30.dp
                        )
                        .fillMaxSize(),

                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.mint_background)
                    )
                ) {
                    Column(modifier = Modifier.padding(25.dp)) {
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    viewModel.favClicked(pokemon.id)
                                    viewModel.saveFavorites(context)
                                },
                                tint = colorResource(id = R.color.teal_primary),
                            )
                        }


                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = pokemon.sprites.front_default),
                                contentDescription = stringResource(id = R.string.enter_pokemon_name),
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                                    .background(Color.White)
                                    .border(2.dp, colorResource(id = R.color.teal_primary))
                            )

                            Column {
                                GreenTV(stringResource(id = R.string.pokemon_id) + pokemon.id)
                                GreenTV(stringResource(id = R.string.height) + pokemon.height)
                                GreenTV(stringResource(id = R.string.weight) + pokemon.weight)
                                GreenTV(stringResource(id = R.string.base_experience) + pokemon.base_experience)
                            }
                        }

                        Row(modifier = Modifier.padding(top = 15.dp, bottom = 10.dp)) {
                            if (pokemon.types.size > 1) {
                                GreenTV(stringResource(id = R.string.pokemon_types))
                            } else {
                                GreenTV(stringResource(id = R.string.pokemon_type))
                            }

                            LazyRow {
                                items(pokemon.types.size) { index ->
                                    GreenTV(pokemon.types[index].type.name.replaceFirstChar { it.uppercase() })
                                    if (index < pokemon.types.size - 1) {
                                        GreenTV(" / ")
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp)
                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(colorResource(id = R.color.teal_primary))
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .testTag("moveList")
                                    .clickable { expanded = !expanded }) {
                                Text(
                                    stringResource(id = R.string.pokemon_moves) + " (${pokemon.moves.size})",
                                    color = colorResource(id = R.color.mint_background),
                                    fontSize = 20.sp,
                                    fontFamily = latoBoldFont,
                                    modifier = Modifier.padding(start = 5.dp)
                                )

                                ArrowIcon(expanded)
                            }

                            if (expanded) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 5.dp)
                                ) {
                                    items(pokemon.moves) { move ->
                                        Column {
                                            GreenTV(
                                                move.move.name.replace("-", " ")
                                                    .replaceFirstChar { it.uppercase() })
                                            HorizontalDivider(
                                                modifier = Modifier.padding(
                                                    vertical = 5.dp
                                                ),
                                                color = colorResource(id = R.color.teal_primary)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ArrowIcon(expanded: Boolean) {
    val arrowIcon: ImageVector = if (expanded) {
        Icons.Default.KeyboardArrowUp
    } else {
        Icons.Default.KeyboardArrowDown
    }

    Image(
        imageVector = arrowIcon,
        contentDescription = if (expanded)
            stringResource(id = R.string.collapse) else stringResource(
            id = R.string.expand
        ),
        modifier = Modifier.size(30.dp),
        colorFilter = ColorFilter.tint(colorResource(id = R.color.mint_background))
    )
}

@Composable
fun GreenTV(text: String, modifier: Modifier = Modifier) {

    val latoBoldFont = FontFamily(Font(R.font.lato_bold))

    Text(
        text = text,
        color = colorResource(id = R.color.teal_primary),
        fontSize = 20.sp,
        modifier = modifier,
        fontFamily = latoBoldFont
    )
}