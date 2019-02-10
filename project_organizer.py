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
    result = subprocess.run(
        command,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE
    )
    return result


def grade_file(classes: str, build_file: str, test_class: str, results) -> dict:
    """
    Grades a file.
    :param classes: a directory contain files under test
    :param build_file: a file to test
    :param test_class: the path to the test file
    :param results: the results file
    :return: None
    """
    classpath = "C:\\Program Files\\JUnit\\junit-4.13-beta-2.jar;C:\\Program Files\\JUnit\\hamcrest-all-1.3.jar;"

    compile_junit(classes, classpath, build_file)
    compilation_results = compile_junit(classes, classpath, test_class)
    execution_results = test_junit(classes, classpath, get_test_name(test_class))
    student_grade_report = generate_student_json(compilation_results, execution_results, build_file)
    return student_grade_report


def generate_student_json(compilation_results: subprocess.CompletedProcess,
                          execution_results: subprocess.CompletedProcess, build_file: str) -> dict:
    """
    Generates the json solution.
    :param compilation_results: the compilations results process
    :param execution_results: the execution results process
    :param build_file: path to solution
    :return: the data dictionary
    """
    raw_test_results = execution_results.stdout.decode("utf-8").splitlines()
    output_dict = dict()
    output_dict["path"] = build_file
    output_dict["run_status"] = "SUCCESS" if len(raw_test_results) > 2 else "FAILURE"
    output_dict["solution"] = read_solution(build_file)
    output_dict["compilation_stdout"] = compilation_results.stdout.decode("utf-8")
    output_dict["compilation_stderr"] = compilation_results.stderr.decode("utf-8")
    output_dict["execution_stdout"] = parse_test_results(raw_test_results)
    output_dict["execution_stderr"] = execution_results.stderr.decode("utf-8")
    return output_dict


def read_solution(solution_path):
    """
    Reads the solution and returns it as a list of lines.
    :param solution_path: path to the solution
    :return: the solution as a list of lines
    """
    with open(solution_path) as solution:
        data = solution.readlines()
    return data


def parse_test_results(raw_test_results: list) -> dict:
    test_results = dict()
    test_results["failed_test_cases"] = dict()
    i = 0
    while i < len(raw_test_results):
        line = raw_test_results[i]
        if "version" in line:
            test_results["junit_version"] = line.split()[-1]
        elif "Time" in line:
            test_results["time"] = float(line.split()[-1])
        elif "Failures" in line:
            fails = int(line.split()[-1])
            successes = int(line.split()[2][:2]) - fails
            test_results["failure_count"] = fails
            test_results["success_count"] = successes
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
    next_failed_index = str(int(line[0]) + 1)
    while len(line) != 0 and line[0] != next_failed_index:
        if "\t" in line:
            failed_test_cases[test_case]["trace"].append(line.replace("\t", ""))
        elif "expected" in line:
            comparison = line.split()
            failed_test_cases[test_case]["expected"] = comparison[1].replace("expected:", "")
            failed_test_cases[test_case]["was"] = comparison[-1].replace("was:", "")
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
    json_file = get_test_name(test_class) + ".json"
    with open(os.path.join(root, json_file), "w") as results:
        for subdir, dirs, files in os.walk(os.path.join(root, DUMP)):
            java_files = [name for name in files if ".java" in name and "module-info" not in name]
            for file_name in java_files:
                file_path = os.path.join(subdir, file_name)
                author_name = get_author_name(file_path)
                classes = os.path.join(test_dir, author_name, file_name.split(".")[0])
                pathlib.Path(classes).mkdir(parents=True, exist_ok=True)
                student_grade_report = grade_file(classes, file_path, test_class, results)
                grade_report[author_name] = student_grade_report
        write_to_file(results, grade_report)


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
