import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Tests project 3 as specified by:
 * http://web.cse.ohio-state.edu/cse1223/currentsem/projects/CSE1223Project03.html
 * 
 * This test file verifies that the Project 3 solution passes on the basis of
 * content rather than structure. In other words, we don't care if the output
 * doesn't structurally look exactly like the expected output. However, we do
 * care that the solution has all the expected content.
 */
public class Project04Test {
  
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
   * An inner class used for storing math data.
   */
  private static class MathData {
    private int result;
    private String equation;
    
    /**
     * The MathData constructor.
     */
    public MathData(int result, String equation) {
      this.result = result;
      this.equation = equation;
    }
    
    /**
     * Gets the result of the equation.
     * 
     * @return the result of the equation as an integer
     */
    public int getResult() {
      return this.result;
    }
    
    /**
     * Gets the equation.
     * 
     * @return the equation as a string
     */
    public String getEquation() {
      return this.equation;
    }
  }
  
  /**
   * Generates 4 MathData objects.
   * 
   * @param first the first value of an equation
   * @param second the second value of an equation
   * @return an array of 4 MathData objects
   */
  private static MathData[] generateAllMathData(int first, int second) {
    MathData addition = generateMathData(first, second, '+');
    MathData multiplication = generateMathData(first, second, '*');    
    MathData division = generateMathData(first, second, '/');    
    MathData remainder = generateMathData(first, second, '%');
    return new MathData[] {addition, multiplication, division, remainder};
  }
  
  /**
   * Returns a MathData object.
   * 
   * @param firstVal some integer value
   * @param secondVal some integer value
   * @param operator some binary arithmetic operator (+, -, *, /, %)
   * @return a String in the form "firstVal operator secondVal = result"
   */
  private static MathData generateMathData(int firstVal, int secondVal, char operator) {
    int result = 0;
    
    switch (operator) {
      case '+':
        result = firstVal + secondVal;
        break;
      case '-':
        result = firstVal - secondVal;
        break;
      case '*':
        result = firstVal * secondVal;
        break;
      case '/':
        result = firstVal / secondVal;
        break;
      case '%':
        result = firstVal % secondVal;
        break;
      default: throw new ArithmeticException();
    }
    
    String equation = firstVal + " " + operator + " " + secondVal + " = " + System.lineSeparator();
    
    return new MathData(result, equation);
  }
  
  /**
   * Generates the solution for testing.
   * 
   * @param name the user's name
   * @param first the first number in each equation
   * @param second the second number in each equation
   * @return the expected solution string 
   */
  public String buildSolution(String name, int first, int second, int guess) {
    MathData[] equations = generateAllMathData(first, second);
    ArrayList<String> solutionList = new ArrayList<String>();
    solutionList.add("Enter your name: ");
    solutionList.add("Welcome " + name + "! Please answer the following questions:");
    int correct = 0;
    for (MathData equation : equations) {
      solutionList.add(equation.getEquation());
      if (equation.getResult() == guess) {
        solutionList.add("Correct!");
        correct++;
      } else {
        solutionList.add("Wrong!");
        solutionList.add("The correct answer is " + equation.getResult());
      }
    }
    solutionList.add("You got " + correct + " correct answers");
    solutionList.add("That's " + ((double) correct) / equations.length * 100 + "%!");
    return String.join("\n", solutionList);
  }
  
  /**
   * Takes a set of inputs and joins them with newlines.
   * 
   * @param inputs an variable length collection of strings
   * @return the input collection as a string separated by newlines
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
   * @return an ArrayList of strings to test
   */
  public ArrayList<String> getTestClasses(int project) {
    ArrayList<String> toTest = new ArrayList<String>();
    toTest.add("osu.cse1223.Project%1$s");
    toTest.add("osu.cse1223.Project%1$sa");
    toTest.add("osu.cse1223.CSEProject%1$s");
    toTest.add("cse1223.Project%1$sa");
    toTest.add("cse1223.Project%1$s");    
    toTest.add("project%1$s.Project%1$s");
    toTest.add("Project%1$s");
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
  public String reduceString(String input) {
    return input.replace("\n", "").replaceAll("\\s+", "");
  }
  
  /**
   * Parses the users solution for a pair of numbers
   * surrounding the addition equation. 
   * 
   * @param output the users solution
   * @return an array containing the first and second number from parsing
   */
  private int[] getIntegers(String output) {
    String digits = output.replaceAll("[^-?0-9]+", " "); 
    String[] integers = digits.trim().split(" ");
    int first = Integer.parseInt(integers[0]);
    int second = Integer.parseInt(integers[1]);
    return new int[] {first, second};
  }
  
  /**
   * A helper method which allows us to rapidly build test cases.
   * 
   * @param name the user's name
   * @param guess our guess for each question
   */
  public void runCase(String name, int guess) {
    String guessString = Integer.toString(guess);
    String input = buildLines(name, guessString, guessString, guessString, guessString);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    runMain(getTestClasses(4));
    int[] firstAndSecond = getIntegers(outContent.toString());
    String solution = buildSolution(name, firstAndSecond[0], firstAndSecond[1], guess);
    assertEquals(reduceString(solution), reduceString(outContent.toString()));
  }
  
  /**
   * Tests a random guess of 2 for every answer
   */
  @Test
  public void testMain01() {
    runCase("Jeremy", 2);
  }
  
  /**
   * Tests a random guess of 5 for every answer
   */
  @Test
  public void testMain02() {
    runCase("Anastasia", 5);
  }
  
  /**
   * Tests a random guess of 0 for every answer
   */
  @Test
  public void testMain03() {
    runCase("Anastasia", 0);
  }
}
