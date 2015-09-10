#include <iostream>
#include <fstream>
#include <string>
#include <cstring>
#include <map>
#include <vector>
#include <cmath>
#include <cstdlib>

#define CAT1INSTCNT 9
#define CAT2INSTCNT 8
#define CAT3INSTCNT 5
#define COMMA ", "
#define TAB "\t"
#define R "R" <<

using namespace std;

class instructions
{
 public:
 int rs, rt, rd, offset, base, sa, hint, imm, type, target, value;
 string todo;
};

int instructioncount;

string getsubstring(string temp, int x, int y);
int bintoint(string temp, int x, int y);
int bintotwoscomplement(string temp, int x, int y);
void execute(vector<instructions> &x);

string category1values[] = {"J", "ALLZERO", "BEQ", "BNE", "BLTZORBGEZ", "BGTZ", "BLEZ", "SW", "LW"};
string category2values[] = {"ADD",  "SUB", "MUL", "AND", "OR", "XOR", "NOR", "SLT"};
string category3values[] = {"ADDI", "ANDI", "ORI", "XORI", "MOVZ"};

string category1keys[] = {"00010", "00000", "00100", "00101", "00001", "00111", "00110", "01011", "00011"};
string category2keys[] = {"00011", "00010", "00110", "01001", "01101", "01010", "00001", "01011"};
string category3keys[] = {"10000", "10010", "10011", "10101", "10110"};

string errorstring = "Usage:\nMIPSsim -I <inputfilename> -D <outputfilename> -S <simulation.txt>\n-I\tto name the input file\n-D\tto name the file where the disassembled code would be written\n-S\tto name the file where simulation output would be written\n";

