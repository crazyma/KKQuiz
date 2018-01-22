package com.beibeilab.kkquiz.playpage

import com.beibeilab.kkquiz.model.Artist
import com.beibeilab.kkquiz.model.Track
import com.beibeilab.kkquiz.playpage.PlayPageFragment.Companion.urlKKBoxWidget

/**
 * Created by david on 2018/1/20.
 */
class PlayPageModel(val trackList: List<Track>, val artist: Artist) {
    var index = 0

    fun getCurrentTrackId(): String {
        return trackList[index].id
    }

    fun getCurrentTrackName(): String {
        return trackList[index].name
    }

    fun getCurrentAlbumImageUrl(): String {
        return trackList[index].album.images.last().url
    }

    fun getCurrentAlbumName(): String {
        return trackList[index].album.name
    }

    fun nextSong(): Boolean {
        return ++index < trackList.size
    }

    fun getCurrentTrackUrl(): String {
        val builder = StringBuilder()
        builder.append(urlKKBoxWidget)
        builder.append("&id=")
        builder.append(getCurrentTrackId())
        return builder.toString()
    }

}