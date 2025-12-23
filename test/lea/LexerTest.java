package lea;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import java_cup.runtime.Symbol;
import lea.Reporter.Phase;

/**
 * JUnit tests for the Lexer class.
 */
public final class LexerTest {

	private static List<Symbol> analyse(String source, Reporter reporter) {
		var tokens = new ArrayList<Symbol>();
		try (Reader reader = new StringReader(source)) {
			var lexer = new Lexer(reader, reporter);
			while (!lexer.yyatEOF()) {
				tokens.add(lexer.next_token());
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
		return tokens;
	}

	private static void assertHasErrorContaining(String source, String fragment) {
		var reporter = new Reporter();
		analyse(source, reporter);
		boolean matches = reporter.getErrors(Phase.LEXER)
				.stream()
				.anyMatch(m -> m.contains(fragment));
		assertTrue(matches, () -> "Expected error containing: \"" + fragment + "\"");
	}

	private static void assertMatches(String source, int... terminals) {
		var reporter = new Reporter();
		var tokens = analyse(source, reporter);
		assertEquals(terminals.length, tokens.size()-1, "Token count mismatch" + tokens);
		for (int i = 0; i < terminals.length; i++) {
			assertEquals(terminals[i], tokens.get(i).sym, "Token mismatch at index " + i);
		}
	}

	/* =========================
	 * === SYMBOLES / OPERATEURS
	 * ========================= */

	@Test
	void punctuation_and_assignment() {
		assertMatches("+ - * ( )",
				Terminal.PLUS, Terminal.MOINS, Terminal.MULTIPLIE, Terminal.PAR_G, Terminal.PAR_D
		);
	}

	/* =========================
	 * === LITTERAUX
	 * ========================= */

	@Test
	void integer_literals() {
		assertMatches("0 7 42 123456", Terminal.ENTIER, Terminal.ENTIER, Terminal.ENTIER, Terminal.ENTIER);
	}


	/* =========================
	 * === ESPACES / COMMENTAIRES
	 * ========================= */

	@Test
	void whitespace_is_ignored() {
		assertMatches(" \n\t  4 \r\f 5 ", Terminal.ENTIER, Terminal.ENTIER);
	}

	@Test
	void line_comment_is_ignored() {
		assertMatches("""
			2 // comment
			3
			""", Terminal.ENTIER, Terminal.ENTIER);
	}

	@Test
	void block_comment_is_ignored() {
		assertMatches("37 /* comment */ 5", Terminal.ENTIER, Terminal.ENTIER);
	}

	@Test
	void block_comment_with_stars_is_ignored() {
		assertMatches("25 /* ** */ 4", Terminal.ENTIER, Terminal.ENTIER);
	}

	/* =========================
	 * === ERREURS LEXICALES
	 * ========================= */

	@Test
	void illegal_character_is_reported() {
		assertHasErrorContaining("2 @ 8", "Illegal character");
	}

	@Test
	void illegal_character_does_not_prevent_other_tokens() {
		assertMatches("2 @ 8", Terminal.ENTIER, Terminal.ENTIER);
	}
}
