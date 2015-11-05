#include<stdio.h>
#include<stdlib.h>
#include<vector>
#include<algorithm>
using std::vector;
using std::pair;
using std::make_pair;
using std::sort;
int main(void){
  int t,hh;
  scanf("%d",&t);
  for(hh=1;hh<=t;hh++){
    int k,v;
    scanf("%d%d",&k,&v);
    long long int ans=0;
    int i;
    ans+=k+1;
    for(i=2;i<=v+1;i++){
      int a=(i-2)*6+6;
      //printf("%d \n",a);
      ans+=(long long)a*(k+2-i);
    }
    printf("Case #%d: %lld\n",hh,ans);
  }
  return 0;
}

