import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

/**
 * Tests project 5 as specified by:
 * http://web.cse.ohio-state.edu/cse1223/currentsem/projects/CSE1223Project05.html
 *
 * This test file verifies that the Project 5 solution passes on the basis of
 * content rather than structure. In other words, we don't care if the output
 * doesn't structurally look exactly like the expected output. However, we do
 * care that the solution has all the expected content.
 */
public class Project06Test {
  
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
   * Takes a set of inputs and joins them with newlines.
   *
   * @param inputs an variable length collection of strings
   * @return the input collection as a string separated by newlines
   */
  private String buildLines(String ... inputs) {
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
   * Runs the main method of the test class.
   *
   * @param toTest an array of strings to test
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
   *
   * @param project the current project number
   * @return an ArrayList of strings to test
   */
  private ArrayList<String> getTestClasses(int project) {
    ArrayList<String> toTest = new ArrayList<String>();
    toTest.add("osu.cse1223.Project%1$s");
    toTest.add("osu.cse1223.Project%1$sa");
    toTest.add("osu.cse1223.CSEProject%1$s");
    toTest.add("cse1223.Project%1$sa");
    toTest.add("cse1223.Project%1$s");
    toTest.add("project%1$s.Project%1$s");
    toTest.add("Project%1$s");
    toTest.add("osu.cse1223.DragonsGame");
    toTest.add("Project04.DragonTrainers");
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
   * A helper method for converting characters to integers
   */
  private int convertCharToInt(char someChar) {
    return ((int) someChar) - 48;
  }
  
  /**
   * A helper method for generating the valid string.
   */
  private String getValidString(int expectedCheckDigit, int actualCheckDigit) {
    boolean verified = expectedCheckDigit == actualCheckDigit;
    if (verified) {
      return "Number is valid";
    } else {
      return "Number is not valid";
    }
  }
  
  /**
   * A helper method for getting the expected check digit
   */
  private int getExpectedCheckDigit(String creditCardNumber) {
    int sum = 0;
    for(int i = creditCardNumber.length() - 2; i > -1; i--) {
      int curr = convertCharToInt(creditCardNumber.charAt(i));
      if (i % 2 == 0) {
        curr *= 2;
        if (curr > 9) {
          curr -= 9;
        }
      } 
      sum += curr;
    }
    return 10 - (sum % 10);
  }
  
  /**
   * Generates the expected output for testing.
   */
  private String buildSolution(String... creditCardNumbers) {
    ArrayList<String> solutionList = new ArrayList<String>();
    for (String creditCardNumber : creditCardNumbers) {
      solutionList.add("Enter a credit card number (enter a blank line to quit):");
      if (creditCardNumber.equals("\n")) {
        solutionList.add("Goodbye!");
      } else if (creditCardNumber.length() != 16) {
        solutionList.add("ERROR! Number MUST have exactly 16 digits");
      } else {
        int expectedCheckDigit = getExpectedCheckDigit(creditCardNumber);
        int actualCheckDigit = convertCharToInt(creditCardNumber.charAt(15));
        solutionList.add("Check digit should be: " + expectedCheckDigit);
        solutionList.add("Check digit is: " + actualCheckDigit);
        solutionList.add(getValidString(expectedCheckDigit, actualCheckDigit));
      }
    }
    return String.join("\n", solutionList);
  }
  
  /**
   * A helper method which allows us to rapidly build test cases.
   */
  private void runCase(String... creditCardNumbers) {
    String input = buildLines(creditCardNumbers);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    
    // Run student solution
    runMain(getTestClasses(6));
    
    // Test expected output to output
    String output = outContent.toString();
    String expectedOutput = buildSolution(creditCardNumbers);
    assertEquals(reduceString(expectedOutput), reduceString(output));
  }
  
  /**
   * Tests a valid credit card.
   */
  @Test
  public void testValidCreditCard() {
    runCase("5457623898234113", "\n");
  }
  
  /**
   * Tests an invalid credit card.
   */
  @Test
  public void testInvalidCreditCard() {
    runCase("5457623898234112", "\n");
  }
  
  /**
   * Tests an incomplete credit card.
   */
  @Test
  public void testIncompleteCreditCard() {
    runCase("545762389823", "\n");
  }
  
  /**
   * Tests multiple credit cards.
   */
  @Test
  public void testMultipleCreditCards() {
    runCase("5457623898234113", "5555555555554444", "\n");
  }
}
