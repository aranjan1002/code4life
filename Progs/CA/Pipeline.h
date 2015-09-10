#include <iostream>
#include <fstream>

#include "Buffer.h"

#define COMMA ", "
#define TAB "\t"
#define R "R" <<

using namespace std;

class IF;
class Issue;
class ALU;
class WB;
class MEM;
class Pre_IssueBuffer;
class Pre_ALUBuffer;
class Post_ALUBuffer;
class Post_MemBuffer;

class instructions
{ 
 public:
 int rs, rt, rd, offset, base, sa, hint, imm, type, target, value;
 bool branch;
 string todo;
 instructions(){todo=""; branch=false;}
 int findDestination();
 int findSource1();
 int findSource2();
};

class calculatedInstructions
{
 public:
 instructions theinstruction;
 int destination;
 int value;
 calculatedInstructions(){theinstruction.todo="";};
};

class Pipeline
{
 public: 

 int reg_status[32];
 int reg[32];
 IF *fetchUnit;
 Issue *issueUnit;
 ALU *aluUnit;
 WB *writebackUnit;
 MEM *memUnit;
 int instructioncount;
 vector<instructions> memory;
 vector<instructions> :: iterator PC;
 Buffer <instructions> *preIssueBuffer;
 Buffer <instructions> *preALUBuffer;
 Buffer <calculatedInstructions> *postALUBuffer;
 Buffer <calculatedInstructions> *postMEMBuffer;
 
 Pipeline(){};
 Pipeline(vector<instructions> &allinstructions, int count) ;
 void execute(char *);
 void setMemory(int, int);
 void displayBuffers();
 void displayRegisters();
 void displayDataMemory();
};

class WB
{
 public:
 void execute(Pipeline *pipe);
};

class MEM
{
 public:
 void execute(Pipeline *pipe);
};

class Issue
{
 public:
 void execute(Pipeline *pipe);
 bool checkRAW(int index, Pipeline *pipe);
};

class ALU
{
 public:
 void execute(Pipeline *pipe);
};

class IF
{
 public:
 bool isStalled;
 bool isBranchInstruction;
 instructions stalledInstruction;
 IF(){isStalled=false; isBranchInstruction=false;};
 bool findisStalled(int source1, int source2, Pipeline *pipe);
 bool findisStalled(int source1, Pipeline *pipe);
 bool execute(Pipeline *pipe);
};

//Auxilliary functions

ostream& operator<<(ostream &os, const instructions instr)
{
 if(instr.type==1)
	{os << instr.todo;
	 if(instr.todo!="BREAK" && instr.todo!="NOP")
 	 os << "\t";
	 if(instr.todo=="J")
		{
		 os << "#" << instr.target;
		}
	 else if(instr.todo=="BEQ" || instr.todo=="BNE")
		{
		 os << R instr.rs << COMMA << R instr.rt << COMMA << "#" << instr.offset;
		}
	 else if(instr.todo=="BGTZ" || instr.todo=="BGEZ" || instr.todo=="BLTZ" || instr.todo=="BLEZ")
		{
		 os << R instr.rs << COMMA << "#" << instr.offset;
		}
	 else if(instr.todo=="BREAK")
		{
		 os;
		}
	 else if(instr.todo=="SW" || instr.todo=="LW")
		{
		 os << R instr.rt << COMMA << instr.offset << "(" << R instr.base << ")";
		}
	 else if(instr.todo=="SLL" || instr.todo=="SRL" || instr.todo=="SRA")
		{
		 os << R instr.rd << COMMA << R instr.rt << COMMA << "#" << instr.sa;
		}
	 else if(instr.todo=="NOP")
		os;
	 else if(instr.todo=="JR")
		{
		 os << R instr.rs;
		}
	}
 else if(instr.type==2)
	{os << instr.todo << TAB;
	 os << R instr.rd << COMMA << R instr.rs << COMMA << R instr.rt;
	}
 else if(instr.type==3)
	{
	 os << instr.todo << TAB;
	 os << R instr.rt << COMMA << R instr.rs << COMMA << "#" << instr.imm;
	}
 return os;
}

