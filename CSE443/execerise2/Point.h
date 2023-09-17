/*
 * File:   Point.h
 * Author: Hornsl2
 *  Course:CSE443
 * Created on September 6, 2017, 9:27 PM
 * Copyright 2017
 */

#ifndef POINT_H
#define POINT_H

class Point {
public:
    double x;
    double y;
    Point(double x = 0, double y = 0);
    Point(const Point& orig);
    virtual ~Point();
    double dist(const Point& other) const;
private:
};
#endif /* POINT_H */
