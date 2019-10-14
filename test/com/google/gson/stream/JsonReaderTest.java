/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.stream;

import static com.google.gson.stream.JsonToken.BEGIN_ARRAY;
import static com.google.gson.stream.JsonToken.BEGIN_OBJECT;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import junit.framework.TestCase;

@SuppressWarnings("resource")
public final class JsonReaderTest extends TestCase {
  public void testReadArray() throws IOException {
    JsonReader reader = new JsonReader(reader("[true, true]"));
    reader.beginArray();
    assertEquals(true, reader.nextBoolean());
    assertEquals(true, reader.nextBoolean());
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testReadEmptyArray() throws IOException {
    JsonReader reader = new JsonReader(reader("[]"));
    reader.beginArray();
    assertFalse(reader.hasNext());
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testReadObject() throws IOException {
    JsonReader reader = new JsonReader(reader(
        "{\"a\": \"android\", \"b\": \"banana\"}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    assertEquals("android", reader.nextString());
    assertEquals("b", reader.nextName());
    assertEquals("banana", reader.nextString());
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testReadEmptyObject() throws IOException {
    JsonReader reader = new JsonReader(reader("{}"));
    reader.beginObject();
    assertFalse(reader.hasNext());
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testSkipArray() throws IOException {
    JsonReader reader = new JsonReader(reader(
        "{\"a\": [\"one\", \"two\", \"three\"], \"b\": 123}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    reader.skipValue();
    assertEquals("b", reader.nextName());
    assertEquals(123, reader.nextInt());
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testSkipArrayAfterPeek() throws Exception {
    JsonReader reader = new JsonReader(reader(
        "{\"a\": [\"one\", \"two\", \"three\"], \"b\": 123}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    assertEquals(BEGIN_ARRAY, reader.peek());
    reader.skipValue();
    assertEquals("b", reader.nextName());
    assertEquals(123, reader.nextInt());
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testSkipTopLevelObject() throws Exception {
    JsonReader reader = new JsonReader(reader(
        "{\"a\": [\"one\", \"two\", \"three\"], \"b\": 123}"));
    reader.skipValue();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testSkipObject() throws IOException {
    JsonReader reader = new JsonReader(reader(
        "{\"a\": { \"c\": [], \"d\": [true, true, {}] }, \"b\": \"banana\"}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    reader.skipValue();
    assertEquals("b", reader.nextName());
    reader.skipValue();
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testSkipObjectAfterPeek() throws Exception {
    String json = "{" + "  \"one\": { \"num\": 1 }"
        + ", \"two\": { \"num\": 2 }" + ", \"three\": { \"num\": 3 }" + "}";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginObject();
    assertEquals("one", reader.nextName());
    assertEquals(BEGIN_OBJECT, reader.peek());
    reader.skipValue();
    assertEquals("two", reader.nextName());
    assertEquals(BEGIN_OBJECT, reader.peek());
    reader.skipValue();
    assertEquals("three", reader.nextName());
    reader.skipValue();
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testSkipInteger() throws IOException {
    JsonReader reader = new JsonReader(reader(
        "{\"a\":123456789,\"b\":-123456789}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    reader.skipValue();
    assertEquals("b", reader.nextName());
    reader.skipValue();
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testSkipDouble() throws IOException {
    JsonReader reader = new JsonReader(reader(
        "{\"a\":-123.456e-789,\"b\":123456789.0}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    reader.skipValue();
    assertEquals("b", reader.nextName());
    reader.skipValue();
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testHelloWorld() throws IOException {
    String json = "{\n" +
        "   \"hello\": true,\n" +
        "   \"foo\": [\"world\"]\n" +
        "}";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginObject();
    assertEquals("hello", reader.nextName());
    assertEquals(true, reader.nextBoolean());
    assertEquals("foo", reader.nextName());
    reader.beginArray();
    assertEquals("world", reader.nextString());
    reader.endArray();
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testInvalidJsonInput() throws IOException {
    String json = "{\n"
        + "   \"h\\ello\": true,\n"
        + "   \"foo\": [\"world\"]\n"
        + "}";

    JsonReader reader = new JsonReader(reader(json));
    reader.beginObject();
    try {
      reader.nextName();
      fail();
    } catch (IOException expected) {
    }
  }
  
  public void testNulls() {
    try {
      new JsonReader(null);
      fail();
    } catch (NullPointerException expected) {
    }
  }

  public void testEmptyString() {
    try {
      new JsonReader(reader("")).beginArray();
      fail();
    } catch (IOException expected) {
    }
    try {
      new JsonReader(reader("")).beginObject();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testCharacterUnescaping() throws IOException {
    String json = "[\"a\","
        + "\"a\\\"\","
        + "\"\\\"\","
        + "\":\","
        + "\",\","
        + "\"\\b\","
        + "\"\\f\","
        + "\"\\n\","
        + "\"\\r\","
        + "\"\\t\","
        + "\" \","
        + "\"\\\\\","
        + "\"{\","
        + "\"}\","
        + "\"[\","
        + "\"]\","
        + "\"\\u0000\","
        + "\"\\u0019\","
        + "\"\\u20AC\""
        + "]";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    assertEquals("a", reader.nextString());
    assertEquals("a\"", reader.nextString());
    assertEquals("\"", reader.nextString());
    assertEquals(":", reader.nextString());
    assertEquals(",", reader.nextString());
    assertEquals("\b", reader.nextString());
    assertEquals("\f", reader.nextString());
    assertEquals("\n", reader.nextString());
    assertEquals("\r", reader.nextString());
    assertEquals("\t", reader.nextString());
    assertEquals(" ", reader.nextString());
    assertEquals("\\", reader.nextString());
    assertEquals("{", reader.nextString());
    assertEquals("}", reader.nextString());
    assertEquals("[", reader.nextString());
    assertEquals("]", reader.nextString());
    assertEquals("\0", reader.nextString());
    assertEquals("\u0019", reader.nextString());
    assertEquals("\u20AC", reader.nextString());
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testUnescapingInvalidCharacters() throws IOException {
    String json = "[\"\\u000g\"]";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    try {
      reader.nextString();
      fail();
    } catch (NumberFormatException expected) {
    }
  }

  public void testUnescapingTruncatedCharacters() throws IOException {
    String json = "[\"\\u000";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    try {
      reader.nextString();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testUnescapingTruncatedSequence() throws IOException {
    String json = "[\"\\";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    try {
      reader.nextString();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testIntegersWithFractionalPartSpecified() throws IOException {
    JsonReader reader = new JsonReader(reader("[1.0,1.0,1.0]"));
    reader.beginArray();
    assertEquals(1.0, reader.nextDouble());
    assertEquals(1, reader.nextInt());
    assertEquals(1L, reader.nextLong());
  }

  public void testDoubles() throws IOException {
    String json = "[-0.0,"
        + "1.0,"
        + "1.7976931348623157E308,"
        + "4.9E-324,"
        + "0.0,"
        + "-0.5,"
        + "2.2250738585072014E-308,"
        + "3.141592653589793,"
        + "2.718281828459045]";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    assertEquals(-0.0, reader.nextDouble());
    assertEquals(1.0, reader.nextDouble());
    assertEquals(1.7976931348623157E308, reader.nextDouble());
    assertEquals(4.9E-324, reader.nextDouble());
    assertEquals(0.0, reader.nextDouble());
    assertEquals(-0.5, reader.nextDouble());
    assertEquals(2.2250738585072014E-308, reader.nextDouble());
    assertEquals(3.141592653589793, reader.nextDouble());
    assertEquals(2.718281828459045, reader.nextDouble());
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testStrictNonFiniteDoubles() throws IOException {
    String json = "[NaN]";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    try {
      reader.nextDouble();
      fail();
    } catch (MalformedJsonException expected) {
    }
  }

  public void testStrictQuotedNonFiniteDoubles() throws IOException {
    String json = "[\"NaN\"]";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    try {
      reader.nextDouble();
      fail();
    } catch (MalformedJsonException expected) {
    }
  }


  public void testStrictNonFiniteDoublesWithSkipValue() throws IOException {
    String json = "[NaN]";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    try {
      reader.skipValue();
      fail();
    } catch (MalformedJsonException expected) {
    }
  }

  public void testLongs() throws IOException {
    String json = "[0,0,0,"
        + "1,1,1,"
        + "-1,-1,-1,"
        + "-9223372036854775808,"
        + "9223372036854775807]";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    assertEquals(0L, reader.nextLong());
    assertEquals(0, reader.nextInt());
    assertEquals(0.0, reader.nextDouble());
    assertEquals(1L, reader.nextLong());
    assertEquals(1, reader.nextInt());
    assertEquals(1.0, reader.nextDouble());
    assertEquals(-1L, reader.nextLong());
    assertEquals(-1, reader.nextInt());
    assertEquals(-1.0, reader.nextDouble());
    try {
      reader.nextInt();
      fail();
    } catch (NumberFormatException expected) {
    }
    assertEquals(Long.MIN_VALUE, reader.nextLong());
    try {
      reader.nextInt();
      fail();
    } catch (NumberFormatException expected) {
    }
    assertEquals(Long.MAX_VALUE, reader.nextLong());
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void disabled_testNumberWithOctalPrefix() throws IOException {
    String json = "[01]";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    try {
      reader.peek();
      fail();
    } catch (MalformedJsonException expected) {
    }
    try {
      reader.nextInt();
      fail();
    } catch (MalformedJsonException expected) {
    }
    try {
      reader.nextLong();
      fail();
    } catch (MalformedJsonException expected) {
    }
    try {
      reader.nextDouble();
      fail();
    } catch (MalformedJsonException expected) {
    }
    assertEquals("01", reader.nextString());
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testBooleans() throws IOException {
    JsonReader reader = new JsonReader(reader("[true,false]"));
    reader.beginArray();
    assertEquals(true, reader.nextBoolean());
    assertEquals(false, reader.nextBoolean());
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  /**
   * This test fails because there's no double for 9223372036854775806, and
   * our long parsing uses Double.parseDouble() for fractional values.
   */
  public void disabled_testHighPrecisionLong() throws IOException {
    String json = "[9223372036854775806.000]";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    assertEquals(9223372036854775806L, reader.nextLong());
    reader.endArray();
  }

  public void testMixedCaseLiterals() throws IOException {
    JsonReader reader = new JsonReader(reader("[True,TruE,False,FALSE,NULL,nulL]"));
    reader.beginArray();
    assertEquals(true, reader.nextBoolean());
    assertEquals(true, reader.nextBoolean());
    assertEquals(false, reader.nextBoolean());
    assertEquals(false, reader.nextBoolean());
    reader.nextNull();
    reader.nextNull();
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testMissingValue() throws IOException {
    JsonReader reader = new JsonReader(reader("{\"a\":}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    try {
      reader.nextString();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testPrematureEndOfInput() throws IOException {
    JsonReader reader = new JsonReader(reader("{\"a\":true,"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    assertEquals(true, reader.nextBoolean());
    try {
      reader.nextName();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testPrematurelyClosed() throws IOException {
    try {
      JsonReader reader = new JsonReader(reader("{\"a\":[]}"));
      reader.beginObject();
      reader.close();
      reader.nextName();
      fail();
    } catch (IllegalStateException expected) {
    }

    try {
      JsonReader reader = new JsonReader(reader("{\"a\":[]}"));
      reader.close();
      reader.beginObject();
      fail();
    } catch (IllegalStateException expected) {
    }

    try {
      JsonReader reader = new JsonReader(reader("{\"a\":true}"));
      reader.beginObject();
      reader.nextName();
      reader.peek();
      reader.close();
      reader.nextBoolean();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testNextFailuresDoNotAdvance() throws IOException {
    JsonReader reader = new JsonReader(reader("{\"a\":true}"));
    reader.beginObject();
    try {
      reader.nextString();
      fail();
    } catch (IllegalStateException expected) {
    }
    assertEquals("a", reader.nextName());
    try {
      reader.nextName();
      fail();
    } catch (IllegalStateException expected) {
    }
    try {
      reader.beginArray();
      fail();
    } catch (IllegalStateException expected) {
    }
    try {
      reader.endArray();
      fail();
    } catch (IllegalStateException expected) {
    }
    try {
      reader.beginObject();
      fail();
    } catch (IllegalStateException expected) {
    }
    try {
      reader.endObject();
      fail();
    } catch (IllegalStateException expected) {
    }
    assertEquals(true, reader.nextBoolean());
    try {
      reader.nextString();
      fail();
    } catch (IllegalStateException expected) {
    }
    try {
      reader.nextName();
      fail();
    } catch (IllegalStateException expected) {
    }
    try {
      reader.beginArray();
      fail();
    } catch (IllegalStateException expected) {
    }
    try {
      reader.endArray();
      fail();
    } catch (IllegalStateException expected) {
    }
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    reader.close();
  }

  public void testIntegerMismatchFailuresDoNotAdvance() throws IOException {
    JsonReader reader = new JsonReader(reader("[1.5]"));
    reader.beginArray();
    try {
      reader.nextInt();
      fail();
    } catch (NumberFormatException expected) {
    }
    assertEquals(1.5d, reader.nextDouble());
    reader.endArray();
  }

  public void testStringNullIsNotNull() throws IOException {
    JsonReader reader = new JsonReader(reader("[\"null\"]"));
    reader.beginArray();
    try {
      reader.nextNull();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testNullLiteralIsNotAString() throws IOException {
    JsonReader reader = new JsonReader(reader("[null]"));
    reader.beginArray();
    try {
      reader.nextString();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testStrictNameValueSeparator() throws IOException {
    JsonReader reader = new JsonReader(reader("{\"a\"=true}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    try {
      reader.nextBoolean();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("{\"a\"=>true}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    try {
      reader.nextBoolean();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictNameValueSeparatorWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("{\"a\"=true}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("{\"a\"=>true}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testCommentsInStringValue() throws Exception {
    JsonReader reader = new JsonReader(reader("[\"// comment\"]"));
    reader.beginArray();
    assertEquals("// comment", reader.nextString());
    reader.endArray();

    reader = new JsonReader(reader("{\"a\":\"#someComment\"}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    assertEquals("#someComment", reader.nextString());
    reader.endObject();

    reader = new JsonReader(reader("{\"#//a\":\"#some //Comment\"}"));
    reader.beginObject();
    assertEquals("#//a", reader.nextName());
    assertEquals("#some //Comment", reader.nextString());
    reader.endObject();
  }

  public void testStrictComments() throws IOException {
    JsonReader reader = new JsonReader(reader("[// comment \n true]"));
    reader.beginArray();
    try {
      reader.nextBoolean();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("[# comment \n true]"));
    reader.beginArray();
    try {
      reader.nextBoolean();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("[/* comment */ true]"));
    reader.beginArray();
    try {
      reader.nextBoolean();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictCommentsWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("[// comment \n true]"));
    reader.beginArray();
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("[# comment \n true]"));
    reader.beginArray();
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("[/* comment */ true]"));
    reader.beginArray();
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictUnquotedNames() throws IOException {
    JsonReader reader = new JsonReader(reader("{a:true}"));
    reader.beginObject();
    try {
      reader.nextName();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictUnquotedNamesWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("{a:true}"));
    reader.beginObject();
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictSingleQuotedNames() throws IOException {
    JsonReader reader = new JsonReader(reader("{'a':true}"));
    reader.beginObject();
    try {
      reader.nextName();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictSingleQuotedNamesWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("{'a':true}"));
    reader.beginObject();
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictUnquotedStrings() throws IOException {
    JsonReader reader = new JsonReader(reader("[a]"));
    reader.beginArray();
    try {
      reader.nextString();
      fail();
    } catch (MalformedJsonException expected) {
    }
  }

  public void testStrictUnquotedStringsWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("[a]"));
    reader.beginArray();
    try {
      reader.skipValue();
      fail();
    } catch (MalformedJsonException expected) {
    }
  }
  public void testStrictSingleQuotedStrings() throws IOException {
    JsonReader reader = new JsonReader(reader("['a']"));
    reader.beginArray();
    try {
      reader.nextString();
      fail();
    } catch (IOException expected) {
    }
  }
  public void testStrictSingleQuotedStringsWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("['a']"));
    reader.beginArray();
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictSemicolonDelimitedArray() throws IOException {
    JsonReader reader = new JsonReader(reader("[true;true]"));
    reader.beginArray();
    try {
      reader.nextBoolean();
      reader.nextBoolean();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictSemicolonDelimitedArrayWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("[true;true]"));
    reader.beginArray();
    try {
      reader.skipValue();
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictSemicolonDelimitedNameValuePair() throws IOException {
    JsonReader reader = new JsonReader(reader("{\"a\":true;\"b\":true}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    try {
      reader.nextBoolean();
      reader.nextName();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictSemicolonDelimitedNameValuePairWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("{\"a\":true;\"b\":true}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    try {
      reader.skipValue();
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictUnnecessaryArraySeparators() throws IOException {
    JsonReader reader = new JsonReader(reader("[true,,true]"));
    reader.beginArray();
    assertEquals(true, reader.nextBoolean());
    try {
      reader.nextNull();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("[,true]"));
    reader.beginArray();
    try {
      reader.nextNull();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("[true,]"));
    reader.beginArray();
    assertEquals(true, reader.nextBoolean());
    try {
      reader.nextNull();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("[,]"));
    reader.beginArray();
    try {
      reader.nextNull();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictUnnecessaryArraySeparatorsWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("[true,,true]"));
    reader.beginArray();
    assertEquals(true, reader.nextBoolean());
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("[,true]"));
    reader.beginArray();
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("[true,]"));
    reader.beginArray();
    assertEquals(true, reader.nextBoolean());
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }

    reader = new JsonReader(reader("[,]"));
    reader.beginArray();
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictMultipleTopLevelValues() throws IOException {
    JsonReader reader = new JsonReader(reader("[] []"));
    reader.beginArray();
    reader.endArray();
    try {
      reader.peek();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictMultipleTopLevelValuesWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("[] []"));
    reader.beginArray();
    reader.endArray();
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testTopLevelValueTypes() throws IOException {
    JsonReader reader1 = new JsonReader(reader("true"));
    assertTrue(reader1.nextBoolean());
    assertEquals(JsonToken.END_DOCUMENT, reader1.peek());

    JsonReader reader2 = new JsonReader(reader("false"));
    assertFalse(reader2.nextBoolean());
    assertEquals(JsonToken.END_DOCUMENT, reader2.peek());

    JsonReader reader3 = new JsonReader(reader("null"));
    assertEquals(JsonToken.NULL, reader3.peek());
    reader3.nextNull();
    assertEquals(JsonToken.END_DOCUMENT, reader3.peek());

    JsonReader reader4 = new JsonReader(reader("123"));
    assertEquals(123, reader4.nextInt());
    assertEquals(JsonToken.END_DOCUMENT, reader4.peek());

    JsonReader reader5 = new JsonReader(reader("123.4"));
    assertEquals(123.4, reader5.nextDouble());
    assertEquals(JsonToken.END_DOCUMENT, reader5.peek());

    JsonReader reader6 = new JsonReader(reader("\"a\""));
    assertEquals("a", reader6.nextString());
    assertEquals(JsonToken.END_DOCUMENT, reader6.peek());
  }

  public void testTopLevelValueTypeWithSkipValue() throws IOException {
    JsonReader reader = new JsonReader(reader("true"));
    reader.skipValue();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testStrictNonExecutePrefix() {
    JsonReader reader = new JsonReader(reader(")]}'\n []"));
    try {
      reader.beginArray();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testStrictNonExecutePrefixWithSkipValue() {
    JsonReader reader = new JsonReader(reader(")]}'\n []"));
    try {
      reader.skipValue();
      fail();
    } catch (IOException expected) {
    }
  }
  public void testBomIgnoredAsFirstCharacterOfDocument() throws IOException {
    JsonReader reader = new JsonReader(reader("\ufeff[]"));
    reader.beginArray();
    reader.endArray();
  }

  public void testBomForbiddenAsOtherCharacterInDocument() throws IOException {
    JsonReader reader = new JsonReader(reader("[\ufeff]"));
    reader.beginArray();
    try {
      reader.endArray();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testFailWithPositionDeepPath() throws IOException {
    JsonReader reader = new JsonReader(reader("[1,{\"a\":[2,3,}"));
    reader.beginArray();
    reader.nextInt();
    reader.beginObject();
    reader.nextName();
    reader.beginArray();
    reader.nextInt();
    reader.nextInt();
    try {
      reader.peek();
      fail();
    } catch (IOException expected) {
      assertEquals("Expected value at line 1 column 14 path $[1].a[2]", expected.getMessage());
    }
  }

  public void testStrictVeryLongNumber() throws IOException {
    JsonReader reader = new JsonReader(reader("[0." + repeat('9', 8192) + "]"));
    reader.beginArray();
    try {
      assertEquals(1d, reader.nextDouble());
      fail();
    } catch (MalformedJsonException expected) {
    }
  }

  public void testDeeplyNestedArrays() throws IOException {
    // this is nested 40 levels deep; Gson is tuned for nesting is 30 levels deep or fewer
    JsonReader reader = new JsonReader(reader(
        "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]"));
    for (int i = 0; i < 40; i++) {
      reader.beginArray();
    }
    assertEquals("$[0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0]"
        + "[0][0][0][0][0][0][0][0][0][0][0][0][0][0]", reader.getPath());
    for (int i = 0; i < 40; i++) {
      reader.endArray();
    }
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testDeeplyNestedObjects() throws IOException {
    // Build a JSON document structured like {"a":{"a":{"a":{"a":true}}}}, but 40 levels deep
    String array = "{\"a\":%s}";
    String json = "true";
    for (int i = 0; i < 40; i++) {
      json = String.format(array, json);
    }

    JsonReader reader = new JsonReader(reader(json));
    for (int i = 0; i < 40; i++) {
      reader.beginObject();
      assertEquals("a", reader.nextName());
    }
    assertEquals("$.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a"
        + ".a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a", reader.getPath());
    assertEquals(true, reader.nextBoolean());
    for (int i = 0; i < 40; i++) {
      reader.endObject();
    }
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  public void testVeryLongQuotedString() throws IOException {
    char[] stringChars = new char[1024 * 16];
    Arrays.fill(stringChars, 'x');
    String string = new String(stringChars);
    String json = "[\"" + string + "\"]";
    JsonReader reader = new JsonReader(reader(json));
    reader.beginArray();
    assertEquals(string, reader.nextString());
    reader.endArray();
  }

  public void testSkipVeryLongQuotedString() throws IOException {
    JsonReader reader = new JsonReader(reader("[\"" + repeat('x', 8192) + "\"]"));
    reader.beginArray();
    reader.skipValue();
    reader.endArray();
  }

  public void testStrictExtraCommasInMaps() throws IOException {
    JsonReader reader = new JsonReader(reader("{\"a\":\"b\",}"));
    reader.beginObject();
    assertEquals("a", reader.nextName());
    assertEquals("b", reader.nextString());
    try {
      reader.peek();
      fail();
    } catch (IOException expected) {
    }
  }

  private String repeat(char c, int count) {
    char[] array = new char[count];
    Arrays.fill(array, c);
    return new String(array);
  }

 
  /**
   * Returns a reader that returns one character at a time.
   */
  private Reader reader(final String s) {
    /* if (true) */ return new StringReader(s);
    /* return new Reader() {
      int position = 0;
      @Override public int read(char[] buffer, int offset, int count) throws IOException {
        if (position == s.length()) {
          return -1;
        } else if (count > 0) {
          buffer[offset] = s.charAt(position++);
          return 1;
        } else {
          throw new IllegalArgumentException();
        }
      }
      @Override public void close() throws IOException {
      }
    }; */
  }
}
