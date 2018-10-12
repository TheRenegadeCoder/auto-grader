import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Test;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

/**
 * Tests project 7 as specified by:
 * http://web.cse.ohio-state.edu/cse1223/currentsem/projects/CSE1223Project07.html
 *
 * This test file verifies that the Project 7 solution passes on the basis of
 * content rather than structure. In other words, we don't care if the output
 * doesn't structurally look exactly like the expected output. However, we do
 * care that the solution has all the expected content.
 */
public class Project07Test {
  
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private static final int PROJECT_NUMBER = 7;
  private static Class<?> cls = null;
  
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
   * Generates the expected output for testing.
   */
  private String buildSolution(String... creditCardNumbers) {
    ArrayList<String> solutionList = new ArrayList<String>();
    solutionList.add("You have 100 dollars.");
    solutionList.add("Enter an amount to bet (0 to quit):");
    solutionList.add("You have 100 dollars left.");
    solutionList.add("Goodbye!");
    return String.join("\n", solutionList);
  }
  
  /**
   * A helper method for testing main.
   */
  private void runMainCase(String... guessesAndBets) {
    String input = buildLines(guessesAndBets);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    
    // Run student solution
    runMain(getTestClasses(PROJECT_NUMBER));
    
    // Test expected output to output
    String output = outContent.toString();
    String expectedOutput = buildSolution();
    assertEquals(reduceString(expectedOutput), reduceString(output));
  }
  
  /**
   * A helper method for testing getRoll.
   */
  private int runGetRollCase() {
    Class<?>[] parameters = null;
    Object[] args = null;
    return (Integer) runStaticMethod("getRoll", parameters, args);
  }
  
  /**
   * A helper method for testing getBet.
   */
  private int runGetBetCase(int pool, String... bets) {
    String input = buildLines(bets);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    Class<?>[] parameters = {Scanner.class, int.class};
    Object[] args = {new Scanner(System.in), pool};
    return (Integer) runStaticMethod("getBet", parameters, args);
  }
  
  /**
   * A helper method for testing getHighLow.
   */
  private char runGetHighLow(String... selections) {
    String input = buildLines(selections);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    Class<?>[] parameters = {Scanner.class};
    Object[] args = {new Scanner(System.in)};
    return (Character) runStaticMethod("getHighLow", parameters, args);
  }
  
  /**
   * A helper method for testing determineWinnings.
   */
  private int runDetermineWinnings(char highLow, int bet, int roll) {
    Class<?>[] parameters = {char.class, int.class, int.class};
    Object[] args = {highLow, bet, roll};
    return (Integer) runStaticMethod("determineWinnings", parameters, args);
  }
  
  @Test
  public void testGetRoll() {
    int result = runGetRollCase();
    assertTrue("getRoll returned a value out of the range: " + result, 1 <= result && result <= 6);
  }
  
  @Test
  public void testGetBetValid() {
    int result = runGetBetCase(100, "50");
    assertEquals(50, result);
  }
  
  @Test
  public void testGetBetTooBig() {
    int result = runGetBetCase(50, "100", "75", "40");
    assertEquals(40, result);
  }
  
  @Test
  public void testGetBetNegative() {
    int result = runGetBetCase(50, "-100", "-75", "40");
    assertEquals(40, result);
  }
  
  @Test
  public void testHighLowH() {
    char result = runGetHighLow("H");
    assertEquals('H', result);
  }
  
  @Test
  public void testHighLowS() {
    char result = runGetHighLow("S");
    assertEquals('S', result);
  }
  
  @Test
  public void testHighLowL() {
    char result = runGetHighLow("L");
    assertEquals('L', result);
  }
  
  @Test
  public void testHighLowInvalid() {
    char result = runGetHighLow("G", "L");
    assertEquals('L', result);
  }
  
  @Test
  public void testDetermineWinningsHighRight() {
    int winnings = runDetermineWinnings('H', 100, 10);
    assertEquals(100, winnings);
  }
  
  @Test
  public void testDetermineWinningsHighWrong() {
    int winnings = runDetermineWinnings('H', 100, 3);
    assertEquals(-100, winnings);
  }
  
  @Test
  public void testDetermineWinningsLowRight() {
    int winnings = runDetermineWinnings('L', 100, 2);
    assertEquals(100, winnings);
  }
  
  @Test
  public void testDetermineWinningsLowWrong() {
    int winnings = runDetermineWinnings('L', 100, 10);
    assertEquals(-100, winnings);
  }
  
  @Test
  public void testDetermineWinningsSevensRight() {
    int winnings = runDetermineWinnings('S', 100, 7);
    assertEquals(400, winnings);
  }
  
  @Test
  public void testDetermineWinningsSevensWrong() {
    int winnings = runDetermineWinnings('S', 100, 10);
    assertEquals(-100, winnings);
  }
}
