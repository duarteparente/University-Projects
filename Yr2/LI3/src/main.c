/**  
* @file main.c   
* \brief Main execution. 
*/ 

#include <stdio.h>
#include "../include/parsing.h"
#include "../include/ui.h"


/**
 * \brief Main funtion of the program
 */
int main(int argc, char* argv[]){
    if (argc != 2) load_files();
    else parsing(argv[1]);
    return 0;
}
