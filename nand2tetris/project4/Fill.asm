// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.


(STAYWHITE)
	@24576
	D=M
	@STAYWHITE
	D, JEQ
	@8192
	D=A
	@counter
	M=D
	@FILLBLACK
	0, JMP
(FILLWHITE)
	@SCREEN
	A=A+D
	M=0
	@STAYWHITE
	D, JEQ
	@counter
	D=M
	M=M-1
	@FILLWHITE
	0, JMP
(STAYBLACK)
	@24576
	D=M
	@STAYBLACK
	D, JNE
	@8192	
	D=A
	@counter
	M=D
	@FILLWHITE
	0, JMP
(FILLBLACK)
	@SCREEN
	A=A+D
	M=-1
	@STAYBLACK
	D, JEQ
	@counter
	D=M
	M=M-1
	@FILLBLACK
	0, JMP
	