/**  
* @file pages.c   
* \brief Module used to hold the output in UI. 
*/ 
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "../include/pages.h"

struct page {
    char *s[20];
};

struct vector_page {
    P *p;
    int length;
    int size;
};

/**
 * \brief Initialize page struct 
 */
void init_page(P page){
    for(int i=0; i<20; i++) page->s[i] = "\0";
}

/**
 * Creates a new instance of a page.
 */
P create_page(){
    P p = malloc(sizeof(*p));
    init_page(p);
    return p;
}

/**
 * Creates a new instance of pages.
 */
PAGES create_pages(){
    PAGES pages = calloc(1, sizeof(*pages));
    pages->size = 5;
    pages->p = calloc(pages->size, sizeof(pages->p));
    return pages;
}

/**
 * Adds a page to the vector of pages.
 */
void add (P page, PAGES pages){
    if (pages->length == pages->size){
        pages->size*=2;
        pages->p = realloc(pages->p, pages->size*sizeof(pages->p));
    }
    pages->p[pages->length++] = page;
}

/**
 * Fills the vector of pages with the lines from the query output file.
 */
PAGES fill_pages(){
    char *s[20];
    for(int i=0; i<20; i++) s[i] = "\0";
    PAGES pages = create_pages();
    int i = 0;
    char *line = NULL;
    size_t len = 0;
    FILE *output_file = fopen("saida/ui_output.csv", "r");
    while(getline(&line, &len, output_file) != -1){
        if(i==20){  
            i = 0;
            P p = create_page();
            for(int i=0; i<20; i++) p->s[i] = s[i];
            add(p,pages);
            for(int i=0; i<20; i++) s[i] = "\0";
        } 
        s[i++] = strdup(line);
    }
    fclose(output_file);
    if (i!=0){
        P page = create_page();
        for(int i=0; i<20; i++) page->s[i] = s[i];
        add(page,pages);
    }
    return pages;
}

/**
 * Returns the parameter 'length' of pages.
 */
int get_pages_length(PAGES pages){
    return pages->length;
}

/**
 * \brief Page visualization.
 */
void show_page(int page_number, PAGES pages){
    for(int i=0; i<20 && *pages->p[page_number-1]->s[i] != '\0'; i++) 
        printf("%s", pages->p[page_number-1]->s[i]);
}

/**
 * Find the page a User or Repository id appears.
 * Returns first page if non-existing.
 */
int get_pageById(int query_id, char *line, PAGES pages){
    int i = 0;
    for( ; i<pages->length; i++){
        for(int j=0; j<20 && (*pages->p[i]->s[j]!='\0'); j++){
            char *new = strdup(pages->p[i]->s[j]);
            if (query_id==10 && *new == '-') strsep(&new, " ");
            strsep(&new, " ");
            char *compare = strdup(strsep(&new, " "));
            if(!strcmp(compare,line)) return (i+1);
        }
    }
    return 1;
}

/**
 * Find the page a username appears.
 * Returns first page if non-existing.
 */
int get_pageByUsername(char *line, PAGES pages){
    int i = 0;
    for( ; i<pages->length; i++){
        for(int j=0; j<20 && (*pages->p[i]->s[j]!='\0'); j++){
            char *new = strdup(pages->p[i]->s[j]);
            if (*new != '-'){
                strsep(&new, " ");
                strsep(&new, " ");
                strsep(&new, " ");
                char *compare = strdup(strsep(&new, " "));
                if(!strcmp(compare,line)) return (i+1);
            }
        }
    }
    return 1;
}
