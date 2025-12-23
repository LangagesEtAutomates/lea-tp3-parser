package lea;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import lea.Reporter.Phase;

/**
 * JUnit tests for the Parser class.
 */
public final class ParserTest {

	private static int analyse(String source, Reporter reporter) {
		int result = 0;
		try(Reader reader = new StringReader(source)) {
			var lexer = new Lexer(reader, reporter);
			var parser = new Parser(lexer,reporter);
			result = parser.parseInt();
			assertTrue(reporter.getErrors(Phase.LEXER).isEmpty(), "Lexing errors");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		return result;
	}

	private static void assertHasErrorContaining(String source, String fragment) {
		Reporter reporter = new Reporter();
		analyse(source, reporter);
		boolean matches = reporter.getErrors(Phase.PARSER)
				.stream()
				.anyMatch(m -> m.contains(fragment));
		assertTrue(matches,	() -> "Expected error containing: \"" + fragment);
	}

	private static void assertMatches(String source, int expected) {
		Reporter reporter = new Reporter();
		int result = analyse(source, reporter);
		var errors = reporter.getErrors(Phase.PARSER);
		assertTrue(errors.isEmpty(), () -> String.join("\n", errors));
		assertEquals(result, expected);
	}

	/* =========================
	 * === INFIX ARITHMETIC =====
	 * ========================= */

	@Test
	void infix_simpleSum() {
		assertMatches("1 + 1", 2);
	}

	@Test
	void infix_precedence_multiplicationOverAddition() {
		assertMatches("1 + 2 * 3", 7);
	}

	@Test
	void infix_parentheses_overridePrecedence() {
		assertMatches("(1 + 2) * 3", 9);
	}

	@Test
	void infix_leftAssociativity_minus() {
		assertMatches("10 - 3 - 2", 5);
	}

	@Test
	void infix_nestedParentheses() {
		assertMatches("2 * (3 + (4 * 5))", 46);
	}

	/* =========================
	 * === UNARY MINUS ==========
	 * ========================= */

	@Test
	void unaryMinus_number() {
		assertMatches("-3", -3);
	}

	@Test
	void unaryMinus_parenthesizedExpression() {
		assertMatches("-(2 * -3)", 6);
	}

	@Test
	void unaryMinus_precedence_betweenParenthesesAndMultiplication() {
		assertMatches("-2 * 3", -6);
	}

	/* =========================
	 * === EQUALITY =============
	 * ========================= */

	@Test
	void equality_trueIs1_falseIs0() {
		assertMatches("2 = 2", 1);
		assertMatches("2 = 3", 0);
	}

	@Test
	void equality_bindsLessThanArithmetic() {
		assertMatches("1 + 2 = 3", 1);
	}

	/* =========================
	 * === BOOLEAN OPS ==========
	 * ========================= */

	@Test
	void boolean_non() {
		assertMatches("non 0", 1);
		assertMatches("non 2", 0);
	}

	@Test
	void boolean_et_ou_basic() {
		assertMatches("0 et 1", 0);
		assertMatches("2 et 3", 1);
		assertMatches("0 ou 0", 0);
		assertMatches("0 ou 5", 1);
	}

	@Test
	void boolean_precedence_nonOverEtOverOu() {
		assertMatches("non 0 et 0 ou 0", 0);
	}

	@Test
	void boolean_precedence_equalBetweenNonAndArithmetic() {
		assertMatches("1 + 1 = 2 et non 0", 1);
	}

	/* =========================
	 * === INVALID SYNTAX =======
	 * ========================= */

	@Test
	void invalidExpression_reportsError() {
		assertHasErrorContaining("1 + * 2", "Exception");
	}

}
