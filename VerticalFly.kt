package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventState
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.util.math.Vec3d


/**
 *@author ycx_mulin
 */

@ModuleInfo(name = "VerticalFly", description = "CCBlueX", category = ModuleCategory.MOVEMENT)
class VerticalFly : Module() {

    private val toggle = IntegerValue("Toggle", 0, 0,100)
    private val timer = FloatValue("Timer", 0.446f, 0.1f,1f)

    var ticks = 0
    var pos: Vec3d? = null

    override fun onEnable() {
        ticks = 0
        pos = null
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1f
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val player = mc2.player ?: return
        if (ticks == 0)
            player.jump()
        // For some reason, low timer makes the timer jump (2 tick start)
        // A lot more stable.
        else if (ticks <= 5)
            mc.timer.timerSpeed = timer.get()
        else if (ticks > 5)
            mc.timer.timerSpeed = 1f
        // If ticks goes over toggle limit and toggle isnt 0, disable.
        if (ticks >= toggle.get() && toggle.get() != 0)
            this.state= false

        ticks++
    }


    @EventTarget
    fun onMotion(event: MotionEvent) {
        val player = mc2.player ?: return
        if (ticks >= 2) {
            when (event.eventState) {
                EventState.PRE -> {
                    pos = player.positionVector
                    player.setPosition(player.posX + 1152, player.posY, player.posZ + 1152)
                }

                else -> {
                    if (pos != null) {
                        player.setPosition(pos!!.x, pos!!.y, pos!!.z)
                    }
                }
            }
        }
    }
}