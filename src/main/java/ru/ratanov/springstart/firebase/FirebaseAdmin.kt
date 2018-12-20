package ru.ratanov.springstart.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.ratanov.springstart.persistence.repo.MagnetRepository


@Component
class FirebaseAdmin {

    private data class User(val fcmToken: String = "", val films: MutableMap<String, String> = mutableMapOf())

    private var firestore: Firestore? = null

    init {
        val serviceAccount = this.javaClass.getResourceAsStream("/firebase.json")
        val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

        FirebaseApp.initializeApp(options)
        firestore = FirestoreClient.getFirestore()

        readDatabase()
    }

    @Scheduled(fixedRate = 15000)   // cron = "0 * * * * *" (every hour at 0 minutes`)
    private fun readDatabase() {
        var needUpdateDb = false

        firestore?.let { db ->
            val usersRef = db.collection("users").get()
            val future = usersRef.get()
            val users = future.toObjects(User::class.java)

            users.forEach { user ->
                val films = user.films
                films.forEach { url, oldMagnet ->
                    val newMagnet = MagnetRepository.getMagnetLink(url)
                    println(oldMagnet)
                    println(newMagnet)
                    if (!newMagnet.equals(oldMagnet, true)) {
                        needUpdateDb = true
                        println("Need to send push to ${user.fcmToken}")
                        sendMessage(fcmToken = user.fcmToken)
                        films[url] = newMagnet

                    } else {
                        println("No updates")
                    }
                    println("--------")
                }

                if (needUpdateDb) {
                    val updateFuture = db.collection("users").document("admin").update("films",  films as Map<String, Any>)
                    println("Update time : ${updateFuture.get().updateTime}")
                }
            }

            println("---new---")
            println(users)


        }
    }


    private fun sendMessage(fcmToken: String) {
        val message = Message.builder()
                .putData("url", "someUrl")
                .putData("magnetLink", "someMagnet")
                .setToken(fcmToken)
                .build()

        val response = FirebaseMessaging.getInstance().send(message)
        println("Successfully sent msg: $response")
    }

}