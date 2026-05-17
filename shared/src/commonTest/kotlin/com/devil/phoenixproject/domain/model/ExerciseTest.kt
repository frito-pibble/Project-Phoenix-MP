package com.devil.phoenixproject.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ExerciseTest {

    @Test
    fun `muscleGroups defaults to muscleGroup for backward compatibility`() {
        val exercise = Exercise(
            name = "Test Exercise",
            muscleGroup = "Chest",
        )

        assertEquals("Chest", exercise.muscleGroups)
    }

    @Test
    fun `muscleGroups can be set independently`() {
        val exercise = Exercise(
            name = "Bench Press",
            muscleGroup = "Chest",
            muscleGroups = "Chest,Triceps,Shoulders",
        )

        assertEquals("Chest,Triceps,Shoulders", exercise.muscleGroups)
    }

    @Test
    fun `displayName returns exercise name`() {
        val exercise = Exercise(
            name = "Bench Press",
            muscleGroup = "Chest",
        )

        assertEquals("Bench Press", exercise.displayName)
    }

    @Test
    fun `default values are set correctly`() {
        val exercise = Exercise(
            name = "Test",
            muscleGroup = "Test",
        )

        assertEquals("", exercise.equipment)
        assertEquals(null, exercise.id)
        assertEquals(false, exercise.isFavorite)
        assertEquals(false, exercise.isCustom)
        assertEquals(0, exercise.timesPerformed)
        assertEquals(null, exercise.oneRepMaxKg)
    }

    @Test
    fun `live unified accessory display multiplier doubles only dual bar or belt exercises`() {
        val dualBar = Exercise(
            name = "Bench Press",
            muscleGroup = "Chest",
            equipment = "BAR,BENCH,BLACK_CABLES",
            cableIntent = ExerciseCableIntent.DUAL,
        )
        val dualBelt = Exercise(
            name = "Hip Thrust",
            muscleGroup = "Glutes",
            equipment = "BELT,BENCH,BLACK_CABLES",
            cableIntent = ExerciseCableIntent.DUAL,
        )

        assertEquals(2, dualBar.liveUnifiedAccessoryDisplayMultiplier())
        assertEquals(2, dualBelt.liveUnifiedAccessoryDisplayMultiplier())
    }

    @Test
    fun `live unified accessory display multiplier does not double individual attachments`() {
        listOf("HANDLES", "ROPE", "SHORT_BAR", "STRAPS").forEach { equipment ->
            val exercise = Exercise(
                name = "Dual $equipment",
                muscleGroup = "Test",
                equipment = equipment,
                cableIntent = ExerciseCableIntent.DUAL,
            )

            assertEquals(1, exercise.liveUnifiedAccessoryDisplayMultiplier(), equipment)
        }
    }

    @Test
    fun `live unified accessory display multiplier fails closed for non-explicit dual unified metadata`() {
        val unilateralBar = Exercise(
            name = "Single Cable Bar",
            muscleGroup = "Back",
            equipment = "BAR",
            cableIntent = ExerciseCableIntent.SINGLE,
        )
        val alternatingBar = Exercise(
            name = "Alternating Lunge",
            muscleGroup = "Legs",
            equipment = "BAR",
            cableIntent = ExerciseCableIntent.EITHER,
        )
        val unknownBar = Exercise(
            name = "Custom Bar",
            muscleGroup = "Back",
            equipment = "BAR",
            cableIntent = null,
            isCustom = true,
        )
        val nullExercise: Exercise? = null

        assertEquals(1, unilateralBar.liveUnifiedAccessoryDisplayMultiplier())
        assertEquals(1, alternatingBar.liveUnifiedAccessoryDisplayMultiplier())
        assertEquals(1, unknownBar.liveUnifiedAccessoryDisplayMultiplier())
        assertEquals(1, nullExercise.liveUnifiedAccessoryDisplayMultiplier())
    }
}
