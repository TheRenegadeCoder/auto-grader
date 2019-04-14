import os
import pathlib
import zipfile
import subprocess
import json
import re
from tkinter import filedialog

ARCHIVE = "Archives"
DUMP = "Dump"


def extract_main_zip() -> str:
    """
    Extracts an archive given by the user.
    :return: the path to the unzipped archive
    """
    archive_name = filedialog.askopenfilename(
        title="Select Zip File",
        filetypes=(("zip files", "*.zip"), ("all files", "*.*"))
    )
    archive = zipfile.ZipFile(archive_name)
    archive_path = os.path.join(os.path.dirname(archive_name), ARCHIVE)
    archive.extractall(archive_path)
    archive.close()
    return archive_path


def extract_solutions():
    """
    Extracts user folders.
    :return: the path to the extraction site
    """
    unzipped_archive = extract_main_zip()

    dump = os.path.join(os.path.dirname(unzipped_archive), DUMP)
    pathlib.Path(dump).mkdir(parents=True, exist_ok=True)

    for file in os.listdir(unzipped_archive):
        file_name = os.fsdecode(file)
        file_path = os.path.join(unzipped_archive, file_name)
        file_path_plus_name = os.path.join(dump, file_name.split("_")[0])
        if file_name.endswith(".zip"):
            zip_file = zipfile.ZipFile(file_path, "r")
            zip_file.extractall(file_path_plus_name)
            zip_file.close()
        else:
            name = file_name.split("_")[0]
            project = file_name.split("_")[-1]
            pathlib.Path(os.path.join(dump, name)).mkdir(parents=True, exist_ok=True)
            new_file_path = os.path.join(dump, name, project)
            os.rename(file_path, new_file_path)

    return dump


def compile_junit(classes: str, classpath: str, test_file: str) -> subprocess.CompletedProcess:
    """
    Runs the java compilation command.
    :param classes: a directory of classes under test
    :param classpath: a list of dependencies
    :param test_file: a test file to be compiled
    :return: the completed process object after compilation
    """
    command = "javac -d \"%s\" -cp \"%s\". \"%s\"" % (classes, classpath, test_file)
    return run_command(command)


def test_junit(classes: str, classpath: str, test_class: str) -> subprocess.CompletedProcess:
    """
    Runs the java execution command.
    :param classes: a directory of classes under test
    :param classpath: a list of dependencies
    :param test_class: a test file to be executed
    :return: the completed process object after execution
    """
    command = "java -cp \"%s;%s\". org.junit.runner.JUnitCore %s" % (classes, classpath, test_class)
    return run_command(command)


def run_command(command: str) -> subprocess.CompletedProcess:
    """
    Runs a system command.
    :param command: a string command
    :return: the completed process object after execution5
    """
    try:
        result = subprocess.run(
            command,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            timeout=60  # 1 minute
        )
    except subprocess.TimeoutExpired as e:
        result = subprocess.CompletedProcess(command, 27, stdout=e.stdout, stderr=e.stderr)
    return result


def grade_file(classes: str, build_file: str, test_class: str) -> dict:
    """
    Grades a file.
    :param classes: a directory contain files under test
    :param build_file: a file to test
    :param test_class: the path to the test file
    :return: None
    """
    classpath = "C:\\Program Files\\JUnit\\junit-4.13-beta-2.jar;C:\\Program Files\\JUnit\\hamcrest-all-1.3.jar;"

    compile_junit(classes, classpath, build_file)
    compilation_results = compile_junit(classes, classpath, test_class)
    execution_results = test_junit(classes, classpath, get_test_name(test_class))
    return generate_student_json(compilation_results, execution_results, build_file)


