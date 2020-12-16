package id.ajiguna.moviecatalogue5.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetServices : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactorys(this.applicationContext)
}