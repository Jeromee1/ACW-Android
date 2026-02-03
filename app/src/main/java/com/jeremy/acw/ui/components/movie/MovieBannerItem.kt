package com.jeremy.acw.ui.components.movie

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.jeremy.acw.core.utils.longToStringTimeConverter
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.ui.components.core.SkeletonUI
import com.jeremy.acw.ui.theme.BlackTG
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary
import com.jeremy.acw.ui.theme.Gray
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MovieBannerItem(
    movies: List<Movie>,
    isLoggedIn: Boolean,
    onInfoClicked: (String) -> Unit,
    onBuyClicked: (String) -> Unit,
    navToLogin: () -> Unit
) {
    var index by remember { mutableIntStateOf(0) }
    var prevIndex by remember { mutableIntStateOf(0) }

    if (movies.size > 1) {
        LaunchedEffect(movies.size) {
            while (true) {
                delay(6000)
                prevIndex = index
                index = (index + 1) % movies.size
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .shadow(8.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AnimatedContent(
            targetState = index,
            transitionSpec = {
                if (targetState > prevIndex) {
                    (slideInHorizontally { fullWidth -> fullWidth } + fadeIn()).togetherWith(
                        slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut())
                } else {
                    (slideInHorizontally { fullWidth -> -fullWidth } + fadeIn()).togetherWith(
                        slideOutHorizontally { fullWidth -> fullWidth } + fadeOut())
                }.using(SizeTransform(clip = false))
            }
        ) { currentIndex ->
            val movie = movies[currentIndex]
            Box {
                SubcomposeAsyncImage(
                    model = movie.imgUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(8f / 11f),
                    contentScale = ContentScale.Crop,
                    loading = { SkeletonUI() }
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                        .background(BlackTG)
                        .align(Alignment.BottomCenter)
                        .padding(20.dp, 20.dp, 20.dp, 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            movie.title,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable { onInfoClicked(movie.id) }
                            )
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    movie.genre.firstOrNull() ?: "",
                                    lineHeight = 16.sp,
                                    color = Gray
                                )
                                Text(
                                    longToStringTimeConverter(movie.duration),
                                    lineHeight = 16.sp,
                                    color = Gray
                                )
                            }
                        }
                        Button(
                            onClick = { if (isLoggedIn) onBuyClicked(movie.id) else navToLogin() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(if (isLoggedIn) Primary else Secondary),
                        ) { Text("BUY NOW") }
                    }
                }
            }
        }
    }
}