# Languages and Automata – TP 3: Introduction to CUP

This repository contains the **starter code** for **TP 3** of the *Languages and Automata* course at Nantes University.

This practical session introduces **syntactic analysis** using **Java CUP**.  
Students learn how to describe a grammar, generate a parser, and associate **semantic actions**
to grammar rules in order to compute values or build intermediate representations.

This TP builds directly on the lexer developed in TP 2 and marks the first use of a **parser generator**
in the compilation pipeline.

See the [main organization](https://github.com/LangagesEtAutomates/) for more information on the course
and related teaching materials.

---

## Structure

```
├── LICENSE.txt           # MIT license (see organization-wide license file)
├── README.md             # This file
├── build.xml             # Ant build file
├── lib/                  # External libraries (JFlex, Java CUP, JUnit, etc.)
├── src/
│   └── lea/
│       ├── Lexer.flex    # JFlex lexer (already provided)
│       ├── Parser.cup    # CUP grammar specification (to be modified)
│       ├── Main.java     # Entry point (reads input and runs the parser)
│       └── *.java        # Support classes
├── gen/                  # Generated sources (lexer and parser)
├── build/                # Compiled main classes
└── test/                 # (Optional) test inputs or examples
```

Generated directories (`gen/`, `build/`) are produced automatically and should not be edited manually.

---

## Goals of the TP

This TP focuses on:
- understanding the structure of a **CUP grammar file**,
- declaring terminals and non-terminals,
- writing grammar rules compatible with **LR parsing**,
- associating **semantic actions** to grammar productions,
- handling operator precedence and associativity,
- experimenting with syntax errors and recovery.

The TP progresses through several grammars, including:
- a postfix (RPN) calculator,
- the Dyck language (balanced parentheses),
- a full arithmetic and boolean expression grammar.

---

## Build and Execution

The project uses **Apache Ant**.

- Generate the lexer and parser, then compile the project:

```bash
ant compile
```

This command:
- runs **JFlex** and **Java CUP**,
- generates `Parser.java` and `Terminal.java`,
- compiles all Java sources into `build/`.

- Run the automated test suite:

```bash
ant test
```

- Run the example program:

```bash
ant run
```

or equivalently:

```bash
java -cp "build:lib/*" lea.Main
```

The program reads expressions from standard input and prints the result of their evaluation.

After **each modification** of `Parser.cup`, the project must be regenerated and recompiled.

---

## Dependencies

All dependencies are provided in the `lib/` directory:

- **JFlex** — lexical analysis  
- **Java CUP** — syntactic analysis  
- **JUnit** — testing support  

No external installation is required beyond a JDK and Ant.

---

## License

All **source code** in this repository is distributed under the **MIT License**.

- The full legal text is available in [LICENSE.txt](LICENSE.txt).
- Organization-wide licensing details and attributions are documented in  
  [https://github.com/LangagesEtAutomates/.github/blob/main/LICENSE.md](https://github.com/LangagesEtAutomates/.github/blob/main/LICENSE.md).

This license applies to all Java sources, grammar files (`.flex`, `.cup`),
and supporting code in this repository.

---

## Contributing

Contributions are welcome, in particular:
- clarifications or improvements to diagnostics,
- fixes to the starter grammar,
- improvements to example inputs.

Please use pull requests to propose changes.
For significant modifications, consider opening an issue first to discuss the design.
