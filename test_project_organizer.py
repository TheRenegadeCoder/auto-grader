import unittest
from Tools import project_organizer

class TestProjectOrganizer(unittest.TestCase):

    def test_get_author_name(self):
        path = "C:\\Users\\Jerem\\Downloads\\Dump\\grifskijeremy\\Project08\\src\\osu\\cse1223"
        self.assertEqual(project_organizer.get_author_name(path), "grifskijeremy")


if __name__ == '__main__':
    unittest.main()