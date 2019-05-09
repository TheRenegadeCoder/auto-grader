# auto-grader

The auto-grader tool is a Python script which unpacks, tests, and grades student Java programs. 

## Usage

To use one of the test files, I typically download all student student submissions and run them
using the Python tool. The tool uses the JUnit projects to test each student's solution before
dumping the results to a JSON file.

To make testing easier, I don't deal with solution structure. Instead, I focus on solution content.
In other words, when I test, I remove all whitespace including newlines before comparison. This allows
me to verify that each student solution is at least semantically correct. I don't personally care
to nitpick on structure.

Before grading, I typically open one solution at a time and look them over for style. 
If necessary, I'll launch [Dr. Java][1] and run the file by hand.

## Report Format

Old report format:

```
+++++++++++++++ student name ++++++++++++++++

C:/Users/Jerem/Downloads\Dump\student name\Project04\src\osu\cse1223\Project04.java


JUnit version 4.13-beta-2
..........E....E.E.
Time: 0.088
There were 3 failures:
1) testAllCapsFire(Project04Test)
org.junit.ComparisonFailure: expected:<...ire/plant/water]:you[chose:firedragonichose:waterdragonwaterdefeatsfire-youlose]!> but was:<...ire/plant/water]:you[don'thaveafiredragon,soyouchoosenodragons.ichose:waterdragonyoulosebydefault]!>
	at org.junit.Assert.assertEquals(Assert.java:117)
	at org.junit.Assert.assertEquals(Assert.java:146)
	at Project04Test.runCase(Project04Test.java:268)
	at Project04Test.testAllCapsFire(Project04Test.java:308)
2) testAllCapsPlant(Project04Test)
org.junit.ComparisonFailure: expected:<...ire/plant/water]:you[chose:plantdragonichose:plantdragonatie]!> but was:<...ire/plant/water]:you[don'thaveaplantdragon,soyouchoosenodragons.ichose:plantdragonyoulosebydefault]!>
	at org.junit.Assert.assertEquals(Assert.java:117)
	at org.junit.Assert.assertEquals(Assert.java:146)
	at Project04Test.runCase(Project04Test.java:268)
	at Project04Test.testAllCapsPlant(Project04Test.java:388)
3) testAllCapsWater(Project04Test)
org.junit.ComparisonFailure: expected:<...ire/plant/water]:you[chose:waterdragonichose:waterdragonatie]!> but was:<...ire/plant/water]:you[don'thaveawaterdragon,soyouchoosenodragons.ichose:waterdragonyoulosebydefault]!>
	at org.junit.Assert.assertEquals(Assert.java:117)
	at org.junit.Assert.assertEquals(Assert.java:146)
	at Project04Test.runCase(Project04Test.java:268)
	at Project04Test.testAllCapsWater(Project04Test.java:348)

FAILURES!!!
Tests run: 16,  Failures: 3
```

Current log format:

