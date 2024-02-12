/**  
* @file parameters.c  
* \brief Parameters analysis and execution.  
*/ 

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#define __USE_XOPEN
#include <time.h>
#include "../include/parameters.h"
#include "../include/cat_users.h"
#include "../include/cat_commits.h"
#include "../include/cat_repos.h"
#include "../include/auxiliar.h"

struct aux_parameters{
    int *user_id;
    int length;
    int size;
    int *qtty;
    int length_qtty;
    int *user_id_final;
};


/**
 * \brief Linear Search in Repositories catalogue for the fisrt ocurrence of a given Language
 */
char *find_language(REPOS repos, int ascii){
    char *lang;
    for(int i=0; i<get_lenght_repos(repos); i++){
        lang = get_repos_language(repos,i);
        if (soma_ASCII(lang) == ascii) break;
    }
    return lang;
}

/**
 * Simultaneously swap two positions in arrays 'qtty' and 'user_id_final'.
 */
void swap_qtty (PARAM comms_date, int i, int j){
    int temp_id = comms_date->user_id_final[i], temp_qtty = comms_date->qtty[i];
    comms_date->user_id_final[i] = comms_date->user_id_final[j];
    comms_date->qtty[i] = comms_date->qtty[j];
    comms_date->user_id_final[j] = temp_id;
    comms_date->qtty[j] = temp_qtty;
}

/**
 * Simultaneously swap three positions in all three arrays for PARAM struct.
 */
void swap_3WAY (PARAM comms_date, int i, int j){
    int temp_id_final = comms_date->user_id_final[i], temp_id = comms_date->user_id[i], temp_qtty = comms_date->qtty[i];
    comms_date->user_id_final[i] = comms_date->user_id_final[j];
    comms_date->qtty[i] = comms_date->qtty[j];
    comms_date->user_id[i] = comms_date->user_id[j];
    comms_date->user_id_final[j] = temp_id_final;
    comms_date->qtty[j] = temp_qtty;
    comms_date->user_id[j] = temp_id;
}

/**
 * HeapSort's bubbleDown function.
 * 
 * This function sorts 'user_id_final' based on 'qtty'. 
 */
void bubbleDown_qtty(PARAM comms_date, int N, int i){
    int f = 2*i + 1;
    while(f<N){
        if (f+1 < N && comms_date->qtty[f+1] < comms_date->qtty[f]) f++; // right descendent < left descendent
        if (comms_date->qtty[f]>comms_date->qtty[i]) break;
        swap_qtty (comms_date,i,f);
        i=f; f = 2*i + 1; 
    }
}

/**
 * HeapSort's bubbleDown function.
 * 
 * This function sorts 'user_id' and 'user_id_final' based on 'qtty'. 
 */
void bubbleDown_3WAY(PARAM comms_date, int N, int i){
    int f = 2*i + 1;
    while(f<N){
        if (f+1 < N && comms_date->qtty[f+1] < comms_date->qtty[f]) f++; // right descendent < left descendent
        if (comms_date->qtty[f]>comms_date->qtty[i]) break;
        swap_3WAY (comms_date,i,f);
        i=f; f = 2*i + 1; 
    }
}

/**
 * Converts 'user_id_final'into an Heap, by 'qtty'. 
 */
void heapify_qtty (PARAM comms_date, int N){
    int i;
    for(i = (N-2)/2; i>=0; i--) bubbleDown_qtty(comms_date,N,i);
}

/**
 * Converts the PARAM struct into an Heap, by 'qtty'. 
 */
void heapify_3WAY (PARAM comms_date, int N){
    int i;
    for(i = (N-2)/2; i>=0; i--) bubbleDown_3WAY(comms_date,N,i);
}

/**
 * Sorts 'user_id_final' in descending order, by 'qtty'.
 */
void heapSort_qtty (PARAM comms_date, int N){
    heapify_qtty(comms_date,N);
    while(--N > 0){
        swap_qtty (comms_date,0,N);
        bubbleDown_qtty(comms_date,N,0);
    }
}

/**
 * Sorts PARAM struct in descending order, by 'qtty'.
 */
