/**
 * @file cat_commits.c 
 * \brief Commits catalogue.
 */ 
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "../include/cat_commits.h"


struct vector_commits {
    COMMIT *commit;  /**< Commits Array */
    int lenght;
    int size;
};

/**
 * Creates a new instance of a Commits catalogue.
 */
COMMITS create_commits(){
    COMMITS commits = calloc(1, sizeof(*commits));
    commits->size = 5000;
    commits->commit = calloc(commits->size, sizeof(commits->commit));
    return commits;
}

/**
 * Stores a new commit in the catalogue.
 */
void add_commit(COMMIT commit, COMMITS commits){
    if(commits->size == commits->lenght){
        commits->size*=2;
        commits->commit = realloc(commits->commit, commits->size*sizeof(commits->commit));
    }
    commits->commit[commits->lenght++] = commit;
}

/**
 * Frees all memory allocated in the commits catalogue.
 */
void destroy_COMMITS(COMMITS commits){
    for(int i=0; i<commits->lenght; i++){
        destroy_COMMIT(commits->commit[i]);
    }   
    free(commits);
}

/**
 * Returns the number of commits stored in the catalogue.
 */
int get_lenght_commits(COMMITS commits){
    return commits->lenght;
}

/**
 * Returns the parameter 'repo_id' of a commit.
 */
int get_comms_repo_id(COMMITS commits, int i){
    return get_repo_id(commits->commit[i]);
}

/**
 * Returns the parameter 'commiter_id' of a commit.
 */
int get_comms_commiter_id(COMMITS commits, int i){
    return get_commiter_id(commits->commit[i]);
}

/**
 * Returns the parameter 'author_id' of a commit.
 */
int get_comms_author_id(COMMITS commits, int i){
    return get_author_id(commits->commit[i]);
}

/**
 * Returns a copy of the parameter 'commit_at' of a commit.
 */
char* get_comms_commit_at(COMMITS commits, int i){
    return get_commit_at(commits->commit[i]);
}

/**
 * Returns a copy of the parameter 'message' of a commit.
 */
char *get_comms_message(COMMITS commits, int i){
    return get_message(commits->commit[i]);
}

/**
 * Returns the size of a message of a commit.
 */
int get_msg_size(COMMITS commits, int pos){
    char *message = get_comms_message(commits, pos);
    int t = strlen(message)-1;
    free(message);
    return t;
}

/**
 * Swap the position of two commits in the catalogue.
 */
void swap_COMMITS (COMMITS commits, int i, int j){
    COMMIT temp_commit = commits->commit[i];
    commits->commit[i] = commits->commit[j];
    commits->commit[j] = temp_commit;
}

/**
 * HeapSort's bubbleDown function.
 * 
 * This function sorts the commits catalogue in descending order by the parameter 'repo_id'. 
 */
void bubbleDown_COMMITS (COMMITS commits, int N, int i){
    int f = 2*i + 1;
    while(f<N){
        if (f+1 < N && get_repo_id(commits->commit[f+1]) < get_repo_id(commits->commit[f])) f++; // right descendent < left descendent
        if (get_repo_id(commits->commit[f]) > get_repo_id(commits->commit[i])) break;
        swap_COMMITS (commits,i,f);
        i=f; f = 2*i + 1; 
    }
}

/**
 * Converts the commits catalogue into an Heap catalogue. 
 */
void heapify_COMMITS (COMMITS commits, int N){
    int i;
    for(i = (N-2)/2; i>=0; i--) bubbleDown_COMMITS(commits,N,i);
}


/**
 * Sorts a Heap commits catalogue in descending order, by 'repo_id'.
 */
void heapSort_COMMITS (COMMITS commits, int N){
    heapify_COMMITS(commits,N);
    while(--N > 0){
        swap_COMMITS (commits,0,N);
        bubbleDown_COMMITS(commits,N,0);
    }
}

/**
 * BinarySearch for a commit in a commits catalogue.
 * 
 * @return The index where the commit is stored.
 */
int posBinarySearch_COMMITS(COMMITS commits, int l, int r, int x){
    if (r >= l) {
        int mid = l + (r - l) / 2;
        if (get_repo_id(commits->commit[mid]) == x)
            return mid;
        if (get_repo_id(commits->commit[mid]) < x)
            return posBinarySearch_COMMITS(commits, l, mid - 1, x);
        return posBinarySearch_COMMITS(commits, mid + 1, r, x);
    }
    return -1;
}
