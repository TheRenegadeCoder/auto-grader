import unittest
from grade import project_organizer


class TestProjectOrganizer(unittest.TestCase):

    def test_get_author_name(self):
        path = "C:\\Users\\Jerem\\Downloads\\Dump\\grifskijeremy\\Project08\\src\\osu\\cse1223"
        self.assertEqual(project_organizer.get_author_name(path), "grifskijeremy")

    def test_get_test_name(self):
        path = "E:/Projects/CSE1223/Projects/Project08/Project08.java"
        self.assertEqual(project_organizer.get_test_name(path), "Project08")


if __name__ == '__main__':
    unittest.main()
