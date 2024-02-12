#include <unistd.h>
#include <stdio.h>
#include <fcntl.h>
#include <string.h>
#include <sys/stat.h>



void execute_task(int argc, char *argv[]){
    pid_t pid = getpid();
    char p[50];
    sprintf(p, "task-%d", pid);
    if (mkfifo(p, 0644) != 0) {
            perror("Error creating fifo");
            _exit(-1);
    }

    int notebook_fd, read_fd;
    
    if ( (notebook_fd = open("notebook", O_WRONLY)) == -1){
        perror("Error opening notebook pipe");
        unlink(p);
        _exit(-1);
    } 
    write(notebook_fd, p, strlen(p));
    write(notebook_fd, " ", 1);
    for (int i = 1; i < argc; i++){
        write(notebook_fd, argv[i], strlen(argv[i]));
        write(notebook_fd, " ", 1);
    }
    close(notebook_fd);
    
    if ( (read_fd = open(p, O_RDONLY)) == -1){
        perror("Error opening server pipe 2");
        //unlink(p);
        _exit(-1);
    }
    char buffer;
    while( read(read_fd,&buffer,1) > 0) 
        write(1, &buffer, 1);
    close(read_fd);
    unlink(p);
}


int main(int argc, char *argv[]){
    if(argc == 1){
        write(1,"./bin/sdstore status\n", 21);
        write(1,"./bin/sdstore proc-file priority input-filename output-filename transformation-id-1 transformation-id-2 ...\n", 108);
    }
    else if ((argc == 2 && !strcmp("status", argv[1])) || (argc >= 5 && !strcmp("proc-file", argv[1]))){
        execute_task(argc,argv);
    }
    else write(1,"wrong arguments: try ./sdstore for help\n", 40);
    return 0;
}