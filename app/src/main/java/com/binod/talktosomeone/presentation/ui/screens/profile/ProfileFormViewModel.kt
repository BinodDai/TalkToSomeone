package com.binod.talktosomeone.presentation.ui.screens.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.binod.talktosomeone.data.local.preferences.LocalStorage
import com.binod.talktosomeone.data.local.preferences.PrefKeys
import com.binod.talktosomeone.di.DefaultStorage
import com.binod.talktosomeone.di.SecureStorage
import com.binod.talktosomeone.domain.aggregator.ProfileUseCases
import com.binod.talktosomeone.domain.model.Profile
import com.binod.talktosomeone.domain.usecase.GenerateTextUseCase
import com.binod.talktosomeone.presentation.base.BaseViewModel
import com.binod.talktosomeone.presentation.navigation.Screen
import com.binod.talktosomeone.utils.getFirebaseInstallationId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileFormViewModel @Inject constructor(
    private val generateText: GenerateTextUseCase,
    private val profileUseCases: ProfileUseCases,
    @param:SecureStorage private val secureStorage: LocalStorage,
    @param:DefaultStorage private val defaultStorage: LocalStorage,
) : BaseViewModel() {
    private val _aiNameCode = MutableStateFlow("")
    val aiNameCode = _aiNameCode.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating = _isGenerating.asStateFlow()

    private val _age = MutableStateFlow("")
    val age = _age.asStateFlow()

    private val _gender = MutableStateFlow("")
    val gender = _gender.asStateFlow()

    var userId: String = ""


    fun updateAge(value: String) {
        _age.value = value
    }

    fun updateGender(value: String) {
        _gender.value = value
    }

    fun updateNameCode(name: String) {
        _aiNameCode.value = name
    }

    init {
        generateAiName()
        signInAnonymously()
    }

    fun generateAiName() {
        viewModelScope.launch {
            try {
                _isGenerating.value = true
                val generatedName = generateText(
                    "Generate a unique, short, and creative code name. The code name can be: " +
                            "1) an animal + adjective (e.g., SilentJackal), " +
                            "2) a bird + adjective (e.g., CrimsonFalcon), " +
                            "3) a universe-inspired word + adjective (e.g., LunarWolf), " +
                            "4) a movie or fictional character name (e.g., RedDragon), " +
                            "or 5) just an animal, bird, or universe-inspired word alone (e.g., Jackal). " +
                            "Keep it catchy, easy to remember, and unique. Return only the name, nothing else."
                )
                _aiNameCode.value = generatedName.trim()
            } catch (e: Exception) {
                _aiNameCode.value = generateName().trim()
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun signInAnonymously() {
        viewModelScope.launch {
            setLoading(true)
            try {
                userId = profileUseCases.signInAnonymously()
            } catch (e: Exception) {
                setLoading(false)
            } finally {
                setLoading(false)
            }
        }
    }

    fun createProfile() {
        viewModelScope.launch {
            val nameCode = aiNameCode.value
            setLoading(true)
            try {
                val deviceId = getFirebaseInstallationId()
                val profile = Profile(
                    docId = userId,
                    userId = userId,
                    codeName = nameCode,
                    age = age.value.toIntOrNull() ?: 0,
                    gender = gender.value,
                    deviceId = deviceId,
                    accountCreatedDate = System.currentTimeMillis(),
                    online = true
                )
                profileUseCases.createProfile(profile)
                defaultStorage.set(PrefKeys.IS_PROFILE_SETUP_COMPLETED, true)
                secureStorage.set(PrefKeys.CODE_NAME, nameCode)
                sendEvent(UiEvent.Navigate(Screen.Home.route))
            } catch (e: Exception) {
                sendEvent(UiEvent.ShowToast("Profile creation failed"))
            } finally {
                setLoading(false)
            }
        }
    }

    fun clearName() {
        _aiNameCode.value = ""
    }

    fun generateName(): String {
        val adjectives = listOf(
            "Red", "Dark", "Silent", "Crimson", "Lunar", "Shadow",
            "Silver", "Golden", "Mystic", "Fierce", "Blazing", "Swift",
            "Stormy", "Bright", "Shadowy", "Iron", "Thunder", "Night"
        )
        val animals = listOf(
            "Dragon", "Falcon", "Wolf", "Jackal", "Tiger", "Phoenix",
            "Lion", "Eagle", "Panther", "Hawk", "Raven", "Leopard",
            "Fox", "Griffin", "Cobra", "Shark", "Bear", "Owl", "Jackal"
        )
        val birds = listOf(
            "Falcon", "Eagle", "Hawk", "Raven", "Owl", "Parrot",
            "Phoenix", "Sparrow", "Crow", "Peacock"
        )
        val universeWords = listOf(
            "Lunar", "Solar", "Cosmic", "Nebula", "Stellar", "Orbit",
            "Galaxy", "Meteor", "Comet", "Astro", "Nova", "Eclipse"
        )
        val waterAnimals = listOf(
            "Shark", "Dolphin", "Whale", "Octopus", "Squid", "Seal",
            "Penguin", "Turtle", "Stingray", "Crab", "Lobster",
            "Jellyfish", "Seahorse", "Orca", "Swordfish", "Marlin"
        )
        val movieCharacters = listOf(
            "Gandalf", "Frodo", "DarthVader", "Yoda", "IronMan", "SpiderMan",
            "Batman", "Superman", "Hannibal", "Vader", "LaraCroft", "Elsa",
            "JackSparrow", "Neo", "Trinity", "OptimusPrime", "Wolverine",
            "Joker", "BlackPanther", "Hermione", "HarryPotter", "Thanos"
        )

        val random = java.util.Random()
        val useAdjective = random.nextBoolean()
        val category = random.nextInt(5)

        val baseName = when (category) {
            0 -> animals.random()
            1 -> birds.random()
            2 -> universeWords.random()
            3 -> waterAnimals.random()
            4 -> movieCharacters.random()
            else -> listOf(animals, birds, universeWords, waterAnimals, movieCharacters).flatten()
                .random()
        }

        return if (useAdjective) {
            "${adjectives.random()}$baseName"
        } else {
            baseName
        }
    }
}
