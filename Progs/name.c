#include <stdio.h>
#include <string.h>
#include<conio.h>
void swap(char *x, char *y, int n)
{char z[100];
 //z = (char *) malloc (10);
 //void *z1 = malloc((size_t)n);
 //z = (char*)z1;
 strcpy(z,x);
 strcpy(x,y);
 strcpy(y,z);/*
 trans(z,x);
 trans(x,y);
 trans(y,z);*/
}

trans(char *x, char *y)
{int i;
 i=0;
 while(x[i])
	y[i]=x[i++];
 y[i]=0;
}

int main()
{
scanf("%d %d", &m, &n);
for(i=0;i<10;i++)
	solved[i]=0;
for(i=0;i<m;i++)
	{j=0;
 	 do
	 {scanf("%s", a[i]);
	  //if(a[i][j]==0)
		//a[i][j]=' ';
	 }
	 while(a[i][j]!=0);
	 a[i][j]=0;
	 printf("hello %s %d\n",a[i],m);
	 /*j=0;
	 count=0;
	 while(a[i][j])
	 if(a[i][j]=='1' || a[i][j]=='0')
		{count++;
	         if(a[i][j]==1)
		 	{points[i]++;
			 solved[count-1]++;
			}
		}*/	
	}

printf("i am");
for(i=0;i<m;i++)
	for(j=i+1;j<m;j++)
		{printf("hello %d\n", strcmp(a[i], a[j]));
		if(strcmp(a[i],a[j])==1)
			swap(a[i],a[j], n);
		}


for(i=0;i<m;i++)
printf("%s %d %d\n",a[i], points[i],solved[i]);

return 0;
}



