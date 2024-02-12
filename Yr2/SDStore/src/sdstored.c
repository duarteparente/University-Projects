#include <unistd.h>
#include <stdio.h>
#include <fcntl.h>
#include <string.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <sys/types.h>

typedef struct {
    char name[12];
    int max_limit;
    int usage_counter;
} *Tranformations;

typedef struct {
    char *fifo;
    char *arguments;
    int task_id;
    int priority;
    char *input;
    char *output;
    char **transformations;
    int transformations_counter;
    int state;
} *Task;

typedef struct {
    int nr;
    Task *tasks;
} *Task_Controller;


void termination(int signum){
    unlink("notebook");
    write(1,"\n",1);
    _exit(0);
}


ssize_t readln(int fd, char* line, ssize_t size) {
    ssize_t count_bytes = 0;
    while(count_bytes < size - 1) {
        ssize_t bytes_read = read(fd, &line[count_bytes], 1);
        if (bytes_read < 1 || line[count_bytes++] == '\n') break;
    }
    line[count_bytes] = 0;
    return count_bytes;
}

Tranformations read_config_file(char *config_filename){
    Tranformations transf = calloc(7, sizeof(*transf));

    int fd, index = 0;
    if ((fd = open(config_filename, O_RDONLY)) < 0) {
        perror("Couldn't open file");
        _exit(-1);
    }
    char *token, *line = malloc(100);
    while(readln(fd,line,100) > 0){
        token = strtok(line, " ");
        strcpy(transf[index].name, token);
        token = strtok(NULL, " ");
        transf[index++].max_limit = atoi(token);
    }
    close(fd);
    return transf;
}


void update_counter_transformations(Tranformations transf, char *name){
    for(int i=0; i<7; i++){
        if (!strcmp(name,transf[i].name)){
            transf[i].usage_counter++;
            break;
        }
    }
}


int transf_available(Tranformations transf, char *name){
    for(int i=0; i<7; i++){
        if (!strcmp(name,transf[i].name)){
            return (transf[i].usage_counter <= transf[i].max_limit);
        }
    }
    return 0;
}

void clean_transformations(Task task, Tranformations transf){
    for(int i=0; i<task->transformations_counter; i++){
        for(int j=0; j<7; j++){
            if (!strcmp(task->transformations[i],transf[j].name)){
                transf[j].usage_counter--;
            }
        }
    }
}

int get_max_usage(Tranformations transf){
    int counter = 0;
    for(int i=0; i<7; i++)
        counter += transf[i].max_limit;
    return counter;
}

int count_bytes (char *path){
    char buffer[50];
    int status, bytes_read, pipefd[2];
    if (pipe(pipefd) == -1) {
        perror("error creating pipe");
        _exit(-1);
    }
    pid_t pid = fork();
    if (pid == -1){
        perror("Error creating fork");
        _exit(-1);
    }
    if(!pid){
       close(pipefd[0]);
       dup2(pipefd[1],1);
       execlp("wc", "wc", "-c", path, NULL);
    }
    else{
        wait(&status); 
        close(pipefd[1]);
        bytes_read = read(pipefd[0], buffer, 50);
        buffer[bytes_read] = '\0';
    }
    
    return atoi(buffer);
}

void get_status(int write_fd, Task_Controller task_controller, Tranformations transformations){
    char new[1024];

    for(int i=0;i<task_controller->nr;i++){
        if(task_controller->tasks[i]->state == 1){
            sprintf(new,"Task #%d: %s\n",task_controller->tasks[i]->task_id,task_controller->tasks[i]->arguments);
            write(write_fd,new,strlen(new));
        }     
    }
    for(int i=0;i<7;i++){
        sprintf(new,"transf %s: %d/%d (running/max)\n", transformations[i].name, transformations[i].usage_counter, transformations[i].max_limit);
        write(write_fd,new,strlen(new));
    }
    close(write_fd);

}


void execute(Task transf, int input_fd, int output_fd, char *path){
    int pipefd[transf->transformations_counter-1][2];
    
    
    for(int i=0; i<transf->transformations_counter; i++){
        if (pipe(pipefd[i]) == -1) {
            perror("Error creating pipe");
            _exit(-1);
        }
    }
    pid_t pid; int i=0;
    for ( ; i < transf->transformations_counter; i++) {
        pid = fork();
        if (!pid) {
            if (i==0) dup2(input_fd,0);
            else dup2(pipefd[i-1][0],0);
            if (i == transf->transformations_counter-1) dup2(output_fd,1);
            else dup2(pipefd[i][1],1);
            break;
        }
    }
    for (int p = 0; p < transf->transformations_counter; p++) {
        close(pipefd[p][1]);
        close(pipefd[p][0]);
    }
    if (!pid) {
        close(input_fd);
        close(output_fd);
        char exec[50];
        sprintf(exec, "%s/%s", path, transf->transformations[i]);
        execlp(exec, transf->transformations[i], NULL);
        perror("Error in execution");
        _exit(-1);
    }
    for(int j=0; j<transf->transformations_counter; j++){
        wait(NULL);
    }
}

