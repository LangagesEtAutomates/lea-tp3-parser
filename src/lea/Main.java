/*
 * SPDX-License-Identifier: MIT
 * Author: Matthieu Perrin
 * Session: Introduction to CUP
 */

package lea;

import java.io.*;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {

		try(Scanner sc = new Scanner(System.in)) {
			while(sc.hasNextLine()) {

				String line = sc.nextLine();
				Reader reader = new StringReader(line);
				
				var reporter = new Reporter();
				var lexer = new Lexer(reader, reporter);
				var parser = new Parser(lexer, reporter);
				
				System.out.println(parser.parseInt());

			}
		}
	}
}
