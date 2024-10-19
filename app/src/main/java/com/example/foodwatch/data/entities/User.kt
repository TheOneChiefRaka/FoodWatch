package com.example.foodwatch.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true) val pkUserId: Long = 0,
    val lastName: String,
    val firstName: String,
    val email: String,
    val passwordHash: String
) {
    // Constructor that allows primary key to be auto generated
    constructor(lastName: String, firstName: String, email: String, passwordHash: String) : this(0, lastName, firstName, email, passwordHash)
}