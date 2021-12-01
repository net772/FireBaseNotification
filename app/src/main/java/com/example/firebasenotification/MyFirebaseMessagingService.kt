package com.example.firebasenotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/** 다음과 같은 경우 토큰이 변경될 수 있다.
 * - 새 기기에서 앱 복원하는 경우
 * - 사용자가 앱 삭제/재설치 하는 경우
 * - 사용자가 앱 데이터 소거하는 경우
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    // 토큰이 갱신 될 때마다 이 곳에서 작업처리를 해주면 된다.
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    // 메시지 수신시마다 실행
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 채널 생성
        createNotificationChannelIfNeeded()

        val type = remoteMessage.data["type"]?.let { NotificationType.valueOf(it) }
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        type ?: return

        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 채널의 경우에는 버전 O부터 생성
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT // 중요도
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun createNotification(type: NotificationType, title: String?, message: String?): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)
        // 실제 알림 컨텐츠 만들기
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24) // 아이콘 보여주기
            .setContentTitle(title) // 타이틀
            .setContentText(message) // 메시지 내용
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // 오레오 이하 버전에서는 지정 필요
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // 알림 클릭 시 자동 제거

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle() // 확장 가능한 알림
                        .bigText(
                            "😀 😃 😄 😁" +
                                "🤢 🤮 🤧 😷  🤕"
                        )
                )
            }
            //커스텀 알림
            NotificationType.CUSTOM -> {
                notificationBuilder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.message, message)
                        }
                    )
            }
        }
        return notificationBuilder.build()
    }

    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
    }
}
