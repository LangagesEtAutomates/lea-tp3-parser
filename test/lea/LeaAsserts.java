/*
 * SPDX-License-Identifier: MIT
 * Author: Matthieu Perrin
 * Session: Introduction to CUP
 */

package lea;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import java_cup.runtime.Symbol;
import lea.Reporter.Phase;

public class LeaAsserts {

	private final Reporter reporter = new Reporter();
	private final List<Symbol> tokens = new ArrayList<Symbol>();
	private int output;

	public LeaAsserts(String source) {
		try(Reader reader = new StringReader(source)) {
			var lexer = new Lexer(reader, new Reporter());
			while (!lexer.yyatEOF()) {
				Symbol token = lexer.next_token();
				if(token.sym != Terminal.EOF)
					tokens.add(token);
			}
		} catch (Exception e) {
			fail(e);
		}
		try(Reader reader = new StringReader(source)) {
			var lexer = new Lexer(reader, reporter);
			var parser = new Parser(lexer,reporter);
			output = parser.parseInt();
		} catch (Exception e) {
			fail(e);
		}
	}

	private List<String> getErrors(Phase phase, String fragment) {
		return reporter
				.getErrors()
				.stream()
				.filter(d -> d.phase() == phase)
				.map(d->d.message())
				.filter(m -> m.contains(fragment))
				.toList();
	}

	public LeaAsserts assertHasNoErrorAt(Phase phase) {
		List<String> errors = getErrors(phase, "");
		assertTrue(errors.isEmpty(), () -> phase +  " errors:\n" + String.join("\n", errors));
		return this;
	}

	public LeaAsserts assertHasNoError() {
		assertHasNoErrorAt(Phase.LEXER);
		assertHasNoErrorAt(Phase.PARSER);
		return this;
	}

	public LeaAsserts assertHasErrorContaining(Phase phase, String fragment) {
		List<String> errors = getErrors(phase, fragment);
		assertTrue(!errors.isEmpty(), () -> "Expected " + phase + " error containing: \"" + fragment + "\"\nGot:\n" + String.join("\n", errors));
		return this;
	}

	public LeaAsserts assertReturns(int expected) {
		assertHasNoError();
		assertEquals(output, expected, "Output mismatch: " + output);
		return this;
	}

	public LeaAsserts assertMatches(int... expected) {
		assertEquals(expected.length, tokens.size(), "Token count mismatch: " + tokens);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], tokens.get(i).sym, "Token mismatch at index " + i);
		}
		return this;
	}

}
