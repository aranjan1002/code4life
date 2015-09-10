#include <iostream>
#include <fstream>
#include <string>
#include <cstring>
#include <map>
#include <vector>
#include <cmath>
#include <cstdlib>
#include "Pipeline.h"

#define CAT1INSTCNT 9
#define CAT2INSTCNT 8
#define CAT3INSTCNT 5

using namespace std;

int instructioncount;

string getsubstring(string temp, int x, int y);
int bintoint(string temp, int x, int y);
int bintotwoscomplement(string temp, int x, int y);

string category1values[] = {"J", "ALLZERO", "BEQ", "BNE", "BLTZORBGEZ", "BGTZ", "BLEZ", "SW", "LW"};
string category2values[] = {"ADD",  "SUB", "MUL", "AND", "OR", "XOR", "NOR", "SLT"};
string category3values[] = {"ADDI", "ANDI", "ORI", "XORI", "MOVZ"};

string category1keys[] = {"00010", "00000", "00100", "00101", "00001", "00111", "00110", "01011", "00011"};
string category2keys[] = {"00011", "00010", "00110", "01001", "01101", "01010", "00001", "01011"};
string category3keys[] = {"10000", "10010", "10011", "10101", "10110"};

string errorstring = "Usage:\nMIPSsim -I <inputfilename> -O <outputfile>\n-I\tto name the input file\n-O\tto name the file where simulation output would be written\n";

