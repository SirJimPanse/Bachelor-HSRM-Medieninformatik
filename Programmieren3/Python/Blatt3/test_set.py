#!/usr/bin/python
# -*- coding: utf-8 -*-

from menge import Set # die eigene zu testende Klasse
import unittest

class TestSet(unittest.TestCase):
    "Testen der Mengenimplementierung"
    

    def setUp(self):
        "vor jedem Test, Beispielmengen instanziieren"
        self.s = set([1, 5, 3])
        self.mys = Set([1, 5, 3])
        self.myt = Set([1, 3, 5])
        

    def same_set(self):
        "Hilfsroutine, prüft ob zwei Mengen gleich sind"
        if len(self.s) != len(self.mys):
            return False
        for e in self.s:
            if not e in self.mys:
                return False
        return True


    def test_initialize(self):
        "Initialisieren"
        self.assert_(self.same_set())


    def test_add_existing(self):
        "Vorhandenes Element dazu, gleiche Liste"
        self.mys.add(3)
        self.mys.add(5)
        self.assert_(self.same_set())


    def test_equals(self):
        "equals, =="
        self.assertEqual(self.mys, self.myt)


    def test_remove1(self):
        "Einzelne Elemente loeschen"
        self.mys.remove(1)
        self.mys.remove(2)
        self.myt.remove(5)
        self.myt.remove(4)
        self.assertEqual(len(self.mys), len(self.myt))


    def test_remove2(self):
        "In zwei Mengen die gleichen Elemente loeschen"
        remove = [3, 4, 5]
        for e in remove:
            self.mys.remove(e)
        for e in reversed(remove):
            self.myt.remove(e)
        self.assertEqual(self.mys, self.myt)


    def test_union_update(self):
        "Elemente mit Vereinigung aufnehmen"
        self.mys.remove(1)
        self.mys.remove(2)
        self.myt.remove(5)
        self.myt.remove(4)
        self.mys.union_update(self.myt)
        self.myt.union_update(self.mys)
        self.assertEqual(self.mys, self.myt)


    def test_difference_update(self):
        "Mengendifferenz, Menge aendern"
        self.mys.difference_update(Set([1, 5]))
        self.myt.remove(5)
        self.myt.remove(1)
        self.assertEqual(self.mys, self.myt)


    def test_difference(self):
        "Mengendifferenz, neu berechnen"
        self.myt.remove(5)
        self.myt.remove(1)
        self.assertEqual(self.mys.difference([1, 5]), self.myt)


    def test_plus_op(self):
        "Der '+'-Operator"
        self.assertEqual(self.myt + self.mys, self.mys)
        self.assertEqual(self.myt + [1, 3], self.mys)
        self.assertEqual(self.myt + [1, 3, 7, 9 ], [7, 9] + self.mys)
        

    def test_in(self):
        "Der in-Test"
        for e in self.mys:
            self.assert_(e in self.myt)
        for e in self.myt:
            self.assert_(e in self.mys)


    def test_clear(self):
        "Alle Elemente entfernen"
        self.myt.clear()
        self.assert_(self.myt.size() == 0)


if __name__ == '__main__':
    suite = unittest.TestSuite()
    suite.addTest(unittest.makeSuite(TestSet))
    unittest.TextTestRunner(verbosity=2).run(suite)



