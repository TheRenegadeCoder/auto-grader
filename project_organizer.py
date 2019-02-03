import os
import pathlib
import zipfile
import subprocess
from tkinter import filedialog


def extract_main_zip():
    archive_name = filedialog.askopenfilename()
    archive = zipfile.ZipFile(archive_name)
    archive_path = os.path.join(os.path.dirname(archive_name), "Archives")
    archive.extractall(archive_path)
    archive.close()
    return archive_path


def extract_solutions():
    DIR = extract_main_zip()

    DUMP = os.path.join(os.path.dirname(DIR), "Dump")
    pathlib.Path(DUMP).mkdir(parents=True, exist_ok=True)

    for file in os.listdir(DIR):
        file_name = os.fsdecode(file)
        file_path = os.path.join(DIR, file_name)
        file_path_plus_name = os.path.join(DUMP, file_name.split("_")[0])
        if file_name.endswith(".zip"):
            zip_file = zipfile.ZipFile(file_path, "r")
            zip_file.extractall(file_path_plus_name)
            zip_file.close()
        else:
            name = file_name.split("_")[0]
            project = file_name.split("_")[-1]
            pathlib.Path(os.path.join(DUMP, name)).mkdir(parents=True, exist_ok=True)
            new_file_path = os.path.join(DUMP, name, project)
            os.rename(file_path, new_file_path)

    return DUMP


def compile_junit(classes, classpath, test_file):
    command = "javac -d \"%s\" -cp \"%s\". \"%s\"" % (classes, classpath, test_file)
    return run_command(command)


def test_junit(classes, classpath, test_class):
    command = "java -cp \"%s;%s\". org.junit.runner.JUnitCore %s" %(classes, classpath, test_class)
    return run_command(command)


def run_command(command):
    result = subprocess.run(
        command,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE
    )
    return result


def main():
    root = os.path.dirname(extract_solutions())
    classes = os.path.join(root, "Test")
    os.mkdir(classes)

    classpath = "C:\\Program Files\\JUnit\\junit-4.13-beta-2.jar;C:\\Program Files\\JUnit\\hamcrest-all-1.3.jar;"
    test_class = "E:\\Projects\\CSE1223\\Projects\\Project03\\Project03Test.java"
    build_file = "C:\\Users\\Jerem\\Downloads\\Dump\\thaparanjana\\Project03\\osu\\cse1223\\Project03.java"
    compile_junit(classes, classpath, build_file)
    compilation_results = compile_junit(classes, classpath, test_class)
    execution_results = test_junit(classes, classpath, "Project03Test")
    print(compilation_results)
    print(execution_results)
    print(execution_results.stdout.decode("utf-8"))
    print(execution_results.stderr.decode("utf-8"))


if __name__ == '__main__':
    main()