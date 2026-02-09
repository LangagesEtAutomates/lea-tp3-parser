/*
 * SPDX-License-Identifier: MIT
 * Author: Matthieu Perrin
 * Session: Introduction to CUP
 */

package lea;

import org.junit.jupiter.api.Test;

import lea.Reporter.Phase;

/**
 * JUnit tests for the Calculator.
 */
public final class Exo3Test {

	/* =========================
	 * === INFIX ARITHMETIC =====
	 * ========================= */

	@Test
	void infix_simpleSum() {
		new LeaAsserts("1 + 1").assertReturns(2);
	}

	@Test
	void infix_precedence_multiplicationOverAddition() {
		new LeaAsserts("1 + 2 * 3").assertReturns(7);
	}

	@Test
	void infix_parentheses_overridePrecedence() {
		new LeaAsserts("(1 + 2) * 3").assertReturns(9);
	}

	@Test
	void infix_leftAssociativity_minus() {
		// Vérifie que (10 - 3) - 2 = 5 et non 10 - (3 - 2) = 9
		new LeaAsserts("10 - 3 - 2").assertReturns(5);
	}

	@Test
	void infix_nestedParentheses() {
		new LeaAsserts("2 * (3 + (4 * 5))").assertReturns(46);
	}

	/* =========================
	 * === UNARY MINUS ==========
	 * ========================= */

	@Test
	void unaryMinus_number() {
		new LeaAsserts("-3").assertReturns(-3);
	}

	@Test
	void unaryMinus_parenthesizedExpression() {
		new LeaAsserts("-(2 * -3)").assertReturns(6);
	}

	@Test
	void unaryMinus_precedence_betweenParenthesesAndMultiplication() {
		// -2 * 3 doit être (-2) * 3 = -6
		new LeaAsserts("-2 * 3").assertReturns(-6);
	}

	/* =========================
	 * === EQUALITY =============
	 * ========================= */

	@Test
	void equality_trueIs1_falseIs0() {
		new LeaAsserts("2 = 2").assertReturns(1);
		new LeaAsserts("2 = 3").assertReturns(0);
	}

	@Test
	void equality_bindsLessThanArithmetic() {
		// 1 + 2 = 3 doit être (1 + 2) = 3 -> 1
		new LeaAsserts("1 + 2 = 3").assertReturns(1);
	}

	/* =========================
	 * === BOOLEAN OPS ==========
	 * ========================= */

	@Test
	void boolean_non() {
		new LeaAsserts("non 0").assertReturns(1); // non faux = vrai
		new LeaAsserts("non 2").assertReturns(0); // non vrai = faux
	}

	@Test
	void boolean_et_ou_basic() {
		new LeaAsserts("0 et 1").assertReturns(0);
		new LeaAsserts("2 et 3").assertReturns(1);
		new LeaAsserts("0 ou 0").assertReturns(0);
		new LeaAsserts("0 ou 5").assertReturns(1);
	}

	@Test
	void boolean_precedence_nonOverEtOverOu() {
		// (non 0) et 0 ou 0  => (1 et 0) ou 0 => 0 ou 0 => 0
		new LeaAsserts("non 0 et 0 ou 0").assertReturns(0);
	}

	@Test
	void boolean_precedence_equalBetweenNonAndArithmetic() {
		// ((1 + 1) = 2) et (non 0) => (2 = 2) et 1 => 1 et 1 => 1
		new LeaAsserts("1 + 1 = 2 et non 0").assertReturns(1);
	}

	/* =========================
	 * === INVALID SYNTAX =======
	 * ========================= */

	@Test
	void invalidExpression_reportsError() {
		new LeaAsserts("1 + * 2")
			.assertHasErrorContaining(Phase.PARSER, "Exception");
	}    
}
