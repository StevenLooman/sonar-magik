package org.stevenlooman.sw.magik;

import org.stevenlooman.sw.magik.checks.CommentRegularExpressionCheck;
import org.stevenlooman.sw.magik.checks.DuplicateMethodInFileCheck;
import org.stevenlooman.sw.magik.checks.EmptyBlockCheck;
import org.stevenlooman.sw.magik.checks.ExemplarSlotCountCheck;
import org.stevenlooman.sw.magik.checks.FileNotInLoadListCheck;
import org.stevenlooman.sw.magik.checks.ForbiddenCallCheck;
import org.stevenlooman.sw.magik.checks.FormattingCheck;
import org.stevenlooman.sw.magik.checks.LhsRhsComparatorEqualCheck;
import org.stevenlooman.sw.magik.checks.LineLengthCheck;
import org.stevenlooman.sw.magik.checks.LocalImportProcedureCheck;
import org.stevenlooman.sw.magik.checks.MethodComplexityCheck;
import org.stevenlooman.sw.magik.checks.MethodDocCheck;
import org.stevenlooman.sw.magik.checks.ScopeCountCheck;
import org.stevenlooman.sw.magik.checks.SimplifyIfCheck;
import org.stevenlooman.sw.magik.checks.SizeZeroEmptyCheck;
import org.stevenlooman.sw.magik.checks.SwMethodDocCheck;
import org.stevenlooman.sw.magik.checks.SyntaxErrorCheck;
import org.stevenlooman.sw.magik.checks.TrailingWhitespaceCheck;
import org.stevenlooman.sw.magik.checks.UndefinedVariableCheck;
import org.stevenlooman.sw.magik.checks.UnusedVariableCheck;
import org.stevenlooman.sw.magik.checks.UseValueCompareCheck;
import org.stevenlooman.sw.magik.checks.VariableDeclarationUsageDistanceCheck;
import org.stevenlooman.sw.magik.checks.VariableNamingCheck;
import org.stevenlooman.sw.magik.checks.WarnedCallCheck;
import org.stevenlooman.sw.magik.checks.XPathCheck;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CheckList {
  public static final String REPOSITORY_KEY = "magik";
  public static final String PROFILE_DIR = "org/stevenlooman/sw/sonar/l10n/magik/rules";
  public static final String PROFILE_LOCATION = PROFILE_DIR + "/Sonar_way_profile.json";

  private CheckList() {
  }

  /**
   * Get the list of {{MagikCheck}}s.
   * @return List of with {{MagikCheck}}s
   */
  public static List<Class<?>> getChecks() {
    return Arrays.asList(
      CommentRegularExpressionCheck.class,
      DuplicateMethodInFileCheck.class,
      EmptyBlockCheck.class,
      ExemplarSlotCountCheck.class,
      FileNotInLoadListCheck.class,
      ForbiddenCallCheck.class,
      FormattingCheck.class,
      LhsRhsComparatorEqualCheck.class,
      LineLengthCheck.class,
      LocalImportProcedureCheck.class,
      MethodComplexityCheck.class,
      MethodDocCheck.class,
      ScopeCountCheck.class,
      SimplifyIfCheck.class,
      SizeZeroEmptyCheck.class,
      SwMethodDocCheck.class,
      SyntaxErrorCheck.class,
      TrailingWhitespaceCheck.class,
      UndefinedVariableCheck.class,
      UnusedVariableCheck.class,
      UseValueCompareCheck.class,
      VariableDeclarationUsageDistanceCheck.class,
      VariableNamingCheck.class,
      WarnedCallCheck.class,
      XPathCheck.class);
  }

  /**
   * Get the list of {{MagikCheck}}s which are templated.
   * @return List of with {{MagikCheck}}s which are templated.
   */
  public static List<Class<?>> getTemplatedChecks() {
    return getChecks().stream()
        .filter(checkClass -> checkClass.getAnnotation(TemplatedMagikCheck.class) != null)
        .collect(Collectors.toList());
  }

}
