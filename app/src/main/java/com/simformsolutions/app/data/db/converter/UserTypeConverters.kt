package com.simformsolutions.app.data.db.converter

import androidx.room.TypeConverter
import com.simformsolutions.app.data.db.entity.UserResponseEntity
import com.simformsolutions.app.domain.model.UserEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

/**
 * Type converters for Room database
 */
class UserTypeConverters {
    // Create a Json instance with more lenient settings
    private val json = Json { 
        ignoreUnknownKeys = true 
        isLenient = true
    }

    /**
     * Converts List<UserEntity> to String for storage in SQLite
     */
    @TypeConverter
    fun fromUserEntityList(users: List<UserEntity>?): String {
        return if (users.isNullOrEmpty()) {
            "[]"
        } else {
            json.encodeToString(users)
        }
    }

    /**
     * Converts String to List<UserEntity> when reading from SQLite
     */
    @TypeConverter
    fun toUserEntityList(usersString: String?): List<UserEntity> {
        return if (usersString.isNullOrEmpty() || usersString == "[]") {
            emptyList()
        } else {
            try {
                json.decodeFromString(usersString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * Converts InfoEntity to String for storage in SQLite
     */
    @TypeConverter
    fun fromInfoEntity(info: UserResponseEntity.InfoEntity?): String {
        return if (info == null) {
            "{}"
        } else {
            json.encodeToString(info)
        }
    }

    /**
     * Converts String to InfoEntity when reading from SQLite
     */
    @TypeConverter
    fun toInfoEntity(infoString: String?): UserResponseEntity.InfoEntity {
        return if (infoString.isNullOrEmpty() || infoString == "{}") {
            UserResponseEntity.InfoEntity()
        } else {
            try {
                json.decodeFromString(infoString)
            } catch (e: Exception) {
                UserResponseEntity.InfoEntity()
            }
        }
    }
}
