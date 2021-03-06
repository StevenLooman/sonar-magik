package org.stevenlooman.sw.sonar.language;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;
import org.stevenlooman.sw.sonar.MagikPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Smallworld Magik language.
 */
public class Magik extends AbstractLanguage {

  public static final String KEY = "magik";

  private static final String[] DEFAULT_FILE_SUFFIXES = {"magik"}; // NOSONAR: S1192

  private Configuration configuration;

  /**
   * Constructor.
   * @param configuration Configuration.
   */
  public Magik(Configuration configuration) {
    super(KEY, "Magik");
    this.configuration = configuration;
  }

  @Override
  public String[] getFileSuffixes() {
    String[] stringArray = configuration.getStringArray(MagikPlugin.FILE_SUFFIXES_KEY);
    String[] suffixes = filterEmptyStrings(stringArray);
    return suffixes.length == 0 ? Magik.DEFAULT_FILE_SUFFIXES : suffixes;
  }

  private static String[] filterEmptyStrings(String[] stringArray) {
    List<String> nonEmptyStrings = new ArrayList<>();
    for (String string : stringArray) {
      if (!string.trim().isEmpty()) {
        nonEmptyStrings.add(string.trim());
      }
    }
    return nonEmptyStrings.toArray(new String[nonEmptyStrings.size()]);
  }
}
