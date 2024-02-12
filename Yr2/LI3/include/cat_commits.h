#ifndef CAT_COMMITS_H__
#define CAT_COMMITS_H__

#include "commit.h"

/**
 * \brief Structure used to store a catalogue of Commits.
 */
typedef struct vector_commits * COMMITS;


COMMITS create_commits();
void add_commit(COMMIT commit, COMMITS commits);
void destroy_COMMITS(COMMITS commits);
void heapSort_COMMITS (COMMITS commits, int N);
int posBinarySearch_COMMITS(COMMITS commits, int l, int r, int x);

int get_lenght_commits(COMMITS commits);
int get_comms_repo_id(COMMITS commits, int i);
int get_comms_commiter_id(COMMITS commits, int i);
int get_comms_author_id(COMMITS commits, int i);
char* get_comms_commit_at(COMMITS commits, int i);
char *get_comms_message(COMMITS commits, int i);
int get_msg_size(COMMITS commits, int pos);

#endif