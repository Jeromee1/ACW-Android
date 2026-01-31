package com.jeremy.acw.ui.components.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.ui.components.core.SkeletonUI
import com.jeremy.acw.ui.theme.Gray
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun MovieItem(
    movie: Movie,
    onClicked: (String) -> Unit,
    onLongPressed: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            model = movie.imgUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .background(Secondary)
                .combinedClickable(
                    onClick = { onClicked(movie.id) },
                    onLongClick = { onLongPressed(movie.id) }
                ),
            contentScale = ContentScale.FillWidth,
            loading = { SkeletonUI() }
        )
        Text(
            movie.title,
            fontSize = 12.sp,
            color = Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
            )
    }
}