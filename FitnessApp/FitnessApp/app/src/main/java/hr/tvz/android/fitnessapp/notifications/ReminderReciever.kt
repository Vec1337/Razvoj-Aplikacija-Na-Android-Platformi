package hr.tvz.android.fitnessapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hr.tvz.android.fitnessapp.FitnessApp
import hr.tvz.android.fitnessapp.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val builder = NotificationCompat.Builder(context, FitnessApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_fitness)
            .setContentTitle("Time to work out!")
            .setContentText("Don't forget to log your workout today.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1001, builder.build())
        }
    }
}
