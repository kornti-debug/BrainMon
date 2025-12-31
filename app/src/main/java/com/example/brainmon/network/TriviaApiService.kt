package com.example.brainmon.network


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// --- 1. The Data Models ---

data class TriviaResponse(
    val results: List<TriviaQuestion>
)

data class TriviaQuestion(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)

// --- 2. The API Interface ---
// Base URL: https://opentdb.com/api.php?amount=1&category=19&type=multiple
// We make 'difficulty' dynamic.

interface TriviaApiService {
    // We removed "category=19" from the fixed string and made it a parameter
    @GET("api.php?amount=1&type=multiple")
    suspend fun getQuestion(
        @Query("category") category: Int, // <--- New Parameter
        @Query("difficulty") difficulty: String
    ): TriviaResponse
}

// --- 3. The Singleton ---

object TriviaInstance {
    private const val BASE_URL = "https://opentdb.com/"

    val api: TriviaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TriviaApiService::class.java)
    }
}