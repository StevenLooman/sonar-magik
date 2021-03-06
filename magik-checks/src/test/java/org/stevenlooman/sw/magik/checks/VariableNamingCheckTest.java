package org.stevenlooman.sw.magik.checks;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.stevenlooman.sw.magik.MagikCheck;
import org.stevenlooman.sw.magik.MagikIssue;

import java.util.List;

public class VariableNamingCheckTest extends MagikCheckTestBase {

  @Test
  public void testValidName() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "_local coord";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testValidNameAssignment() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "coord << 10";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testInvalidName() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "_local c";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).hasSize(1);
  }

  @Test
  public void testInvalidNameAssignment() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "c << 10";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).hasSize(1);
  }

  @Test
  public void testWhitelistedName() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "_local x";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testValidNameParameter() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "_method a.b(coord) _endmethod";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testInvalidNameParameter() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "_method a.b(c) _endmethod";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).hasSize(1);
  }

  @Test
  public void testPrefixedValidName() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "_local l_coord";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testPrefixedInvalidName() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "_local l_c";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).hasSize(1);
  }

  @Test
  public void testWhitelistedPrefixedName() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "_local l_x";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testMultiVariableDeclarationValidName() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "_local (l_item, l_result) << (1, 2)";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testMultiVariableDeclarationInvalidName() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "_local (l_i, l_r) << (1, 2)";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).hasSize(2);
  }

  @Test
  public void testAugmentedAssignment() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "result +<< 10";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testAugmentedAssignmentMulti() {
    MagikCheck check = new VariableNamingCheck();
    String code =
        "result +<< str << _self.a";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

}
