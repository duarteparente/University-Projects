#ifndef REPO_H__
#define REPO_H__

/**
 * \brief Structure used to store the information of one single Repositorie.
 */
typedef struct one_repo * REPO;

REPO create_repo (char *info);
void destroy_REPO(REPO repo);

int get_id_REPO(REPO repo);
int get_owner_id(REPO repo);
char *get_full_name(REPO repo);
char *get_license(REPO repo);
char *get_has_wiki(REPO repo);
char *get_description(REPO repo);
char *get_language(REPO repo);
char *get_default_branch(REPO repo);
char *get_created_at_REPO(REPO repo);
char *get_updated_at_REPO(REPO repo);
int get_forks_count(REPO repo);
int get_open_issues(REPO repo);
int get_stargazers_count(REPO repo);
int get_size(REPO repo);

#endif
