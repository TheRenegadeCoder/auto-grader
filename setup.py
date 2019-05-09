import setuptools

with open("README.md", "r") as fh:
    long_description = fh.read()

setuptools.setup(
    name="auto_grader",
    version="1.0.0",
    author="The Renegade Coder",
    author_email="jeremy.grifski@therenegadecoder.com",
    description="An automated testing and grading tool for Java projects",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/TheRenegadeCoder/auto-grader",
    packages=setuptools.find_packages(),
    entry_points = {
        "console_scripts": [
            'grade = grade.project_organizer:main',
        ],
    },
    classifiers=(
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: GNU General Public License v3 (GPLv3)",
        "Operating System :: OS Independent",
    ),
)
