package hr.tvz.android.listdetailapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ShareBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null) {
            if(intent.action == "hr.tvz.android.listdetailapp.ACTION_SHARE") {
                Toast.makeText(context, "Broadcast Recieved", Toast.LENGTH_SHORT).show()
            }
        }

    }
}