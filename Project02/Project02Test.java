import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Tests project 2 as specified by:
 * http://web.cse.ohio-state.edu/cse1223/currentsem/projects/CSE1223Project02.html
 * 
 * This test file verifies that the Project 2 solution passes on the basis of
 * content rather than structure. In other words, we don't care if the output
 * doesn't structurally look exactly like the expected output. However, we do
 * care that the solution has all the expected content.
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
  private static Class<?> getMain(ArrayList<String> toTest) {
    Class<?> cls;
    try {
      cls = Class.forName(toTest.get(0));
    } catch (ClassNotFoundException e) {
      System.err.println("Failed to find the class: " + toTest.get(0));
      toTest.remove(0);
      if (!toTest.isEmpty()) {
        cls = getMain(toTest);
      } else {
        cls = null;
        System.exit(1);
      }
    }
    return cls;
  }
  
  /**
   * Runs the main method of the test class
   */
  private static void runMain(ArrayList<String> toTest) {
    Class<?> cls = getMain(toTest);
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
   * Generates a list of test classes.
   * Add test cases to this list as you find them.
   */
  public ArrayList<String> getTestClasses() {
    ArrayList<String> toTest = new ArrayList<String>();
    toTest.add("osu.cse1223.Project02"); // Typical package
    toTest.add("osu.cse1223.project02");
    toTest.add("osu.cse1223.project2");
    toTest.add("cse1223.Project02");    
    toTest.add("cse1223.project02");
    toTest.add("Project02"); // Typical packageless
    toTest.add("project02"); // Typical unconventional packageless 
    toTest.add("project2"); // Atypical unconventional packageless
    return toTest;
  }
  
  /**
   * Removes all newlines and spaces, so strings can be
   * compared on a content basis.
   */
  public String reduceString(String input) {
    return input.replace("\n", "").replaceAll("\\s+", "");
  }
  
  /**
   * A helper method which allows us to rapidly build test cases.
   */
  public void runCase(String fullString, String substring, int position, String replacement) {
    String input = constructInput(fullString, substring, position, replacement);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    runMain(getTestClasses());
    String solution = buildSolution(fullString, substring, position, replacement);
    assertEquals(reduceString(solution), reduceString(outContent.toString()));
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
