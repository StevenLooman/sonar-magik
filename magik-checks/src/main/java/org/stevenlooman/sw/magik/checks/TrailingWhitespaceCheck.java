package org.stevenlooman.sw.magik.checks;

import com.sonar.sslr.api.AstNode;

import org.sonar.check.Rule;
import org.stevenlooman.sw.magik.MagikCheck;

import javax.annotation.Nullable;

@Rule(key = TrailingWhitespaceCheck.CHECK_KEY)
public class TrailingWhitespaceCheck extends MagikCheck {

  private static final String MESSAGE = "Remove the trailing whitespace at line %s.";
  public static final String CHECK_KEY = "TrailingWhitespace";

  /**
   * Visit the file
   * @param node Root node.
   */
  public void visitFile(@Nullable AstNode node) {
    String[] lines = getContext().fileContentLines();
    if (lines == null) {
      lines = new String[]{};
    }
    for (int lineNo = 0; lineNo < lines.length; ++lineNo) {
      String line = lines[lineNo];

      // strip \r, if any
      if (line.endsWith("\r")) {
        line = line.substring(0, line.length() - 1);
      }

      if (line.endsWith(" ")
          || line.endsWith("\t")) {
        String message = String.format(MESSAGE, lineNo + 1);
        addIssue(message, lineNo + 1, line.length());
      }
    }
  }

}
