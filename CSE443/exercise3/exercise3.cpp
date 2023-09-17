/* 
 * File:   exercise3.cpp
 * Author: raodm
 *Edited By: SAM HORN
 * Copyright (C) 2017 raodm@miamiOH.edu
 */

#include <iostream>
#include <string>
#include <vector>
#include <iterator>
#include <algorithm>
#include <fstream>
#include "Person.h"

// Alias data types to streamline code below.
using Item = int;
// A list of data types
using ItemVec = std::vector<Item>;

/**
 * A function that prints a sorted list of items.
 * 
 * This function uses a declarative style of coding to print a
 * sorted list of items.  It uses a stream iterators to read/
 * write items from/to input and output streams. 
 * 
 * @param in
 * @param out
 */
void sortedPrint(std::istream& in, std::ostream& out) {
    // Create a input stream iterator to read Item(s) from input stream
    std::istream_iterator<Item> inputs(in), eof;
    // Create a vector of items to sort them
    ItemVec list(inputs, eof);
    // Sort the list of items
    std::sort(list.begin(), list.end());
    // Print the list of items using an output iterator
    std::ostream_iterator<Item> outputs(out, "\n");
    std::copy(list.begin(), list.end(), outputs);
}

/*
 * A simple main function that checks command-line arguments and 
 * calls the sortedPrint function.
 */
int main(int argc, char** argv) {
    if (argc != 3) {
        std::cerr << "Specify input & output file paths.\n";
        return 1;
    }
    // Open input file and ensure it is valid
    std::ifstream dataFile(argv[1]);
    if (!dataFile.good()) {
        std::cerr << "Unable to open file: " << argv[1] << std::endl;
        return 2;
    }
    // Ok. Sort and print items in the data file and write to
    // output file.
    std::ofstream outFile(argv[2]);
    sortedPrint(dataFile, outFile);
    return 0;
}

// End of source code
