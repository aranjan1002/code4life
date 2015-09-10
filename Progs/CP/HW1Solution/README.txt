HOW TO TEST YOUR PROJECT (AND HOW I TESTED IT)


Put the contents of your src folder into the src folder here. 

On a *NIX system that has Java and Ant installed, type


ant clean build TestPetersonLocks TestPetersonTest 


in the directory which has the "build.xml" file.


This would first "clean", then "build", then run the JUnit test files "TestPetersonLocks" followed by "TestPetersonTest".


At the end, you should see a summary of the number of failed test cases for each and the standard output and standard error streams printed.