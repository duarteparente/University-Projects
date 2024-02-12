/**
 * @file cat_repos.c 
 * \brief Repositories catalogue.
 */ 
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "../include/cat_repos.h"


struct vector_repos {
    REPO *repo;       /**< Repositories Array */
    int lenght;
    int size;
};

/**
 * Creates a new instance of a Repositories catalogue.
 */
REPOS create_repos(){
    REPOS repos = calloc(1, sizeof(*repos));
    repos->size = 5000;
    repos->repo = calloc(repos->size, sizeof(repos->repo));
    return repos;
}

/**
 * Stores a new Repository in the catalogue.
 */
void add_repo(REPO repo, REPOS repos){
    if(repos->size == repos->lenght){
        repos->size*=2;
        repos->repo = realloc(repos->repo, repos->size*sizeof(repos->repo));
    }
    repos->repo[repos->lenght++] = repo;
}

/**
 * Moves a repository to another catalogue.
 */
void move_repo(int pos, REPOS src, REPOS dest){
    add_repo(src->repo[pos],dest);
}

/**
 * Frees all memory allocated in the repositories catalogue.
 */
void destroy_REPOS(REPOS repos){
    for(int i=0; i<repos->lenght; i++){
        destroy_REPO(repos->repo[i]);
    }    
    free(repos);
}

/**
 * Returns the number of repositories stored in the catalogue.
 */
int get_lenght_repos(REPOS repos){
    return repos->lenght;
}

/**
 * Returns the parameter 'id' of a repository.
 */
int get_repos_id(REPOS repos, int i){
    return get_id_REPO(repos->repo[i]);
}

/**
 * Returns the parameter 'id' of a repository.
 */
int get_repos_owner_id(REPOS repos, int i){
    return get_owner_id(repos->repo[i]);
}

/**
 * Returns a copy of the parameter 'description' of a repository
 */
char *get_repos_description(REPOS repos, int i){
    return get_description(repos->repo[i]);
}

/**
 * Returns a copy of the parameter 'language' of a repository
 */
char *get_repos_language(REPOS repos, int i){
    return get_language(repos->repo[i]);
}

/**
 * Swap the position of two repositories in the catalogue.
 */
void swap_REPOS (REPOS repos, int i, int j){
    REPO temp_repo = repos->repo[i];
    repos->repo[i] = repos->repo[j];
    repos->repo[j] = temp_repo;
}


/**
 * HeapSort's bubbleDown function.
 * 
 * This function sorts the repositories catalogue in descending order by the parameter 'id'. 
 */
void bubbleDown_REPOS (REPOS repos, int N, int i){
    int f = 2*i + 1;
    while(f<N){
        if (f+1 < N && get_id_REPO(repos->repo[f+1]) < get_id_REPO(repos->repo[f])) f++; // right descendent < left descendent
        if (get_id_REPO(repos->repo[f]) > get_id_REPO(repos->repo[i])) break;
        swap_REPOS (repos,i,f);
        i=f; f = 2*i + 1; 
    }
}

/**
 * Converts the repositories catalogue into an Heap catalogue. 
 */
void heapify_REPOS (REPOS repos, int N){
    int i;
    for(i = (N-2)/2; i>=0; i--) bubbleDown_REPOS(repos,N,i);
}

/**
 * Sorts a Heap repositories catalogue in descending order, by 'id'.
 */
void heapSort_REPOS (REPOS repos, int N){
    heapify_REPOS(repos,N);
    while(--N > 0){
        swap_REPOS (repos,0,N);
        bubbleDown_REPOS(repos,N,0);
    }
}


/**
 * BinarySearch for a repository in a repositories catalogue, by the parameter 'id'.
 * 
 * @return The index where the repository is stored.
 */
int posBinarySearch_REPOS(REPOS repos, int l, int r, int x){
    if (r >= l) {
        int mid = l + (r - l) / 2;
        if (get_id_REPO(repos->repo[mid]) == x)
            return mid;
        if (get_id_REPO(repos->repo[mid]) < x)
            return posBinarySearch_REPOS(repos, l, mid - 1, x);
        return posBinarySearch_REPOS(repos, mid + 1, r, x);
    }
    return -1;
}
