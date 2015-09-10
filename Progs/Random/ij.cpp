#include <sys/stat.h>
#include <time.h>
#include <stdio.h>

/*
 Take the difference between current time in seconds and access time of the
 users terminal device
*/

int main(int argc, char *argv[])
{
    struct stat f_info;
    /* terminal device for the user */
    const char *file_name = "/dev/pts/ptmx";
    stat(file_name, &f_info);

    printf("%d\n", (int)(time(NULL) - f_info.st_atime));
    return 0;
}
