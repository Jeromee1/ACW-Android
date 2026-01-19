package com.jeremy.acw.core.utils

import com.jeremy.acw.data.model.user.forms.UpdateMovieForm
import com.jeremy.acw.data.model.user.requests.UpdateMovieReq

fun buildUpdateReq(data: UpdateMovieForm): UpdateMovieReq {
    return UpdateMovieReq(
        title = data.title,
        desc = data.desc,
        duration = data.duration,
        genre = data.genre,
        language = data.language,
        subtitles = data.subtitles,
        ageRating = data.ageRating,
        status = data.status,
        imgUrl = data.imgUrl
    )
}