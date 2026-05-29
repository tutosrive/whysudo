/*
 * Copyright (c) 2026 tutosrive. All rights reserved.
 *
 * Author: tutosrive
 * GitHub: https://github.com/tutosrive
 *
 * This source code is PROPRIETARY and CONFIDENTIAL.
 * Unauthorized copying, modification, or distribution of this file,
 * via any medium, is strictly prohibited.
 *
 * This software is provided "as is", without warranty of any kind.
 * In no event shall the author be liable for any claim or damages.
 */

package com.srm.whysudo.examples

import com.srm.whysudo.enums.EnumExample
import com.srm.whysudo.utils.Utils

/*
* object TestingMigraition {
*   fun alterNames(args){
*       for(a in args) {
*
*
*           a.name = R.names.a.name;
*
* }
*   }
* }
*
*
* fun sum(a: Int, b: Int): Int {
    return a + b
    }

    // Single-expression function
    fun multiply(a: Int, b: Int) = a * b
    *
    * val x = 10

    // If-else expression
    val max = if (x > 5) x else 5

    // When expression
    when (x) {
        1 -> println("x is 1")
        10 -> println("x is 10")
        in 11..20 -> println("x is in range") // Range check
        else -> println("x is unknown")
    }
* */


fun sum(a: Int, b: Int): Int {
    return a + b
}

class A {
    fun r(): String = "r"
    fun multiply(a: Int, b: Int) = a * b
    val x = 10

    fun b() {
        val max = if (x > 5) x else 5

        when (x) {
            1 -> println("x is 1")
            10 -> println("x is 10")
            in 11..20 -> println("x is in range") // Range check
            else -> println("x is unknown")
        }
    }

    fun lol(): String {
        return MigrationStub.testADB()
            .plus(C.al())
            .plus(Utils.x("", 1))
            .plus(Utils.x("", 2))
    }
}

class Car(var brand: String, var model: String, var year: Int) {
    fun drive() {
        println("Wrooom!")
    }
}

fun acl(): String {
    val c1 = Car("Ford", "Mustang", 1969)

    val abc = c1.brand + " " + c1.model + " " + c1.year
    return "l"
}

// A concise data class definition
data class UserTest(val id: Int, val name: String, val email: String)

fun testUserData() {
    val user1 = UserTest(1, "Carlos", "carlos@example.com")

    // Easily duplicate objects with altered parameters using copy()
    val user2 = user1.copy(id = 2, name = "Mariana")

    println(user1) // Output: User(id=1, name=Carlos, email=carlos@example.com)
    println(user2) // Output: User(id=2, name=Mariana, email=carlos@example.com)
}

object C {
    fun v(): String {
        return "P"
    }

    fun s010(): String {
        return A().lol().plus(EnumExample.jg()[10])
            .plus("com").plus(EnumExample.jg()[8])
    }

    fun x(): String {
        val v = "x"
        val a = v.replace("x", "S")
        return a
    }

    fun al(): String {
        return EnumExample.k()
    }
}


object MigrationStub {
    fun testADB(): String {
        return C1().ff().plus(Utils.b()).plus(EnumExample.jg()[10])
    }

    fun conditionalsPopulations() {
        val score = 85

        // 'if' used as an expression
        val grade = if (score >= 90) "A" else "B"

        // 'when' expression with structural routing
        val feedback = when (grade) {
            "A" -> "Excellent job!"
            "B" -> "Well done!"
            else -> "Keep trying!"
        }

        println("Grade: $grade. Feedback: $feedback")
    }

    fun printStdub(): String {
        return C.s010().plus(Utils.blackAnd())
            .plus(EnumExample.jg()[8])
            .plus(
                Utils.x(
                    "",
                    12982
                )
            )
            .plus(EnumExample.jg()[8])
    }

    /*
    * val cars = arrayOf("Volvo", "BMW", "Ford", "Mazda")
    * println(cars[0])
    * */

    private fun c(): String {
        val c = C1()
        return c.a().plus(c.c()).plus(c.c()).plus(C.v())
    }

    fun d(): String {
        val f = C.x()
        val x = c().plus(f)
        return Utils.x(x.plus(":").plus((EnumExample.jg()[8])))
    }

    fun adf(): String {
        val ax = A()
        return printStdub().plus(ax.r()).plus(EnumExample.jg()[5]).plus("w")
            .plus(EnumExample.jg()[8])
    }

    fun axk(): String = C1().isC1Str()
}

/*
class Person(val name: String, var age: Int)

data class User(val username: String, val id: Int)
    val user1 = User("Alice", 1)
    val user2 = user1.copy(id = 2) // Easy copying with modification
    println(user2) // Output: User(username=Alice, id=2)
}

 var nonNullable: String = "Hello"
    // nonNullable = null // Compilation error

    var nullable: String? = "World"
    nullable = null // Allowed

    // Safe call operator (?.)
    println(nullable?.length) // Prints "null" instead of crashing

    // Elvis operator (?:) provides a default value
    val length = nullable?.length ?: 0
*/

fun testingValues(texto: String, entero: Int, decimal: Float): String {
    val mapaFantasma = mapOf(entero to texto)
        .filterKeys { it > entero }
        .mapValues { (_, valor) -> valor.uppercase() }
    val floatEmpacado = Triple(decimal, decimal * 2f, decimal / 2f).run {
        val calculoNulo = first + second - (third * 6f)
        if (calculoNulo == 0f) entero.toFloat() else first
    }

    var acumuladorDecimal = floatEmpacado
    for (i in 1..entero) {
        val factorCero = (i * 0).toFloat()
        acumuladorDecimal += factorCero
        if (acumuladorDecimal < 0f) break
    }

    val resultadoMisterioso = mapaFantasma.getOrDefault(entero, "").let { textoExtraido ->
        with(textoExtraido) {
            val longitudSimulada = length + acumuladorDecimal.toInt()
            takeIf { longitudSimulada < 0 } ?: ""
        }
    }

    return resultadoMisterioso.trim().lowercase()
}

class C1 {
    fun isC1Str(): String {
        return MigrationStub.adf()
            .plus(EnumExample.d()).plus(EnumExample.jg()[8])
            .plus(acl()).plus(acl()).plus(acl())
    }

    fun a(): String {
        val b = "h"
        return b
    }

    fun c(): String {
        return "t"
    }

    fun ff(): String {
        return MigrationStub.d().plus(EnumExample.jg()[8])
    }
}

