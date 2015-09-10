#include <stdio.h>
#include <conio.h>
#include <limits.h>
#define e 0.000001

int top[100][100];
int bot[100][100];
int best[100][100];
int wlev, H, W;
  
struct SStat{
  int x, y;
  int t;
};
struct SQ{
  SStat s[1000000];
  int idpop, idpush;
};

SQ q;

void push(SStat s1){
  q.s[q.idpush] = s1;
  q.idpush++;
  if(q.idpush == 1000000) q.idpush = 0;
}
void pop(SStat &s2){
  s2 = q.s[q.idpop];
  q.idpop++;
  if(q.idpop == 1000000) q.idpop = 0;
}
void rec(int x, int y){
  if(best[y][x] == 0) return;
  SStat s;
  s.y = y; s.x = x; s.t = 0;
  push(s);
  best[y][x] = 0;
  if(x > 0 && best[y][x-1] != 0){
    if(top[y][x] - bot[y][x-1] >= 50 && top[y][x-1] - bot[y][x] >= 50 && top[y][x-1] - bot[y][x-1] >= 50 && top[y][x-1] - wlev >= 50){
      rec(x-1, y);
    }
  }
  if(y > 0 && best[y-1][x] != 0){
    if(top[y][x] - bot[y-1][x] >= 50 && top[y-1][x] - bot[y][x] >= 50 && top[y-1][x] - bot[y-1][x] >= 50 && top[y-1][x] - wlev >= 50){
      rec(x, y-1);
    }
  }
  if(x < W-1 && best[y][x+1] != 0){
    if(top[y][x] - bot[y][x+1] >= 50 && top[y][x+1] - bot[y][x] >= 50 && top[y][x+1] - bot[y][x+1] >= 50 && top[y][x+1] - wlev >= 50){
      rec(x+1, y);
    }
  }
  if(y < H-1 && best[y+1][x] != 0){
    if(top[y][x] - bot[y+1][x] >= 50 && top[y+1][x] - bot[y][x] >= 50 && top[y+1][x] - bot[y+1][x] >= 50 && top[y+1][x] - wlev >= 50){
      rec(x, y+1);
    }
  }
}

int main(){
  int jcase;
  freopen("B-large.in", "r", stdin);
  freopen("B-large.out", "w", stdout);
  
  
  scanf("%d", &jcase);
  q.idpop = q.idpush = 0;
  for(int icase=0; icase<jcase; icase++){
    scanf("%d %d %d", &wlev, &H, &W);
    for(int i=0; i<H; i++){
      for(int j=0; j<W; j++){
        scanf("%d", &top[i][j]);
      }
    }
    for(int i=0; i<H; i++){
      for(int j=0; j<W; j++){
        scanf("%d", &bot[i][j]);
      }
    }
    
    for(int i=0; i<H; i++){
      for(int j=0; j<W; j++){
        best[i][j] = INT_MAX;
      }
    }
    
    SStat s, s2;
    s.x = 0; s.y = 0; s.t = 0;
    
    rec(0, 0);
    
    while(q.idpop < q.idpush){
      pop(s);
      int t = s.t;
      int y = s.y, x = s.x;
      int t2=t;
      //      printf("x=%d y=%d t=%d\n", x, y, t);
      if(t > best[y][x]) continue;
      if(x > 0 && best[y][x-1] != 0){
        if(top[y][x] - bot[y][x-1] >= 50 && top[y][x-1] - bot[y][x] >= 50 && top[y][x-1] - bot[y][x-1] >= 50){
          if(top[y][x-1] - wlev + t < 50) t = -(top[y][x-1] - wlev - 50);
          s2.x = x-1;
          s2.y = y;
          if(wlev - t - bot[y][x] >= 20) s2.t = t+10;
          else s2.t = t+100;
          if(best[y][x-1] > s2.t) {
            best[y][x-1] = s2.t;
            push(s2);
          }
        }
      }
      t=t2;
      if(y > 0 && best[y-1][x] != 0){
        if(top[y][x] - bot[y-1][x] >= 50 && top[y-1][x] - bot[y][x] >= 50 && top[y-1][x] - bot[y-1][x] >= 50){
          if(top[y-1][x] - wlev + t < 50) t = -(top[y-1][x] - wlev - 50);
          s2.x = x;
          s2.y = y-1;
          if(wlev - t - bot[y][x] >= 20) s2.t = t+10;
          else s2.t = t+100;
          if(best[y-1][x] > s2.t) {
            best[y-1][x] = s2.t;
            push(s2);
          }
        }
      }
      t=t2;
      if(x < W-1 && best[y][x+1] != 0){
        if(top[y][x] - bot[y][x+1] >= 50 && top[y][x+1] - bot[y][x] >= 50 && top[y][x+1] - bot[y][x+1] >= 50){
          if(top[y][x+1] - wlev + t < 50) t = -(top[y][x+1] - wlev - 50);
          s2.x = x+1;
          s2.y = y;
          if(wlev - t - bot[y][x] >= 20) s2.t = t+10;
          else s2.t = t+100;
          if(best[y][x+1] > s2.t) {
            best[y][x+1] = s2.t;
            push(s2);
          }
        }
      }
      t=t2;
      if(y < H-1 && best[y+1][x] != 0){
        if(top[y][x] - bot[y+1][x] >= 50 && top[y+1][x] - bot[y][x] >= 50 && top[y+1][x] - bot[y+1][x] >= 50){
          if(top[y+1][x] - wlev + t < 50) t = -(top[y+1][x] - wlev - 50);
          s2.x = x;
          s2.y = y+1;
          if(wlev - t - bot[y][x] >= 20) s2.t = t+10;
          else s2.t = t+100;
          if(best[y+1][x] > s2.t) {
            best[y+1][x] = s2.t;
            push(s2);
          }
        }
      }
    }
    
    //    for(int i=0; i<H; i++){
    //      for(int j=0; j<W; j++) printf("%d.%d", best[i][j]/10, best[i][j]%10);
    //      puts("");
    //    }
    printf("Case #%d: %d.%d\n", icase+1, best[H-1][W-1]/10, best[H-1][W-1]%10);
  }
}
