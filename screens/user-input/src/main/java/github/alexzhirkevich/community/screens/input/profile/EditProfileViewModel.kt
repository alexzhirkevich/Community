//
//
//
//@FlowPreview
//@ExperimentalCoroutinesApi
//@HiltViewModel
//class EditProfileViewModel @Inject constructor(
//    @Stage private val usersRepo : UsersRepository,
//    @Stage private val channelsRepo : ChannelsRepository,
//    @Stage private val userProfileRepo : UserProfileRepository,
//) : DataViewModel<User>() {
//
//    private val _descriptionText = MutableStateFlow("")
//    val descriptionText = _descriptionText.asStateFlow()
//
//    private val _nameText = MutableStateFlow("")
//    val nameText = _nameText.asStateFlow()
//
//    private val _usernameText = MutableStateFlow("")
//    val usernameText = _usernameText.asStateFlow()
//
//    private val _isUsernameAvailable = MutableStateFlow<EditProfileError>(EditProfileError.None)
//    private val isUsernameAvailable = _isUsernameAvailable.asStateFlow()
//
//    private val _error = MutableStateFlow<EditProfileError>(EditProfileError.None)
//    val error = _error.asStateFlow()
//
//    override val sourceFlow: Flow<DataState<User>> = usersRepo
//        .get(usersRepo.currentUserId)
//        .map(Response<User>::toDataState)
//
//    private fun isCorrectUsername(username: String): Boolean =
//        username.isEmpty() ||
//                username.all { it in Config.TagSymbols } &&
//                username.length in Config.TagMinLength..Config.TagMaxLength
//
//    fun applyChanges(onSuccess: () -> Unit = {}) {
//        viewModelScope.launch(Dispatchers.IO) {
//            listOf(
//                async {
//                    userProfileRepo
//                        .setName(usersRepo.currentUserId, nameText.value)
//                },
//                async {
//                    userProfileRepo
//                        .setUsername(usersRepo.currentUserId, usernameText.value)
//                },
//                async {
//                    userProfileRepo
//                        .setDescription(usersRepo.currentUserId, descriptionText.value)
//                }
//            ).awaitAll().let {
//                onSuccess()
//            }
//        }
//    }
//
//    fun setName(name: String) {
//        if (name.length in 0..Config.NameMaxLength)
//            _nameText.tryEmit(name)
//    }
//
//    fun setUsername(username: String) {
//        if (username.length in 0..Config.TagMaxLength)
//            _usernameText.tryEmit(username)
//    }
//
//    fun setDescription(desc: String) {
//        if (desc.length in 0..Config.DescriptionMaxLength)
//            _descriptionText.tryEmit(desc)
//    }
//
//
//    private var usernameJob: Job? = null
//    private var correctJob: Job? = null
//
//
//    override fun update() {
//        usernameJob?.cancel()
//        usernameJob = runUsernameCheckJob()
//        correctJob = runCorrectInfoCheckJob()
//        super.update()
//    }
//
//    private fun runCorrectInfoCheckJob() : Job {
//        _error.tryEmit(EditProfileError.Unchecked)
//        return combine(
//            isUsernameAvailable, nameText, descriptionText
//        ) { usernameCorrect, name, desc ->
//            {
//                _error.tryEmit(
//                    when {
//                        usernameCorrect !is EditProfileError.None ->
//                            usernameCorrect
//                        !(name.length in Config.NameMinLength..Config.NameMaxLength) ->
//                            EditProfileError.Name(R.string.error_name_length)
//                        else ->
//                            EditProfileError.None
//                    }
//                )
//            }
//        }.launchIn(viewModelScope)
//    }
//
//    private fun runUsernameCheckJob() : Job {
//        _isUsernameAvailable.tryEmit(EditProfileError.Unchecked)
//        return usernameText
//            .onEach {
//                _isUsernameAvailable.tryEmit(EditProfileError.Unchecked)
//            }
//            .debounce { 500 }
//            .flatMapLatest {
//                if (!isCorrectUsername(it) && it.isNotEmpty())
//                    return@flatMapLatest flowOf(false)
//                if (it.isEmpty())
//                    return@flatMapLatest flowOf(true)
//                usersRepo
//                    .findByTag(it)
//                    .combine(channelsRepo.findByTag(it)) { a, b ->
//                        (a as? Response.Success)?.value?.toString()?.let {
//                            Log.i(LTAG, it)
//                        }
//                        a is Response.Success && a.value.id == usersRepo.currentUserId ||
//                                a is Response.Error && a.error is SnapshotNotFoundException &&
//                                b is Response.Error && b.error is SnapshotNotFoundException
//                    }.onEach {
//                        val error =  if (it)
//                            EditProfileError.None
//                        else EditProfileError.Username(R.string.error_usernaname_taken)
//
//                        _isUsernameAvailable.tryEmit(error)
//
//                    }.catch {
//                        if (BuildConfig.DEBUG) {
//                            Log.e(
//                                this@EditProfileViewModel.LTAG,
//                                "Failed to check username availability"
//                            )
//                        }
//                        _isUsernameAvailable.tryEmit(EditProfileError.)
//                    }
//            }.launchIn(viewModelScope)
//
//    }
//    init {
//        update()
//    }
//
//    override fun onCleared() {
//        usernameJob?.cancel()
//        super.onCleared()
//    }
//
//}