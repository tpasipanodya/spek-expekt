package com.taff.hephaestustest.expectation.boolean

import com.taff.hephaestustest.expectation.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object BeTrueOrFalseSpek : Spek({
    describe(".toBeTrue .toBeFalse") {
        context("when true"){
            it("passes") {
                true should beTrue()
                true should beFalse().not()
            }
        }
        context("when false"){
            it("passes") {
                false should beTrue().not()
                false should beFalse()
            }
        }
    }
})