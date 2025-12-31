package com.example.brainmon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brainmon.data.Monster
import com.example.brainmon.data.MonsterRepository
import com.example.brainmon.network.ApiMonsterResponse
import com.example.brainmon.network.RetrofitInstance
import com.example.brainmon.network.TriviaInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Simple data class to hold the current trivia state for the UI
data class QuestionState(
    val text: String,
    val answers: List<String>,
    val correctAnswer: String
)

class MonstersViewModel(private val repository: MonsterRepository) : ViewModel() {

    // --- 1. DATA STREAMS (StateFlow) ---
    val monstersUiState: StateFlow<List<Monster>> =
        repository.monsters
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val totalCaught: StateFlow<Int> = repository.monsters
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    // --- 2. BATTLE STATE MANAGEMENT ---
    private val _currentEncounter = MutableStateFlow<ApiMonsterResponse?>(null)
    val currentEncounter = _currentEncounter.asStateFlow()

    private val _currentQuestion = MutableStateFlow<QuestionState?>(null)
    val currentQuestion = _currentQuestion.asStateFlow()

    private val _searchError = MutableStateFlow(false)
    val searchError = _searchError.asStateFlow()

    private val _battleDifficulty = MutableStateFlow("Unknown")
    val battleDifficulty = _battleDifficulty.asStateFlow()

    private val _battleCategory = MutableStateFlow("General")
    val battleCategory = _battleCategory.asStateFlow()

    private val _battleCp = MutableStateFlow(0)
    val battleCp = _battleCp.asStateFlow()

    // --- 3. BIOME DATA ---
    private val biomeMonsters = mapOf(
        "plains" to listOf("pidgey", "pidgeotto", "pidgeot", "rattata", "raticate", "spearow", "fearow", "jigglypuff", "wigglytuff", "meowth", "persian", "farfetchd", "doduo", "dodrio", "lickitung", "chansey", "kangaskhan", "tauros", "ditto", "eevee", "snorlax"),
        "forest" to listOf("caterpie", "metapod", "butterfree", "weedle", "kakuna", "beedrill", "bulbasaur", "ivysaur", "venusaur", "oddish", "gloom", "vileplume", "paras", "parasect", "venonat", "venomoth", "bellsprout", "weepinbell", "victreebel", "exeggcute", "exeggutor", "scyther", "pinsir", "tangela"),
        "beach" to listOf("squirtle", "wartortle", "blastoise", "psyduck", "golduck", "poliwag", "poliwhirl", "poliwrath", "tentacool", "tentacruel", "slowpoke", "slowbro", "krabby", "kingler", "horsea", "seadra", "goldeen", "seaking", "staryu", "starmie", "magikarp", "gyarados"),
        "desert" to listOf("sandshrew", "sandslash", "mankey", "primeape", "diglett", "dugtrio", "geodude", "graveler", "golem", "onix", "cubone", "marowak", "rhyhorn", "rhydon", "aerodactyl"),
        "cave" to listOf("zubat", "golbat", "machop", "machoke", "machamp", "clefairy", "clefable", "omanyte", "omastar", "kabuto", "kabutops"),
        "swamp" to listOf("ekans", "arbok", "nidoran-f", "nidorina", "nidoqueen", "nidoran-m", "nidorino", "nidoking", "grimer", "muk", "koffing", "weezing", "gastly", "haunter", "gengar"),
        "city" to listOf("pikachu", "raichu", "magnemite", "magneton", "voltorb", "electrode", "hitmonlee", "hitmonchan", "drowzee", "hypno", "mr-mime", "electabuzz", "porygon", "zapdos"),
        "tundra" to listOf("seel", "dewgong", "shellder", "cloyster", "jynx", "lapras", "vaporeon", "articuno"),
        "volcano" to listOf("charmander", "charmeleon", "charizard", "vulpix", "ninetales", "growlithe", "arcanine", "ponyta", "rapidash", "magmar", "flareon", "moltres"),
        "mystic" to listOf("abra", "kadabra", "alakazam", "jolteon", "dratini", "dragonair", "dragonite", "mewtwo", "mew")
    )

