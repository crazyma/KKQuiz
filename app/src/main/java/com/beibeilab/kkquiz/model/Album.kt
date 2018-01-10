package com.beibeilab.kkquiz.model

/**
 * Created by david on 2018/1/7.
 */
class Album(val id: String, val name: String, val images: List<AlbumImage>, val artist: Artist) {

    class AlbumImage(val url: String){}
}