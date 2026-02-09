/*
 * SPDX-License-Identifier: MIT
 * Author: Matthieu Perrin
 * Session: Introduction to CUP
 */

package lea;

import org.junit.jupiter.api.Test;

import lea.Reporter.Phase;

/**
 * JUnit tests for the Dyck language.
 */
public final class Exo2Test {

	@Test
    void empty_dyck_returns_zero() {
        new LeaAsserts("")
            .assertHasNoError()
            .assertReturns(0);
    }

    @Test
    void simple_brackets() {
        new LeaAsserts("[]")
            .assertHasNoError()
            .assertReturns(1);
    }

    @Test
    void nested_brackets() {
        new LeaAsserts("[[[]]]")
            .assertHasNoError()
            .assertReturns(3);
    }

    @Test
    void sequence_of_brackets() {
        new LeaAsserts("[] [] []")
            .assertHasNoError()
            .assertReturns(1);
    }

    @Test
    void complex_nesting() {
        new LeaAsserts("[ [ ] [ ] ]")
            .assertHasNoError()
            .assertReturns(2);
        
        new LeaAsserts("[ [ [ ] ] [ ] ]")
            .assertHasNoError()
            .assertReturns(3);
    }

    @Test
    void invalid_dyck_reported() {
        new LeaAsserts("]")
            .assertHasErrorContaining(Phase.PARSER, "Erreur de syntaxe");

        new LeaAsserts("[[]")
            .assertHasErrorContaining(Phase.PARSER, "Erreur de syntaxe");
    }
    
}