void heapSort_3WAY (PARAM repos_lang, int N){
    heapify_3WAY(repos_lang,N);
    while(--N > 0){
        swap_3WAY (repos_lang,0,N);
        bubbleDown_3WAY(repos_lang,N,0);
    }
}

/**
 * BinarySearch for an id in a sorted 'user_id_final' by 'qtty' in descending order.
 * 
 * @return The index where the id is stored.
 */
int posBinarySearch_PARAM(PARAM comms_date, int l, int r, int x){
    if (r >= l) {
        int mid = l + (r - l) / 2;
        if ((comms_date->user_id_final[mid]) == x)
            return mid;
        if ((comms_date->user_id_final[mid]) < x)
            return posBinarySearch_PARAM(comms_date, l, mid - 1, x);
        return posBinarySearch_PARAM(comms_date, mid + 1, r, x);
    }
    return -1;
}

/**
 * \brief Creates a new instance of an aux_parameters struct
 * 
 * This struct contains 3 integer arrays, that are used to store and manipulate any type of information needed to complete
 * the parameterizable queries.
 * 
 * This function is particularly used only to maninulate with a maximum of two of the arrays. 
 * 
 */
PARAM create_PARAM(){
    PARAM comms_date = calloc(1,sizeof(*comms_date));
    comms_date->size = 10;
    comms_date->user_id = calloc(comms_date->size, sizeof(int));
    return comms_date;
}

/**
 * \brief Creates a new instance of an aux_parameters struct
 * 
 * This struct contains 3 integer arrays, that are used to store and manipulate any type of information needed to complete
 * the parameterizable queries.
 * 
 * This function is particularly used to manipulate with the three arrays at the same time.
 * 
 */
PARAM create_3WAYPARAM(){
    PARAM comms_date = calloc(1,sizeof(*comms_date));
    comms_date->size = 10;
    comms_date->user_id = calloc(comms_date->size, sizeof(int));
    comms_date->qtty = calloc(comms_date->size, sizeof(int));
    comms_date->user_id_final = calloc(comms_date->size, sizeof(int));
    return comms_date;
}

/**
 * Frees all memory allocated in a PARAM struct
 */
void destroy_PARAM (PARAM comms_date){
    free(comms_date->qtty);
    free(comms_date->user_id_final);
    free(comms_date);
}

/**
 * \brief Stores an id only in 'users_id'; 
 */
void add_id (PARAM comms_date, int id){
    if(comms_date->size == comms_date->length){
        comms_date->size*=2;
        comms_date->user_id = realloc(comms_date->user_id, comms_date->size*sizeof(int));
    }
    comms_date->user_id[comms_date->length] = id;
    comms_date->length++;
}

/**
 * \brief Stores values in all arrays of the PARAM struct:
 */
void add_3WAY(PARAM topN_msg, int repo_id, int commiter_id, int msg_size){
    if(topN_msg->size == topN_msg->length){
        topN_msg->size*=2;
        topN_msg->user_id = realloc(topN_msg->user_id, topN_msg->size*sizeof(int));
        topN_msg->qtty = realloc(topN_msg->qtty, topN_msg->size*sizeof(int));
        topN_msg->user_id_final = realloc(topN_msg->user_id_final, topN_msg->size*sizeof(int));
    }
    topN_msg->user_id[topN_msg->length] = commiter_id;
    topN_msg->qtty[topN_msg->length] = msg_size;
    topN_msg->user_id_final[topN_msg->length] = repo_id;
    topN_msg->length++;
}

/**
 * Fills 'qtty' array with the number of times an id is repeated in 'user_id'
 * 
 * For example, [112,112,112,225,333,333]  -> [3,1,2]
 */
void create_qtty_array (PARAM comms_date, int sorted_flag){
    if (sorted_flag) heapSort(comms_date->user_id, comms_date->length);
    int counter = 1;
    for(int i=0; i<comms_date->length-1; i++){
        if (comms_date->user_id[i] == comms_date->user_id[i+1]) counter++;
        else {
            comms_date->qtty = realloc(comms_date->qtty, (comms_date->length_qtty+1)*sizeof(int));
            comms_date->qtty[comms_date->length_qtty] = counter;
            counter = 1; comms_date->length_qtty++;
        }
    }
    comms_date->qtty = realloc(comms_date->qtty, (comms_date->length_qtty+1)*sizeof(int));
    comms_date->qtty[comms_date->length_qtty++] = counter; 
}

