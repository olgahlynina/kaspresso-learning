package ru.stimmax.lesson04.homework

import kotlin.collections.mutableListOf
import kotlin.random.Random

class Inventory () {
    private val items = mutableListOf<String>()
    operator fun plus(item: String) = apply { items.add(item) }
    operator fun get(index: Int): String = items[index]
    operator fun contains(item: String): Boolean = items.contains(item)
}

class Toggle (private val enabled: Boolean) {
    operator fun not() = Toggle(!enabled)
    override fun toString(): String = enabled.toString()
}

class Price (private val amount: Int) {
    operator fun times(quantity: Int): Int = amount * quantity
}

class Step (val number: Int) {
    operator fun rangeTo(other: Step) = number..other.number
}
operator fun IntRange.contains(step: Step) = step.number in this

class Log {
    private val entries = mutableListOf<String>()
    operator fun plus(entry: String) = apply { entries.add(entry) }
    fun print() {
        println(entries.joinToString())
    }
}

class Person(private val name: String) {

    private val phrases = mutableListOf<String>()

    fun print() {
        println(phrases.joinToString(" "))
    }

    private fun selectPhrase(first: String, second: String): String {
        val random = Random.nextInt(0, 2)
        return if (random == 0) first else second
    }
    infix fun says(phrase: String): Person = apply {
        phrases.add(phrase)
    }
    infix fun and(phrase: String): Person = apply {
        check(phrases.isNotEmpty()) { "Сначала используй says" }
        phrases.add(phrase)
    }
    infix fun or(phrase: String): Person = apply {
        check(phrases.isNotEmpty()) { "Сначала используй says" }
        phrases[phrases.lastIndex] = selectPhrase(phrases[phrases.lastIndex], phrase)
    }
}

    fun main() {
        val bag = Inventory()
        bag + "Яблоко"
        bag + "Банан"
        println(bag[0])
        println("Банан" in bag)

        val light = Toggle(true)
        println(!light)

        val cost = Price(10)
        println(cost * 3)

        val start = Step(5)
        val end = Step(15)
        val current = Step(8)
        val range = start..end
        println(current in range)

        val history = Log()
        history + "Старт" + "Загрузка" + "Готово"
        history.print()

        val tom = Person("Tom")
        tom says "Один" and "Два" or "Три" and "Конец"
        tom.print()
    }


