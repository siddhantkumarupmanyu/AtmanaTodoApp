package sku.challenge.atmanatodoapp.vo

import com.google.gson.annotations.SerializedName

data class Item(
    val id: Int,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String
)