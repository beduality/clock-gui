package io.github.beduality.clock_time.domain.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import org.junit.jupiter.api.Test;

class LocaleUtilsTest {

  @Test
  void testNullReturnsRoot() {
    assertEquals(Locale.ROOT, LocaleUtils.parseLocale(null));
  }

  @Test
  void testEmptyReturnsRoot() {
    assertEquals(Locale.ROOT, LocaleUtils.parseLocale(""));
  }

  @Test
  void testRootStringReturnsRoot() {
    assertEquals(Locale.ROOT, LocaleUtils.parseLocale("root"));
    assertEquals(Locale.ROOT, LocaleUtils.parseLocale("ROOT"));
  }

  @Test
  void testLanguageOnly() {
    Locale result = LocaleUtils.parseLocale("en");
    assertEquals("en", result.getLanguage());
  }

  @Test
  void testLanguageAndCountry() {
    Locale result = LocaleUtils.parseLocale("en_us");
    assertEquals("en", result.getLanguage());
    assertEquals("US", result.getCountry());
  }

  @Test
  void testLanguageCountryVariant() {
    Locale result = LocaleUtils.parseLocale("en_us_posix");
    assertEquals("en", result.getLanguage());
    assertEquals("US", result.getCountry());
    assertEquals("posix", result.getVariant());
  }

  @Test
  void testMinecraftLocales() {
    Locale ptBr = LocaleUtils.parseLocale("pt_br");
    assertEquals("pt", ptBr.getLanguage());
    assertEquals("BR", ptBr.getCountry());

    Locale deDE = LocaleUtils.parseLocale("de_de");
    assertEquals("de", deDE.getLanguage());
    assertEquals("DE", deDE.getCountry());
  }
}
