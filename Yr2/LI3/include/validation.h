#ifndef VALIDATION_H__
#define VALIDATION_H__

#include "user.h"
#include "commit.h"
#include "repo.h"
#include "cat_users.h"
#include "cat_commits.h"
#include "cat_repos.h"

int is_valid_int (int n1, int n2, int n3, int n4, int n5, int n6);
int is_valid_type (char *type);
int is_valid_follower_list (char *list, int followers);
int is_valid_following_list (char *list, int following);
int is_int_list(char *list);
int list_lenght (char *list);
int is_empty (char *input);
int is_valid_string (char *login);
int is_valid_string_repos (char *full_name, char *license, char *language, char *default_branch);
int is_bissexto (int year);
int is_future (int ano, int mes, int dia);
int is_valid_hour(int hh, int mm, int ss);
int is_valid_day_string(char *date);
int is_valid_days(int AAAA, int MM, int DD);
int is_valid_date (char *date);
int is_valid_has_wiki (char *wiki);

int is_valid_user(USER user);
int is_valid_commit(COMMIT commit);
int is_valid_repo(REPO repo);

int existing_user(int committer_id, int author_id, USERS users);
int existing_repo(int repo_id, REPOS repos);
int existing_elems(int owner_id, int id, USERS users, COMMITS commits);

int validate_parameters(int N, char *s1, char *s2, int query_id);

#endif
