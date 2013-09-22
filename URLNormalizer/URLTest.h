#include<string>
#include<vector>
#include<fstream>
#include<sstream>
#include<cppunit/extensions/HelperMacros.h>
#include<boost/algorithm/string.hpp>

using namespace std;
extern std::string clean_url(const std::string&);

class URLTest : public CppUnit::TestFixture {
    private:
        std::ifstream URLTests;

    public:
        void setUp() {
            URLTests.open("URLTestsFile.txt");
        }

        void testCleanURL() {
            std::string line;

            if (URLTests.is_open()) {
                while(getline(URLTests, line)) {
                    if (line.empty()) continue;
                    string url_input, url_output;
                    stringstream buffer(line);
                    buffer >> url_input >> url_output;
                    string output = clean_url(url_input);
                    CPPUNIT_ASSERT_EQUAL(output, url_output); 
                }     
                URLTests.close();
            }
            else { 
               std::cout << "Unable to open test file" << std::endl << std::endl;
            }    
        }         

    CPPUNIT_TEST_SUITE(URLTest);
    CPPUNIT_TEST(testCleanURL);
    CPPUNIT_TEST_SUITE_END();

};

CPPUNIT_TEST_SUITE_REGISTRATION(URLTest);