    // --- 5. REAL POKEDEX ORDER (Gen 1) ---
    val pokedexOrder = listOf(
        "bulbasaur", "ivysaur", "venusaur", "charmander", "charmeleon", "charizard",
        "squirtle", "wartortle", "blastoise", "caterpie", "metapod", "butterfree",
        "weedle", "kakuna", "beedrill", "pidgey", "pidgeotto", "pidgeot",
        "rattata", "raticate", "spearow", "fearow", "ekans", "arbok",
        "pikachu", "raichu", "sandshrew", "sandslash", "nidoran-f", "nidorina",
        "nidoqueen", "nidoran-m", "nidorino", "nidoking", "clefairy", "clefable",
        "vulpix", "ninetales", "jigglypuff", "wigglytuff", "zubat", "golbat",
        "oddish", "gloom", "vileplume", "paras", "parasect", "venonat", "venomoth",
        "diglett", "dugtrio", "meowth", "persian", "psyduck", "golduck",
        "mankey", "primeape", "growlithe", "arcanine", "poliwag", "poliwhirl",
        "poliwrath", "abra", "kadabra", "alakazam", "machop", "machoke", "machamp",
        "bellsprout", "weepinbell", "victreebel", "tentacool", "tentacruel", "geodude",
        "graveler", "golem", "ponyta", "rapidash", "slowpoke", "slowbro",
        "magnemite", "magneton", "farfetchd", "doduo", "dodrio", "seel", "dewgong",
        "grimer", "muk", "shellder", "cloyster", "gastly", "haunter", "gengar",
        "onix", "drowzee", "hypno", "krabby", "kingler", "voltorb", "electrode",
        "exeggcute", "exeggutor", "cubone", "marowak", "hitmonlee", "hitmonchan",
        "lickitung", "koffing", "weezing", "rhyhorn", "rhydon", "chansey", "tangela",
        "kangaskhan", "horsea", "seadra", "goldeen", "seaking", "staryu", "starmie",
        "mr-mime", "scyther", "jynx", "electabuzz", "magmar", "pinsir", "tauros",
        "magikarp", "gyarados", "lapras", "ditto", "eevee", "vaporeon", "jolteon",
        "flareon", "porygon", "omanyte", "omastar", "kabuto", "kabutops", "aerodactyl",
        "snorlax", "articuno", "zapdos", "moltres", "dratini", "dragonair", "dragonite",
        "mewtwo", "mew"
    )

    fun getRealPokedexNumber(name: String): Int {
        val index = pokedexOrder.indexOf(name.lowercase())
        return if (index != -1) index + 1 else 999
    }

    // --- 4. ACTIONS (API CALLS) ---

