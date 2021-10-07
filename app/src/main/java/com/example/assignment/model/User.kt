package com.example.assignment.model

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("data")
    var `data`: List<Data> = listOf(),
    @SerializedName( "page")
    var page: Int = 0,
    @SerializedName( "per_page")
    var perPage: Int = 0,
    @SerializedName( "support")
    var support: Support = Support(),
    @SerializedName( "total")
    var total: Int = 0,
    @SerializedName( "total_pages")
    var totalPages: Int = 0
) {

    data class Data(
        @SerializedName( "avatar")
        var avatar: String = "",
        @SerializedName( "email")
        var email: String = "",
        @SerializedName( "first_name")
        var firstName: String = "",
        @SerializedName( "id")
        var id: Int = 0,
        @SerializedName( "last_name")
        var lastName: String = ""
    )

    data class Support(
        @SerializedName( "text")
        var text: String = "",
        @SerializedName( "url")
        var url: String = ""
    )
}