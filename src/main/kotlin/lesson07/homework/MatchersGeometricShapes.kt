package ru.stimmax.lesson07.homework

import org.hamcrest.Description
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeDiagnosingMatcher

enum class Color { RED, BLUE, GREEN, YELLOW, BLACK, WHITE }

data class Shape(val sideLength: Float, val numberSides: Int, val color: Color)

class SideLengthInRange(private val min: Float, private val max: Float) :
    TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("длина стороны в диапазоне ")
            .appendValue(min).appendText("..").appendValue(max)
    }

    override fun matchesSafely(shape: Shape, mismatch: Description): Boolean {
        if (shape.sideLength < min || shape.sideLength > max) {
            mismatch.appendText("длина стороны = ").appendValue(shape.sideLength)
            return false
        }
        return true
    }
}

class PositiveSideLength : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("неотрицательная длина стороны")
    }

    override fun matchesSafely(shape: Shape, mismatch: Description): Boolean {
        if (shape.sideLength < 0f) {
            mismatch.appendText("длина стороны отрицательна: ")
                .appendValue(shape.sideLength)
            return false
        }
        return true
    }
}

class PositiveSidesCount : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("неотрицательное количество сторон")
    }

    override fun matchesSafely(shape: Shape, mismatch: Description): Boolean {
        if (shape.numberSides < 0) {
            mismatch.appendText("количество сторон отрицательно: ")
                .appendValue(shape.numberSides)
            return false
        }
        return true
    }
}

class CornersCount(private val expected: Int) : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("количество углов = ").appendValue(expected)
    }

    override fun matchesSafely(shape: Shape, mismatch: Description): Boolean {
        val actual = if (shape.numberSides <= 2) 0 else shape.numberSides
        if (actual != expected) {
            mismatch.appendText("фактическое количество углов = ")
                .appendValue(actual)
            return false
        }
        return true
    }
}

class EvenSidesCount : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("чётное количество сторон")
    }

    override fun matchesSafely(shape: Shape, mismatch: Description): Boolean {
        if (shape.numberSides % 2 != 0) {
            mismatch.appendText("количество сторон = ")
                .appendValue(shape.numberSides)
            return false
        }
        return true
    }
}

class ShapeColor(private val expected: Color) : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("цвет ").appendValue(expected)
    }

    override fun matchesSafely(shape: Shape, mismatch: Description): Boolean {
        if (shape.color != expected) {
            mismatch.appendText("фактический цвет ").appendValue(shape.color)
            return false
        }
        return true
    }
}

fun hasSideLengthInRange(min: Float, max: Float) = SideLengthInRange(min, max)
fun hasPositiveSideLength() = PositiveSideLength()
fun hasPositiveSidesCount() = PositiveSidesCount()
fun hasCorners(n: Int) = CornersCount(n)
fun hasEvenSides() = EvenSidesCount()
fun hasColor(c: Color) = ShapeColor(c)


val shapes = listOf(
    Shape(10f, 3, Color.RED), Shape(5f, 4, Color.BLUE), Shape(7f, 2, Color.GREEN),
    Shape(0.5f, 1, Color.YELLOW), Shape(-3f, 5, Color.BLACK), Shape(8f, -2, Color.WHITE),
    Shape(12f, 6, Color.RED), Shape(15f, 8, Color.BLUE), Shape(20f, 4, Color.GREEN),
    Shape(9f, 5, Color.YELLOW), Shape(2f, 3, Color.BLACK), Shape(11f, 7, Color.WHITE),
    Shape(6f, 10, Color.RED), Shape(3f, 2, Color.BLUE), Shape(4f, 1, Color.GREEN),
    Shape(25f, 12, Color.YELLOW), Shape(30f, 14, Color.BLACK), Shape(35f, 16, Color.WHITE),
    Shape(40f, 18, Color.RED), Shape(50f, 20, Color.BLUE)
)

val validEven = shapes.filter {
    allOf(
        hasPositiveSideLength(),
        hasPositiveSidesCount(),
        hasSideLengthInRange(1f, 20f),
        hasEvenSides()
    ).matches(it)
}

val lines = shapes.filter {
    it.numberSides in 1..2 && allOf(
        hasPositiveSideLength(),
        hasPositiveSidesCount(),
        hasCorners(0)
    ).matches(it)
}

val broken = shapes.filter {
    anyOf(
        not(hasPositiveSideLength()),
        not(hasPositiveSidesCount())
    ).matches(it)
}

val bigRedOrBlue = shapes.filter {
    allOf(
        anyOf(hasColor(Color.RED), hasColor(Color.BLUE)),
        hasSideLengthInRange(10.1f, 100f),
        hasPositiveSideLength(),
        hasPositiveSidesCount()
    ).matches(it)
}

fun checkTriangle() {
    val triangle = Shape(6f, 3, Color.GREEN)
    assertThat(triangle, hasSideLengthInRange(1f, 10f))
    assertThat(triangle, hasColor(Color.GREEN))
    assertThat(triangle, hasPositiveSideLength())
    assertThat(triangle, hasPositiveSidesCount())
    assertThat(triangle, hasCorners(3))
    assertThat(triangle, not(hasEvenSides()))
}

fun checkSquare() {
    val square = Shape(4f, 4, Color.BLUE)
    assertThat(square, hasCorners(4))
    assertThat(square, hasEvenSides())
    assertThat(square, hasColor(Color.BLUE))
    assertThat(square, hasSideLengthInRange(0.1f, 100f))
}

fun checkLine() {
    val line = Shape(0.5f, 1, Color.YELLOW)
    assertThat(line, hasCorners(0))
    assertThat(line, hasPositiveSideLength())
    assertThat(line, hasPositiveSidesCount())
}

fun checkInvalid() {
    val bad = Shape(-2f, -3, Color.BLACK)
    assertThat(bad, not(hasPositiveSideLength()))
    assertThat(bad, not(hasPositiveSidesCount()))
}

fun main() {
    checkTriangle()
    checkSquare()
    checkLine()
    checkInvalid()

    println("Валидные чётные фигуры: $validEven")
    println("Линии: $lines")
    println("Некорректные фигуры: $broken")
    println("Крупные красные/синие: $bigRedOrBlue")
}