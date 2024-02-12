/**
 * @file commit.c 
 * \brief Information about a single commit.
 */ 
#include <string.h>
#include <stdlib.h>
#include "../include/commit.h"
#include "../include/validation.h"


struct one_commit {
	int repo_id;
	int author_id;
	int committer_id;
	char *commit_at;
	char *message;
};

/**
 * Creates a new instance of a Commit.
 */
COMMIT create_commit (char *info){
	COMMIT c = (COMMIT) malloc (sizeof(*c));
	c->repo_id = is_empty (strsep (&info, ";"));
	c->author_id = is_empty (strsep (&info, ";"));
	c->committer_id = is_empty (strsep (&info, ";"));
	c->commit_at = strdup (strsep (&info, ";"));
	c->message = strdup (strsep (&info, ";"));
	return c;
};


/**
 * Frees all memory allocated to store the information of a single commit.
 */
void destroy_COMMIT(COMMIT commit){
    free(commit->commit_at);
    free(commit->message);
    free(commit);
}

/**
 * Returns a copy of the parameter 'message' of a commit.
 */
char *get_message(COMMIT commit){
	return strdup(commit->message);
}

/**
 * Returns a copy of the parameter 'commit_at' of a commit.
 */
char *get_commit_at(COMMIT commit){
	return strdup(commit->commit_at);
}

/**
 * Returns the parameter 'repo_id' of a commit.
 */
int get_repo_id(COMMIT commit){
	return commit->repo_id;
}

/**
 * Returns the parameter 'author_id' of a commit.
 */
int get_author_id(COMMIT commit){
	return commit->author_id;
}

/**
 * Returns the parameter 'commiter_id' of a commit.
 */
int get_commiter_id(COMMIT commit){
	return commit->committer_id;
}
