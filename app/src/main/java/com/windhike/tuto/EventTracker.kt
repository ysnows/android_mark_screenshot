package com.windhike.tuto



/**
 * author:gzzyj on 2017/9/25 0025.
 * email:zhyongjun@windhike.cn
 */
object EventTracker{
    fun trackClickAlbum(position:Int) {
        val map = mutableMapOf<String,String>(Pair("position","$position"))

    }

    fun trackClickAnno(position: Int) {
        val map = mutableMapOf<String,String>(Pair("position","$position"))

    }

    fun trackCreateText(text: String) {
        val map = mutableMapOf<String,String>(Pair("text",text))

    }

    fun trackOpenFloat(isOpen: Boolean) {
        val map = mutableMapOf<String,String>(Pair("text","$isOpen"))

    }
}
