package com.beibeilab.kkquiz.model

/**
 * Created by david on 2018/1/10.
 */
class Artist(val id: String, val name: String, val images: List<Image>) {
    class Image(val url: String) {}
}