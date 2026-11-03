package com.quackmove.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.quackmove.app.domain.TripRepository

object QuackMoveTripDependencies {

    val tripRepository: TripRepository by lazy {
        FirestoreTripRepository(
            auth = FirebaseAuth.getInstance(),
            firestore = FirebaseFirestore.getInstance()
        )
    }
}
