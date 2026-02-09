/*
 * SPDX-License-Identifier: MIT
 * Author: Matthieu Perrin
 * Session: Introduction to CUP
 */

package lea;

import java_cup.runtime.Symbol;

%%

%public
%class Lexer
%cupsym Terminal
%cup
%unicode
%line
%column

%{
  private Reporter reporter;

  public Lexer(java.io.Reader reader, Reporter reporter) {
    this(reader);
    this.reporter = reporter;
  }

  private void error(String message) {
    reporter.error(Reporter.Phase.LEXER, new Reporter.Span(yyline+1, yycolumn+1, yylength()), message);
  }
  
%}

%%

 /* Symbols */
"+"							{ return new Symbol(Terminal.PLUS); }
"-"							{ return new Symbol(Terminal.MOINS); }
"*"							{ return new Symbol(Terminal.MULTIPLIE); }
"("							{ return new Symbol(Terminal.PAR_G); }
")"							{ return new Symbol(Terminal.PAR_D); }
"["							{ return new Symbol(Terminal.CRO_G); }
"]"							{ return new Symbol(Terminal.CRO_D); }

 /* Integers */
0|[1-9][0-9]*		 		{ return new Symbol(Terminal.ENTIER, Integer.parseInt(yytext())); }

 /* Removal of characters that should not be parsed */
[ \t\n\r\f]					{ /* ignore white space. */ }
\/\/.*						{ /* One-line comments */ }
\/\*([^\*]|\*[^\/])*\*\/	{ /* Multi-line comments */ }

 /* Unexpected characters */
<<EOF>>						{ return new Symbol(Terminal.EOF); }
[^]           				{ error("Illegal character: " + yytext()); }
