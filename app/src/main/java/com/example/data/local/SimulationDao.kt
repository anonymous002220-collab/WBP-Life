package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SimulationDao {

    @Query("SELECT * FROM simulation_state WHERE simulationId = :simId LIMIT 1")
    fun getState(simId: String): Flow<SimulationStateEntity?>

    @Query("SELECT * FROM simulation_state WHERE simulationId = :simId LIMIT 1")
    suspend fun getStateDirect(simId: String): SimulationStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertState(state: SimulationStateEntity)

    @Query("SELECT * FROM feed_items WHERE simulationId = :simId ORDER BY timestampMs ASC")
    fun getFeedItems(simId: String): Flow<List<FeedItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedItem(item: FeedItemEntity)

    @Update
    suspend fun updateFeedItem(item: FeedItemEntity)

    @Query("SELECT * FROM npc_records WHERE simulationId = :simId ORDER BY name ASC")
    fun getAllNpcs(simId: String): Flow<List<NpcEntity>>

    @Query("SELECT * FROM npc_records WHERE simulationId = :simId")
    suspend fun getAllNpcsDirect(simId: String): List<NpcEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNpcs(npcs: List<NpcEntity>)

    @Update
    suspend fun updateNpc(npc: NpcEntity)

    @Query("SELECT * FROM case_records WHERE simulationId = :simId ORDER BY caseNumber ASC")
    fun getAllCases(simId: String): Flow<List<CaseEntity>>

    @Query("SELECT * FROM case_records WHERE simulationId = :simId")
    suspend fun getAllCasesDirect(simId: String): List<CaseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCases(cases: List<CaseEntity>)

    @Update
    suspend fun updateCase(caseEntity: CaseEntity)

    @Query("SELECT * FROM official_documents WHERE simulationId = :simId ORDER BY id DESC")
    fun getAllDocuments(simId: String): Flow<List<OfficialDocumentEntity>>

    @Query("SELECT * FROM official_documents WHERE simulationId = :simId")
    suspend fun getAllDocumentsDirect(simId: String): List<OfficialDocumentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: OfficialDocumentEntity)

    @Query("UPDATE official_documents SET isRead = 1 WHERE id = :id")
    suspend fun markDocumentRead(id: String)

    @Query("DELETE FROM feed_items WHERE simulationId = :simId")
    suspend fun clearFeed(simId: String)

    @Query("DELETE FROM simulation_state WHERE simulationId = :simId")
    suspend fun deleteState(simId: String)

    @Query("DELETE FROM official_documents WHERE simulationId = :simId")
    suspend fun clearDocuments(simId: String)

    @Query("DELETE FROM case_records WHERE simulationId = :simId")
    suspend fun clearCases(simId: String)

    @Query("DELETE FROM npc_records WHERE simulationId = :simId")
    suspend fun clearNpcs(simId: String)
}
