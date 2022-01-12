package github.alexzhirkevich.community.core.repo.imp

import android.annotation.SuppressLint
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.SETTINGS
import github.alexzhirkevich.community.core.repo.interfaces.SettingsChangeListener
import github.alexzhirkevich.community.core.repo.interfaces.SettingsRepository
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import com.google.firebase.firestore.SetOptions
import github.alexzhirkevich.community.core.asResponse
import github.alexzhirkevich.community.core.await
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.lang.Runnable
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class SettingsRepositoryImp @Inject constructor(
    private val scope : CoroutineScope,
    private val firebaseRepository : FirebaseRepository,
    private val usersProvider: UsersRepository,
//    private val contactsProvider: ContactsProvider
) : SettingsRepository, CoroutineScope by scope, AutoCloseable {

    override val confidentiality: SettingsRepository.Confidentiality by lazy {
        RemoteConfidentialityImpl()
    }

    override var isSynchronized: Boolean = false
        private set

    override fun addOnSynchronizationCompleteListener(action: Runnable) {
        if (isSynchronized)
            action.run()
        else
            completeListeners.add(action)
    }

    override fun removeOnSynchronizationCompleteListener(action: Runnable) {
        completeListeners.remove(action)
    }

    override fun addOnConfidentialitySettingsChangedListener(
        listener: SettingsChangeListener<SettingsRepository.Confidentiality>
    ) {
        confidentialityChangeListeners.add(listener)
    }

    override fun removeOnConfidentialitySettingsChangedListener(
        listener: SettingsChangeListener<SettingsRepository.Confidentiality>
    ) {
        confidentialityChangeListeners.remove(listener)
    }

    override fun close() {
        confidentialityJob?.cancel()
    }


    private val completeListeners = mutableListOf<Runnable>()
    private val confidentialityChangeListeners =
        mutableListOf<SettingsChangeListener<SettingsRepository.Confidentiality>>()

    private var confidentialityJob : Job? = null

    @ExperimentalCoroutinesApi
    @SuppressLint("CheckResult")
    inner class RemoteConfidentialityImpl : SettingsRepository.Confidentiality {

        private val settingCollection = firebaseRepository.usersCollection
            .document(usersProvider.currentUserId).collection(SETTINGS)


        private var mBlackList: List<String> = emptyList()
        override var blackList: List<String>
            get() = mBlackList
            set(value) {
                if (mBlackList != value) {
                    mBlackList = ArrayList(value)
                    kotlin.runCatching {
                        launch(Dispatchers.IO) {
                            settingCollection.document(S_CONFIDENTIALITY)
                                .set(S_BLACKLIST to value, SetOptions.merge())
                                .await()
                        }
                    }
                }
            }
        private var mPhoneAccess: Byte = SettingsRepository.Confidentiality.ACCESS_CONTACTS
        override var phoneAccess: Byte
            get() = mPhoneAccess
            set(value) {
                if (mPhoneAccess != value) {
                    mPhoneAccess = value
                    kotlin.runCatching {
                        launch(Dispatchers.IO) {
                            settingCollection.document(S_CONFIDENTIALITY)
                                .set(S_PHONEACCESS to value, SetOptions.merge())
                                .await()
                        }
                    }
                }
            }

        private var mActivityAccess: Byte = SettingsRepository.Confidentiality.ACCESS_CONTACTS
        override var activityAccess: Byte
            get() = mActivityAccess
            set(value) {
                if (mActivityAccess != value) {
                    mActivityAccess = value
                    kotlin.runCatching {
                        launch(Dispatchers.IO) {
                            settingCollection.document(S_CONFIDENTIALITY)
                                .set(S_ACTIVITYACCESS to value, SetOptions.merge())
                                .await()
                        }
                    }
                }
            }

        private var mCallsAccess: Byte = SettingsRepository.Confidentiality.ACCESS_CONTACTS
        override var callsAccess: Byte
            get() = mCallsAccess
            set(value) {
                if (mCallsAccess != value) {
                    mCallsAccess = value
                    kotlin.runCatching {
                        launch(Dispatchers.IO) {
                            settingCollection.document(S_CONFIDENTIALITY)
                                .set(S_CALLSACCESS to value, SetOptions.merge())
                                .await()
                        }
                    }
                }
            }

        private var mGroupInviteAccess: Byte = SettingsRepository.Confidentiality.ACCESS_CONTACTS
        override var groupInviteAccess: Byte
            get() = mGroupInviteAccess
            set(value) {
                if (mGroupInviteAccess != value) {
                    mGroupInviteAccess = value
                    kotlin.runCatching {
                        launch(Dispatchers.IO) {
                            settingCollection.document(S_CONFIDENTIALITY)
                                .set(S_GROUPINVITEACCESS to value, SetOptions.merge())
                                .await()
                        }
                    }
                }
            }

        private var mIsContactsSynchronizationEnabled: Boolean = true
        override var isContactsSynchronizationEnabled: Boolean
            get() = mIsContactsSynchronizationEnabled
            set(value) {
                if (isContactsSynchronizationEnabled != value) {
                    mIsContactsSynchronizationEnabled = value
                    kotlin.runCatching {
                        launch(Dispatchers.IO) {
                            settingCollection.document(S_CONFIDENTIALITY)
                                .set(S_SYNCCONTACTS to value, SetOptions.merge())
                                .await()
                        }
                    }
                }
            }

        private val S_CONFIDENTIALITY = "confidentiality"
        private val S_BLACKLIST = "blackList"
        private val S_PHONEACCESS = "phoneAccess"
        private val S_ACTIVITYACCESS = "activityAccess"
        private val S_CALLSACCESS = "callsAccess"
        private val S_GROUPINVITEACCESS = "groupInviteAccess"
        private val S_SYNCCONTACTS = "isContactsSyncEnabled"


        init {
            confidentialityJob =   launch(Dispatchers.IO) {
                settingCollection.document(S_CONFIDENTIALITY)
                    .asResponse {
                        kotlin.runCatching {
                            mBlackList = it[S_BLACKLIST] as List<String>
                        }
                        kotlin.runCatching {
                            mPhoneAccess = (it[S_PHONEACCESS] as Long).toByte()
                        }
                        kotlin.runCatching {
                            mActivityAccess = (it[S_ACTIVITYACCESS] as Long).toByte()
                        }
                        kotlin.runCatching {
                            mCallsAccess = (it[S_CALLSACCESS] as Long).toByte()
                        }

                        kotlin.runCatching {
                            mGroupInviteAccess = (it[S_GROUPINVITEACCESS] as Long).toByte()
                        }
                        kotlin.runCatching {
                            mIsContactsSynchronizationEnabled = it[S_SYNCCONTACTS] as Boolean
                        }

                        if (!isSynchronized) {
                            isSynchronized = true
                            completeListeners.forEach {
                                it.run()
                            }
                            completeListeners.clear()
                        }
                        this@RemoteConfidentialityImpl
                    }
                    .onEach {
                        runCatching {
                            confidentialityChangeListeners.forEach {
                                it(this@RemoteConfidentialityImpl)
                            }
                        }
                    }
                    .collect()
            }
        }
    }
}