package com.rickex.notivac

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rickex.notivac.MainActivity
import com.rickex.notivac.app.MyApp
import com.rickex.notivac.preferences.UserPreferenceManager
import java.io.IOException
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val data = remoteMessage.data
        //val title = remoteMessage.data.get("title").toString()
        //val body = remoteMessage.data.get("body").toString()
        //val picUrl = remoteMessage.data.get("picurl").toString()

        val title1 = data.get("title").toString()
        //val title2 = data.get("title2").toString()
        val body = data.get("body").toString()
        val picUrl = data.get("picurl").toString()
        //val postId = data.get("postid")!!.toInt()
        val fromNoti = data.get("fromnoti")!!.toBoolean()
        val notiType = data.get("notitype").toString()
        val notiUrl = data.get("notiurl").toString()

        sendNotificationNew(title1, body, picUrl, fromNoti, notiType, notiUrl)
        //////////////sendNotification("","","","",true)
        //remoteMessage.data.get("message")
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    /*private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }*/

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        UserPreferenceManager.setPushNotiId(this, token)
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */

    private fun sendNotificationNew(messageTitle : String,messageBody: String, picUrl : String, fromNoti : Boolean, notiType : String, notiUrl : String) {
        if(notiType == "1"){
            sendNotiType1(messageTitle, messageBody, picUrl, fromNoti, notiType)
        }
        else if(notiType == "2"){
            sendNotiType2(messageTitle, messageBody, picUrl, fromNoti, notiType, notiUrl)
        }
    }

    fun sendNotiType1(messageTitle : String,messageBody: String, picUrl : String, fromNoti : Boolean, notiType : String){
        var image : Bitmap? = null

        try {
            val url = URL(picUrl)
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: IOException) {
            //System.out.println(e)
        }


        val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("id",postId)
        //intent.putExtra("fromNoti", fromNoti)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 99 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = MyApp.CHANNEL_ID
        //val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val appIcon = BitmapFactory.decodeResource(
            resources,
            R.drawable.applogo
        )

        val notificationBuilder : Notification

        if(picUrl.startsWith("http")) {
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setLargeIcon(appIcon)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(image)
                    .bigLargeIcon(null))

                //.setStyle(NotificationCompat.BigTextStyle()
                //    .bigText(messageBody))
                .setAutoCancel(true)
                .build()
        }
        else{
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setLargeIcon(appIcon)
                //.setStyle(NotificationCompat.BigPictureStyle()
                //    .bigPicture(image)
                //    .bigLargeIcon(null))

                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(messageBody))
                .setAutoCancel(true)
                .build()
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify((101..200).random() /* ID of notification */, notificationBuilder)
    }

    fun sendNotiType2(messageTitle : String,messageBody: String, picUrl : String, fromNoti : Boolean, notiType : String, notiUrl : String){
        var image : Bitmap? = null

        try {
            val url = URL(picUrl)
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: IOException) {
            //System.out.println(e)
        }


        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("fromNoti",true)
        intent.putExtra("notiType",notiType)
        intent.putExtra("notiUrl",notiUrl)
        //intent.putExtra("fromNoti", fromNoti)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 99 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = MyApp.CHANNEL_ID
        //val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val appIcon = BitmapFactory.decodeResource(
            resources,
            R.drawable.applogo
        )

        val notificationBuilder : Notification

        if(picUrl.startsWith("http")) {
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setLargeIcon(appIcon)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(image)
                    .bigLargeIcon(null))

                //.setStyle(NotificationCompat.BigTextStyle()
                //    .bigText(messageBody))
                .setAutoCancel(true)
                .build()
        }
        else{
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setLargeIcon(appIcon)
                //.setStyle(NotificationCompat.BigPictureStyle()
                //    .bigPicture(image)
                //    .bigLargeIcon(null))

                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(messageBody))
                .setAutoCancel(true)
                .build()
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify((101..200).random() /* ID of notification */, notificationBuilder)
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

}