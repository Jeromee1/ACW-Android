package com.jeremy.acw.ui.screens.details

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.jeremy.acw.core.utils.longToStringTimeConverter
import com.jeremy.acw.core.utils.naIfBlank
import com.jeremy.acw.core.utils.timestampToDMY
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.core.SkeletonUI
import com.jeremy.acw.ui.components.movie.DetailsInfo
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.BlackTG
import com.jeremy.acw.ui.theme.Gray
import com.jeremy.acw.ui.theme.KindaWhite
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun DetailsScreen(
    isLoggedIn: Boolean,
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val movie by viewModel.movie.collectAsStateWithLifecycle()
    var isExpanded by remember { mutableStateOf(false) }
    var canExpand by remember { mutableStateOf(false) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.toast.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    if (isLoading) {
        LoadingSpinner()
    } else {
        Details(
            movie = movie!!,
            isLoggedIn = isLoggedIn,
            isExpanded = isExpanded,
            canExpand = canExpand,
            onExpandToggle = { isExpanded = !isExpanded },
            onOverflowDetected = { canExpand = true },
            onBuyClicked = { navController.navigate(Screen.Booking(it)) },
            navToLogin = { navController.navigate(Screen.Login) }
        )
    }
}

@Composable
fun Details(
    movie: Movie,
    isLoggedIn: Boolean,
    isExpanded: Boolean,
    canExpand: Boolean,
    onExpandToggle: () -> Unit,
    onOverflowDetected: () -> Unit,
    onBuyClicked: (String) -> Unit,
    navToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            SubcomposeAsyncImage(
                model = movie.imgUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
                contentScale = ContentScale.Crop,
                loading = { SkeletonUI() }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(BlackTG)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = movie.title,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "${naIfBlank(movie.genre.firstOrNull())} • ${longToStringTimeConverter(movie.duration)}",
                    fontSize = 18.sp,
                    color = Gray
                )
                Button(
                    onClick = { if (isLoggedIn) onBuyClicked(movie.id) else navToLogin() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLoggedIn) Primary else Secondary
                    ),
                ) {
                    Text("BUY TICKET", modifier = Modifier.padding(8.dp), fontSize = 20.sp)
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(20.dp)
        ) {
            Text(
                text = movie.desc,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                        onOverflowDetected()
                    }
                },
                modifier = Modifier.animateContentSize()
            )
            if (canExpand) {
                Text(
                    text = if (isExpanded) "Show Less" else "Read More",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { onExpandToggle() },
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = KindaWhite)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "INFORMATION",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DetailsInfo("Release Date", naIfBlank(movie.releaseDate?.let { timestampToDMY(it) }))
                    DetailsInfo("Genre", naIfBlank(movie.genre.takeIf { it.isNotEmpty() }?.joinToString(" / ")))
                    DetailsInfo("Subtitles", naIfBlank(movie.subtitles.takeIf { it.isNotEmpty() }?.joinToString(" / ")))
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DetailsInfo("Running Time", naIfBlank(movie.duration.let { longToStringTimeConverter(it) }))
                    DetailsInfo("Spoken Language", naIfBlank(movie.language.takeIf { it.isNotEmpty() }?.joinToString(" / ")))
                    DetailsInfo("Classifications", naIfBlank(movie.ageRating))
                }
            }
        }
        Spacer(Modifier.height(40.dp))
    }
}