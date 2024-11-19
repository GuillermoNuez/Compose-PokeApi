package com.gnuez.stibodxcodingchallenge.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.gnuez.stibodxcodingchallenge.viewModel.MainActivityViewModel

import com.gnuez.stibodxcodingchallenge.R
import com.gnuez.stibodxcodingchallenge.ui.theme.StiboDXCodingChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StiboDXCodingChallengeTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        val context = LocalContext.current

        val viewModel: MainActivityViewModel = viewModel()

        val pokemonList by viewModel.pokemonList.collectAsState()

        val gradientColors = listOf(
            Color(0xFFB2E5D4),
            Color(0xFFB2E5D4),
            Color(0xFFD2F6EC),
            Color(0xFFB2E5D4),
        )

        val latoFont = FontFamily(Font(R.font.lato_regular))
        val latoBoldFont = FontFamily(Font(R.font.lato_bold))

        val name = viewModel.name.collectAsState()

        var offset by remember {
            mutableIntStateOf(0)
        }

        var showPagination by remember {
            mutableStateOf(true)
        }

        val itemAmount = 24

        window.navigationBarColor = Color(0xFFB2E5D4).toArgb()

        val errorMessage by viewModel.errorMessage.collectAsState()
        val emptyMessage by viewModel.noResult.collectAsState()
        val loading by viewModel.loading.collectAsState()

        fun searchAll() {
            viewModel.fetchPokemons(itemAmount, offset)
            showPagination = true
        }

        fun searchEvent() {
            if (name.value.isNotBlank()) {
                showPagination = false
                offset = 0
                viewModel.fetchPokemonByName(name.value.lowercase())

            } else {
                searchAll()
            }
        }

        LaunchedEffect(name.value) {
            if (name.value.isBlank()) {
                searchEvent()
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(
                            0f,
                            0f
                        ),
                        end = Offset(
                            size.width,
                            size.height
                        )
                    )
                )
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = 20.dp,
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        OutlinedTextField(
                            value = name.value,
                            onValueChange = { viewModel.onNameChange(it) },
                            textStyle = TextStyle(fontFamily = latoFont, fontSize = 20.sp),
                            placeholder = {
                                Text(
                                    stringResource(id = R.string.enter_pokemon_name),
                                    fontFamily = latoFont,
                                    fontSize = 20.sp
                                )
                            },
                            trailingIcon = {
                                if (name.value.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onNameChange("")}) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = stringResource(id = R.string.clear_Text_ContentDescription)
                                        )
                                    }
                                }
                            },

                            modifier = Modifier
                                .padding(top = 20.dp)
                                .weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = colorResource(id = R.color.mint_background),
                                unfocusedContainerColor = colorResource(id = R.color.mint_background),
                                cursorColor = colorResource(id = R.color.teal_primary),
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    searchEvent()
                                }
                            )
                        )

                        Spacer(modifier = Modifier.size(16.dp))
                        Button(
                            onClick = {
                                if (name.value.isNotBlank()) {
                                    searchEvent()
                                }
                            },
                            modifier = Modifier
                                .size(56.dp),
                            shape = RoundedCornerShape(20),
                            colors = ButtonDefaults.buttonColors(
                                colorResource(id = R.color.teal_primary),
                            ),
                            contentPadding = PaddingValues(14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(id = R.string.button_search)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(
                            top = 15.dp,
                            bottom = 60.dp,
                            start = 10.dp,
                            end = 10.dp
                        ),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (errorMessage.isNotBlank()) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = colorResource(id = R.color.teal_primary),
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = errorMessage,
                                    textAlign = TextAlign.Center,
                                    fontFamily = latoBoldFont,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Button(
                                    onClick = { searchEvent() },
                                    modifier = Modifier
                                        .padding(top = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        colorResource(id = R.color.teal_primary),
                                    ),
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                    Text(
                                        text = stringResource(id = R.string.button_refresh),
                                        color = Color.White,
                                        fontFamily = latoBoldFont
                                    )
                                }
                            }
                        } else if (emptyMessage) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = colorResource(id = R.color.teal_primary),
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = stringResource(id = R.string.pokemon_not_found),
                                    textAlign = TextAlign.Center,
                                    fontFamily = latoBoldFont,
                                    fontSize = 20.sp
                                )
                            }

                        } else {
                            if (showPagination) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .background(
                                            colorResource(id = R.color.teal_primary),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clip(RoundedCornerShape(8.dp))
                                        .fillMaxWidth()
                                        .height(50.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                        contentDescription = stringResource(id = R.string.previousPage),
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clickable {
                                                if (offset > 0) {
                                                    offset -= itemAmount
                                                    searchAll()
                                                }
                                            },
                                        tint = colorResource(id = R.color.aqua_light),
                                    )

                                    Text(
                                        text = (offset / itemAmount).toString(),
                                        fontFamily = latoBoldFont,
                                        color = colorResource(id = R.color.aqua_light),
                                        fontSize = 30.sp,
                                    )

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = stringResource(id = R.string.nextPage),
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clickable {
                                                offset += itemAmount
                                                searchAll()
                                            },
                                        tint = colorResource(id = R.color.aqua_light),
                                    )
                                }
                            }

                            if (loading) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    CircularProgressIndicator(
                                        color = colorResource(id = R.color.teal_primary),
                                        strokeWidth = 6.dp,
                                        modifier = Modifier
                                            .size(100.dp)
                                    )
                                }
                            } else {

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(top = 10.dp)
                                        .testTag("gridList")
                                ) {
                                    items(pokemonList) { pokemon ->
                                        Card(
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .fillMaxWidth()
                                                .height(150.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .clickable {
                                                        val intent = Intent(
                                                            context,
                                                            PokemonDetailActivity::class.java
                                                        ).apply {
                                                            putExtra("pokemonData", pokemon)
                                                        }
                                                        context.startActivity(intent)
                                                    },
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center

                                            ) {
                                                Text(
                                                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                                                    color = colorResource(id = R.color.aqua_light),
                                                    fontFamily = latoBoldFont,
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 20.sp,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(colorResource(id = R.color.teal_primary))
                                                        .padding(vertical = 5.dp)
                                                )
                                                Image(
                                                    painter = rememberAsyncImagePainter(model = pokemon.sprites.front_default),
                                                    contentDescription = stringResource(id = R.string.pokemon_Sprite),
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(
                                                            colorResource(
                                                                id = R.color.mint_background
                                                            )
                                                        )
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
}