void execution_controller(Task_Controller task_controller, int i, char *path){
    int write_fd;
    if ( (write_fd = open(task_controller->tasks[i-1]->fifo, O_WRONLY)) == -1){
        perror("Error opening server pipe");
        _exit(-1);
    }
    write(write_fd, "processing\n", 11);
    int input_fd, output_fd, input_bytes, output_bytes;
    if ( (input_fd = open(task_controller->tasks[i-1]->input, O_RDONLY)) == -1 || (output_fd = open(task_controller->tasks[i-1]->output, O_WRONLY | O_CREAT, 0666)) == -1){
        perror("Error opening files");
        _exit(-1);
    }
    execute(task_controller->tasks[i-1], input_fd, output_fd, path);
    close(output_fd);
   
    input_bytes = count_bytes(task_controller->tasks[i-1]->input);
    output_bytes = count_bytes(task_controller->tasks[i-1]->output);
    char out[150];
    sprintf(out, "concluded (bytes-input: %d, bytes-output: %d)\n", input_bytes, output_bytes);
    write(write_fd, out, strlen(out));
    close(input_fd);
    close(output_fd);
    close(write_fd);
}


void fill_task(char *keeper, char *fifo, Tranformations transf, Task_Controller task_controller, char *path){
    char *token_buffer = strtok(NULL, " ");
    if(*token_buffer == 's'){
        pid_t p = fork();
        if(!p) {
            int write_fd;
            if ( (write_fd = open(fifo, O_WRONLY)) == -1){
                perror("Error opening server pipe");
                _exit(-1);
            }
            get_status(write_fd,task_controller,transf);
            _exit(0);
        }
    }
    else{
        int write_fd;
        if ( (write_fd = open(fifo, O_WRONLY)) == -1){
            perror("Error opening server pipe");
            _exit(-1);
        }
        write(write_fd,"pending\n", 8);
        task_controller->nr++;
        task_controller->tasks = realloc(task_controller->tasks, task_controller->nr*sizeof(task_controller->tasks));
        Task t = calloc(1,sizeof(*t));
        t->task_id = task_controller->nr;

        while(*keeper != 'p') keeper++ ;
        t->arguments = strdup(keeper);

        t->fifo = strdup(fifo);

        token_buffer = strtok(NULL," ");
        if(!strcmp(token_buffer,"-p")){
            token_buffer = strtok(NULL," ");
            t->priority = atoi(token_buffer);
            token_buffer = strtok(NULL, " ");
        }
        t->input = strdup(token_buffer);
        token_buffer = strtok(NULL," ");
        t->output = strdup((token_buffer));

        int max = get_max_usage(transf), index = 0;
        t->transformations = malloc(sizeof(max+1));
        token_buffer = strtok(NULL," ");
        while(token_buffer){
            t->transformations[index] = strdup(token_buffer);
            update_counter_transformations(transf, t->transformations[index]);
            token_buffer = strtok(NULL," ");
            index++;
        }
        t->transformations_counter = index;
        t->state = 2;
        for(int i = 0; i<index; i++){
            if (!transf_available (transf, t->transformations[i])) { 
                t->state = 1; 
                clean_transformations(t,transf);
                break; 
            }
        }
        task_controller->tasks[task_controller->nr-1] = t;
        int id = 1;
        for(int i=0; i<task_controller->nr; i++){
            if (task_controller->tasks[i]->state == 2){
                id = i+1; 
                break;
            }
            else if (task_controller->tasks[i]->state == 1) {
                int r = 1; 
                for(int i = 0; i<index; i++){
                    if (!transf_available (transf, t->transformations[i])) r = 0;
                }
                if (r) { 
                    id = i+1; 
                    break;
                }
            }
        }
        clean_transformations(task_controller->tasks[id-1], transf);
        pid_t pid = fork();
        if (pid == -1){
            perror("Error creating fork");
            _exit(-1);
        }
        if (!pid){
            execution_controller(task_controller, id, path);
            _exit(0);
        }
        task_controller->tasks[id-1]->state = 3;
        close(write_fd);
    }

}

int main(int argc, char *argv[]){

    if (signal(SIGTERM, termination) || signal(SIGINT, termination)) {
        perror("Signal");
        _exit(-1);
    }

    if (argc != 3){
        write(1,"couldn't access server program: not enough arguments\n", 53);
        kill(getpid(),SIGTERM);
        _exit(-1);
    }

    Task_Controller task_controller = calloc(1,sizeof(*task_controller));
    Tranformations transf = read_config_file(argv[1]);

    if (mkfifo("notebook", 0644) != 0) {
        perror("Error creating fifo");
        _exit(-1);
    }

   

    while(1){
        int notebook_fd = open("notebook", O_RDONLY);
        char *task = malloc(2048);
        readln(notebook_fd, task, 2048);

        char *keeper = strdup(task);
        char *fifo = strtok(task, " ");

        fill_task(keeper, fifo, transf, task_controller, argv[2]);

        close(notebook_fd);

        free(task);
    }



    
    return 0;
}