/**
 * Using 'qtty', fills 'user_id_final' with non-repeated values from 'user_id' 
 */
void organize_id (PARAM comms_date){
    comms_date->user_id_final = calloc(comms_date->length_qtty,sizeof(int));
    for (int i=0, j=0; i<comms_date->length && j<comms_date->length_qtty; j++){ 
        comms_date->user_id_final[j] = comms_date->user_id[i];
        i += comms_date->qtty[j];
    }
    free(comms_date->user_id);
}


/**
 * Store the 'commiter_id' of commit made beetween a date interval 
 */
void fill_commitsByDate (PARAM comms_date, COMMITS commits, char *from, char *to){
    int from_date = convert_date(from), to_date = convert_date(to);
    for(int i=0; i<get_lenght_commits(commits); i++){
        char *at = get_comms_commit_at(commits,i);
        int date = convert_date(at);
        int id = get_comms_commiter_id(commits,i);
        if (date >= from_date && date <= to_date) add_id(comms_date, id);
        free(at);
    }
}

/**
 * Store the 'commiter_id' of commit made in a repository of a certain language
 */
void fill_commitsByLang(PARAM comms_date, COMMITS commits, REPOS repos, char *language){
    for(int i=0; i<get_lenght_commits(commits); i++){
        int repo_id = get_comms_repo_id(commits, i);
        int pos = posBinarySearch_REPOS(repos, 0, get_lenght_repos(repos)-1, repo_id);
        char *lang = get_repos_language(repos,pos);
        if (!strcasecmp(lang, language)) {
            int id = get_comms_commiter_id(commits,i);
            add_id(comms_date, id);
        }
        free(lang);
    }
}

/**
 * Store the 'repo_id' of a repository with a commit made after a given date
 */
void fill_reposByLang(PARAM no_comms, COMMITS commits, char *date_from){
    int from_date = convert_date(date_from);
    for(int i=0; i<get_lenght_commits(commits); i++){
        char *comm_at = get_comms_commit_at(commits,i);
        int date = convert_date(comm_at);
        int id =  get_comms_repo_id(commits, i);
        if (date > from_date) add_id(no_comms, id);
        free(comm_at);
    }
}

/**
 * Store the Language of a repository with commits after a given date.
 * 
 * Special Note: repositories with no language declared are not accounted.
 */
void fill_Lang(PARAM lang, COMMITS commits, REPOS repos, char *date_from){
    int from_date = convert_date(date_from);
    for(int i=0; i<get_lenght_commits(commits); i++){
        char *comm_at = get_comms_commit_at(commits,i);
        int date = convert_date(comm_at);
        if (date > from_date){
            int id =  get_comms_repo_id(commits, i);
            int pos = posBinarySearch_REPOS(repos, 0, get_lenght_repos(repos)-1, id);
            char *language = get_repos_language(repos,pos);
            if (strcasecmp(language, "None")) { add_id(lang, soma_ASCII(language)); }//printf("%d\n",c++); }
            free(language);
        }
    }          
}


/**  
 * Stores the 'commiter_id' of an user that contributed to a friend's repository
 * Fills 'qtty' array with the number of times an id is repeated in 'user_id'
 * Using 'qtty', fills 'user_id_final' with non-repeated values from 'user_id'
 * Sorts 'user_id_final' in descending order, by 'qtty'.
 */
void fill_friends(COMMITS commits, USERS users, PARAM friends){
    int t = get_lenght_users(users);
    for (int i=0; i<get_lenght_commits(commits); i++){
        int pos = posBinarySearch_Users(users, 0, t-1, get_comms_commiter_id(commits, i));
        int fwers = get_followers_users(users, pos);
        int fwing = get_following_users(users,pos);
        if (!fwers || !fwing);
        else {
            int id = get_comms_author_id(commits, i);
            int *followers = calloc(fwers, sizeof(int));
            followers = get_users_followersList(users, pos);
            int *following = calloc(fwing, sizeof(int));
            following = get_users_followingList(users, pos);
            if (is_elem(followers, fwers, id) && is_elem(following, fwing, id)) add_id(friends, get_comms_commiter_id(commits, i));
            free(followers);
            free(following);
        }
    }
    create_qtty_array(friends,1);
    organize_id(friends);
    heapSort_qtty(friends, friends->length_qtty);
}


