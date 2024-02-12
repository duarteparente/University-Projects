/**
 * @file validation.c 
 * \brief Files validation.
 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#define __USE_XOPEN
#include <time.h>
#include "../include/validation.h"
#include "../include/auxiliar.h"


/**
 * \brief Checks if input is a numerical string
 */
int is_empty (char *input){
    int r = atoi(input);
    if (!isdigit(*input)) r = -1;
    return r;
}

/**
 * \brief Checks if all arguments are positive integers.
 */
int is_valid_int (int n1, int n2, int n3, int n4, int n5, int n6){
    return (n1>=0 && n2>=0 && n3>=0 && n4>=0 && n5>=0 && n6>=0);
}

/**
 * \brief Checks if user type is valid 
 */
int is_valid_type (char *type){
    return (strcmp("Bot", type)==0 || strcmp("User", type)==0 || strcmp("Organization", type)==0);
}

/**
 * \brief Counts number of id in a follower/following list.
 */
int list_lenght (char *list){
    char *p = list;
    int contador = 0;
    while(*p){
        if(*p==',') contador++;
        p++;
    }
    if (strcmp (list,"[]")==0) return contador;
    else return (contador+1);
}

/**
 * \brief Checks if all id in a follower/following list are positive integers.
 */
int is_int_list(char *list){
    int t = strlen(list);
    char*s = list;
    int r = 1;
    if (s[0] != '[' || s[t-1] != ']') r=0;
    else {
        while(*s){
            if(*s=='-' || isalpha(*s)) {r=0;break;}
            s++;
        }    
    }
    return r;
}

/**
 * \brief Checks if the followers_list of an user is valid.
 */
int is_valid_follower_list (char *list, int followers){
    int c = list_lenght(list);
    return (followers == c && is_int_list(list));
}

/**
 * \brief Checks if the following_list of an user is valid.
 */
int is_valid_following_list (char *list, int following){
    int c = list_lenght(list);
    return (following == c && is_int_list(list));
}

/**
 * \brief Checks if a string is valid
 */
int is_valid_string (char *login){
    int t = strlen(login);
    return (t!=0);
}

/**
 * \brief Checks if multiple strings are valid
 */
int is_valid_string_repos (char *full_name, char *license, char *language, char *default_branch){
    int a = strlen(full_name);
    int b = strlen(license);
    int d = strlen(language);
    int e = strlen(default_branch);
    return (a!=0 && b!=0 && d!=0 && e!=0);
}

/**
 * \brief Checks if it is a leap year
 */
int  is_bissexto (int year) {
    return (((year%4==0) && (year%100!=0)) || (year%400==0));
}

/**
 * \brief Checks if a date is in future
 * 
 * Based on today's date.
 */
int is_future (int ano, int mes, int dia){
    time_t rawtime;
    struct tm *t;
    time( &rawtime );
    t = localtime( &rawtime );
    int data_hoje = (t->tm_year+1900)*10000+(t->tm_mon+1)*100+t->tm_mday;
    int data = ano*10000+mes*100+dia;
    return (data > data_hoje);
}

/**
 * \brief Checks if an hour is valid
 */
int is_valid_hour(int hh, int mm, int ss){
    return (hh<24 && hh >= 0 && mm < 60 && mm >= 0 && ss < 60 && ss >=0);
}

/**
 * \brief Checks if a day is valid (string format)
 */
int is_valid_day_string(char *date){
    struct tm time = {0};
    char *d = strptime(date, "%Y-%m-%d" , &time);
    if (d == NULL || *d != '\0') return 0;
    return is_valid_days(time.tm_year+1900,time.tm_mon+1,time.tm_mday);
}

/**
 * \brief Checks if a day is valid 
 */
int is_valid_days(int AAAA, int MM, int DD){
    if (is_future (AAAA,MM,DD) || AAAA < 2005 || (AAAA==2005 && (MM<4 || (MM==4 && DD<7)))) return 0;
    if (MM==2) return ((is_bissexto(AAAA) && DD<=29) || DD<=28);
    if (MM==4 || MM==6 || MM==9 || MM==11) return (DD<=30);
    return (DD<=31);
}