```json
{
    "students": {
        "student": {
            "path": "C:/Users/Jerem/Downloads\\Dump\\student\\Project04\\src\\osu\\cse1223\\Project04.java",
            "run_status": "SUCCESS",
            "solution": [
                "/*\n",
                " * This code will create a game between the system and the user.  The user will be prompted to choose one of three dragons to go up against the computer.\n",
                " * @author: student\n",
                " * Date Due: February 7th, 2019\n",
                " */\n",
                "\n",
                "package osu.cse1223;\n",
                "\n",
                "import java.util.Scanner;\n",
                "\n",
                "public class Project04 {\n",
                "\n",
                "\tpublic static void main(String[] args) {\n",
                "\t\t\n",
                "\t\tScanner keyboard = new Scanner(System.in);\n",
                "\t\tSystem.out.print(\"Please select one of your dragons [Fire/Plant/Water]: \");\n",
                "\t\tString yourDragon = keyboard.nextLine();\n",
                "\t\tString myDragon = \"\";\n",
                "\t\t\n",
                "\t\tif (yourDragon.equals(\"F\") || yourDragon.equals(\"f\") || yourDragon.equals(\"Fire\") || yourDragon.equals(\"fire\"))\n",
                "\t\t{\n",
                "\t\t\tyourDragon = \"Fire\";\n",
                "\t\t\tSystem.out.println(\"You chose: \" + yourDragon + \" dragon\");\n",
                "\t\t}\n",
                "\t\telse if (yourDragon.equals(\"P\") || yourDragon.equals(\"p\") || yourDragon.equals(\"Plant\") || yourDragon.equals(\"plant\"))\n",
                "\t\t{\n",
                "\t\t\tyourDragon = \"Plant\";\n",
                "\t\t\tSystem.out.println(\"You chose: \" + yourDragon + \" dragon\");\n",
                "\t\t}\n",
                "\t\telse if (yourDragon.equals(\"W\") || yourDragon.equals(\"w\") || yourDragon.equals(\"Water\") || yourDragon.equals(\"water\"))\t\t\n",
                "\t\t{\n",
                "\t\t\tyourDragon = \"Water\";\n",
                "\t\t\tSystem.out.println(\"You chose: \" + yourDragon + \" dragon\");\n",
                "\t\t}\n",
                "\t\telse\n",
                "\t\t{\n",
                "\t\t\tSystem.out.println(\"You don't have a \" + yourDragon + \" dragon, so you choose no dragons.\");\n",
                "\t\t}\n",
                "\t\t\n",
                "\t\tint random = (int)(3 * Math.random()) +1;\n",
                "\t\t\n",
                "\t\tif (random == 1)\n",
                "\t\t{\n",
                "\t\t\tmyDragon = \"Fire\";\n",
                "\t\t\tSystem.out.println(\"I chose: \" + myDragon + \" dragon\");\n",
                "\t\t}\n",
                "\t\tif (random == 2)\n",
                "\t\t{\n",
                "\t\t\tmyDragon = \"Plant\";\n",
                "\t\t\tSystem.out.println(\"I chose: \" + myDragon + \" dragon\");\n",
                "\t\t}\n",
                "\t\tif (random == 3)\n",
                "\t\t{\n",
                "\t\t\tmyDragon = \"Water\";\n",
                "\t\t\tSystem.out.println(\"I chose: \" + myDragon + \" dragon\");\n",
                "\t\t}\n",
                "\t\tString lose = \"you lose!\";\n",
                "\t\tString win = \"you win!\";\n",
                "\t\t\n",
                "\t\tif (yourDragon.equals(myDragon))\n",
                "\t\t{\n",
                "\t\t\tSystem.out.println(\"A Tie!\");\n",
                "\t\t}\n",
                "\t\telse if (yourDragon.equals(\"Fire\") && myDragon.equals(\"Water\"))\n",
                "\t\t{\n",
                "\t\t\tSystem.out.println(\"Water defeats Fire - \" + lose);\n",
                "\t\t}\n",
                "\t\telse if (yourDragon.equals(\"Water\") && myDragon.equals(\"Plant\"))\n",
                "\t\t{\n",
                "\t\t\tSystem.out.println(\"Plant defeats Water - \" + lose);\n",
                "\t\t}\n",
                "\t\telse if (yourDragon.equals(\"Plant\") && myDragon.equals(\"Fire\"))\n",
                "\t\t{\n",
                "\t\t\tSystem.out.println(\"Fire defeats Plant - \" + lose);\n",
                "\t\t}\n",
                "\t\telse if (yourDragon.equals(\"Fire\") && myDragon.equals(\"Plant\"))\n",
                "\t\t{\n",
                "\t\t\tSystem.out.println(\"Fire defeats Plant - \" + win);\n",
                "\t\t}\n",
                "\t\telse if (yourDragon.equals(\"Water\") && myDragon.equals(\"Fire\"))\n",
                "\t\t{\n",
                "\t\t\tSystem.out.println(\"Water defeats Fire - \" + win);\n",
                "\t\t}\n",
                "\t\telse if (yourDragon.equals(\"Plant\") && myDragon.equals(\"Water\"))\n",
                "\t\t{\n",
                "\t\t\tSystem.out.println(\"Plant defeats Water - \" + win);\n",
                "\t\t}\n",
                "\t\telse if (!yourDragon.equals(\"Fire\") || !yourDragon.equals(\"Plant\") || !yourDragon.equals(\"Water\"))\n",
                "\t\t{\n",
                "\t\t\tSystem.out.println(\"You lose by default!\");\n",
                "\t\t}\n",
                "\t\t\n",
                "\t\n",
                "\t}\n",
                "\n",
                "}\n"
            ],
            "compilation_stdout": "",
            "compilation_stderr": "",
            "execution_stdout": {
                "failed_test_cases": {
                    "testAllCapsFire(Project04Test)": {
                        "trace": [
                            "at org.junit.Assert.assertEquals(Assert.java:117)",
                            "at org.junit.Assert.assertEquals(Assert.java:146)",
                            "at Project04Test.runCase(Project04Test.java:268)",
                            "at Project04Test.testAllCapsFire(Project04Test.java:308)"
                        ],
                        "expected": "<...ire/plant/water]:you[chose:firedragonichose:plantdragonfiredefeatsplant-youwin]!>",
                        "was": "<...ire/plant/water]:you[don'thaveafiredragon,soyouchoosenodragons.ichose:plantdragonyoulosebydefault]!>"
                    },
                    "testAllCapsPlant(Project04Test)": {
                        "trace": [
                            "at org.junit.Assert.assertEquals(Assert.java:117)",
                            "at org.junit.Assert.assertEquals(Assert.java:146)",
                            "at Project04Test.runCase(Project04Test.java:268)",
                            "at Project04Test.testAllCapsPlant(Project04Test.java:388)"
                        ],
                        "expected": "<...ire/plant/water]:you[chose:plantdragonichose:waterdragonplantdefeatswater-youwin]!>",
                        "was": "<...ire/plant/water]:you[don'thaveaplantdragon,soyouchoosenodragons.ichose:waterdragonyoulosebydefault]!>"
                    },
                    "testAllCapsWater(Project04Test)": {
                        "trace": [
                            "at org.junit.Assert.assertEquals(Assert.java:117)",
                            "at org.junit.Assert.assertEquals(Assert.java:146)",
                            "at Project04Test.runCase(Project04Test.java:268)",
                            "at Project04Test.testAllCapsWater(Project04Test.java:348)"
                        ],
                        "expected": "<...ire/plant/water]:you[chose:waterdragonichose:waterdragonatie]!>",
                        "was": "<...ire/plant/water]:you[don'thaveawaterdragon,soyouchoosenodragons.ichose:waterdragonyoulosebydefault]!>"
                    }
                },
                "junit_version": "4.13-beta-2",
                "time": 0.112,
                "failure_count": 3,
                "success_count": 13
            },
            "execution_stderr": "",
            "grade_estimate": 8.125
        }
    },
    "successful_runs": 17,
    "failing_runs": 19,
    "passed_test_cases": 75,
    "failed_test_cases": 197,
    "skipped_test_cases": 304
}
```

[1]: http://www.drjava.org/
