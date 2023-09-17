/* 
 * File:   Point.cpp
 * Author: Hornsl2
 * 
 *
 *
 * Copyright 2017
 */

#include "Point.h"
#include <math.h>

Point::Point(double x, double y) {
    this->x = x;
    this->y = y;
}

Point::Point(const Point& other) {
    this->x = other.x;
    this->y = other.y;
}

Point::~Point() {
}

double Point::dist(const Point& other) const {
    double d = sqrt(pow((this->x - other.x), 2) + pow((this->y - other.y), 2));
    return d;
}
