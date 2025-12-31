package com.example.brainmon.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// --- 1. The UPDATED Data Models ---

data class ApiMonsterResponse(
    val id: Int,
    val name: String,
    val base_experience: Int?,
    val height: Int,
    val weight: Int,
    val sprites: ApiSprites,
    val stats: List<ApiStatEntry>,
    val types: List<ApiTypeEntry>
)

data class ApiSprites(
    val front_default: String?
)

data class ApiStatEntry(
    val base_stat: Int,
    val stat: ApiStatName
)

data class ApiStatName(
    val name: String
)

data class ApiTypeEntry(
    val type: ApiTypeName
)

data class ApiTypeName(
    val name: String
)

// --- 2. The API Interface (Stays the same) ---
interface PokeApiService {
    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): ApiMonsterResponse
}

// ... RetrofitInstance stays the same ...

object RetrofitInstance {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }
}