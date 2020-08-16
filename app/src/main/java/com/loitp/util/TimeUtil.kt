package com.loitp.util

class TimeUtil {
    companion object {
        fun getDurationString(s: Long): String? {
            var seconds = s
            val hours = seconds / 3600
            val minutes = seconds % 3600 / 60
            seconds %= 60
            return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds)
        }

        private fun twoDigitString(number: Long): String {
            if (number == 0L) {
                return "00"
            }
            return if (number / 10 == 0L) {
                "0$number"
            } else {
                number.toString()
            }
        }
    }
}