    fun startEncounter(biome: String) {
        _currentEncounter.value = null
        _currentQuestion.value = null
        _searchError.value = false
        _battleDifficulty.value = "..."
        _battleCp.value = 0

        val categoryName = when (biome) {
            "plains", "forest" -> "General"
            "beach", "swamp", "tundra" -> "Biology"
            "desert", "volcano" -> "Math"
            "cave" -> "History"
            "city" -> "Science"
            "mystic" -> "Mythology"
            else -> "General"
        }
        _battleCategory.value = categoryName

        val categoryId = when (biome) {
            "desert", "volcano" -> 19
            "beach", "swamp", "tundra" -> 17
            "city" -> 18
            "cave" -> 23
            "mystic" -> 20
            else -> 9
        }

        val possibleMonsters = biomeMonsters[biome] ?: biomeMonsters["plains"]!!
        val ownedSpecies = monstersUiState.value.map { it.originalName.lowercase() }
        val availableMonsters = possibleMonsters.filter { !ownedSpecies.contains(it) }

        val targetMonster = if (availableMonsters.isNotEmpty()) availableMonsters.random() else possibleMonsters.random()

        viewModelScope.launch {
            try {
                val pokemon = RetrofitInstance.api.getPokemon(targetMonster)
                _currentEncounter.value = pokemon

                val hp = pokemon.stats.find { it.stat.name == "hp" }?.base_stat ?: 0
                val atk = pokemon.stats.find { it.stat.name == "attack" }?.base_stat ?: 0
                val def = pokemon.stats.find { it.stat.name == "defense" }?.base_stat ?: 0
                val calculatedCp = ((hp + atk + def) / 3) * 5
                _battleCp.value = calculatedCp

                val calculatedDifficulty = when {
                    calculatedCp < 300 -> "easy"
                    calculatedCp < 450 -> "medium"
                    else -> "hard"
                }
                _battleDifficulty.value = calculatedDifficulty

                val triviaResponse = TriviaInstance.api.getQuestion(categoryId, calculatedDifficulty)

                if (triviaResponse.results.isNotEmpty()) {
                    val rawQuestion = triviaResponse.results[0]
                    val decodedQuestion = android.text.Html.fromHtml(rawQuestion.question, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
                    val allAnswers = (rawQuestion.incorrect_answers + rawQuestion.correct_answer).map {
                        android.text.Html.fromHtml(it, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
                    }
                    val decodedCorrect = android.text.Html.fromHtml(rawQuestion.correct_answer, android.text.Html.FROM_HTML_MODE_LEGACY).toString()

                    _currentQuestion.value = QuestionState(
                        text = decodedQuestion,
                        answers = allAnswers.shuffled(),
                        correctAnswer = decodedCorrect
                    )
                } else {
                    _searchError.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _searchError.value = true
            }
        }
    }

    fun captureCurrentMonster() {
        val wildMonster = _currentEncounter.value ?: return

        val hp = wildMonster.stats.find { it.stat.name == "hp" }?.base_stat ?: 0
        val atk = wildMonster.stats.find { it.stat.name == "attack" }?.base_stat ?: 0
        val def = wildMonster.stats.find { it.stat.name == "defense" }?.base_stat ?: 0
        val cp = ((hp + atk + def) / 3) * 5

        val type = wildMonster.types.firstOrNull()?.type?.name ?: "normal"
        val image = wildMonster.sprites.front_default ?: ""

        saveMonster(
            name = wildMonster.name.replaceFirstChar { it.uppercase() },
            originalName = wildMonster.name,
            type = type,
            power = cp,
            imageUrl = image
        )
    }

    // --- HELPER FUNCTIONS ---

    fun checkAnswer(selectedAnswer: String, onCorrect: () -> Unit, onWrong: () -> Unit) {
        val q = _currentQuestion.value ?: return
        if (selectedAnswer == q.correctAnswer) onCorrect() else onWrong()
    }

    // --- UPDATED FOR REACTIVITY ---

    // 1. Get the max capacity for a biome (e.g., Forest has 24)
    fun getBiomeCapacity(biomeId: String): Int {
        return biomeMonsters[biomeId]?.size ?: 0
    }

    // 2. Get status using the LIVE list (passed from UI)
    fun getBiomeStatus(biomeId: String, currentList: List<Monster>): Pair<Int, Boolean> {
        val totalInBiome = biomeMonsters[biomeId]?.size ?: 0
        if (totalInBiome == 0) return Pair(0, false)

        val ownedNames = currentList.map { it.originalName.lowercase() }
        val caughtInBiome = biomeMonsters[biomeId]?.count { ownedNames.contains(it) } ?: 0
        val isComplete = caughtInBiome >= totalInBiome

        return Pair(caughtInBiome, isComplete)
    }


    // --- CRUD OPERATIONS (DATABASE) ---

    fun saveMonster(name: String, originalName: String, type: String, power: Int, imageUrl: String) {
        viewModelScope.launch {
            repository.addMonster(name, originalName, type, power, imageUrl)
        }
    }

    fun deleteMonster(monster: Monster) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            repository.deleteMonster(monster)
        }
    }

    fun updateMonsterName(originalMonster: Monster, newName: String) {
        val updatedMonster = originalMonster.copy(name = newName)
        viewModelScope.launch { repository.updateMonster(updatedMonster) }
    }

    fun getMonsterStream(id: Int): Flow<Monster?> {
        return repository.getMonsterStream(id)
    }
}