def generate_student_json(compilation_results: subprocess.CompletedProcess,
                          execution_results: subprocess.CompletedProcess, build_file: str) -> dict:
    """
    Generates the json solution.
    :param compilation_results: the compilations results process
    :param execution_results: the execution results process
    :param build_file: path to solution
    :return: the data dictionary
    """
    output_dict = dict()
    output_dict["path"] = build_file
    output_dict["solution"] = read_solution(build_file)
    output_dict["compilation_stdout"] = compilation_results.stdout.decode("utf-8")
    output_dict["compilation_stderr"] = compilation_results.stderr.decode("utf-8")
    output_dict["execution_stderr"] = execution_results.stderr.decode("utf-8")
    if execution_results.returncode == 27:
        output_dict["run_status"] = "FAILURE"
        output_dict["execution_stdout"] = execution_results.stdout.decode("utf-8")
        output_dict["grade_estimate"] = 0
    else:
        raw_test_results = execution_results.stdout.decode("utf-8").splitlines()
        output_dict["run_status"] = "SUCCESS" if len(raw_test_results) > 2 else "FAILURE"
        output_dict["execution_stdout"] = parse_test_results(raw_test_results)
        output_dict["grade_estimate"] = calculate_grade(output_dict)
    return output_dict


def calculate_grade(student: dict) -> float:
    """
    Gives a grade estimate for the student.
    :param student: a student's data
    :return: a grade estimate
    """
    if student["run_status"] == "FAILURE":
        grade_estimate = 0
    else:
        test_data = student["execution_stdout"]
        failed_tests = test_data["failure_count"]
        passed_tests = test_data["success_count"]
        total_tests = failed_tests + passed_tests
        try:
            grade_estimate = (passed_tests / total_tests) * 10
        except ZeroDivisionError:
            grade_estimate = 0
    return grade_estimate


def read_solution(solution_path):
    """
    Reads the solution and returns it as a list of lines.
    :param solution_path: path to the solution
    :return: the solution as a list of lines
    """
    with open(solution_path, encoding="utf8") as solution:
        data = solution.readlines()
    return data


def parse_test_results(raw_test_results: list) -> dict:
    """
    Parses a list of test results.
    :param raw_test_results: a list of test results
    :return: the results as a dictionary
    """
    test_results = dict()
    test_results["failed_test_cases"] = dict()
    i = 0
    while i < len(raw_test_results):
        line = raw_test_results[i]
        if "version" in line:
            test_results["junit_version"] = line.split()[-1]
        elif "Time:" in line:
            test_results["time"] = float(line.split()[-1])
        elif "Failures:" in line:
            fails = int(line.split()[-1])
            successes_string = line.split()[2]
            successes = int(successes_string[:successes_string.index(",")]) - fails
            test_results["failure_count"] = fails
            test_results["success_count"] = successes
        elif "OK (" in line:  # Passed all tests
            test_results["failure_count"] = 0
            test_results["success_count"] = int(line.split()[1][1:])
        elif re.search(r"\d+\) ", line):
            failed_test_cases = test_results["failed_test_cases"]
            i = parse_test_cases(raw_test_results, failed_test_cases, i)
        i += 1

    return test_results


def parse_test_cases(raw_test_results: list, failed_test_cases: dict, index: int) -> int:
    """
    Parses a test case.

    :param raw_test_results: the list of test results by line
    :param failed_test_cases: the failed test cases dictionary
    :param index: the current index into the raw test results
    :return: the index at the end of execution
    """
    test_case = raw_test_results[index].split()[-1]
    failed_test_cases[test_case] = dict()
    failed_test_cases[test_case]["trace"] = list()
    line = raw_test_results[index]
    next_failed_index = str(int(line[:line.find(")")]) + 1)
    while len(line) != 0 and line[:line.find(")")] != next_failed_index:
        if "\t" in line:
            failed_test_cases[test_case]["trace"].append(line.replace("\t", ""))
        elif "expected" in line:
            message = line[:line.find("expected")]
            expected = line[line.find("expected:") + 9: line.find("but")]
            was = line[line.find("was:") + 4:]
            failed_test_cases[test_case]["message"] = message
            failed_test_cases[test_case]["expected"] = expected
            failed_test_cases[test_case]["was"] = was
        index += 1
        line = raw_test_results[index]
    return index - 1


