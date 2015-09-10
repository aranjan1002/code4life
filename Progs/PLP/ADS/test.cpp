#include <iostream>
using namespace std;

class box
{public:
	void hello(){cout << "hello";}
};

class hello:public box
{public :
	void world(){hello();cout << "world";}
};

