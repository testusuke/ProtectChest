package net.testusuke.open.protectchest.Chest

import org.bukkit.Location
import org.bukkit.Material
import java.util.*

/**
 * Created on 2020/06/22
 * Author testusuke
 */
data class ChestInformation(val location: Location,val material:Material,val author:String,val date:String)
/**
 *  Locationは [ , ]で分割保存する。
 */