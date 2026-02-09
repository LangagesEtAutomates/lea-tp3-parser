/*
 * SPDX-License-Identifier: MIT
 * Author: Matthieu Perrin
 * Session: Introduction to CUP
 */

package lea;

import java.util.*;

/**
 * Gestionnaire de diagnostics chargé de collecter les erreurs lors de l'analyse.
 * Permet de différer l'affichage des erreurs pour ne pas polluer la sortie standard.
 */
public final class Reporter {

	public enum Phase { LEXER, PARSER }

	private final List<Diagnostics> diags = new ArrayList<>();

	/**
	 * Enregistre une nouvelle erreur.
	 * @param position L'emplacement précis (ligne/colonne) de l'erreur.
	 * @param message  La description de l'erreur pour l'utilisateur.
	 */
	public void error(Phase phase, Span position, String message) {
		diags.add(new Diagnostics(phase, position, message));
	}
	
	/**
	 * Affiche un résumé des diagnostics sur la console.
	 * @return true si des erreurs ont été détectées, false sinon.
	 */
	public boolean reportErrors(Phase phase) {
		String errs = "";
		for (var d : diags) {
			if(d.phase() == phase) {
				String pos = d.span().isEmpty() ? "" : d.span().toString();
				errs += "\t" + pos + d.message() + "\n";
			}
		}
		if(errs != "") {
			System.out.println("La phase " + phase + " s'est terminée avec des erreurs");
			System.out.println(errs);
			return true;
		} else {
			System.out.println("La phase " + phase + " s'est terminée avec succès");
			return false;
		}
	}

	public List<Diagnostics> getErrors() {
		return diags;
	}
	
	public record Diagnostics(Phase phase, Span span, String message) {}

	/**
	 * Représente une portion du fichier source par ses coordonnées.
	 */
	public static record Span(int startLine, int startColumn, int endLine, int endColumn) {
		public Span(int startLine, int startColumn, int length) { 
			this(startLine, startColumn, startLine, startColumn + length - 1);
		}
		private Span() { 
			this(-1,-1,-1,-1);
		}
		public Span union(Span other) {
			if (isEmpty()) return other;
			if (other.isEmpty()) return this;
			int sl = startLine, sc = startColumn, el=endLine, ec = endColumn;
			if(other.startLine < startLine || (other.startLine == startLine && other.startColumn < startColumn)) {
				sl = other.startLine; sc = other.startColumn;
			}
			if(endLine < other.endLine || (endLine == other.endLine && endColumn < other.endColumn)) {
				el = other.endLine; ec = other.endColumn;
			}
			return new Span (sl, sc, el, ec);
		}
		public String toString() {
			return "[" + startLine() + ":" + startColumn() + "->" + endLine() + ":" + endColumn() + "]\t";
		}
		public final static Span EMPTY = new Span();
		public boolean isEmpty() { return startLine < 0; }
	}

}
