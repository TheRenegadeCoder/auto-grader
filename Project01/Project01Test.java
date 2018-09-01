import junit.framework.TestCase;
import java.io.*;

/**
 * Tests the problem defined by the following URL:
 * // http://web.cse.ohio-state.edu/cse1223/currentsem/projects/CSE1223Project01.html
 * 
 * @author Jeremy Grifski
 */
public class Project01Test extends TestCase {
  
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  /**
   * Returns a string in "operand operator operand = result" format
   * 
   * @param firstVal some integer value
   * @param secondVal some integer value
   * @param operator some binary arithmetic operator (+, -, *, /, %)
   * @return a String in the form "firstVal operator secondVal = result"
   */
  public static String outputArithmetic(int firstVal, int secondVal, char operator) {
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
    
    return firstVal + " " + operator + " " + secondVal + " = " + result + System.lineSeparator();
  }
  
  /**
   * Returns some string in the form "The average of your two numbers is: average"
   * 
   * @param firstVal some integer value
   * @param secondVal some integer value
   * @return a string in the form "The average of your two numbers is: average"
   */
  public static String outputAverage(int firstVal, int secondVal) {
    int average = (firstVal + secondVal) / 2;
    return "The average of your two numbers is: " + average + System.lineSeparator();
  }
  
  /**
   * Returns the master string based on the problem defined above
   * 
   * @param firstNumber some integer value
   * @param secondNumber some integer value
   * @return the master string to be output as the solution
   */
  public static String generateOutput(int firstNumber, int secondNumber) {
    String output = "Enter the first number: " + System.lineSeparator();
    output += "Enter the second number: " + System.lineSeparator();
    char[] operators = {'+', '-', '*', '/', '%'};
    for(char operator : operators) {
      output += outputArithmetic(firstNumber, secondNumber, operator);
    }
    output += outputAverage(firstNumber, secondNumber);
    return output;
  }

  /**
   * A helper method for running test cases. Pass any two values, and it
   * generates the proper solution to the problem.
   * 
   * @param firstVal some integer value
   * @param secondVal some integer value
   */
  public void runCase(int firstVal, int secondVal) {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
    
    String data = firstVal + System.lineSeparator() + secondVal + System.lineSeparator();
    InputStream inContent = new ByteArrayInputStream(data.getBytes());
    System.setIn(inContent);
    
    Project01.main(new String[0]);

    assertEquals(generateOutput(firstVal, secondVal), outContent.toString());
    
    System.setIn(System.in);
    System.setOut(System.out);
  }
  
  /**
   * The first arbitrary test case
   */
  public void testMainCase1() {
    runCase(12, 3);
  }
  
  /**
   * The second arbitrary test case
   */
  public void testMainCase2() {
    runCase(5, 7);
  }
  
  /**
   * The third arbitrary test case
   */
  public void testMainCase3() {
    runCase(311, 14);
  }
  
}
