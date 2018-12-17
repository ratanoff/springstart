package ru.ratanov.springstart.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FirebaseAdmin {

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

    @Scheduled(fixedRate = 5000)
    private fun readDatabase() {
        firestore?.let { db ->
            val query = db.collection("users").get()
            val users = query.get().documents

            users.forEach {
                val films = db.collection("users").document(it.id).get()
                println(films.get().getString("link"))
            }


        }
    }

}