int main(int argc, char **argv)
{
 map <string, string> category1, category2, category3;
 map <string, string> :: iterator cat2it;
 vector <instructions> :: iterator it;
 string currinstruction, dispbin, opcode;
 int i, breakflag=0, address=64;
 char *inputfile,  *simulationfile; 
 vector <instructions> x;
 string temp;
 instructions tempinstr;

 if(argc!=5)
	{
	 cout << "Invalid number of arguments" << endl << errorstring;
	 exit(0);
	}
 if(strcmp(argv[1],"-I")==0)
 	inputfile=argv[2];
 else if(strcmp(argv[3],"-I")==0)
	inputfile=argv[4];
 else {
	cout << "Invalid Usage" << endl << errorstring;
	exit(0);
	}
 if(strcmp(argv[1],"-O")==0)
 	simulationfile=argv[2];
 else if(strcmp(argv[3],"-O")==0)
	simulationfile=argv[4];
 else {
	cout << "Invalid Usage" << endl << errorstring;
	exit(0);
	}
 freopen(inputfile, "r", stdin);
 //freopen(disassemblyfile, "w", stdout);
 
 for(i=0;i<CAT1INSTCNT;i++)
	category1.insert(pair<string, string> (category1keys[i], category1values[i]));
 for(i=0;i<CAT2INSTCNT;i++)
	category2.insert(pair<string, string> (category2keys[i], category2values[i]));
 for(i=0;i<CAT3INSTCNT;i++)
	category3.insert(pair<string, string> (category3keys[i], category3values[i]));
 i=0;
 while(cin >> currinstruction)
	{instructions tempinstr;
	 dispbin=currinstruction;
	 if(breakflag==0)
		{
		 dispbin.insert(1, " ");
		 dispbin.insert(7, " ");
		 dispbin.insert(13, " ");
		 dispbin.insert(19, " ");
		 dispbin.insert(25, " ");
		 dispbin.insert(31, " ");
		 //cout << dispbin << "\t" << address;
		}
	 if(breakflag==0)
	 {
	 opcode=getsubstring(currinstruction, 1, 5);
	 if(currinstruction[0]=='0')
		{temp=category1.find(opcode)->second;
		 if(temp=="ALLZERO")
			{
			 if(getsubstring(currinstruction, 26, 31)=="001000")
				temp="JR";
			 else if(getsubstring(currinstruction, 26, 31)=="000010")
				temp="SRL";
			 else if(getsubstring(currinstruction, 26, 31)=="000011")
				temp="SRA";
			 else if(getsubstring(currinstruction, 0, 31)=="00000000000000000000000000000000")
				temp="NOP";
			 else if(getsubstring(currinstruction, 26, 31)=="001101")
				temp="BREAK";
			 else temp="SLL";
			}
		 else if(temp=="BLTZORBGEZ")
			{
			 if(getsubstring(currinstruction, 11, 15)=="00000")
				temp="BLTZ";
			 else temp="BGEZ";
			}
		 //cout << "\t" << temp;
		 /*if(temp!="BREAK" && temp!="NOP")
			cout << TAB;*/
		 tempinstr.todo=temp;
		 tempinstr.type=1;
		 if(temp=="J")
			{tempinstr.target=((address & 0xf0000000) | bintoint(currinstruction+"00", 6, 33));
			 //cout << "#" << tempinstr.target << endl;
			 tempinstr.branch=true;
			}
		 else if(temp=="BEQ" || temp=="BNE")
			{tempinstr.rs=bintoint(currinstruction, 6, 10);
			 tempinstr.rt=bintoint(currinstruction, 11, 15);
			 tempinstr.offset=((address & 0xf0000000) | bintoint(currinstruction+"00", 16, 33));
			 //cout << R tempinstr.rs << COMMA << R tempinstr.rt << COMMA << "#" << tempinstr.offset <<endl;
			 tempinstr.branch=true;
			}
		 else if(temp=="BGTZ" || temp=="BGEZ" || temp=="BLTZ" || temp=="BLEZ")
			{tempinstr.rs=bintoint(currinstruction, 6, 10);
			 tempinstr.offset=((address & 0xf0000000) | bintoint(currinstruction+"00", 16, 33));
			 //cout << R tempinstr.rs << COMMA << "#" << tempinstr.offset <<endl;
			 tempinstr.branch=true;
			}
		 else if(temp=="BREAK")
			{breakflag=1;
			 instructioncount=x.size()+1;
			 //cout << endl;
			}
		 else if(temp=="SW" || temp=="LW")
			{tempinstr.base=bintoint(currinstruction, 6, 10);
			 tempinstr.rt=bintoint(currinstruction, 11, 15);
			 tempinstr.offset=bintoint(currinstruction, 16, 31);
			 //cout << R tempinstr.rt << COMMA << tempinstr.offset << "(" << R tempinstr.base << ")" << endl;
			}
		 else if(temp=="SLL" || temp=="SRL" || temp=="SRA")
			{tempinstr.rt=bintoint(currinstruction, 11, 15);
		 	 tempinstr.rd=bintoint(currinstruction, 16, 20);
		 	 tempinstr.sa=bintoint(currinstruction, 21, 25);
			 //cout << R tempinstr.rd << COMMA << R tempinstr.rt << COMMA << "#" << tempinstr.sa << endl;
			}
		 /*else if(temp=="NOP")
			cout << endl;*/
		 else if(temp=="JR")
			{tempinstr.rs=bintoint(currinstruction, 6, 10);
		 	 tempinstr.hint=bintoint(currinstruction, 21, 25);
			 //cout << R tempinstr.rs << endl;
			 tempinstr.branch=true;
			}
			 
		 x.push_back(tempinstr);
		}
	 else if(currinstruction[1]=='0' && category2.find(opcode)!=category2.end())
		{temp=category2.find(opcode)->second;
		 //cout << TAB << temp << TAB;
		 tempinstr.todo=temp;
		 tempinstr.rs=bintoint(currinstruction, 6, 10);
		 tempinstr.rt=bintoint(currinstruction, 11, 15);
		 tempinstr.rd=bintoint(currinstruction, 16, 20);
		 tempinstr.type=2;
		 //cout << R tempinstr.rd << COMMA << R tempinstr.rs << COMMA << R tempinstr.rt << endl;
		 x.push_back(tempinstr);
		}
	 else if(category3.find(opcode)!=category3.end())
		{temp=category3.find(opcode)->second;
		 //cout << TAB << temp << TAB;
		 tempinstr.todo=temp;
		 tempinstr.rs=bintoint(currinstruction, 6, 10);
		 tempinstr.rt=bintoint(currinstruction, 11, 15);
		 tempinstr.imm=bintoint(currinstruction, 16, 31);
		 //cout << R tempinstr.rt << COMMA << R tempinstr.rs << COMMA << "#" << tempinstr.imm << endl;
		 tempinstr.type=3;
		 x.push_back(tempinstr);
		}
	}
	else if(currinstruction[0]=='0')
		{//cout << dispbin << "\t" << address;
		 tempinstr.value=bintoint(currinstruction, 1, 31);
		 //cout << TAB << tempinstr.value << endl;
		 x.push_back(tempinstr);
		}
	else
		{//cout << dispbin << "\t" << address;
		 tempinstr.value=(-1)*bintotwoscomplement(currinstruction, 1, 31);
		 //cout << TAB << tempinstr.value << endl;
		 x.push_back(tempinstr);
		}
	address += 4;
	}
 Pipeline *pipe=new Pipeline(x, instructioncount);
 pipe->execute(simulationfile);
 fclose(stdin);
 fclose(stdout);
}

string getsubstring(string temp, int x, int y)
{
 string temp2;
 temp2=&temp[x];
 temp2.erase(y-x+1);
 return temp2;
}

int bintoint(string temp, int x, int y)
{
 int i=0, sum=0;
 while(y>=x)
	{sum += (temp[y--]-48)*pow(2, i++);
	// cout << temp[y+1] << sum << endl;
	}
 return(sum);
}

int bintotwoscomplement(string temp, int x, int y)
{
 int i=0, sum=0;
 while(y>=x)
	{sum += ((-1)*(temp[y--]-49))*pow(2, i++);
	 //cout << temp[y+1] << sum << endl;
	}
 return(++sum);
}