int main(int argc, char **argv)
{
 map <string, string> category1, category2, category3;
 map <string, string> :: iterator cat2it;
 vector <instructions> :: iterator it;
 string currinstruction, dispbin, opcode;
 int i, breakflag=0, address=64;
 char *inputfile, *disassemblyfile, *simulationfile; 
 vector <instructions> x;
 string temp;
 instructions tempinstr;

 if(argc!=7)
	{
	 cout << "Invalid number of arguments" << endl << errorstring;
	 exit(0);
	}
 if(strcmp(argv[1],"-I")==0)
 	inputfile=argv[2];
 else if(strcmp(argv[3],"-I")==0)
	inputfile=argv[4];
 else if(strcmp(argv[5],"-I")==0)
	inputfile=argv[5];
 else {
	cout << "Invalid Usage" << endl << errorstring;
	exit(0);
	}
 if(strcmp(argv[1],"-D")==0)
 	disassemblyfile=argv[2];
 else if(strcmp(argv[3],"-D")==0)
	disassemblyfile=argv[4];
 else if(strcmp(argv[5],"-D")==0)
	disassemblyfile=argv[5];
 else {
	cout << "Invalid Usage" << endl << errorstring;
	exit(0);
	}
 if(strcmp(argv[1],"-S")==0)
 	simulationfile=argv[2];
 else if(strcmp(argv[3],"-S")==0)
	simulationfile=argv[4];
 else if(strcmp(argv[5],"-S")==0)
	simulationfile=argv[6];
 else {
	cout << "Invalid Usage" << endl << errorstring;
	exit(0);
	}
 //cout << inputfile << endl << disassemblyfile << endl << simulationfile << endl;
 freopen(inputfile, "r", stdin);
 freopen(disassemblyfile, "w", stdout);
 
 for(i=0;i<CAT1INSTCNT;i++)
	category1.insert(pair<string, string> (category1keys[i], category1values[i]));
 for(i=0;i<CAT2INSTCNT;i++)
	category2.insert(pair<string, string> (category2keys[i], category2values[i]));
 for(i=0;i<CAT3INSTCNT;i++)
	category3.insert(pair<string, string> (category3keys[i], category3values[i]));
 i=0;
 while(cin >> currinstruction)
	{dispbin=currinstruction;
	 if(breakflag==0)
		{
		 dispbin.insert(1, " ");
		 dispbin.insert(7, " ");
		 dispbin.insert(13, " ");
		 dispbin.insert(19, " ");
		 dispbin.insert(25, " ");
		 dispbin.insert(31, " ");
		 cout << dispbin << "\t" << address;
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
		 cout << "\t" << temp;
		 if(temp!="BREAK" && temp!="NOP")
			cout << TAB;
		 tempinstr.todo=temp;
		 tempinstr.type=1;
		 if(temp=="J")
			{tempinstr.target=((address & 0xf0000000) | bintoint(currinstruction+"00", 6, 33));
			 cout << "#" << tempinstr.target << endl;
			}
		 else if(temp=="BEQ" || temp=="BNE")
			{tempinstr.rs=bintoint(currinstruction, 6, 10);
			 tempinstr.rt=bintoint(currinstruction, 11, 15);
			 tempinstr.offset=((address & 0xf0000000) | bintoint(currinstruction+"00", 16, 33));
			 cout << R tempinstr.rs << COMMA << R tempinstr.rt << COMMA << "#" << tempinstr.offset <<endl;
			}
		 else if(temp=="BGTZ" || temp=="BGEZ" || temp=="BLTZ" || temp=="BLEZ")
			{tempinstr.rs=bintoint(currinstruction, 6, 10);
			 tempinstr.offset=((address & 0xf0000000) | bintoint(currinstruction+"00", 16, 33));
			 cout << R tempinstr.rs << COMMA << "#" << tempinstr.offset <<endl;
			}
		 else if(temp=="BREAK")
			{breakflag=1;
			 instructioncount=x.size()+1;
			 cout << endl;
			}
		 else if(temp=="SW" || temp=="LW")
			{tempinstr.base=bintoint(currinstruction, 6, 10);
			 tempinstr.rt=bintoint(currinstruction, 11, 15);
			 tempinstr.offset=bintoint(currinstruction, 16, 31);
			 cout << R tempinstr.rt << COMMA << tempinstr.offset << "(" << R tempinstr.base << ")" << endl;
			}
		 else if(temp=="SLL" || temp=="SRL" || temp=="SRA")
			{tempinstr.rt=bintoint(currinstruction, 11, 15);
		 	 tempinstr.rd=bintoint(currinstruction, 16, 20);
		 	 tempinstr.sa=bintoint(currinstruction, 21, 25);
			 cout << R tempinstr.rd << COMMA << R tempinstr.rt << COMMA << "#" << tempinstr.sa << endl;
			}
		 else if(temp=="NOP")
			cout << endl;
		 else if(temp=="JR")
			{tempinstr.rs=bintoint(currinstruction, 6, 10);
		 	 tempinstr.hint=bintoint(currinstruction, 21, 25);
			 cout << R tempinstr.rs << endl;
			}
			 
		 x.push_back(tempinstr);
		}
	 else if(currinstruction[1]=='0' && category2.find(opcode)!=category2.end())
		{temp=category2.find(opcode)->second;
		 cout << TAB << temp << TAB;
		 tempinstr.todo=temp;
		 tempinstr.rs=bintoint(currinstruction, 6, 10);
		 tempinstr.rt=bintoint(currinstruction, 11, 15);
		 tempinstr.rd=bintoint(currinstruction, 16, 20);
		 tempinstr.type=2;
		 cout << R tempinstr.rd << COMMA << R tempinstr.rs << COMMA << R tempinstr.rt << endl;
		 x.push_back(tempinstr);
		}
	 else if(category3.find(opcode)!=category3.end())
		{temp=category3.find(opcode)->second;
		 cout << TAB << temp << TAB;
		 tempinstr.todo=temp;
		 tempinstr.rs=bintoint(currinstruction, 6, 10);
		 tempinstr.rt=bintoint(currinstruction, 11, 15);
		 tempinstr.imm=bintoint(currinstruction, 16, 31);
		 cout << R tempinstr.rt << COMMA << R tempinstr.rs << COMMA << "#" << tempinstr.imm << endl;
		 tempinstr.type=3;
		 x.push_back(tempinstr);
		}
	}
	else if(currinstruction[0]=='0')
		{cout << dispbin << "\t" << address;
		 tempinstr.value=bintoint(currinstruction, 1, 31);
		 cout << TAB << tempinstr.value << endl;
		 x.push_back(tempinstr);
		}
	else
		{cout << dispbin << "\t" << address;
		 tempinstr.value=(-1)*bintotwoscomplement(currinstruction, 1, 31);
		 cout << TAB << tempinstr.value << endl;
		 x.push_back(tempinstr);
		}
	address += 4;
	}

 //fclose(stdout);
 freopen(simulationfile, "w", stdout);
 
 execute(x);
 fclose(stdin);
 fclose(stdout);
 	
 /*it=x.begin();
 while(it!=x.end())
	{
	 cout << it->todo;
	 if(it->type==2)
		cout << "\t" << it->rd << "\t" << it->rs << "\t" << it->rt << endl;
	 else if(it->type==3)
		cout <<  "\t" << it->rt << "\t" << it->rs << "\t" << it->imm << endl;
	 else cout << endl;
	 it++;
	}*/
 /*cat2it=category3.begin();
 while(cat2it!=category3.end())
	{cout << cat2it.first << " means " << cat2it.second << endl;
 	 cat2it++;
	}
 string temp = "01101000111010001001001";
 cout << getsubstring(currinstruction2,4);*/
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

