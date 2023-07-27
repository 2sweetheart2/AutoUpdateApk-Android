package me.sweetie.autoupdate.interfaces

import me.sweetie.autoupdate.objs.UpdateObject

fun interface IUpdate {
    fun getUpdate(update:UpdateObject)
}