import os
import zipfile
import pathlib

DIR = "C:/Users/Jerem/Downloads/CSE_1223_SP2019_8281-Project_1_submissions"

DUMP = os.path.join(DIR, "DUMP")
pathlib.Path(DUMP).mkdir(parents=True, exist_ok=True)

for file in os.listdir(DIR):
    file_name = os.fsdecode(file)
    file_path = os.path.join(DIR, file_name)
    file_path_plus_name = os.path.join(DUMP, file_name.split("_")[0])
    if file_name.endswith(".zip"):
        zip_file = zipfile.ZipFile(file_path, "r")
        zip_file.extractall(file_path_plus_name)
        zip_file.close()
    elif file_name == "DUMP":
        pass
    else:
        name = file_name.split("_")[0]
        project = file_name.split("_")[-1]
        pathlib.Path(os.path.join(DUMP, name)).mkdir(parents=True, exist_ok=True)
        new_file_path = os.path.join(DUMP, name, project)
        os.rename(file_path, new_file_path)
