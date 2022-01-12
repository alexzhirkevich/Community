package github.alexzhirkevich.community.common.util

import android.os.CountDownTimer


class FlowableDelayedTask(delay : Long, task : Runnable) : Runnable{

    private val timer = object :CountDownTimer(delay,delay){
        override fun onFinish() {
            task.run()
        }
        override fun onTick(p0: Long) {}
    }

    override fun run() {
        timer.start()
    }

    fun cancel(){
        timer.cancel()
    }

    fun updateCountdown() {
        cancel()
        run()
    }
}