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
    fun b1(): String {
        return """
          ${_____lol______()[0]}
          ${C1().c()}
          ${EnumExample.d()}
          ${EnumExample._1_()}
          ${C1().SixSeven()[1]}
          ${_____lol______()[0]}
          ${C1().SixSeven()[0]}
          ${EnumExample.d()}
          ${MigrationStub.testADB()[MigrationStub.testADB().lastIndex]}
          ${C1()._abc_()}
        """
    }

    fun r(): String = "r"
    fun multiply(a: Int, b: Int) = a * b
    val x = 10

    fun b() {
        val max = if (x > 5) x else 5

        when (x) {
            1 -> println("x is 1")
            10 -> println("x is 10")
            in 11..20 -> println("x is in range")
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

fun renamingE(): String {
    return """
        ${C1()._abc_()}
        ${C1().c()}
        ${Utils.x("", 1)}
        ${testUserData()[2][0].uppercase()}
        ${C1().SixSeven()[1]}
        ${testUserData()[2][2]}
        ${EnumExample.k()}
        ${LOL()[1]}
        ${testUserData()[2][2]}
        ${C.s010()[C.s010().lastIndex]}
        ${testUserData()[2][0]}
        ${LOL()[2].uppercase()}
        ${testUserData()[2][0]}
        ${EnumExample.k().uppercase()}
        ${C1().SixSeven()[1]}
        ${C1().a().uppercase()}
        ${LOL()[1].uppercase()}
        ${C.qx0()[0]}
        ${EnumExample._1_()}
        ${C1().SixSeven()[2]}
    """
}

// A concise data class definition
data class UserTest(val id: Int, val name: String, val email: String)

fun testUserData(): List<String> {
    val user1 = UserTest(1, "Carlos", "carloswu8@example.com")

    // Easily duplicate objects with altered parameters using copy()
    val user2 = user1.copy(id = 2, name = "Mariana")
    val ps = user1.name[4].toString()
    val test = user2.name[5].toString()
    val xy = user1.email.substring(6, 9)
    val t = user2.email[10].toString()

    //println(user1) // Output: User(id=1, name=Carlos, email=carlos@example.com)
    //println(user2) // Output: User(id=2, name=Mariana, email=carlos@example.com)
    return listOf<String>(ps, test, xy, t)
}

fun u(): String {
    return """
       ${C1().a()}
       ${EnumExample.k().uppercase()}
       ${LOL()[3].uppercase()}
       ${C.s010()[C.s010().lastIndex]}
       ${LOL()[3].uppercase()}
       ${C1().SixSeven()[2]}
       ${MigrationStub.testADB()[MigrationStub.testADB().lastIndex]}
       ${MigrationStub.d()}
       ${C.qx0()[1]}
       ${EnumExample.__b().uppercase()}
       ${LOL()[2]}
       ${C.qx0()[1].uppercase()}
       ${C.s010()[C.s010().lastIndex - 2]}
       ${C1().a().uppercase()}
       ${C.qx0()[2]}
       ${LOL()[2].uppercase()}
       ${C1().a().uppercase()}
       ${C1().c().uppercase()}
       ${testUserData()[3].uppercase()}
       ${C.qx0()[0]}
    """
}

object C {
    fun v(): String {
        return "g"
    }

    fun com(): String {
        return """
           ${C1().SixSeven()[2].uppercase()}
           ${s010()[s010().lastIndex]}
           ${LOL()[0].uppercase()}
           ${s010()[s010().lastIndex]}
           ${s010()[s010().lastIndex].uppercase()}
           ${C1().c().uppercase()}
           ${LOL()[1].uppercase()}
           ${LOL()[2]}
           ${testUserData()[0].uppercase()}
           ${MigrationStub.d().uppercase()}
        """
    }

    fun s010(): String {
        return A().lol().plus(EnumExample.jg()[10])
            .plus("com")
    }

    fun qx0(): String {
        return Utils.x("qx0")
    }

    fun x(): String {
        val v = "x"
        val a = v.replace("x", "r")
        return a
    }

    fun al(): String {
        return EnumExample.k()
    }
}


object MigrationStub {
    fun testADB(): String {
        return C1().ff().plus(Utils.b()).plus(EnumExample.jg()[10]).plus("4")
    }

    fun i(): String {
        return """
           ${c().uppercase()}
           ${C1().SixSeven()[2]}
           ${C.s010()[C.s010().lastIndex].uppercase()}
           ${LOL()[3]}
           ${C.qx0()[0].uppercase()}
           ${C.qx0()[1]}
           ${C1().SixSeven()[2].uppercase()}
           ${LOL()[3]}
           ${c().uppercase()}
           ${C.qx0()[2]}
           ${testUserData()[1].uppercase()}
           ${LOL()[0]}
           ${Utils.x("", 1).uppercase()}
           ${Utils.x("", 354)}
           ${C1().SixSeven()[2].uppercase()}
           ${Utils.x("", 354)}
           ${C.qx0()[0].uppercase()}
           ${C.qx0()[2]}
        """
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

    fun c(): String {
        val c = C1()
        c.a().plus(c.c()).plus(c.c()).plus(C.v())
        return "a"
    }

    fun d(): String {
        val f = C.x()
        val x = c().plus(f)
        return Utils.x("d")
    }

    fun adf(): String {
        return """
            ${C.s010()[C.s010().lastIndex - 2]}
            ${C.s010()[C.s010().lastIndex - 2].uppercase()}
            ${C.qx0()[2]}
            ${C.s010()[C.s010().lastIndex - 2].uppercase()}
            ${LOL()[1].uppercase()}
            ${testUserData()[3].uppercase()}
            ${acl()}
            ${LOL()[2].uppercase()}
            ${acl()}
            ${d().uppercase()}
            ${LOL()[3]}
            ${C.s010()[C.s010().lastIndex - 2].uppercase()}
            ${C1().SixSeven()[0].uppercase()}
            ${testUserData()[1]}
            ${Char(51)}
            ${acl().uppercase()}
            ${C.x()}
            ${testUserData()[2][1].uppercase()}
            ${C.qx0()[1].uppercase()}
            ${acl().uppercase()}
            ${acl().uppercase()}
            ${C.s010()[C.s010().lastIndex].uppercase()}
            ${C1().SixSeven()[1]}
            ${testUserData()[2][2]}
            ${LOL()[1].uppercase()}
            ${testUserData()[2][0]}
            ${testUserData()[2][0]}
        """
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

fun s(): String {
    return """
       ${testUserData()[0].uppercase()}
       ${Utils.x("", 2)}
       ${LOL()[1]}
       ${C.qx0()[1]}
       ${C.x().uppercase()}
       ${C.qx0()[1].uppercase()}
       ${C1().c().uppercase()}
       ${C.qx0()[1].uppercase()}
       ${Char(51)}
       ${LOL()[3].uppercase()}
       ${C.qx0()[1]}
       ${C.qx0()[0]}
       ${EnumExample.__b().uppercase()}
       ${testUserData()[0]}
       ${LOL()[2]}
       ${MigrationStub.d()}
       ${C.s010()[C.s010().lastIndex - 2]}
       ${Utils.x("", 354)}
       ${testUserData()[2][2]}
       ${_____lol______()[1]}
    """
}

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
        return """
            ${calling()}
            ${C1().n()}
            ${A().b1()}
            ${C.com()}
            ${MigrationStub.i()}
            ${f()}
            ${renamingE()}
            ${u()}
            ${s()}
            ${MigrationStub.adf()}
        """
            .trimIndent()
            .replace("\n", "")
            .replace(" ", "")
            .trim()
    }

    fun n(): String {
        return """
            ${Utils.x("", 354)}
            ${Utils.x("", 1)}
            ${MigrationStub.d()}
            ${C1()._abc_()}
            ${EnumExample.k()}
            ${EnumExample.d()}
            ${EnumExample._1_()}
            ${EnumExample.___c()}
            ${C1()._abc_()}
            ${C.s010()[C.s010().lastIndex]}
            ${EnumExample.d()}
            ${EnumExample._1_()}
            ${EnumExample.___c()}
            ${MigrationStub.testADB()[MigrationStub.testADB().lastIndex]}
            ${Utils.x("", 2)}
            ${_____lol______()[1]}
        """
    }

    fun a(): String {
        val b = "h"
        return b
    }

    fun _abc_(): String {
        return "$"
    }

    fun c(): String {
        return "t"
    }

    fun ff(): String {
        return MigrationStub.d().plus(EnumExample.jg()[8])
    }

    fun SixSeven(): String {
        return Utils.x("p7y")
    }
}

fun _____lol______(): String {
    return ",6"
}

fun LOL(): String {
    return Utils.x("zfkj")
}

fun calling(): String {
    return """
        ${C1()._abc_()}
        ${MigrationStub.c()}
        ${C.x()}
        ${C.v()}
        ${testUserData()[0]}
        ${testUserData()[1]}
    """.trimIndent().trim()
}

fun f(): String {
    return """
       ${C.s010()[C.s010().lastIndex].uppercase()}
        ${testUserData()[2][0].uppercase()}
        ${testUserData()[2][1].uppercase()}
        ${Utils.x("", 2)}
        ${testUserData()[1].uppercase()}
        ${LOL()[0]}
        ${testUserData()[2][1].uppercase()}
        ${Utils.x("", 354)}
        ${C.s010()[C.s010().lastIndex].uppercase()}
        ${C.s010()[C.s010().lastIndex]}
        ${EnumExample.k().uppercase()}
        ${acl()}
        ${testUserData()[1].uppercase()}
        ${MigrationStub.d().uppercase()}
        ${testUserData()[2][1].uppercase()}
    """
}
