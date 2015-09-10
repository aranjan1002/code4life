#include <stdio.h>
#include <stdlib.h>
#include "Fibo.h"

void test1()
{
        printf("[Test1] \n");
        FiboHeap fiboHeap;
        stHeapInfo heapInfo;

        heapInfo.nKey = 1;
        fiboHeap.Insert(heapInfo);

        heapInfo.nKey = 3;
        fiboHeap.Insert(heapInfo);

        heapInfo.nKey = 2;
        fiboHeap.Insert(heapInfo);

        heapInfo.nKey = 5;
        fiboHeap.Insert(heapInfo);

        heapInfo.nKey = 9;
        fiboHeap.Insert(heapInfo);

        fiboHeap.DumpHeap();

        // It should be 1
        fiboHeap.ExtractMin(&heapInfo);
        printf("Extrace min = %d\n", heapInfo.nKey);
        fiboHeap.DumpHeap();
        
        // It should be 2
        fiboHeap.ExtractMin(&heapInfo);
        printf("Extrace min = %d\n", heapInfo.nKey);
        fiboHeap.DumpHeap();
 
        // It should be 3
        fiboHeap.ExtractMin(&heapInfo);
        printf("Extrace min = %d\n", heapInfo.nKey);
        fiboHeap.DumpHeap();
 
        // It should be 5
        fiboHeap.ExtractMin(&heapInfo);
        printf("Extrace min = %d\n", heapInfo.nKey);
        fiboHeap.DumpHeap();
 
        // It should be 9
        fiboHeap.ExtractMin(&heapInfo);
        printf("Extrace min = %d\n", heapInfo.nKey);
        fiboHeap.DumpHeap();

        // Error handling 
        fiboHeap.ExtractMin(&heapInfo);
        fiboHeap.DumpHeap();
        printf("[Test1] Complete!\n");
}

void test2()
{
        printf("[Test2] \n");
        FiboHeap fiboHeap;
        stHeapInfo heapInfo;
        int maxTest = 100;
	 int i;
        srand(0);
        for (i = 0; i < maxTest; i++)
        {
                int nR = rand() % 100000;
                heapInfo.nKey = nR;

                fiboHeap.Insert(heapInfo);
        }
        fiboHeap.DumpHeap();
        for (i = 0; i < maxTest; i++)
        {
                fiboHeap.ExtractMin(&heapInfo);
                printf("Extrace min = %d\n", heapInfo.nKey);
                fiboHeap.DumpHeap();
        }
        printf("[Test2] Complete!\n");
}

int main()
{
        test1();
        test2();

        return 0;
}