ostream& operator<<(ostream &os, const calculatedInstructions execins)
{
 instructions instr=execins.theinstruction;
 //os << execins.value;
 if(instr.type==1)
	{os << instr.todo;
	 if(instr.todo!="BREAK" && instr.todo!="NOP")
 	 os << "\t";
	 if(instr.todo=="J")
		{
		 os << "#" << instr.target;
		}
	 else if(instr.todo=="BEQ" || instr.todo=="BNE")
		{
		 os << R instr.rs << COMMA << R instr.rt << COMMA << "#" << instr.offset;
		}
	 else if(instr.todo=="BGTZ" || instr.todo=="BGEZ" || instr.todo=="BLTZ" || instr.todo=="BLEZ")
		{
		 os << R instr.rs << COMMA << "#" << instr.offset;
		}
	 else if(instr.todo=="BREAK")
		{
		 os;
		}
	 else if(instr.todo=="SW" || instr.todo=="LW")
		{
		 os << R instr.rt << COMMA << instr.offset << "(" << R instr.base << ")";
		}
	 else if(instr.todo=="SLL" || instr.todo=="SRL" || instr.todo=="SRA")
		{
		 os << R instr.rd << COMMA << R instr.rt << COMMA << "#" << instr.sa;
		}
	 else if(instr.todo=="NOP")
		os;
	 else if(instr.todo=="JR")
		{
		 os << R instr.rs;
		}
	}
 else if(instr.type==2)
	{os << instr.todo << TAB;
	 os << R instr.rd << COMMA << R instr.rs << COMMA << R instr.rt;
	}
 else if(instr.type==3)
	{
	 os << instr.todo << TAB;
	 os << R instr.rt << COMMA << R instr.rs << COMMA << "#" << instr.imm;
	}
 return os;
}



//Functions of class instructions

int instructions :: findDestination()
	{if(branch==true || todo=="SW" || todo=="BREAK" || todo=="NOP" || todo=="")
		return -1;
	 if((type==3 && todo!="MOVZ") || todo=="LW")
		return rt;
	 if(todo=="MOVZ")
		return rs;
	 return rd;
	}

int instructions :: findSource1()
	{
	 if(todo=="J" || todo=="BREAK" || todo=="NOP" || todo=="")
		return -1;
	 if(todo=="LW")
		return base;
	 if(todo=="SW" || todo=="SLL" || todo=="SRL" || todo=="MOVZ")
		return rt;
	 return rs;
	}

int instructions :: findSource2()
	{
	 if(type==2 || todo=="BEQ" || todo=="BNE" || todo=="")
		return rt;
	 if(todo=="SLL" || todo=="SRL" || todo=="SRA")
		return sa;
	 return -1;
	}	

//Functions of class Pipeline

Pipeline :: Pipeline(vector<instructions> &allinstructions, int count)
{fetchUnit=new IF();
 instructioncount=count;
 preIssueBuffer=new Buffer<instructions>(3);
 preALUBuffer=new Buffer<instructions>(2);
 postALUBuffer=new Buffer<calculatedInstructions>(1);
 postMEMBuffer=new Buffer<calculatedInstructions>(1);
 issueUnit=new Issue();
 aluUnit=new ALU();
 writebackUnit=new WB();
 memUnit=new MEM();
 memset(reg, 0, sizeof(reg));
 memset(reg, 0, sizeof(reg_status));
 memory=allinstructions;
 PC=memory.begin();
}

void Pipeline :: displayBuffers()
{
 cout << "IF Unit:" << endl;
 cout << TAB << "Waiting Instruction: ";
 if(fetchUnit->isStalled==true)
	cout << *PC;
 cout << endl; 
 cout << TAB << "Executed Instruction: "; 
 if(fetchUnit->isBranchInstruction==true && fetchUnit->isStalled==false)
	cout << fetchUnit->stalledInstruction;
 cout << endl;
 cout << "Pre-Issue Buffer:" << endl;
 cout << TAB << "Entry 0:";
 if(preIssueBuffer->isElement(1))
 	{cout << "[";
	 cout << preIssueBuffer->peek(1);
	 cout << "]";
	}
 cout << endl;
 cout << TAB << "Entry 1:";
 if(preIssueBuffer->isElement(2))
 	{cout << "[";
	 cout << preIssueBuffer->peek(2);
	 cout << "]";}
 cout << endl;
 cout << TAB << "Entry 2:";
 if(preIssueBuffer->isElement(3))
 	{cout << "[";
	 cout << preIssueBuffer->peek(3);
	 cout << "]";} 
 cout << endl;
 cout << "Pre-ALU Queue:" << endl;
 cout << TAB << "Entry 0:";
 if(preALUBuffer->isElement(1))
	{cout << "[";
 	 cout << preALUBuffer->peek(1);
	 cout << "]";
	}
 cout << endl;
 cout << TAB << "Entry 1:";
 if(preALUBuffer->isElement(2))
 	{cout << "[";
	 cout << preALUBuffer->peek(2);
	 cout << "]";
	}
 cout << endl;
 cout << "Post-ALU Buffer:";
 if(postALUBuffer->isElement(1))
 	{cout << "[";
	 cout << postALUBuffer->peek(1);
	 cout << "]";
	}
 cout << endl;
 cout << "Post-MEM Buffer:";
 if(postMEMBuffer->isElement(1))
 	{cout << "[";
	 cout << postMEMBuffer->peek(1);
	 cout << "]";
	}
 cout << endl;
}

