#include<iostream>
#include<string>
#include<algorithm>
#include<vector>
#include<sstream>
#include "URLTest.h"
#include<cppunit/ui/text/TestRunner.h>
#include<cppunit/CompilerOutputter.h>
#include<cppunit/TestResult.h>
#include<cppunit/TestResultCollector.h>
#include<cppunit/TextTestProgressListener.h>

using namespace std;

inline int map_hex_to_ascii(const std::string& hexnum) {
   int code;
   stringstream ss;
   ss << std::hex << hexnum;
   ss >> code;
   
   //ALPHA (%41–%5A and %61–%7A), DIGIT (%30–%39), hyphen (%2D), 
   //period (%2E), underscore (%5F), or tilde (%7E)
   if ((code >= 65 && code <= 90)  ||
       (code >= 97 && code <= 122) ||
       (code >= 48 && code <= 57)  ||
       (code == 45 || code == 46   || 
        code == 95 || code == 126))
        return code;
   return -1;      
}

vector<string> massageURL(const std::string& url) {
    vector<string> segments;
    size_t m = 0, n = 0;
    while (m != string::npos) {
        m = url.find("/", n);
        string cmp = url.substr(n, m - n);
        n = m + 1;

        if (!cmp.empty() && cmp != ".") {
            if (segments.size() > 0) {
                string previous = segments.back();
                if (cmp == ".." ) {
                    if (previous.find(".") == string::npos) {
                        segments.erase(segments.end());
                    }
                    continue;
                }
            } 
            segments.push_back(cmp);
       }        
    }    
   return segments;
}

bool is_valid_scheme(vector<string>& segments) {
    if (segments.at(0) == "ftp:") return false;
    if (segments.at(0) == "https:" ||
            segments.at(0) == "http:") 
    segments.erase(segments.begin());    
    
    return true;
}

void massageDomainName(std::string& domainName) {

    //Remove www. substrings as the first domain name.
    int n = 0, m = 0;
    string domain = domainName;
    while ((m = domain.find("www.", n)) != string::npos) {
        if (m !=n) break;
        domain = domain.substr(m + 4);
        n = 0;
    }
    // Check if default port is 80
    m = 0;
    if((m = domain.find(':')) != string::npos &&
            domain.substr(m + 1) == "80")
        domain = domain.substr(0, m);        
   
   domainName = domain;
}

void decode_octets(std::vector<string>& segments) {
    int m = 0, n = 0; 
    // Decode the unreserved characters
    for ( int i = 0; i < segments.size(); ++i) {
        string decode = segments.at(i);
        string hexstr;
        while ((m = decode.find('%', n)) != string::npos) {
           hexstr = decode.substr(m + 1, 2); 
           int acode  = map_hex_to_ascii(hexstr);
           if (acode != -1) {
               char c = static_cast<char>(acode);
               string decode_suffix = decode.substr(m + 3);
               string decode_prefix = decode.substr(0, m);
               decode = decode_prefix + c + decode_suffix;
               n = m + 1;
           }
           else
              n = m + 3;
        }
        segments.at(i) = decode;
    }
}

void process_index_files(vector<string>& segments) {
    //Remove the directory index file if found?
    string dir_index = segments.at(segments.size() - 1);
    size_t i = 0;
    string file, ext;
    if ((i = dir_index.find('.')) != string::npos) {
        file = dir_index.substr(0, i);
        ext  = dir_index.substr(i + 1);
    }
    else return;

    string exts[5] = { "asp", "html", "htm", "php", "aspx"};
    vector<string> extn;
    extn.assign(exts, exts + 5);

    if (file == "index" || file == "default") {
        if (find(extn.begin(), extn.end(), ext) != 
                extn.end())
        segments.erase(segments.end());        
    }
}

std::string clean_url(const std::string& in) {
    // The normalization process of the URL follows  
    // the RFC 3986 but with certain modifications.
    string inputURL = in;
    
    int pos = inputURL.find_first_of(";?#");
    string output(inputURL, 0, pos - 1);
    
    // Lower case the string.
    std::transform(output.begin(), output.end(), 
                        output.begin(), ::tolower);

    vector<string> segments = massageURL(output);
    if (!is_valid_scheme(segments)) return "InvalidURL";
    massageDomainName(segments.at(0));
    decode_octets(segments);
    process_index_files(segments);

    std::string targetURL;
    vector<string>::iterator it = segments.begin();
    
    for ( ; it != segments.end(); ++it) {
        if((*it).empty()) continue;
        targetURL+= *it;
        if (it != segments.end() - 1)
            targetURL += '/';
    }
    return targetURL;
}

int main(int argc, char** argv) {
    std::string testPath = (argc > 1) ? std::string(argv[1]) : "";
     
    // Create the event manager and test controller
    CppUnit::TestResult controller;
    
    CppUnit::TextUi::TestRunner runner;
    runner.addTest(URLTest::suite());

    // Add a listener that colllects test result
    CppUnit::TestResultCollector result;
    controller.addListener( &result ); 

    // Add a listener that print dots as test run.
    CppUnit::TextTestProgressListener progress;
    controller.addListener( &progress );

    std::cout << "Running "  <<  testPath;
    runner.run( controller, testPath );

    CppUnit::CompilerOutputter outputter( &result, std::cerr );
    outputter.setLocationFormat("%f:%l: ");
    outputter.write(); 
    return result.wasSuccessful() ? 0 : 1;
    
    /*string input = "https://www.www.THIS-IS.www.WRONG:80/%7E%7EJoe%5E%7EBloggs//this/../index1.html/;jsessionid=9a8237a7jf4987?z=&w=1&a=b#fragment";
    string output = clean_url(input);
    cout << "Output:" << output;
    return 0;
    */
}
