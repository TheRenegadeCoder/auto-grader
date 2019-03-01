import json
from tkinter import filedialog


def main():
    json_file = filedialog.askopenfilename(
        title="Select JSON File",
        filetypes=(("json files", "*.json"), ("all files", "*.*"))
    )
    with open(json_file) as f:
        data = json.load(f)
        students_dict = data["students"]
        students = sorted(students_dict, key=lambda x: students_dict.get(x).get("grade_estimate"), reverse=True)
        for index, student in enumerate(students):
            print(str(index + 1) + ". " + student + " (" + str(students_dict[student].get("grade_estimate")) + ")")


if __name__ == '__main__':
    main()