void display(const vector<instructions> &x, const int reg[])
{
 int i, j=0;
 cout << endl << "Registers" << endl;
 for(i=0; i<4; i++)
	{(i<2?cout << "R0" << i*8 <<":"  :cout << "R" << i*8 << ":");
	 for(j=0; j<8; j++)
		cout << TAB << reg[i*8+j];
	 cout << endl;
	}
 cout << endl << "Data" << endl;
 for(i=0; i<(x.size()-instructioncount)/8; i++)
	{cout << instructioncount*4+64+i*32 << ":";
	 for(j=0; j<8; j++)
		cout << TAB << x[instructioncount+(i*8+j)].value;
	 if(i!=(x.size()-instructioncount)/8-1) cout << endl;
	}
}

void execute(vector<instructions> &x)
{int cycle=1, breakflag=0;
 vector<instructions> :: iterator counter;
 int reg[32];
 memset(reg, 0, sizeof(reg));
 string temp;
 counter=x.begin();
 while(breakflag==0)
	{temp=counter->todo;
	 cout << "--------------------" << endl; 
	 cout << "Cycle:" << cycle++ << TAB << (counter-x.begin())*4+64 << TAB;
	 (temp=="NOP" || temp=="BREAK"?cout << temp : cout << temp << TAB);
	 if(counter->type==1)
		{
		 if(temp=="J")
			 {cout << "#" << counter->target << endl;
			  counter=x.begin()+(((counter->target)-64)/4);
			  }
		 else if(temp=="BEQ")
			{cout << R counter->rs << COMMA << R counter->rt << COMMA << "#" << counter->offset <<endl;
			 if(reg[counter->rs]==reg[counter->rt])
				counter += ((counter->offset)/4 +1);
			 else counter++;
			}
		 else if(temp=="BNE")
			{cout << R counter->rs << COMMA << R counter->rt << COMMA << "#" << counter->offset <<endl;
			 if(reg[counter->rs]!=reg[counter->rt])
				counter += ((counter->offset)/4 +1);
			 else counter++;
			}

		 else if(temp=="BGTZ")
			{cout << R counter->rs << COMMA << "#" << counter->offset <<endl;
			 if(reg[counter->rs]>0)
				counter += ((counter->offset)/4 +1);
			 else counter++;
			}
		 else if(temp=="BGEZ")
			{cout << R counter->rs << COMMA << "#" << counter->offset <<endl;
			 if(reg[counter->rs]>=0)
				counter += ((counter->offset)/4 +1);
			 else counter++;
			}
		 else if(temp=="BLTZ")
			{cout << R counter->rs << COMMA << "#" << counter->offset <<endl;
			 if(reg[counter->rs]<0)
				counter += ((counter->offset)/4 +1);
			 else counter++;
			}

		 else if(temp=="BLEZ")
			{cout << R counter->rs << COMMA << "#" << counter->offset <<endl;
			 if(reg[counter->rs]<=0)
				counter += ((counter->offset)/4 +1);
			 else counter++;
			}
		 else if(temp=="LW")
			{cout << R counter->rt << COMMA << counter->offset << "(" << R counter->base << ")" << endl;
			 reg[counter->rt]=x[(reg[counter->base]+counter->offset - 64)/4].value;
			 counter++;
			}
		 else if(temp=="SW")
			{cout << R counter->rt << COMMA << counter->offset << "(" << R counter->base << ")" << endl;
			 x[(reg[counter->base]+counter->offset-64)/4].value=reg[counter->rt];
			 counter++;
			}
		 else if(temp=="SLL")
			{cout << R counter->rd << COMMA << R counter->rt << COMMA << "#" << counter->sa << endl;
			 reg[counter->rd]=(reg[counter->rt]<<counter->sa);
			 counter++;
			}
		 else if(temp=="SRL")
			{cout << R counter->rd << COMMA << R counter->rt << COMMA << "#" << counter->sa << endl;
		 	 if(reg[counter->rt]<0)
				reg[counter->rd]=(((-1)*reg[counter->rt])&(1<<(31-counter->sa)));
			 else
			 	reg[counter->rd]=(reg[counter->rt]>>counter->sa);
			 counter++;
			}
		 else if(temp=="SRA")
			{cout << R counter->rd << COMMA << R counter->rt << COMMA << "#" << counter->sa << endl;
			 reg[counter->rd]=(reg[counter->rt]>>counter->sa);
			 counter++;
			}
		 else if(temp=="JR")
			{cout << R counter->rs << endl;
			 counter=x.begin()+((reg[counter->rs])/4-16);
			}
		 else if(temp!="NOP" && temp!="BREAK")
			cout << "Invalid command";
		 else if(temp=="BREAK")
			{breakflag=1; counter++; cout<<endl;}
		 else {counter++;cout << endl;}
		}
	 else if(counter->type==2)
		{if(temp=="ADD")
			reg[counter->rd]=reg[counter->rs]+reg[counter->rt];
		 else if(temp=="SUB")
		 	reg[counter->rd]=reg[counter->rs]-reg[counter->rt];
		 else if(temp=="MUL")
			reg[counter->rd]=reg[counter->rs]*reg[counter->rt];
		 else if(temp=="OR")
		 	reg[counter->rd]=reg[counter->rs]|reg[counter->rt];
		 else if(temp=="XOR")
			reg[counter->rd]=reg[counter->rs]^reg[counter->rt];
		 else if(temp=="NOR")
		 	reg[counter->rd]=~(reg[counter->rs]|reg[counter->rt]);
		 else if(temp=="AND")
			reg[counter->rd]=reg[counter->rs]&reg[counter->rt];
		 else if(temp=="SLT")
			reg[counter->rd]=reg[counter->rs]<reg[counter->rt];
		 else cout << "Invalid command";
		 cout << R counter->rd << COMMA << R counter->rs << COMMA << R counter->rt << endl;
		 counter++;
		}
	 else if(counter->type==3)
		{
		 if(temp=="ADDI")
			reg[counter->rt]=reg[counter->rs]+(counter->imm);
		 else if(temp=="ORI")
		 	reg[counter->rt]=reg[counter->rs]|(counter->imm);
		 else if(temp=="XORI")
			{reg[counter->rt]=reg[counter->rs]^(counter->imm);}
		 else if(temp=="ANDI")
			reg[counter->rt]=reg[counter->rs]&(counter->imm);
		 else if(temp=="MOVZ")
			if(reg[counter->rt]==0) reg[counter->rs]=(counter->imm);
		 else cout << "Invalid command";
		 cout << R counter->rt << COMMA << R counter->rs << COMMA << "#" << counter->imm << endl;
		 counter++;
		}
	 else cout << "Command not recognized";
 	 display(x, reg);
	 if(breakflag==0)
		cout << endl;
	}
} 	



