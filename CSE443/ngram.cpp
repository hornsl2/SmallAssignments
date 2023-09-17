// Copyright (C) 2017
#ifndef N_GRAM_GENERATOR_CPP
#define N_GRAM_GENERATOR_CPP

//---------------------------------------------------------------------------
//
// CSE Department, Miami University, Oxford, OHIO.
// All rights reserved.
//
// Miami University makes no representations or warranties about the
// suitability of the software, either express or implied, including
// but not limited to the implied warranties of merchantability,
// fitness for a particular purpose, or non-infringement.  Miami
// University shall not be liable for any damages suffered by licensee
// as a result of using, result of using, modifying or distributing
// this software or its derivatives.
//
// By using or copying this Software, Licensee agrees to abide by the
// intellectual property laws, and all other applicable laws of the
// U.S., and the terms of this license.
//
//---------------------------------------------------------------------------
#include "NGramGenerator.h"
#include <string>
#include <iomanip>
#include <iostream>
#include <sstream>
#include <fstream>
#include <vector>
#include <map>
#include <utility>

using namespace std;
vector<string> vec, nG;

std::multimap<std::string, int> wordMap;
// Implement various methods in your NGramGenerator class in this
// source file.
void findNGram(int len, int k);

NGramGenerator::NGramGenerator(const size_t minGramLen, 
        const size_t maxGramLen ) {
    this->minGramLen = minGramLen;
    this->maxGramLen = maxGramLen;
}

void NGramGenerator::extractFrom(std::istream& is) {
    string str;
    is >> str;
    int i = 0;
    while (!is.eof()) {
// make sure to make lowe case
        str = checkAlpha(str);
        vec.push_back(str);
        is >> str;
        i++;
    }
    int s = vec.size();
int z;
    for (int j = 0; j < s; j++) {
        for (int k = 1; k <= s; k++) {
            if ((k+j) <= s) {  
                z++;
                if (k >= this->minGramLen) {
                    findNGram(k, j, s);
                }
            }
        }
    }
}

void NGramGenerator::printTopNGrams(size_t topK, std::ostream& os) {
    while (!nG.empty()) {
        int count = 0;
        std::string s = nG.back();
        count++;
        nG.pop_back();
        for (int i = 0; i< nG.size(); i++) {
            if (nG.at(i) == s) {
                nG.at(i) = "";
                count++;
            }
        }
        cout<< s << " | " << count << "\n";
    }
}

int  // j is the position in the vector 
NGramGenerator::findNGram(int len, int j, int s) {
    std::string comp, str;
    for (int i = j; (i-j)< len; i++) {
        comp = vec.at(i);
        str += comp;
        if (i > 1 && comp < vec.at(i)) {
            comp = vec.at(++i);
            str+= comp;
        } 
    }
    nG.push_back(str);
    return 0;
}


std::string NGramGenerator::checkAlpha(std::string s) {
    for (string::iterator i = s.begin(); i != s.end(); i++) {
        if (!isalpha(s.at(i - s.begin()))) {
            s.erase(i);
            i--;
        }
    }
    return s;
}
#endif

