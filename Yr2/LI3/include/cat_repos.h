#ifndef CAT_REPOS_H__
#define CAT_REPOS_H__

#include "repo.h"


/**
 * \brief Structure used to store a catalogue of Repositories.
 */
typedef struct vector_repos * REPOS;

REPOS create_repos();
void add_repo(REPO repo, REPOS repos);
void move_repo(int pos, REPOS src, REPOS dest);
void destroy_REPOS(REPOS repos);
void heapSort_REPOS (REPOS repos, int N);
void sort_REPOS_ids (REPOS repos);
int posBinarySearch_REPOS(REPOS repos, int l, int r, int x);

int get_lenght_repos(REPOS repos);
int get_repos_id(REPOS repos, int i);
int get_repos_owner_id(REPOS repos, int i);
char *get_repos_description(REPOS repos, int i);
char *get_repos_language(REPOS repos, int i);



#endif