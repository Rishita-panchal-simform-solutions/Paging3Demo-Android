package com.simformsolutions.app.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * User API response.
 */
@Serializable
data class User(
    @SerialName("name")
    val name: Name = Name(),

    @SerialName("location")
    val location: Location = Location(),

    @SerialName("picture")
    val picture: Picture = Picture(),

    @SerialName("login")
    val login: Login = Login()
) {
    @Serializable
    data class Name(
        @SerialName("title")
        val title: String = "",

        @SerialName("first")
        val first: String = "",

        @SerialName("last")
        val last: String = ""
    ) {
        override fun toString(): String = "$title $first $last"
    }

    @Serializable
    data class Location(
        @SerialName("street")
        val street: Street = Street(),

        @SerialName("city")
        val city: String = "",

        @SerialName("state")
        val state: String = "",

        @SerialName("country")
        val country: String = "",

        @SerialName("coordinates")
        val coordinates: Coordinates = Coordinates(),

        @SerialName("timezone")
        val timezone: Timezone = Timezone()
    ) {
        fun address(): String {
            return "$street, $city, $state, $country"
        }
    }

    @Serializable
    data class Coordinates(
        @SerialName("latitude")
        val latitude: String = "",

        @SerialName("longitude")
        val longitude: String = ""
    )

    @Serializable
    data class Timezone(
        @SerialName("offset")
        val offset: String = "",

        @SerialName("description")
        val description: String = ""
    )

    @Serializable
    data class Street(
        @SerialName("number")
        val number: Int = 0,

        @SerialName("name")
        val name: String = ""
    ) {
        override fun toString(): String = "$number, $name"
    }

    @Serializable
    data class Picture(
        @SerialName("large")
        val large: String = "",

        @SerialName("medium")
        val medium: String = "",

        @SerialName("thumbnail")
        val thumbnail: String = ""
    )

    @Serializable
    data class Login(
        @SerialName("uuid")
        val uuid: String = ""
    )
}

/**
 * Room DB entity for User
 */
@Entity(tableName = "user")
@Serializable
data class UserEntity(
    @PrimaryKey
    val uuid: String = "",

    @Embedded(prefix = "name_")
    val name: NameEntity = NameEntity(),

    @Embedded(prefix = "location_")
    val location: LocationEntity = LocationEntity(),

    @Embedded(prefix = "picture_")
    val picture: PictureEntity = PictureEntity(),

    @Embedded(prefix = "login_")
    val login: LoginEntity = LoginEntity()
) {
    /**
     * Name entity for user
     */
    @Serializable
    data class NameEntity(
        val title: String = "",
        val first: String = "",
        val last: String = ""
    )

    /**
     * Location entity for user
     */
    @Serializable
    data class LocationEntity(
        @Embedded(prefix = "street_")
        val street: StreetEntity = StreetEntity(),

        val city: String = "",
        val state: String = "",
        val country: String = "",

        @Embedded(prefix = "coordinates_")
        val coordinates: CoordinatesEntity = CoordinatesEntity(),

        @Embedded(prefix = "timezone_")
        val timezone: TimezoneEntity = TimezoneEntity()
    )

    /**
     * Coordinates entity for user location
     */
    @Serializable
    data class CoordinatesEntity(
        val latitude: String = "",
        val longitude: String = ""
    )

    /**
     * Timezone entity for user location
     */
    @Serializable
    data class TimezoneEntity(
        val offset: String = "",
        val description: String = ""
    )

    /**
     * Street entity for user location
     */
    @Serializable
    data class StreetEntity(
        val number: Int = 0,
        val name: String = ""
    )

    /**
     * Picture entity for user
     */
    @Serializable
    data class PictureEntity(
        val large: String = "",
        val medium: String = "",
        val thumbnail: String = ""
    )

    /**
     * Login entity for user
     */
    @Serializable
    data class LoginEntity(
        val uuid: String = ""
    )
}