/**
 * Produces the output for queries that ask for an user 'id', its 'login' and a qtty parameter
 */
void output_UserLoginQtty(char *file, PARAM comms_date, USERS users, int N){
    FILE *output = fopen(file, "a");
    create_qtty_array(comms_date,1);
    organize_id(comms_date);
    heapSort_qtty(comms_date, comms_date->length_qtty);
    for(int i=0; i<comms_date->length_qtty && i<N; i++){
        int pos = posBinarySearch_Users(users, 0, get_lenght_users(users)-1, comms_date->user_id_final[i]);
        fprintf(output, "%d;%s;%d\n", comms_date->user_id_final[i], get_login_users(users, pos), comms_date->qtty[i]);
    }
    fclose(output);
}

/**
 * Produces the output for queries that ask for an user 'id', its 'login' and a qtty parameter
 * UI special
 */
void output_UserLoginQtty_UI(char *file, PARAM comms_date, USERS users, int N){
    FILE *output = fopen(file, "a");
    create_qtty_array(comms_date,1);
    organize_id(comms_date);
    heapSort_qtty(comms_date, comms_date->length_qtty);
    for(int i=0; i<comms_date->length_qtty && i<N; i++){
        int pos = posBinarySearch_Users(users, 0, get_lenght_users(users)-1, comms_date->user_id_final[i]);
        fprintf(output, "%dº: %d - %s - %d\n", (i+1), comms_date->user_id_final[i], get_login_users(users, pos), comms_date->qtty[i]);
    }
    fclose(output);
}

/**
 * Produces the output for query 7
 */
void output_q7(char *file, PARAM no_comms, REPOS repos){
    create_qtty_array(no_comms,0);
    organize_id(no_comms);
    FILE *output = fopen(file, "a");
    for(int i=0; i<get_lenght_repos(repos); i++){
        int pos = posBinarySearch_PARAM(no_comms, 0, no_comms->length_qtty-1, get_repos_id(repos,i));
        if (pos == -1) fprintf(output, "%d;%s\n", get_repos_id(repos,i), get_repos_description(repos, i));
    }
    free(no_comms);
    fclose(output);
}

/**
 * Produces the output for query 7
 * UI special
 */
void output_q7_UI(char *file, PARAM no_comms, REPOS repos){
    int p=1;
    create_qtty_array(no_comms,0);
    organize_id(no_comms);
    FILE *output = fopen(file, "a");
    for(int i=0; i<get_lenght_repos(repos); i++){
        int pos = posBinarySearch_PARAM(no_comms, 0, no_comms->length_qtty-1, get_repos_id(repos,i));
        if (pos == -1) fprintf(output, "%dº: %d - %s\n", p++,get_repos_id(repos,i), get_repos_description(repos, i));
    }
    free(no_comms);
    fclose(output);
}

/**
 * Produces the output for query 8
 */
void output_q8(char *file, PARAM repos_lang, REPOS repos, int N){
    create_qtty_array(repos_lang,1);
    organize_id(repos_lang);
    heapSort_qtty(repos_lang, repos_lang->length_qtty);
    FILE *output = fopen(file, "a");
    for(int i=0; i<repos_lang->length_qtty && i<N; i++){
        char *lang = find_language(repos, repos_lang->user_id_final[i]);
        fprintf(output, "%s\n", lang);
    }
    destroy_PARAM(repos_lang);
    fclose(output);
}

/**
 * Produces the output for query 8
 * UI special
 */
void output_q8_UI(char *file, PARAM repos_lang, REPOS repos, int N){
    create_qtty_array(repos_lang,1);
    organize_id(repos_lang);
    heapSort_qtty(repos_lang, repos_lang->length_qtty);
    FILE *output = fopen(file, "a");
    for(int i=0; i<repos_lang->length_qtty && i<N; i++){
        char *lang = find_language(repos, repos_lang->user_id_final[i]);
        fprintf(output, " %dº: %s\n", (i+1), lang);
    }
    destroy_PARAM(repos_lang);
    fclose(output);
}

