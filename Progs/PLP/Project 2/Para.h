using namespace std;

class Para
{
 Token list[30];
 int index;
 int maxindex;
 
 public:
	Para()
		{list[0].insertVal((char *)"\0");
	 	 index=0;
		 maxindex=-1;
		}
	void add(char *temp)
		{
		 list[index++].insertVal(temp);
		 maxindex++;
		}
	int getMaxi()
		{return maxindex;
		}
	int getIndex()
		{return index;
		}
	char *getNext()
		{
		 return(list[index++].getVal());
		}
	void setIndex(int temp)
		{
	 	 index = temp;
		}
	void concat (char *temp)
		{
		 list[index-1].concatenate(temp);
		}
	char *pop()
		{
	 	 if(isEmpty())
			{cout << "Fatal Error: Stack Empty";
			 return((char *)"\0");
			}
		 else {
			return(list[maxindex--].getVal());
			}
		}
	void push(char *temp)
		{
		 list[++maxindex].insertVal(temp);
		}
	int isEmpty()
		{
		 if(maxindex<0)
			return(1);
		 else return(0);
		}
	void reset(int x)
		{maxindex = maxindex+x;
		}
	void show()
		{int i;
		 cout << "\n--STACK--------"<<endl;
		 for(i=0; i<=maxindex; i++)
			cout << list[i].getVal() << endl;
		 cout << "---------------\n";
		}
};