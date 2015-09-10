#include <iostream>
#include <string>
#include <cstring>

using namespace std;

void setzero (int *p, int l)
{
 while(l--)
        p[l] = 0;
}

int main()
{
 string names[100];
 int i, j, solved[100][100], tot_solved[100], accuracy[100], persons, probs, min;
 float points[100];

 while ( cin >> persons >> probs )
 if(persons !=0 || probs != 0)
 {memset (tot_solved, 0, 100*sizeof(int));
 memset (accuracy, 0, 100*sizeof(int));
 memset (points, 0, 100*sizeof(float));

 for (i=0; i<persons; i++)
        {
         cin >> names[i];
         for (j=0; j<probs; j++)
                {
                 cin >> solved[i][j];
                 tot_solved[i] += solved[i][j];
                 accuracy[j] += solved[i][j];
                }
        }

 for (i=0; i<persons; i++)
        {for (j=0; j<probs; j++)
                {if(accuracy[j]!=0)
                        points[i] += ((2.0/accuracy[j]) * solved[i][j]);
                }
        }

 for (i=0; i<persons-1; i++)
        {min = i;
        for (j=i+1; j<persons; j++)
                if(names[i].compare(names[j]) >= 0)
                        min = j;
        if (min != i)
                {swap(names[i], names[min]);
                 swap(tot_solved[i], tot_solved[min]);
                 swap(points[i], points[min]);
                }
        }

 for (i=0; i<persons-1; i++)
        {min = i;
        for (j=i+1; j<persons; j++)
                if(tot_solved[i] <= tot_solved[j])
                        min = j;
        if (min != i)
                {swap(names[i], names[min]);
                 swap(tot_solved[i], tot_solved[min]);
                 swap(points[i], points[min]);
                }
        }

  for (i=0; i<persons-1; i++)
        {min = i;
        for (j=i+1; j<persons; j++)
                if(points[i] <= points[j])
                        min = j;
        if (min != i)
                {swap(names[i], names[min]);
                 swap(tot_solved[i], tot_solved[min]);
                 swap(points[i], points[min]);
                }
        }


 for (i=0; i<persons; i++)
         cout << names[i] << endl;
 }         
 return 0;

}


