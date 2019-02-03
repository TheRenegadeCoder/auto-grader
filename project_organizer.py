import os
import pathlib
import zipfile
import subprocess
from tkinter import filedialog


def extract_solutions():
    archive_name = filedialog.askopenfilename()
    archive = zipfile.ZipFile(archive_name)
    DIR = os.path.join(os.path.dirname(archive_name), "Archives")
    archive.extractall(DIR)
    archive.close()

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


def compile_junit(classpath, build_file):
    command = "javac -cp \"%s\";. \"%s\"" % (classpath, build_file)

    result = subprocess.run(
        command,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE
    )

    print(result)


def test_junit():
    command = "java -cp <junit-jar>;<hamcrest-jar>;. org.junit.runner.JUnitCore  TestClass1 TestClass2"


def main():
    extract_solutions()

    classpath = "C:/Program Files/JUnit"
    build_file = "C:\\Users\\Jerem\\Downloads\\Dump\\thaparanjana\\Project03\\osu\\cse1223\\Project03.java"
    compile_junit(classpath, build_file)


if __name__ == '__main__':
    main()