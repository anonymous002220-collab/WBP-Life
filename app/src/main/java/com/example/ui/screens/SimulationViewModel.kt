package com.example.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.repository.SimulationRepository
import com.example.engine.SimulationController
import com.example.model.CaseRecord
import com.example.model.NpcProfile
import com.example.model.OfficialDocument
import com.example.model.SimulationFeedItem
import com.example.model.SimulationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SimulationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SimulationRepository = SimulationRepository(
        AppDatabase.getInstance(application)
    )

    val controller: SimulationController = SimulationController(
        repository = repository,
        scope = viewModelScope
    )

    private val simId = "WBP-SIM-001"

    val simulationState: StateFlow<SimulationState> = controller.currentState

    val isTicking: StateFlow<Boolean> = controller.isEngineTicking

    val feedItems: StateFlow<List<SimulationFeedItem>> = repository.observeFeed(simId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val npcs: StateFlow<List<NpcProfile>> = repository.observeNpcs(simId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cases: StateFlow<List<CaseRecord>> = repository.observeCases(simId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val documents: StateFlow<List<OfficialDocument>> = repository.observeDocuments(simId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dialog & Inspection States
    private val _showDossier = MutableStateFlow(false)
    val showDossier: StateFlow<Boolean> = _showDossier.asStateFlow()

    private val _showPersonnel = MutableStateFlow(false)
    val showPersonnel: StateFlow<Boolean> = _showPersonnel.asStateFlow()

    private val _showModules = MutableStateFlow(false)
    val showModules: StateFlow<Boolean> = _showModules.asStateFlow()

    init {
        controller.initializeSimulation()
    }

    fun onOptionSelected(decisionId: String, optionId: String) {
        controller.onPlayerChooseOption(decisionId, optionId)
    }

    fun performHealthIntervention(actionType: String) {
        controller.performHealthIntervention(actionType)
    }

    fun openDossier() {
        _showDossier.value = true
    }

    fun closeDossier() {
        _showDossier.value = false
    }

    fun openPersonnel() {
        _showPersonnel.value = true
    }

    fun closePersonnel() {
        _showPersonnel.value = false
    }

    fun openModules() {
        _showModules.value = true
    }

    fun closeModules() {
        _showModules.value = false
    }

    fun restartSimulation() {
        controller.restartSimulation()
    }
}
