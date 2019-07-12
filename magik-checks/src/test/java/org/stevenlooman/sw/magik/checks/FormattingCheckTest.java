package org.stevenlooman.sw.magik.checks;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.stevenlooman.sw.magik.MagikCheck;
import org.stevenlooman.sw.magik.MagikIssue;

import java.util.List;

public class FormattingCheckTest extends MagikCheckTestBase {

  @Test
  public void testCommaProper1() {
    MagikCheck check = new FormattingCheck();
    String code = "{1, 2}";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testCommaProper2() {
    MagikCheck check = new FormattingCheck();
    String code = "{1, :|a|, 2}";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testCommaImproper1() {
    MagikCheck check = new FormattingCheck();
    String code = "{1,2}";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isNotEmpty();
  }

  @Test
  public void testCommaImproper2() {
    MagikCheck check = new FormattingCheck();
    String code = "{1 , 2}";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isNotEmpty();
  }

  @Test
  public void testCommaImproper3() {
    MagikCheck check = new FormattingCheck();
    String code =
        "{1 ,\n" +
        " 2}";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isNotEmpty();
  }

  @Test
  public void testBinaryOperatorProper1() {
    MagikCheck check = new FormattingCheck();
    String code = "a * b";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testBinaryOperatorProper2() {
    MagikCheck check = new FormattingCheck();
    String code = "a _isnt b";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testBinaryOperatorProper3() {
    MagikCheck check = new FormattingCheck();
    String code = "a +<< b";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testBinaryOperatorImproper1() {
    MagikCheck check = new FormattingCheck();
    String code = "a*b";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isNotEmpty();
  }

  @Test
  public void testBinaryOperatorImproper2() {
    MagikCheck check = new FormattingCheck();
    String code = "a* b";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isNotEmpty();
  }

  @Test
  public void testBinaryOperatorImproper3() {
    MagikCheck check = new FormattingCheck();
    String code = "a *b";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isNotEmpty();
  }

  @Test
  public void testBracketProper1() {
    MagikCheck check = new FormattingCheck();
    String code = "show(a, b)";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testBracketProper2() {
    MagikCheck check = new FormattingCheck();
    String code = "show(% )";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testBracketProper3() {
    MagikCheck check = new FormattingCheck();
    String code =
        "\t{\n" +
        "\t\t2\n" +
        "\t}\n";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testBracketImproper1() {
    MagikCheck check = new FormattingCheck();
    String code = "show(a, b )";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isNotEmpty();
  }

  @Test
  public void testBracketImproper2() {
    MagikCheck check = new FormattingCheck();
    String code = "show( a, b)";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isNotEmpty();
  }

}