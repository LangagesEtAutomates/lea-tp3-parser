/*
 * SPDX-License-Identifier: MIT
 * Author: Matthieu Perrin
 * Session: Introduction to CUP
 */

package lea;

import org.junit.jupiter.api.Test;

import lea.Reporter.Phase;

/**
 * JUnit tests for the Lexer class.
 */
public final class LexerTest {

	/* =========================
	 * === SYMBOLES / OPERATEURS
	 * ========================= */

	@Test
	void punctuation_and_assignment() {
		new LeaAsserts("+ - * ( ) [ ]")
		.assertHasNoErrorAt(Phase.LEXER)
		.assertMatches(Terminal.PLUS, Terminal.MOINS, Terminal.MULTIPLIE, 
				Terminal.PAR_G, Terminal.PAR_D, Terminal.CRO_G, Terminal.CRO_D);
	}

	/* =========================
	 * === LITTERAUX
	 * ========================= */

	@Test
	void integer_literals() {
		new LeaAsserts("0 7 42 123456")
		.assertHasNoErrorAt(Phase.LEXER)
		.assertMatches(Terminal.ENTIER, Terminal.ENTIER, Terminal.ENTIER, Terminal.ENTIER);
	}

	/* =========================
	 * === ESPACES / COMMENTAIRES
	 * ========================= */

	@Test
	void whitespace_is_ignored() {
		new LeaAsserts(" \n\t  4 \r\f 5 ")
		.assertHasNoErrorAt(Phase.LEXER)
		.assertMatches(Terminal.ENTIER, Terminal.ENTIER);
	}

	@Test
	void line_comment_is_ignored() {
		new LeaAsserts("""
				2 // comment
				3
				""")
		.assertHasNoErrorAt(Phase.LEXER)
		.assertMatches(Terminal.ENTIER, Terminal.ENTIER);
	}

	@Test
	void block_comment_is_ignored() {
		new LeaAsserts("37 /* comment */ 5")
		.assertHasNoErrorAt(Phase.LEXER)
		.assertMatches(Terminal.ENTIER, Terminal.ENTIER);
	}

	@Test
	void block_comment_with_stars_is_ignored() {
		new LeaAsserts("25 /* ** */ 4")
		.assertHasNoErrorAt(Phase.LEXER)
		.assertMatches(Terminal.ENTIER, Terminal.ENTIER);
	}

	/* =========================
	 * === ERREURS LEXICALES
	 * ========================= */

	@Test
	void illegal_character_is_reported() {
		new LeaAsserts("2 @ 8")
		.assertHasErrorContaining(Phase.LEXER, "Illegal character");
	}

	@Test
	void illegal_character_does_not_prevent_other_tokens() {
		new LeaAsserts("2 @ 8")
		.assertMatches(Terminal.ENTIER, Terminal.ENTIER);
	}

}
