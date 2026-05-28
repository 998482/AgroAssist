import androidx.lifecycle.ViewModel
import com.example.agroinnovexa20.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.content.Context
import android.net.Uri

import androidx.lifecycle.viewModelScope
import com.example.agroinnovexa20.data.model.profile.UserProfile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ProfileUiState(
    val name: String = "",
    val gender: String = "",
    val phone: String = "",
    val farmLocation: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null

)

class ProfileViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getProfile().fold(
                onSuccess = { profile ->
                    _uiState.update {
                        it.copy(
                            name = profile.name,
                            gender = profile.gender,
                            phone = profile.phone,
                            farmLocation = profile.farmLocation,
                            isLoading = false
                        )
                    }
                },
                onFailure = {
                    _uiState.update { state ->
                        state.copy(isLoading = false, error = it.message)
                    }
                }
            )
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }
    fun onGenderChange(value: String) = _uiState.update { it.copy(gender = value) }
    fun onPhoneChange(value: String) = _uiState.update { it.copy(phone = value) }
    fun onFarmLocationChange(value: String) = _uiState.update { it.copy(farmLocation = value) }

    fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value
            val profile = UserProfile(
                name = state.name,
                gender = state.gender,
                phone = state.phone,
                farmLocation = state.farmLocation
            )
            repository.saveProfile(profile).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSaved = true) }
                },
                onFailure = {
                    _uiState.update { state ->
                        state.copy(isLoading = false, error = it.message)
                    }
                }
            )
        }
    }

    fun resetSaved() {
        _uiState.update { it.copy(isSaved = false) }
    }
}