void Pipeline :: displayRegisters()
{
 int i, j=0;
 cout << "Registers" << endl;
 for(i=0; i<4; i++)
	{(i<2?cout << "R0" << i*8 <<":"  :cout << "R" << i*8 << ":");
 	 for(j=0; j<8; j++)
		cout << TAB << reg[i*8+j];
	 cout << endl;
	}
}

void Pipeline :: displayDataMemory()
{
 int i, j;
 cout << endl << "Data" << endl;
 //cout << memory.size() << endl;
 for(i=0; i<(memory.size()-instructioncount)/8; i++)
	{cout << instructioncount*4+64+i*32 << ":";
	 for(j=0; j<8; j++)
		cout << TAB << memory[instructioncount+(i*8+j)].value;
	 if(i!=(memory.size()-instructioncount)/8-1) cout << endl;
	}
}

void Pipeline :: execute(char* outfile)
{bool flag=1, newlineflag=0;
 int cycle=1;
 freopen(outfile, "w", stdout);
 //cout << "Pipeline Started";
 while(flag)
	{if(newlineflag)
		cout << endl;
	 else newlineflag=1;
	 flag=fetchUnit->execute(this);
	 cout << "--------------------" << endl;
 	 cout << "Cycle:" << cycle++ << endl << endl;
	 displayBuffers();
        cout << endl;
 	 displayRegisters();
 	 displayDataMemory();
	 //PC++;
	}
}

void Pipeline :: setMemory(int index, int val)
{
 memory[index].value=val;
} 

//Functions of class WB

void WB :: execute(Pipeline *pipe)
	{//cout << "In Write Back Stage" << endl;
	 calculatedInstructions temp;
	 if(!pipe->postMEMBuffer->isEmpty())
		{temp=pipe->postMEMBuffer->pop();
		 pipe->reg[temp.destination]=temp.value;
		 pipe->reg_status[temp.destination]=false;
		}
	 if(!pipe->postALUBuffer->isEmpty())
		if(pipe->postALUBuffer->peek(1).theinstruction.todo!="LW" && pipe->postALUBuffer->peek(1).theinstruction.todo!="SW")
		{temp=pipe->postALUBuffer->pop();
		 pipe->reg[temp.destination]=temp.value;
		 pipe->reg_status[temp.destination]=false;
		}
	}

//Functions of class MEM

void MEM :: execute(Pipeline *pipe)
	{//cout << "In Memory Stage" << endl;
	 calculatedInstructions temp;
 	 if(!pipe->postALUBuffer->isEmpty())
		{if((pipe->postALUBuffer->peek(1)).theinstruction.todo=="SW" || (pipe->postALUBuffer->peek(1)).theinstruction.todo=="LW")
			{temp=pipe->postALUBuffer->peek(1);
			 if(temp.theinstruction.todo=="SW")
				{pipe->setMemory(temp.destination, temp.value);
				 temp.theinstruction.todo="";
				}
			 else temp.value=pipe->memory[temp.value].value;		 	}
		}
	 pipe->writebackUnit->execute(pipe);
	 if((pipe->postALUBuffer->peek(1)).theinstruction.todo=="SW" || (pipe->postALUBuffer->peek(1)).theinstruction.todo=="LW")
		 	{if(temp.theinstruction.todo!="")
			 	pipe->postMEMBuffer->push(temp);
			 pipe->postALUBuffer->pop();
			}
	}

