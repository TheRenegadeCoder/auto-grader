import org.junit.Before;

import com.sun.corba.se.spi.orbutil.fsm.TestAction1;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import java.io.*;
import java.util.*;

/**
 * Tests project 9 as specified by:
 * http://web.cse.ohio-state.edu/cse1223/currentsem/projects/CSE1223Project09.html
 *
 * This test file verifies that the Project 9 solution passes on the basis of
 * content rather than structure. In other words, we don't care if the output
 * doesn't structurally look exactly like the expected output. However, we do
 * care that the solution has all the expected content.
 */
public class Project08Test {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private static final int PROJECT_NUMBER = 9;
  private static Class<?> cls = null;

  /**
   * Gets the class name through trial and error and assigns it
   * permanently for the duration of testing.
   */
  @BeforeClass
  public static void setUpOnce() {
    cls = getClass(getTestClasses(PROJECT_NUMBER));
  }

  /**
   * Sets input and output streams to local print streams for analysis.
   */
  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent));
  }

  /**
   * Sets input and output streams back to normal.
   */
  @After
  public void tearDown() {
    System.setOut(System.out);
  }

  /**
   * Takes a set of inputs and joins them with newlines.
   *
   * @param inputs an variable length collection of strings
   * @return the input collection as a string separated by newlines
   */
  private static String buildLines(String ... inputs) {
    StringBuilder sb = new StringBuilder();
    for (String input: inputs) {
      sb.append(input);
      sb.append(System.lineSeparator());
    }
    return sb.toString();
  }

  /**
   * A recursive method which returns the main method from the proper class.
   *
   * @param toTest an ArrayList of strings to test
   * @return the class object
   */
  private static Class<?> getClass(ArrayList<String> toTest) {
    if (cls == null) {
      try {
        cls = Class.forName(toTest.get(0));
      } catch (ClassNotFoundException e) {
        System.err.println("Failed to find the class: " + toTest.get(0));
        toTest.remove(0);
        if (!toTest.isEmpty()) {
          cls = getClass(toTest);
        } else {
          cls = null;
          System.exit(1);
        }
      }
    }
    return cls;
  }

  /**
   * A generic method for running static methods using reflection.
   */
  public static Object runStaticMethod(String methodName, Class<?>[] parameters, Object[] args) {
    Object returnValue = null;
    try {
      Method meth = cls.getDeclaredMethod(methodName, parameters);
      meth.setAccessible(true);
      String[] params = null;
      returnValue = meth.invoke(null, args);
    } catch (NoSuchMethodException e) {
      System.err.println("No method " + methodName + " for class " + cls.getName());
      System.exit(1);
    } catch (IllegalAccessException e) {
      System.err.println("Can't invoke method " + methodName);
      System.exit(1);
    } catch (InvocationTargetException e) {
      System.err.println("Can't target method " + methodName);
      System.exit(1);
    }
    return returnValue;
  }

  /**
   * Runs the main method of the test class.
   *
   * @param toTest an array of strings to test
   */
  private void runMain(ArrayList<String> toTest) {
    Class<?>[] parameters = {String[].class};
    Object[] args = {null};
    runStaticMethod("main", parameters, args);
  }

  /**
   * Generates a list of test classes.
   * Add test cases to this list as you find them.
   *
   * @param project the current project number
   * @return an ArrayList of strings to test
   */
  private static ArrayList<String> getTestClasses(int project) {
    ArrayList<String> toTest = new ArrayList<String>();
    toTest.add("osu.cse1223.Project%1$s");
    toTest.add("osu.cse1223.Project%1$sa");
    toTest.add("osu.cse1223.CSEProject%1$s");
    toTest.add("cse1223.Project%1$sa");
    toTest.add("cse1223.Project%1$s");
    toTest.add("project%1$s.Project%1$s");
    toTest.add("Project%1$s");
    toTest.add("Project%1$sA");
    toTest.add("osu.cse1223.DragonsGame");
    toTest.add("Project04.DragonTrainers");
    toTest.add("Main");
    String projectNumberWhole = Integer.toString(project);
    String projectNumberPad = "0" + projectNumberWhole;
    int originalSize = toTest.size();
    for (int i = 0; i < originalSize; i++) {
      String test = toTest.get(i);
      toTest.set(i, String.format(test, projectNumberPad));
      toTest.add(String.format(test, projectNumberWhole));
      toTest.add(String.format(test, projectNumberPad).toLowerCase());
      toTest.add(String.format(test, projectNumberWhole).toLowerCase());
    }
    return toTest;
  }

  /**
   * Removes all newlines and spaces, so strings can be
   * compared on a content basis.
   *
   * @param input an input string
   * @return an input string stripped of all spaces and newlines
   */
  private String reduceString(String input) {
    return input.replace("\n", "").replaceAll("\\s+", "").toLowerCase();
  }

  /////////////////// Implementation //////////////////////////////////

  /**
   * Gets the decimal from binary.
   */
  private String getDecimal(String binary) {
    try {
      int decimal = 0;
      for (int i = binary.length(), int pow = 0; i >= 0; i--, pow++) {
        int digit = Integer.parseInt(binary.charAt(i));
        decimal += Math.pow(digit, pow);
      }
      return String.format("The binary value %s is %d in decimal.", binary, decimal);
    } catch (Exception e) {
      return "ERROR - value must be non-negative and contain only digits";
    }
  }

  /**
   * Generates the expected output for testing.
   */
  private String buildSolution(String... numbers) {
    ArrayList<String> solutionList = new ArrayList<String>();
    solutionList.add("Converting from Binary to Decimal");
    for (String number : numbers) {
      solutionList.add("Enter an integer value (negative value to quit):");
      if (Integer.parseInt(number) == -1) {
        break;
      } else {
        solutionList.add(getDecimal(number));
      }
    }
    solutionList.add("Goodbye!");
    return String.join("\n", solutionList);
  }

  /**
   * A helper method for testing main.
   */
  private void runMainCase(String... numbers) {
    String input = buildLines(numbers);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    // Run student solution
    runMain(getTestClasses(PROJECT_NUMBER));

    // Test expected output to output
    String output = outContent.toString();
    String expectedOutput = buildSolution(numbers);
    assertEquals(reduceString(expectedOutput), reduceString(output));
  }

  /**
   * Tests that binary to decimal conversion works.
   */
  private void runBinaryToDecimalCase(int expectedResult, String value) {
    Class<?>[] parameters = {String.class};
    Object[] args = {value};
    int result = (int) runStaticMethod("binaryToDecimal", parameters, args);
    assertEquals(expectedResult, result);
  }

  /**
   * Tests the valid check for binary.
   */
  private void runCheckForBinaryValueCase(boolean expectedResult, String value) {
    Class<?>[] parameters = {String.class};
    Object[] args = {value};
    boolean result = (boolean) runStaticMethod("checkForBinaryValue", parameters, args);
    assertEquals(expectedResult, result);
  }

  /**
   * Tests the prompt for binary method.
   */
  private void runPromptForBinaryCase(String expectedResult, String value) {
    Class<?>[] parameters = {Scanner.class};
    Object[] args = {new Scanner(System.in)};
    String result = (String) runStaticMethod("promptForBinary", parameters, args);
    assertEquals(expectedResult, result);
  }

  @Test
  public void testBinaryTen() {
    runBinaryToDecimalCase(10, "1010");
  }

  @Test
  public void testBinaryZero() {
    runBinaryToDecimalCase(0, "0");
  }

  @Test
  public void testBinaryOne() {
    runBinaryToDecimalCase(1, "1");
  }

  @Test
  public void testBinaryInvalid() {
    runCheckForBinaryValueCase(false, "1234");
  }

  @Test
  public void testBinaryValid() {
    runCheckForBinaryValueCase(true, "1010");
  }

  @Test
  public void testPrompt() {
    runPromptForBinaryCase("111", "111");
  }

  @Test
  public void testMain() {
    runMainCase("101", "-1");
  }
}
