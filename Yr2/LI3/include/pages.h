#ifndef PAGES_H__
#define PAGES_H__

/**
 * Structure used to hold a pagine of the output, contains 20 lines of information.
 */
typedef struct page * P;
/**
 * Structure used to hold all the pagin
 */
typedef struct vector_page * PAGES; 

void init_page(P page);
P create_page();
PAGES create_pages();
void add (P page, PAGES pages);
PAGES fill_pages();
void destroy_PAGES(PAGES pages);
int get_pages_length(PAGES pages);
void show_page(int page_number, PAGES pages);
int get_pageById(int query_id, char *line, PAGES pages);
int get_pageByUsername(char *line, PAGES pages);


#endif