//Functions of class ALU

void ALU :: execute(Pipeline *pipe)
	{//cout << "In ALU Stage" << endl;
	 calculatedInstructions temp;
	 static bool exec_again=false;
	 if(!pipe->preALUBuffer->isEmpty())
		{temp.theinstruction=pipe->preALUBuffer->peek(1);
		 if(temp.theinstruction.type==1)
			{
	 		 if(temp.theinstruction.todo=="LW")
				{temp.destination=temp.theinstruction.findDestination();
		 		 temp.value=(pipe->reg[temp.theinstruction.base]+temp.theinstruction.offset - 64)/4;
				}
	 	 	 else if(temp.theinstruction.todo=="SW")
				{temp.destination=(pipe->reg[temp.theinstruction.base]+temp.theinstruction.offset-64)/4;
		 	 	 temp.value=pipe->reg[temp.theinstruction.rt];
				}
	 	 	 else if(temp.theinstruction.todo=="SLL")
				{temp.destination=temp.theinstruction.findDestination();
		 	 	 temp.value=(pipe->reg[temp.theinstruction.rt]<<temp.theinstruction.sa);
				 if(exec_again==false)
					exec_again=true;
				 else exec_again=false;
				}
	 	 	 else if(temp.theinstruction.todo=="SRL")
				{temp.destination=temp.theinstruction.findDestination();
	 	 	 	 if(pipe->reg[temp.theinstruction.rt]<0)
					{
					 int z=2147483647;
 					 z=z&(pipe->reg[temp.theinstruction.rt]);
 					 z=z>>temp.theinstruction.sa;
 					 z=z|(1<<31-temp.theinstruction.sa);
					 temp.value=z;
					}
		 	 	 else
		 			temp.value=(pipe->reg[temp.theinstruction.rt]>>temp.theinstruction.sa);
				 if(exec_again==false)
					exec_again=true;
				 else exec_again=false;
				}
	 	 	 else if(temp.theinstruction.todo=="SRA")
				{temp.destination=temp.theinstruction.findDestination();
		 	 	 temp.value=(pipe->reg[temp.theinstruction.rt]>>temp.theinstruction.sa); 
				 if(exec_again==false)
					exec_again=true;
				 else exec_again=false;
				}
			}
 		else if(temp.theinstruction.type==2)
			{temp.destination=temp.theinstruction.findDestination();
	 	 	 if(temp.theinstruction.todo=="ADD")
		 		temp.value=pipe->reg[temp.theinstruction.rs]+pipe->reg[temp.theinstruction.rt];
	 		 else if(temp.theinstruction.todo=="SUB")
	 			temp.value=pipe->reg[temp.theinstruction.rs]-pipe->reg[temp.theinstruction.rt];
	 		 else if(temp.theinstruction.todo=="MUL")
				{temp.value=pipe->reg[temp.theinstruction.rs]*pipe->reg[temp.theinstruction.rt];
				 if(exec_again==false)
					exec_again=true;
				 else exec_again=false;
				}
		 	 else if(temp.theinstruction.todo=="OR")
	 			temp.value=pipe->reg[temp.theinstruction.rs]|pipe->reg[temp.theinstruction.rt];
			 else if(temp.theinstruction.todo=="XOR")
				temp.value=pipe->reg[temp.theinstruction.rs]^pipe->reg[temp.theinstruction.rt];
			 else if(temp.theinstruction.todo=="NOR")
	 			temp.value=~(pipe->reg[temp.theinstruction.rs]|pipe->reg[temp.theinstruction.rt]);
			 else if(temp.theinstruction.todo=="AND")
				temp.value=pipe->reg[temp.theinstruction.rs]&pipe->reg[temp.theinstruction.rt];
	 		 else if(temp.theinstruction.todo=="SLT")
				temp.value=pipe->reg[temp.theinstruction.rs]<pipe->reg[temp.theinstruction.rt];
			}
 		else if(temp.theinstruction.type==3)
			{temp.destination=temp.theinstruction.findDestination();
			 if(temp.theinstruction.todo=="ADDI")
				temp.value=pipe->reg[temp.theinstruction.rs]+(temp.theinstruction.imm);
			 else if(temp.theinstruction.todo=="ORI")
	 			temp.value=pipe->reg[temp.theinstruction.rs]|(temp.theinstruction.imm);
			 else if(temp.theinstruction.todo=="XORI")
				{temp.value=pipe->reg[temp.theinstruction.rs]^(temp.theinstruction.imm);}
	 		 else if(temp.theinstruction.todo=="ANDI")
				temp.value=pipe->reg[temp.theinstruction.rs]&(temp.theinstruction.imm);
			 else if(temp.theinstruction.todo=="MOVZ")
				if(temp.destination==0) {temp.destination=temp.theinstruction.rs;
						    	    temp.value=(temp.theinstruction.imm);}
				else temp.theinstruction.todo="";
			}
		 else {temp.theinstruction.todo="";
			pipe->preALUBuffer->pop();
			}
		}
	 pipe->memUnit->execute(pipe);
	 if(temp.theinstruction.todo!="" && exec_again==false)
		 {pipe->preALUBuffer->pop();
		  pipe->postALUBuffer->push(temp);
		 }
	}

