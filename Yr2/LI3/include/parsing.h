#ifndef PARSING_H__
#define PARSING_H__

#include "cat_users.h"
#include "cat_commits.h"
#include "cat_repos.h"
#include "stats.h"

USERS read_users(STATS stats);
COMMITS read_commits(STATS stats, USERS users, REPOS repos_1valid);
REPOS validate_fields_repos();
REPOS read_repos(STATS stats, REPOS repos_valid, USERS users, COMMITS commits);
void parsing(char *commands);

#endif
