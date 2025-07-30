package com.simformsolutions.app.data.db.converters

import androidx.room.TypeConverter
import com.simformsolutions.app.domain.model.User
import com.simformsolutions.app.domain.remote.response.UserResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Type converters for Room database
 * Handles conversion between complex types and database storable types
 */
class UserConverters {
    // Type converters for User's nested classes
    
    /**
     * Converts User.Name to a String that can be stored in the database
     */
    @TypeConverter
    fun fromName(name: User.Name): String {
        return Json.encodeToString(name)
    }

    /**
     * Converts a String from the database back to User.Name
     */
    @TypeConverter
    fun toName(nameString: String): User.Name {
        return try {
            Json.decodeFromString(nameString)
        } catch (e: Exception) {
            User.Name()
        }
    }
    
    /**
     * Converts User.Location to a String that can be stored in the database
     */
    @TypeConverter
    fun fromLocation(location: User.Location): String {
        return Json.encodeToString(location)
    }

    /**
     * Converts a String from the database back to User.Location
     */
    @TypeConverter
    fun toLocation(locationString: String): User.Location {
        return try {
            Json.decodeFromString(locationString)
        } catch (e: Exception) {
            User.Location()
        }
    }
    
    /**
     * Converts User.Picture to a String that can be stored in the database
     */
    @TypeConverter
    fun fromPicture(picture: User.Picture): String {
        return Json.encodeToString(picture)
    }

    /**
     * Converts a String from the database back to User.Picture
     */
    @TypeConverter
    fun toPicture(pictureString: String): User.Picture {
        return try {
            Json.decodeFromString(pictureString)
        } catch (e: Exception) {
            User.Picture()
        }
    }
    
    /**
     * Converts User.Login to a String that can be stored in the database
     */
    @TypeConverter
    fun fromLogin(login: User.Login): String {
        return Json.encodeToString(login)
    }

    /**
     * Converts a String from the database back to User.Login
     */
    @TypeConverter
    fun toLogin(loginString: String): User.Login {
        return try {
            Json.decodeFromString(loginString)
        } catch (e: Exception) {
            User.Login()
        }
    }
}