//Functions of class Issue

void Issue :: execute(Pipeline *pipe)
	{//cout << "In Issue Stage" << endl;
	 instructions temp;
	 int index=1, i;
	 if(!pipe->preIssueBuffer->isEmpty() && !pipe->preALUBuffer->isFull())
		for(i=1; i<=3; i++)	
			{if(pipe->preIssueBuffer->isElement(i) && (checkRAW(i, pipe)==false))
				{temp=pipe->preIssueBuffer->peek(i);
				 index=i;
				 break;
				}
			}
	 pipe->aluUnit->execute(pipe);
	 if(temp.todo!="")
		 	{pipe->preALUBuffer->push(pipe->preIssueBuffer->pop(index));
			 int destination=temp.findDestination();
			 if(destination>=0)
				pipe->reg_status[destination]=true;
			}
	}

bool Issue :: checkRAW(int index, Pipeline *pipe)
	{
	 int i, destination, source1, source2;
	 bool storeflag=false;
	 instructions temp=pipe->preIssueBuffer->peek(index);
 	 if(temp.branch==true)
		return true;
	 source1=temp.findSource1();
	 source2=temp.findSource2();
	 destination=temp.findDestination();
 	 if((source1!=-1 && pipe->reg_status[source1]==true) || (source2!=-1 && pipe->reg_status[source2]==true))
		{//cout << "Register still under execution" << endl;
		 return true;
		}
	 //cout << "passed register test" << endl;
	 for(i=index-1; i>0; i--)
		{if(pipe->preIssueBuffer->peek(i).todo=="SW")
			storeflag=true;
		 if((source1!=-1 && (pipe->preIssueBuffer->peek(i)).findDestination()==source1) || (source2!=-1 && pipe->preIssueBuffer->peek(i).findDestination()==source2))
			return true;
		 //cout << "passed RAW test" << endl;
		 if((destination!=-1 && (pipe->preIssueBuffer->peek(i)).findSource1()==destination) || (destination!=-1 && pipe->preIssueBuffer->peek(i).findSource2()==destination))
			return true;
		 //cout << "passed WAR test" << endl;
		 if((destination!=-1 && (pipe->preIssueBuffer->peek(i)).findDestination()==destination))
			return true;
		 //cout << "passed WAW test" << index << endl;
		}
	 if(storeflag==true && (temp.todo=="SW" || temp.todo=="LW"))
		return true;
	 return false;
	}

//Functions of class IF

bool IF :: findisStalled(int source1, int source2, Pipeline *pipe)
	{
	 if((pipe->preIssueBuffer->isElement(1) && source1==pipe->preIssueBuffer->peek(1).findDestination()) || (pipe->preIssueBuffer->isElement(2) && source1==pipe->preIssueBuffer->peek(2).findDestination())  || (pipe->preIssueBuffer->isElement(3) && source1==pipe->preIssueBuffer->peek(3).findDestination()) )
		return(true);
	 if((pipe->preIssueBuffer->isElement(1) && source2==pipe->preIssueBuffer->peek(1).findDestination()) || (pipe->preIssueBuffer->isElement(2) && source2==pipe->preIssueBuffer->peek(2).findDestination())  || (pipe->preIssueBuffer->isElement(3) && source2==pipe->preIssueBuffer->peek(3).findDestination()) )
		return(true);
	 if(pipe->reg_status[source1]==true || pipe->reg_status[source2]==true)
		return(true);
	 return false;
	}

