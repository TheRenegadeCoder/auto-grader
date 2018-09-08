import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.*;
import java.lang.reflect.*;

/**
 * Tests project 2 as specified by:
 * http://web.cse.ohio-state.edu/cse1223/currentsem/projects/CSE1223Project02.html
 */
public class Project02Test {
  
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  
  /**
   * Sets input and output streams to local print streams for analysis.
   */
  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }
  
  /**
   * Sets input and output streams back to normal.
   */
  @After
  public void tearDown() {
    System.setIn(System.in);
    System.setOut(System.out);
  }
  
  /**
   * Generates the solution for testing.
   */
  public String buildSolution(String fullString, String substring, int position, String replacement) {
    int substringStart = fullString.indexOf(substring);
    String beforeSubstring = fullString.substring(0, substringStart);
    String afterSubstring = fullString.substring(substringStart + substring.length(), fullString.length());
    String replacedString = beforeSubstring + replacement + afterSubstring;
    String[] solutionList = {
      "Enter a long string: ",
      "Enter a substring: ",
      "Length of your string: " + fullString.length(),
      "Length of your substring: " + substring.length(),
      "Starting position of your substring in string: " + substringStart,
      "String before your substring: " + beforeSubstring,
      "String after your substring: " + afterSubstring,
      "Enter a position between 0 and " + (fullString.length() - 1) + ":",
      "The character at position " + position + " is " + fullString.charAt(position),
      "Enter a replacement string: ",
      "Your new string is: " + replacedString
    };
    String solution = buildLines(solutionList);
    return solution;
  }
  
  /**
   * Constructs user input for testing.
   */
  public String constructInput(String fullString, String substring, int position, String replacement) {
    return buildLines(fullString, substring, Integer.toString(position), replacement);
  }
  
  /**
   * Takes a set of inputs and joins them with newlines.
   */
  public String buildLines(String ... inputs) {
    StringBuilder sb = new StringBuilder();
    for (String input: inputs) {
      sb.append(input);
      sb.append(System.lineSeparator());
    }
    return sb.toString();
  }
  
  /**
   * Returns the main method from the proper class
   */
  private static Class getMain() {
    Class cls;
    try {
      cls = Class.forName("osu.cse1223.Project02");
    } catch (ClassNotFoundException e) {
      try {
        cls = Class.forName("Project02");
      } catch (ClassNotFoundException e1) {
        cls = null;
        System.err.println("Failed to find Project02");
        System.exit(1);
      }
    }
    return cls;
  }
  
  /**
   * Runs the main method of the test class
   */
  private static void runMain() {
    Class cls = getMain();
    try {
      Method meth = cls.getMethod("main", String[].class);
      String[] params = null;
      meth.invoke(null, (Object) params);
    } catch (NoSuchMethodException e) {
      System.err.println("No method main");
      System.exit(1);
    } catch (IllegalAccessException e) {
      System.err.println("Can't invoke method main");
      System.exit(1);
    } catch (InvocationTargetException e) {
      System.err.println("Can't target method main");
      System.exit(1);
    }
  }
  
  /**
   * A helper method which allows us to rapidly build test cases.
   */
  public void runCase(String fullString, String substring, int position, String replacement) {
    String input = constructInput(fullString, substring, position, replacement);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    runMain();
    String solution = buildSolution(fullString, substring, position, replacement);
    assertEquals(solution.trim(), outContent.toString().trim());
  }
  
  /**
   * Tests the replacement of a word
   */
  @Test
  public void testMain01() {
    runCase("The quick brown fox jumped over the lazy dog", "jumped", 18, "leaped");
  }
  
  /**
   * Tests the replacement of a partial word
   */
  @Test
  public void testMain02() {
    runCase("Friends, Romans, countrymen, lend me your ears", "try", 21, "catch");
  }
}
