#ifndef COMMIT_H__
#define COMMIT_H__

/**
 * \brief Structure used to store the information of one single Commit.
 */
typedef struct one_commit * COMMIT;

COMMIT create_commit (char *info);
void destroy_COMMIT(COMMIT commit);

char *get_commit_at(COMMIT commit);
char *get_message(COMMIT commit);
int get_repo_id(COMMIT commit);
int get_author_id(COMMIT commit);
int get_commiter_id(COMMIT commit);

#endif