bool IF :: findisStalled(int source1, Pipeline *pipe)
	{if((pipe->preIssueBuffer->isElement(1) && source1==pipe->preIssueBuffer->peek(1).findDestination()) || (pipe->preIssueBuffer->isElement(2) && source1==pipe->preIssueBuffer->peek(2).findDestination())  || (pipe->preIssueBuffer->isElement(3) && source1==pipe->preIssueBuffer->peek(3).findDestination()) )
		return(true);
	 if(pipe->reg_status[source1]==true)
		return(true);
	 return false;
	}

bool IF :: execute(Pipeline *pipe)
	{instructions temp;
	 //cout << "Present instruction " << *(pipe->PC) << endl;
	 if(pipe->PC->todo!="BREAK")
		{isBranchInstruction=(*(pipe->PC)).branch;
		 if(!pipe->preIssueBuffer->isFull())
			{temp=*(pipe->PC);
			 if(temp.branch==true)
				{if(temp.todo=="J")
			 		pipe->PC=pipe->memory.begin()+(((temp.target)-64)/4);
		
			 	 else if(temp.todo=="BEQ")
				 	{isStalled=findisStalled(temp.rs, temp.rt, pipe);
				  	 if(!isStalled)
				  	 	if(pipe->reg[temp.rs]==pipe->reg[temp.rt])
							pipe->PC += ((temp.offset)/4 +1);
				  		else pipe->PC++;
				 	}
		 		 else if(temp.todo=="BNE")
					{isStalled=findisStalled(temp.rs, temp.rt, pipe);
				 	 if(!isStalled)
			 	 		if(pipe->reg[temp.rs]!=pipe->reg[temp.rt])
							pipe->PC += ((temp.offset)/4 +1);
			 	 	 	else pipe->PC++;
					}
			 	 else if(temp.todo=="BGTZ")
					{isStalled=findisStalled(temp.rs, pipe);
				 	 if(!isStalled)
			 	 		if(pipe->reg[temp.rs]>0)
							pipe->PC += ((temp.offset)/4 +1);
			 	 		else pipe->PC++;
					}
		 	 	 else if(temp.todo=="BGEZ")
					{isStalled=findisStalled(temp.rs, pipe);
					 if(!isStalled)
			 	 		if(pipe->reg[temp.rs]>=0)
							pipe->PC += ((temp.offset)/4 +1);
			 	 		else pipe->PC++;
					}
		 	 	 else if(temp.todo=="BLTZ")
					{isStalled=findisStalled(temp.rs, pipe);
				 	 if(!isStalled)
			 	 		if(pipe->reg[temp.rs]<0)
							pipe->PC += ((temp.offset)/4 +1);
			 	 		else pipe->PC++;
					}
		 	 	 else if(temp.todo=="BLEZ")
					{isStalled=findisStalled(temp.rs, pipe);
					 if(!isStalled)
			 	 		if(pipe->reg[temp.rs]<=0)
							pipe->PC += ((temp.offset)/4 +1);
			 	 		else pipe->PC++;
					}
		 	 	 else if(temp.todo=="JR")
					{isStalled=findisStalled(temp.rs, pipe);
		 	 		 if(!isStalled)
					 	 pipe->PC=pipe->memory.begin()+((pipe->reg[temp.rs])/4-16);
					}
				}
			  else if(temp.todo=="NOP")
				{isBranchInstruction=true;
				 stalledInstruction=temp;
				 pipe->PC++;
				}
			  else pipe->PC++;
			}
	   	 stalledInstruction=temp;
		 pipe->issueUnit->execute(pipe);
		 //cout << "checking " << temp << " " << temp.todo << endl;
		 if(temp.todo!="" && isStalled!=true && temp.branch==false && temp.todo!="NOP")
		 	{//cout << "Pushing" << endl;
			 pipe->preIssueBuffer->push(temp);
			}
		 return true;
		}
	 else {isBranchInstruction=true;
		stalledInstruction=*(pipe->PC);
		if(pipe->preIssueBuffer->isEmpty() && pipe->preALUBuffer->isEmpty() && pipe->postALUBuffer->isEmpty() && pipe->postMEMBuffer->isEmpty())
			{isStalled=false;
			 return false;
			}
		else {isStalled=true;
		      pipe->issueUnit->execute(pipe);
		      return true;
		     }
		}
	};

