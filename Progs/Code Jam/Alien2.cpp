#include <iostream>

using namespace std;

int main()
{
 int N, L, D, k, j, m, multiple, flag[25],i;
 char words[25][11], test[10][101];
 
 cin >> L >> D >> N;
 
 for (i=0; i<D; i++)
	cin.getlilne(words[i], L);
 for (i=0; i<N; i++)
	cin >> test[i];

 for (i=0; i<D; i++)
	cout << words[i] << endl;
 for (i=0; i<N; i++)
	cout << test[i] << endl;
 cout << endl << words[0] << endl << test[0] << endl << N << D << L;
}