/**
 * Produces the output for query 9
 */
void output_q9(char *file, PARAM comms_date, USERS users, int N){
    FILE *output = fopen(file, "a");
    for(int i=0; i<comms_date->length_qtty && i<N; i++){
        int pos = posBinarySearch_Users(users, 0, get_lenght_users(users)-1, comms_date->user_id_final[i]);
        fprintf(output, "%d;%s\n", comms_date->user_id_final[i], get_login_users(users, pos));
    }
    fclose(output);
}

/**
 * Produces the output for query 9
 * UI special
 */
void output_q9_UI(char *file, PARAM comms_date, USERS users, int N){
    FILE *output = fopen(file, "a");
    for(int i=0; i<comms_date->length_qtty && i<N; i++){
        int pos = posBinarySearch_Users(users, 0, get_lenght_users(users)-1, comms_date->user_id_final[i]);
        fprintf(output, "%dº: %d - %s\n", (i+1), comms_date->user_id_final[i], get_login_users(users, pos));
    }
    fclose(output);
}

/**
 * Produces the output for query 10
 */
void output_q10(char *file, int N, COMMITS commits, USERS users){
    int i=0;
    PARAM topN_msg = create_3WAYPARAM();
    FILE *output = fopen(file, "a");
    for( ; i<get_lenght_commits(commits)-1; i++){
        topN_msg->length = 0;
        while(i<get_lenght_commits(commits)-1 && (get_comms_repo_id(commits, i) == get_comms_repo_id(commits, i+1))){
            add_3WAY(topN_msg, get_comms_repo_id(commits,i), get_comms_commiter_id(commits,i), get_msg_size(commits, i)); i++;
        }
        add_3WAY(topN_msg, get_comms_repo_id(commits,i), get_comms_commiter_id(commits,i), get_msg_size(commits, i));
        heapSort_3WAY(topN_msg, topN_msg->length);
        for(int i=0; i<topN_msg->length && i<N; i++){
            int pos = posBinarySearch_Users(users, 0, get_lenght_users(users)-1, topN_msg->user_id[i]);
            char *login = get_login_users(users, pos);
            fprintf(output, "%d;%s;%d;%d\n", topN_msg->user_id[i], login, topN_msg->qtty[i], topN_msg->user_id_final[i]);
            free(login);
        }
    }
    fclose(output);
    free(topN_msg->user_id);
    destroy_PARAM(topN_msg);
}

/**
 * Produces the output for query 10
 * UI special
 */
void output_q10_UI(char *file, int N, COMMITS commits, USERS users){
    int i=0;
    PARAM topN_msg = create_3WAYPARAM();
    FILE *output = fopen(file, "a");
    for( ; i<get_lenght_commits(commits)-1; i++){
        topN_msg->length = 0;
        while(i<get_lenght_commits(commits)-1 && (get_comms_repo_id(commits, i) == get_comms_repo_id(commits, i+1))){
            add_3WAY(topN_msg, get_comms_repo_id(commits,i), get_comms_commiter_id(commits,i), get_msg_size(commits, i)); i++;
        }
        add_3WAY(topN_msg, get_comms_repo_id(commits,i), get_comms_commiter_id(commits,i), get_msg_size(commits, i));
        heapSort_3WAY(topN_msg, topN_msg->length);
        fprintf(output,"------------------------ Repositório %d ------------------------\n", get_comms_repo_id(commits, i));
        for(int i=0; i<topN_msg->length && i<N; i++){
            int pos = posBinarySearch_Users(users, 0, get_lenght_users(users)-1, topN_msg->user_id[i]);
            char *login = get_login_users(users, pos);
            fprintf(output, "%dº: %d - %s - %d\n", (i+1), topN_msg->user_id[i], login, topN_msg->qtty[i]);
            free(login);
        }
    }
    fclose(output);
    free(topN_msg->user_id);
    destroy_PARAM(topN_msg);
}
