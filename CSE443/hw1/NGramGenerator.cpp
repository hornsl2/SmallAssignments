#ifndef N_GRAM_GENERATOR_CPP
#define N_GRAM_GENERATOR_CPP

//---------------------------------------------------------------------------
//
//  Copyright (c) 2017 Sam Horn
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

#include <string>
#include <vector>
#include "NGramGenerator.h"

using namespace std;


// Implement various methods in yoru NGramGenerator class in this
// source file.
vector<string> worldmap;
vector<string> f;
vector<int> c;
vector<string> fileVec;
vector<string> gramVec;

//  size_t maxGramLen;
//  size_t minGramLen;
// basic constructor
NGramGenerator::NGramGenerator(const size_t minGramLen,
        const size_t maxGramLen) {
    this->minGramLen = minGramLen;
    this->maxGramLen = maxGramLen;
}

// checks that a character is alphanumeric and valid for an ngram
std::string NGramGenerator::checkValue(std::string str) {
    for (string::iterator iter = str.begin(); iter != str.end(); iter++) {
        if (!isalpha(str.at(iter - str.begin()))) {
            str.erase(iter);
            iter--;
        }
    }
    return str;
}
// Extracts characters from a file and places into a vector for later 
// determination of nGrams 
void NGramGenerator::extractFrom(std::istream& is) {
    string str;
    int i = 0;
    is >> str;
    while (!is.eof()) {
        str = checkValue(str);
        fileVec.push_back(str);
          i++;
        is >> str;
    }
    int size = fileVec.size();   
    for (int outter = 0; outter < size; outter++) {
        for (int inner = 1; inner <= size; inner++) {
            if ((inner + outter) <= size) {
                if (inner >= this->minGramLen) {
                    checkNGram(inner, outter);
                }
            }
        }
    }
}

void NGramGenerator::printTopNGrams(size_t topK, std::ostream& os) {
    while (!gramVec.empty()) {
        int count = 0;
        std::string currentGram = gramVec.back();
        gramVec.pop_back();
         count++;
        for (int i = 0; i < gramVec.size(); i++) {
            if (gramVec.at(i) == currentGram) {
                gramVec.at(i) = "";
                count++;
            }
        }
        cout<< currentGram << " | " << count << "\n";
    }
}
    

//  check if values in vector are valid for NGrams
void NGramGenerator::checkNGram(int len, int val) {
    std::string vecVal, toSend;
    for (int iterator = val; (iterator - val) < len; iterator++) {
        vecVal = fileVec.at(iterator);
        toSend += vecVal;
        if (iterator > 1 && vecVal < fileVec.at(iterator)) {
            vecVal = fileVec.at(++iterator);
            toSend += vecVal;
        }
    }
    gramVec.push_back(toSend);    
}



#endif
