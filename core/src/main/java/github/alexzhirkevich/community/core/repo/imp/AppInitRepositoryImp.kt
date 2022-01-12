package github.alexzhirkevich.community.core.repo.imp

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import github.alexzhirkevich.community.core.repo.interfaces.AppInitRepository
import github.alexzhirkevich.community.core.repo.interfaces.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitRepositoryImp @Inject constructor(
    private val authRepository : AuthRepository,
) : AppInitRepository {

    override fun init(context: Context) {

        initApp(context)
        initDatabase()
        initFirestore()
        initAppCheck()
        initAuth()
    }

    private fun initApp(context: Context){
        FirebaseApp.initializeApp(context)
    }

    private fun initDatabase(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

    }

    private fun initFirestore(){
        FirebaseFirestore.getInstance().firestoreSettings =
            FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build()
    }

    private fun initAppCheck(){
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance())
    }

    private fun initAuth(){
        FirebaseAuth.getInstance().addAuthStateListener { it ->
            if (it.currentUser!=null) {
                authRepository.setOnline(onlineNow = true, onlineOnExit = false)

//                FirebaseMessaging.getInstance().token.toSingle().flatMapCompletable {
//                    authProvider.setNotificationToken(it)
//                }.subscribeOn(Schedulers.io()).subscribe(
//                    {},
//                    { Log.wtf("Failed to update notification token",it)  }
//                )

            }
        }
    }
}