def write_to_file(results, grade_report: dict):
    """
    Writes results to a file.
    :param results: the open file reference
    :param grade_report: a list of grades
    :return: None
    """
    json.dump(grade_report, results, indent=4)


def automate_grading(root: str):
    """
    Grades all files for a project.
    :param root: the root directory for all the folders
    :return: None
    """
    test_class = filedialog.askopenfilename(
        title="Select Test File",
        filetypes=(("java files", "*.java"), ("all files", "*.*"))
    )
    test_dir = os.path.join(root, "Test")
    os.mkdir(test_dir)
    grade_report = dict()
    grade_report["students"] = dict()
    json_file = get_test_name(test_class) + ".json"
    with open(os.path.join(root, json_file), "w") as results:
        for subdir, dirs, files in os.walk(os.path.join(root, DUMP)):
            java_files = [name for name in files if ".java" in name and "module-info" not in name]
            for file_name in java_files:
                file_path = os.path.join(subdir, file_name)
                author_name = get_author_name(file_path)
                classes = os.path.join(test_dir, author_name, file_name.split(".")[0])
                pathlib.Path(classes).mkdir(parents=True, exist_ok=True)
                student_grade_report = grade_file(classes, file_path, test_class)
                grade_report["students"][author_name] = student_grade_report
        report_meta_data(grade_report)
        write_to_file(results, grade_report)


def _rank_students(grade_report: dict) -> list:
    """
    A helper function which ranks students by performance for ease of grading.
    It's much easier to work from highest performing to lowest performing in my
    opinion. There's less of a context switching issue since students with similar
    issues will likely be grouped together by grade.

    :param grade_report: the entire grade report as a dictionary
    :return: a list of students in the order of the performance (best first)
    """
    output = list()
    students_dict = grade_report["students"]
    students = sorted(students_dict, key=lambda x: students_dict.get(x).get("grade_estimate"), reverse=True)
    for index, student in enumerate(students):
        student_rank = "%s (%d)" % (student, students_dict[student].get("grade_estimate"))
        output.append(student_rank)
    return output


def report_meta_data(grade_report: dict):
    """
    Adds some meta data to the report such as the number of successful runs.
    :param grade_report: the grade report dictionary
    :return: None
    """
    successful_runs = 0
    total_passed_test_cases = 0
    total_failed_test_cases = 0
    students = grade_report["students"]
    for student in students:
        student_data = students[student]
        if student_data["run_status"] == "SUCCESS":
            successful_runs += 1
            passed_test_cases = student_data["execution_stdout"]["success_count"]
            failed_test_cases = student_data["execution_stdout"]["failure_count"]
            total_passed_test_cases += passed_test_cases
            total_failed_test_cases += failed_test_cases
    failed_runs = len(grade_report["students"]) - successful_runs
    skipped_test_cases = ((total_failed_test_cases + total_passed_test_cases) / successful_runs) * failed_runs
    grade_report["successful_runs"] = successful_runs
    grade_report["failing_runs"] = failed_runs
    grade_report["passed_test_cases"] = total_passed_test_cases
    grade_report["failed_test_cases"] = total_failed_test_cases
    grade_report["skipped_test_cases"] = int(skipped_test_cases)
    grade_report["rank"] = _rank_students(grade_report)


def get_author_name(file_path: str) -> str:
    """
    Extracts author name from file path
    :param file_path: path to file
    :return: file name
    """
    tokens = file_path.split(os.sep)
    index = tokens.index(DUMP)
    return tokens[index + 1]


def get_test_name(test_path: str) -> str:
    """
    Retrieves test name from file name.
    :param test_path: the path of the test file
    :return: the test file name without the file extension
    """
    test_name = test_path.split("/")[-1].split(".")[0]
    return test_name


def main():
    root = os.path.dirname(extract_solutions())
    automate_grading(root)


if __name__ == '__main__':
    main()
