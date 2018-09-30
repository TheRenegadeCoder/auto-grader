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
public class Project05Test {

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
   * 
   * @param output the solution output
   * @return the expected output
   */
  private String buildSolution(String output) {
    int correctGuess = getCorrectGuess(output);
    ArrayList<String> solutionList = new ArrayList<String>();
    for (int i = 1; i < correctGuess; i++) {
      solutionList.add("Enter a guess between 1 and 200:");
      solutionList.add("Your guess was too low. Try again.");
    }
    solutionList.add("Enter a guess between 1 and 200:");
    solutionList.add("Congratulations!  Your guess was correct!");
    solutionList.add("I had chosen " + correctGuess + "as the target number.");
    solutionList.add("You guessed it in " + correctGuess + " tries.");
    solutionList.add(getSuccessLine(correctGuess));
    return String.join("\n", solutionList);
  }
  
  /**
   * Generates the success line based on the guess count.
   * 
   * @param guessCount the number of guesses
   * @return the success line
   */
  private String getSuccessLine(int guessCount) {
    if (guessCount == 1) {
      return "That was astounding!";
    } else if (guessCount >= 2 && guessCount <= 4) {
      return "That was lucky!";
    } else if (guessCount >= 5 && guessCount <= 6) {
      return "That was pretty good.";
    } else if (guessCount == 7) {
      return "That was not that impressive.";
    } else if (guessCount >= 8 && guessCount <= 9) {
      return "Are you sure this is the right game for you?";
    } else {
      return "This just isn't your game, is it?";
    }
  }
  
  /**
   * Extracts the correct guess from the output string.
   * 
   * @param output the output generated by the solution
   * @return the correct guess
   */
  private int getCorrectGuess(String output) {
    String digitsOnly = output.replaceAll("[^-?0-9]+", " "); 
    String[] digitsList = digitsOnly.trim().split(" ");
    int target = Integer.parseInt(digitsList[digitsList.length - 2]);
    return target;
  }
  
  /**
   * A helper method which allows us to rapidly build test cases.
   *
   * @param dragonType the dragon under test
   */
  private void runCase() {
    // Feed some input to scanner
    String[] numbers = IntStream.rangeClosed(1, 200).mapToObj(String::valueOf).toArray(String[]::new);
    String input = buildLines(numbers);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    
    // Run student solution
    runMain(getTestClasses(5));
    
    // Test expected output to output
    String output = outContent.toString();
    String expectedOutput = buildSolution(output);
    assertEquals(reduceString(expectedOutput), reduceString(output));
  }

  /**
   * Tests the basics.
   */
  @Test
  public void testManyTimes() {
      runCase();
  }
}
