package com.example.data.local

import androidx.room.TypeConverter
import com.example.model.DocumentType
import com.example.model.FeedItemType

class Converters {
    @TypeConverter
    fun fromFeedItemType(value: FeedItemType): String = value.name

    @TypeConverter
    fun toFeedItemType(value: String): FeedItemType = try {
        FeedItemType.valueOf(value)
    } catch (e: Exception) {
        FeedItemType.ROUTINE_EVENT
    }

    @TypeConverter
    fun fromDocumentType(value: DocumentType): String = value.name

    @TypeConverter
    fun toDocumentType(value: String): DocumentType = try {
        DocumentType.valueOf(value)
    } catch (e: Exception) {
        DocumentType.GENERAL_DIARY_ENTRY
    }
}
