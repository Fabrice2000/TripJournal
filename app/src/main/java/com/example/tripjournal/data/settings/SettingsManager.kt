package com.example.tripjournal.data.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.tripjournal.data.database.TripJournalDatabase
import com.example.tripjournal.data.importer.TripJsonImporter
import com.example.tripjournal.domain.ImportResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Gestionnaire réactif des préférences utilisateur de TripJournal.
 * Gère le thème, le tri par défaut, l'unité de distance, la confirmation de suppression et la réinitialisation des données.
 */
class SettingsManager private constructor(context: Context) {

    private val appContext = context.applicationContext
    private val prefs: SharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "trip_journal_settings"
        const val KEY_THEME_MODE = "pref_theme_mode"
        const val KEY_DEFAULT_SORT = "pref_default_sort"
        const val KEY_CONFIRM_DELETE = "pref_confirm_delete"
        const val KEY_DISTANCE_UNIT = "pref_distance_unit"

        const val THEME_SYSTEM = 0
        const val THEME_LIGHT = 1
        const val THEME_DARK = 2

        const val SORT_DATE_DESC = "DATE_DESC"
        const val SORT_DATE_ASC = "DATE_ASC"
        const val SORT_NAME_ASC = "NAME_ASC"
        const val SORT_DESTINATION_ASC = "DESTINATION_ASC"

        const val UNIT_KM = "KM"
        const val UNIT_MILES = "MILES"

        @Volatile
        private var INSTANCE: SettingsManager? = null

        fun getInstance(context: Context): SettingsManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingsManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val _themeMode = MutableStateFlow(getThemeMode())
    val themeMode: StateFlow<Int> = _themeMode.asStateFlow()

    private val _defaultSort = MutableStateFlow(getDefaultSort())
    val defaultSort: StateFlow<String> = _defaultSort.asStateFlow()

    private val _confirmDelete = MutableStateFlow(getConfirmDelete())
    val confirmDelete: StateFlow<Boolean> = _confirmDelete.asStateFlow()

    private val _distanceUnit = MutableStateFlow(getDistanceUnit())
    val distanceUnit: StateFlow<String> = _distanceUnit.asStateFlow()

    fun getThemeMode(): Int = prefs.getInt(KEY_THEME_MODE, THEME_SYSTEM)

    fun setThemeMode(mode: Int) {
        prefs.edit().putInt(KEY_THEME_MODE, mode).apply()
        _themeMode.value = mode
        applyTheme(mode)
    }

    fun getDefaultSort(): String = prefs.getString(KEY_DEFAULT_SORT, SORT_DATE_DESC) ?: SORT_DATE_DESC

    fun setDefaultSort(sort: String) {
        prefs.edit().putString(KEY_DEFAULT_SORT, sort).apply()
        _defaultSort.value = sort
    }

    fun getConfirmDelete(): Boolean = prefs.getBoolean(KEY_CONFIRM_DELETE, true)

    fun setConfirmDelete(confirm: Boolean) {
        prefs.edit().putBoolean(KEY_CONFIRM_DELETE, confirm).apply()
        _confirmDelete.value = confirm
    }

    fun getDistanceUnit(): String = prefs.getString(KEY_DISTANCE_UNIT, UNIT_KM) ?: UNIT_KM

    fun setDistanceUnit(unit: String) {
        prefs.edit().putString(KEY_DISTANCE_UNIT, unit).apply()
        _distanceUnit.value = unit
    }

    fun applySavedTheme() {
        applyTheme(getThemeMode())
    }

    fun applyTheme(mode: Int) {
        val nightMode = when (mode) {
            THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    /**
     * Réinitialise les données de la base Room et réimporte le fichier trips_seed.json.
     */
    suspend fun resetAndReimportData(): ImportResult? = withContext(Dispatchers.IO) {
        val db = TripJournalDatabase.get(appContext)
        db.clearAllTables()

        // Réinitialiser la clé de l'import JSON seed
        val importPrefs = appContext.getSharedPreferences("trip_import", Context.MODE_PRIVATE)
        importPrefs.edit().clear().apply()

        // Réimporter le JSON seed
        TripJsonImporter(appContext, db.tripDao()).importIfNeeded()
    }
}
