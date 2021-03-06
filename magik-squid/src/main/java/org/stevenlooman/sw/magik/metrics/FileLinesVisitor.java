package org.stevenlooman.sw.magik.metrics;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.api.Trivia;
import org.stevenlooman.sw.magik.MagikCommentAnalyser;
import org.stevenlooman.sw.magik.MagikVisitor;
import org.stevenlooman.sw.magik.api.MagikKeyword;
import org.stevenlooman.sw.magik.api.MagikPunctuator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Visitor that computes NCLOC_DATA_KEY and COMMENT_LINES_DATA_KEY metrics used by the DevCockpit.
 */
public class FileLinesVisitor extends MagikVisitor {
  private boolean ignoreHeaderComments;
  private boolean seenFirstToken;

  private Set<Integer> linesOfCode = new HashSet<>();
  private Set<Integer> linesOfComments = new HashSet<>();
  private Set<Integer> executableLines = new HashSet<>();
  private Set<Integer> nosonarLines = new HashSet<>();

  private static final Set<AstNodeType> NON_EXECUTABLE_TOKENS = new HashSet<AstNodeType>(
      Arrays.asList(
        MagikKeyword.HANDLING,
        MagikKeyword.BLOCK,
        MagikKeyword.TRY,
        MagikKeyword.WHEN,
        MagikKeyword.ENDTRY,
        MagikKeyword.PROTECT,
        MagikKeyword.PROTECTION,
        MagikKeyword.ENDPROTECT,
        MagikKeyword.LOCK,
        MagikKeyword.ENDLOCK,
        MagikKeyword.CATCH,
        MagikKeyword.ENDCATCH,
        MagikKeyword.PROC,
        MagikKeyword.ENDPROC,
        MagikPunctuator.DOLLAR
  ));

  public FileLinesVisitor(boolean ignoreHeaderComments) {
    this.ignoreHeaderComments = ignoreHeaderComments;
  }

  public Set<Integer> getLinesOfCode() {
    return Collections.unmodifiableSet(linesOfCode);
  }

  public Set<Integer> getLinesOfComments() {
    return Collections.unmodifiableSet(linesOfComments);
  }

  public Set<Integer> getExecutableLines() {
    return Collections.unmodifiableSet(executableLines);
  }

  public Set<Integer> getNosonarLines() {
    return Collections.unmodifiableSet(nosonarLines);
  }

  @Override
  public void visitFile(AstNode astNode) {
    nosonarLines.clear();
    linesOfCode.clear();
    linesOfComments.clear();
    executableLines.clear();
    seenFirstToken = false;
  }

  @Override
  public void walkToken(Token token) {
    // process lines of code
    String[] tokenLines = token.getValue().split("\n", -1);
    for (int line = token.getLine(); line < token.getLine() + tokenLines.length; line++) {
      linesOfCode.add(line);
    }

    // ignore file header comment
    if (ignoreHeaderComments && !seenFirstToken) {
      seenFirstToken = true;
      return;
    }

    // process comment
    for (Trivia trivia : token.getTrivia()) {
      if (trivia.isComment()) {
        visitComment(trivia);
      }
    }
  }

  @Override
  protected void walkPreStatement(AstNode node) {
    addIfExecutableLine(node);
  }

  @Override
  protected void walkPreExpression(AstNode node) {
    addIfExecutableLine(node);
  }

  private void addIfExecutableLine(AstNode node) {
    // process any executable nodes/tokens
    TokenType tokenType = node.getToken().getType();
    if (!NON_EXECUTABLE_TOKENS.contains(tokenType)) {
      executableLines.add(node.getTokenLine());
    }
  }

  private void visitComment(Trivia trivia) {
    String originalValue = MagikCommentAnalyser.getContents(trivia.getToken().getOriginalValue());
    String[] commentLines = originalValue.split("(\r)?\n|\r", -1);

    int line = trivia.getToken().getLine();
    for (String commentLine : commentLines) {
      if (commentLine.contains("NOSONAR")) {
        linesOfComments.remove(line);
        nosonarLines.add(line);
      } else if (!MagikCommentAnalyser.isBlank(commentLine)
                 && !nosonarLines.contains(line)) {
        linesOfComments.add(line);
      }

      line++;
    }
  }
}
