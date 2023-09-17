/* 
 * File:   Person.cpp
 * Edited by: SAM HORN
 * Copyright (C) 2016 raodm@miamiOH.edu
 */

#include "Person.h"
#include <string>
#include <iomanip>

Person::Person(int id, unsigned short age, const std::string& name) :
    id(id), age(age), name(name) {
    // Nothing else to do.
}

Person::~Person() {
    // Nothing to be done here.
}

std::istream&
Person::operator>>(std::istream& is, Person& p) {
    return (is >> p.id >> p.age >> std::quoted(p.name));
}

std::ostream&
Person::operator<<(std::ostream& os, Person& o) const {
    return (os << o.id << " " << o.age << " " << std::quoted(o.name));
}

bool
Person::operator<(const Person& other) const {
    return (this->age < other.age);
}