/**
 * \brief Checks if both date and hour are valid
 */
int is_valid_date (char *date){
    struct tm time = {0};
    char *d = strptime(date, "%Y-%m-%d %H:%M:%S" , &time);
    if (d == NULL || *d != '\0') return 0;
    int r1 = is_valid_days(time.tm_year+1900,time.tm_mon+1,time.tm_mday);
    int r2 = is_valid_hour(time.tm_hour,time.tm_min, time.tm_sec);
    return (r1 && r2);
}


/**
 * \brief Checks if the parameter has_wiki is valid
 */
int is_valid_has_wiki (char *wiki){
    return (strcmp("True", wiki)==0 || strcmp("False", wiki)==0);
}


/**
 * \brief Checks if a User is Valid.
 */
int is_valid_user(USER user){
    int followers = get_followers(user);
    int following = get_following(user);
    return (  
              is_valid_int(get_user_id(user), followers, following, get_public_gists(user), get_public_repos(user), 11)
              && is_valid_type(get_type(user)) && is_valid_follower_list(get_String_follower_list(user),followers)  
              && is_valid_following_list(get_String_following_list(user),following) && is_valid_string(get_login(user))
              && is_valid_date(get_created_at(user))    
           ); 
}

/**
 * \brief Checks if a Commit is Valid.
 */
int is_valid_commit(COMMIT commit){
    return (is_valid_int(get_repo_id(commit), get_author_id(commit), get_commiter_id(commit),11,11,11) && is_valid_date(get_commit_at(commit)));
}

/**
 * \brief Checks if a Repo is Valid.
 */
int is_valid_repo(REPO repo){
    return (   
               is_valid_int(get_forks_count(repo), get_open_issues(repo), get_stargazers_count(repo), get_owner_id(repo), get_id_REPO(repo), get_size(repo)) 
               && is_valid_string_repos( get_full_name(repo), get_license(repo), get_language(repo), get_default_branch(repo)) 
               && is_valid_has_wiki(get_has_wiki(repo)) && is_valid_date(get_created_at_REPO(repo)) && is_valid_date(get_updated_at_REPO(repo))  
           );
}

/**
 * Checks if a commit contains information about an existing user.
 */
int existing_user(int committer_id, int author_id, USERS users){
    int pos_c = posBinarySearch_Users(users,0,get_lenght_users(users)-1, committer_id);
    if (committer_id == author_id) return (pos_c != -1);
    return (pos_c != -1 && posBinarySearch_Users(users,0,get_lenght_users(users)-1, author_id) != -1);
}

/**
 * Checks if a commit contains information about an existing repository.
 */
int existing_repo(int repo_id, REPOS repos){
    return (posBinarySearch_REPOS(repos,0,get_lenght_repos(repos)-1, repo_id) != -1);
}

/**
 * Checks if a repository contains information about an existing user.
 * Checks if a repository has at least 1 commit.
 */
int existing_elems(int owner_id, int id, USERS users, COMMITS commits){
    if(posBinarySearch_Users(users,0,get_lenght_users(users)-1, owner_id) == -1) return 0;
    if(posBinarySearch_COMMITS(commits,0,get_lenght_commits(commits)-1, id) == -1) return 0;
    return 1;
}

/**
 * Checks if query parameters are valid.
 */
int validate_parameters(int N, char *s1, char *s2, int query_id){
    int r;
    switch(query_id){
        case 5: r = ((N>0) && is_valid_day_string(s1) && is_valid_day_string(s2) && (convert_date(s1)<convert_date(s2))); break;
        case 6: r = (N>0 && is_valid_string(s1)) && !isdigit(*s1); break;
        case 7: r = is_valid_day_string(s1); break;
        case 8: r = (N>0 && is_valid_day_string(s1)); break;
        case 9: case 10: r = (N>0); break;
    }
    return r;
}
