/**
 * @file tests.c 
 * \brief Queries analysis and execution.
 */ 
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "../include/tests.h"
#include "../include/queries.h"
#include "../include/parsing.h"
#include "../include/auxiliar.h"
#define __USE_XOPEN
#include <time.h>

/**
 * \brief Query 1 time execution function.
 */
double query_1_time(char *file, STATS stats){
    clock_t start, end;
    start = clock();
    query_1(file, stats);
    end = clock();
    return ((double) (end - start)) / CLOCKS_PER_SEC;
}


/**
 * \brief Query 2 time execution function.
 */
double query_2_time(char *file, STATS stats){
    clock_t start, end;
    start = clock();
    query_2(file, stats);
    end = clock();
    return ((double) (end - start)) / CLOCKS_PER_SEC;

}


/**
 * \brief Query 3 time execution function.
 */
double query_3_time(char *file, STATS stats){
    clock_t start, end;
    start = clock();
    query_3(file, stats);
    end = clock();
    return ((double) (end - start)) / CLOCKS_PER_SEC;
}


/**
 * \brief Query 4 time execution function.
 */
double query_4_time(char *file, STATS stats){
    clock_t start, end;
    start = clock();
    query_4(file, stats);
    end = clock();
    return ((double) (end - start)) / CLOCKS_PER_SEC;
}


/**
 * \brief Query 5 time execution function.
 */
double query_5_time(char *file, char *date_to, char *date_from, int N, COMMITS commits, USERS users){
    clock_t start, end;
    start = clock();
    query_5(0, file, date_to, date_from, N, commits, users);
    end = clock();
    return ((double) (end - start)) / CLOCKS_PER_SEC;
}


/**
 * \brief Query 6 time execution function.
 */
double query_6_time(char *file, char *language, int N, COMMITS commits, USERS users, REPOS repos){
    clock_t start, end;
    start = clock();
    query_6(0, file, language, N, commits, users, repos);
    end = clock();
    return ((double) (end - start)) / CLOCKS_PER_SEC;
}



/**
 * \brief Query 7 time execution function.
 */
double query_7_time(char *file, char *date_from, COMMITS commits, REPOS repos){
    clock_t start, end;
    start = clock();
    query_7(0, file, date_from, commits, repos); 
    end = clock();
    return ((double) (end - start)) / CLOCKS_PER_SEC;
}

/**
 * \brief Query 8 time execution function.
 */
double query_8_time(char *file, char *date_from, int N, COMMITS commits, REPOS repos){
    clock_t start, end;
    start = clock();
    query_8(0, file, date_from,  N, commits, repos);
    end = clock();
    return ((double) (end - start)) / CLOCKS_PER_SEC;
}

/**
 * \brief Query 9 time execution.
 */
double query_9_time(char *file, int N, COMMITS commits, USERS users){
    clock_t start, end;
    start = clock();
    query_9(0, file, N, commits, users);
    end = clock();
    return ((double) (end - start)) / CLOCKS_PER_SEC;
}

/**
 * \brief Query 10 execution.
 */
double query_10_time(char *file, int N , COMMITS commits, USERS users){
    clock_t start, end;
    start = clock();
    query_10(0, file, N, commits, users);
    end = clock();
    return ((double) (end - start)) / CLOCKS_PER_SEC;
}

/**
 * Get time execution for a given query (Test option 1).
 */
double choose_time_query_op1(int i, STATS stats, USERS users, COMMITS commits, REPOS repos){
    double r;
    switch(i){
        case 1: r = query_1_time("saida/query1.txt", stats); break;
        case 2: r = query_2_time("saida/query2.txt", stats); break;
        case 3: r = query_3_time("saida/query3.txt", stats); break;
        case 4: r = query_4_time("saida/query4.txt", stats); break;
        case 5: r = query_5_time("saida/query5.txt", "2016-01-01", "2014-01-01", 10, commits, users); break;
        case 6: r = query_6_time("saida/query6.txt", "HTML", 5, commits, users, repos); break;
        case 7: r = query_7_time("saida/query7.txt", "2008-12-31", commits, repos); break;
        case 8: r = query_8_time("saida/query8.txt", "2017-12-31", 2, commits, repos); break;
        case 9: r = query_9_time("saida/query9.txt", 3, commits, users); break;
        case 10: r = query_10_time("saida/query10.txt", 3, commits, users); break;
    }
    return r;
}

/**
 * Get time execution for a given query (Test option 2).
 */
double choose_time_query_op2(int i, STATS stats, USERS users, COMMITS commits, REPOS repos){
    double r;
    switch(i){
        case 1: r = query_1_time("saida/query1.txt", stats); break;
        case 2: r = query_2_time("saida/query2.txt", stats); break;
        case 3: r = query_3_time("saida/query3.txt", stats); break;
        case 4: r = query_4_time("saida/query4.txt", stats); break;
        case 5: r = query_5_time("saida/query5.txt", "2016-01-01", "2012-01-01", 200, commits, users); break;
        case 6: r = query_6_time("saida/query6.txt", "Python", 15, commits, users, repos); break;
        case 7: r = query_7_time("saida/query7.txt", "2015-12-31", commits, repos); break;
        case 8: r = query_8_time("saida/query8.txt", "2015-12-31", 6, commits, repos); break;
        case 9: r = query_9_time("saida/query9.txt", 6, commits, users); break;
        case 10: r = query_10_time("saida/query10.txt", 10, commits, users); break;
    }
    return r;
}

/**
 * Get time execution for a given query (Test option 3).
 */
double choose_time_query_op3(int i, STATS stats, USERS users, COMMITS commits, REPOS repos){
    double r;
    switch(i){
        case 1: r = query_1_time("saida/query1.txt", stats); break;
        case 2: r = query_2_time("saida/query2.txt", stats); break;
        case 3: r = query_3_time("saida/query3.txt", stats); break;
        case 4: r = query_4_time("saida/query4.txt", stats); break;
        case 5: r = query_5_time("saida/query5.txt", "2016-01-01", "2008-01-01", 1000, commits, users); break;
        case 6: r = query_6_time("saida/query6.txt", "JavaScript", 100, commits, users, repos); break;
        case 7: r = query_7_time("saida/query7.txt", "2017-12-31", commits, repos); break;
        case 8: r = query_8_time("saida/query8.txt", "2008-12-31", 12, commits, repos); break;
        case 9: r = query_9_time("saida/query9.txt", 20, commits, users); break;
        case 10: r = query_10_time("saida/query10.txt", 50, commits, users); break;
    }
    return r;
}

/**
 * Get time execution for a given query (Test option 4).
 */
double choose_time_query_op4(int i, STATS stats, USERS users, COMMITS commits, REPOS repos){
    double r;
    switch(i){
        case 1: r = query_1_time("saida/query1.txt", stats); break;
        case 2: r = query_2_time("saida/query2.txt", stats); break;
        case 3: r = query_3_time("saida/query3.txt", stats); break;
        case 4: r = query_4_time("saida/query4.txt", stats); break;
        case 5: r = query_5_time("saida/query5.txt", "2020-03-14", "2011-10-31", 500, commits, users); break;
        case 6: r = query_6_time("saida/query6.txt", "Ruby", 5, commits, users, repos); break;
        case 7: r = query_7_time("saida/query7.txt", "2010-04-25", commits, repos); break;
        case 8: r = query_8_time("saida/query8.txt", "2012-02-20", 7, commits, repos); break;
        case 9: r = query_9_time("saida/query9.txt", 1, commits, users); break;
        case 10: r = query_10_time("saida/query10.txt", 1, commits, users); break;
    }
    return r;
}

/**
 * Get time execution for a given query (Test option 5).
 */
double choose_time_query_op5(int i, STATS stats, USERS users, COMMITS commits, REPOS repos){
    double r;
    switch(i){
        case 1: r = query_1_time("saida/query1.txt", stats); break;
        case 2: r = query_2_time("saida/query2.txt", stats); break;
        case 3: r = query_3_time("saida/query3.txt", stats); break;
        case 4: r = query_4_time("saida/query4.txt", stats); break;
        case 5: r = query_5_time("saida/query5.txt", "2013-11-24", "2009-06-30", 750, commits, users); break;
        case 6: r = query_6_time("saida/query6.txt", "C", 20, commits, users, repos); break;
        case 7: r = query_7_time("saida/query7.txt", "2020-01-01", commits, repos); break;
        case 8: r = query_8_time("saida/query8.txt", "2009-12-31", 2, commits, repos); break;
        case 9: r = query_9_time("saida/query9.txt", 10, commits, users); break;
        case 10: r = query_10_time("saida/query10.txt", 25, commits, users); break;
    }
    return r;
}

/**
 * Get time execution for a given query (Test option 6).
 */
double choose_time_query_op6(int i, STATS stats, USERS users, COMMITS commits, REPOS repos){
    double r;
    switch(i){
        case 1: r = query_1_time("saida/query1.txt", stats); break;
        case 2: r = query_2_time("saida/query2.txt", stats); break;
        case 3: r = query_3_time("saida/query3.txt", stats); break;
        case 4: r = query_4_time("saida/query4.txt", stats); break;
        case 5: r = query_5_time("saida/query5.txt", "2016-01-01", "2013-01-01", 3, commits, users); break;
        case 6: r = query_6_time("saida/query6.txt", "Haskell", 3, commits, users, repos); break;
        case 7: r = query_7_time("saida/query7.txt", "2007-08-19", commits, repos); break;
        case 8: r = query_8_time("saida/query8.txt", "2008-09-22", 12, commits, repos); break;
        case 9: r = query_9_time("saida/query9.txt", 5, commits, users); break;
        case 10: r = query_10_time("saida/query10.txt", 5, commits, users); break;
    }
    return r;
}

/**
 * Show the table with all test results.
 */
void print_ResultsTable (int test_option, double load_time, double *time){
    clear_window();
    printf("\n\n      === Módulo de Testes (Opção %d) ===\n\n\n", test_option);
    printf("   »» Preenchimento dos catálogos: %gs\n\n\n", load_time);
    puts("               +-----------+------------------+--------------+\n"
         "       +-------| Tempo de  |  Comparação dos  |  Execução em |\n"
         "       | Query | execução  |    resultados    |  tempo útil  |\n"
         "       +-------+-----------+------------------+--------------+" );
    for(int i=0; i<9; i++) { 
        printf("       |     %d | %.6fs |    %s     |     %s      |\n", i+1, time[i], ((time[20+i] == 0) ? "Incorreto" : " Correto "), ((time[i] > 5) ? "Não" : "Sim"));
        puts("       +-------+-----------+------------------+--------------+");
    }
    printf("       |    10 | %.6fs |    %s     |     %s      |\n", time[9], ((time[29] == 0) ? "Incorreto" : " Correto "), ((time[9] > 5) ? "Não" : "Sim"));
    puts("       +-------+-----------+------------------+--------------+\n\n\n");
}

/**
 * Create a file with commands for queries to be tested.
 */
void create_commands_file(int test_option){
    FILE *commands_file = fopen("entrada/test_commands.txt", "a");
    switch(test_option){
        case 1: fprintf(commands_file, "1\n2\n3\n4\n5 10 2014-01-01 2016-01-01\n6 5 HTML\n7 2008-12-31\n8 2 2017-12-31\n9 3\n10 3"); break;
        case 2: fprintf(commands_file, "1\n2\n3\n4\n5 200 2012-01-01 2016-01-01\n6 15 Python\n7 2015-12-31\n8 6 2015-12-31\n9 6\n10 10"); break;
        case 3: fprintf(commands_file, "1\n2\n3\n4\n5 1000 2008-01-01 2016-01-01\n6 100 JavaScript\n7 2017-12-31\n8 12 2008-12-31\n9 20\n10 50"); break;
        case 4: fprintf(commands_file, "1\n2\n3\n4\n5 500 2011-10-31 2020-03-14\n6 5 Ruby\n7 2010-04-25\n8 7 2012-02-20\n9 1\n10 1"); break;
        case 5: fprintf(commands_file, "1\n2\n3\n4\n5 750 2009-06-30 2013-11-24\n6 20 C\n7 2020-01-01\n8 2 2009-12-31\n9 10\n10 25"); break;
        case 6: fprintf(commands_file, "1\n2\n3\n4\n5 3 2013-01-01 2016-01-01\n6 3 Haskell\n7 2007-08-19\n8 12 2015-09-22\n9 5\n10 5"); break;
        default: break;
    }
    fclose(commands_file);
}


/**
 * Controls file comparison.
 */
void compare_files_controller(double *tests){
    for(int i=20, j=1; i<30; i++, j++)
        tests[i] = compare_files(j);
}

/**
 * Compare expected with resulting file.
 */
int compare_files(int query_id){
    char expected[40];
    snprintf(expected, 40, "saida/command%d_output.txt", query_id);
    char result[30];
    snprintf(result, 30, "saida/query%d.txt", query_id);
    FILE *exp = fopen(expected, "r");
    FILE *res = fopen(result, "r");
    char ch1 = getc(exp);
    char ch2 = getc(res);
    while(ch1 != EOF && ch2 != EOF){
        if(ch1 != ch2) return 0;
        ch1 = getc(exp);
        ch2 = getc(res);
    }
    if(ch1 != EOF || ch2 != EOF) return 0;
    return 1;
    fclose(exp);
    fclose(res);
}

/**
 * Get query time execution (Test option 1).
 */
void fill_time_op1(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos){
    for(int i=0; i<10; i++){
        tests[i] = choose_time_query_op1(i+1, stats, users, commits, repos);
        if(tests[i] > 5) tests[10+i] = 1; 
    } 
}

/**
 * Get query time execution (Test option 2).
 */
void fill_time_op2(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos){
    for(int i=0; i<10; i++){
        tests[i] = choose_time_query_op2(i+1, stats, users, commits, repos);
        if(tests[i] > 5) tests[10+i] = 1; 
    } 
}

/**
 * Get query time execution (Test option 3).
 */
void fill_time_op3(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos){
    for(int i=0; i<10; i++){
        tests[i] = choose_time_query_op3(i+1, stats, users, commits, repos);
        if(tests[i] > 5) tests[10+i] = 1; 
    } 
}

/**
 * Get query time execution (Test option 4).
 */
void fill_time_op4(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos){
    for(int i=0; i<10; i++){
        tests[i] = choose_time_query_op4(i+1, stats, users, commits, repos);
        if(tests[i] > 5) tests[10+i] = 1; 
    } 
}

/**
 * Get query time execution (Test option 5).
 */
void fill_time_op5(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos){
    for(int i=0; i<10; i++){
        tests[i] = choose_time_query_op5(i+1, stats, users, commits, repos);
        if(tests[i] > 5) tests[10+i] = 1; 
    } 
}

/**
 * Get query time execution (Test option 6).
 */
void fill_time_op6(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos){
    for(int i=0; i<10; i++){
        tests[i] = choose_time_query_op6(i+1, stats, users, commits, repos);
        if(tests[i] > 5) tests[10+i] = 1; 
    } 
}

/**
 * Controller of the testing module.
 */
void tests_controller(int test_option, double load_time, STATS stats, USERS users, COMMITS commits, REPOS repos){
    double *tests = calloc(30,sizeof(double));
    switch(test_option){
        case 1: fill_time_op1(tests,stats,users,commits,repos); break;
        case 2: fill_time_op2(tests,stats,users,commits,repos); break;
        case 3: fill_time_op3(tests,stats,users,commits,repos); break;
        case 4: fill_time_op4(tests,stats,users,commits,repos); break;
        case 5: fill_time_op5(tests,stats,users,commits,repos); break;
        case 6: fill_time_op6(tests,stats,users,commits,repos); break;
        default: break;
    }  
    compare_files_controller(tests);
    delete_TestFiles();
    print_ResultsTable(test_option, load_time, tests);
    free(tests);
}

/**
 * Execution of programm's first version.
 */
void create_expected_files(STATS stats, USERS users, COMMITS commits, REPOS repos, int test_option){
    create_commands_file(test_option);
    queries(stats,users,commits,repos,"entrada/test_commands.txt");
    delete_commands_file();
}

/**
 * Basic test module user interface.
 */
int tests_UI(){
    clear_window();
    puts("\n      == Opções para o ficheiro de comandos de teste ==\n\n\n\n"
         "            « 1ª Opção »                    « 2ª Opção »                  « 3ª Opção »\n\n"
         "   | 1                            | 1                            | 1\n"
         "   | 2                            | 2                            | 2\n"
         "   | 3                            | 3                            | 3\n"
         "   | 4                            | 4                            | 4\n"
         "   | 5 10 2014-01-01 2016-01-01   | 5 200 2012-01-01 2016-01-01  | 5 1000 2008-01-01 2016-01-01\n"
         "   | 6 5 HTML                     | 6 15 Python                  | 6 100 JavaScript\n"
         "   | 7 2008-12-31                 | 7 2015-12-31                 | 7 2017-12-31\n"
         "   | 8 2 2017-12-31               | 8 6 2015-12-31               | 8 12 2008-12-31\n"
         "   | 9 3                          | 9 6                          | 9 20\n"
         "   | 10 3                         | 10 10                        | 10 50\n\n"
         "            « 4ª Opção »                    « 5ª Opção »                  « 6ª Opção »\n\n"
         "   | 1                            | 1                            | 1\n"
         "   | 2                            | 2                            | 2\n"
         "   | 3                            | 3                            | 3\n"
         "   | 4                            | 4                            | 4\n"
         "   | 5 500 2011-10-31 2020-03-14  | 5 750 2009-06-30 2013-11-24  | 5 3 2013-01-01 2016-01-01\n"
         "   | 6 5 Ruby                     | 6 20 C                       | 6 3 Haskell\n"
         "   | 7 2010-04-25                 | 7 2020-01-01                 | 7 2007-08-19\n"
         "   | 8 7 2012-02-20               | 8 2 2009-12-31               | 8 12 2015-09-22\n"
         "   | 9 1                          | 9 10                         | 9 5\n"
         "   | 10 1                         | 10 25                        | 10 5\n\n");
    printf(" »» Opção: ");
    int option = getchar() - '0';
    return option;
}


/**
 * \brief Main funtion of the testing module.
 */
int main(int argc, char* argv[]){
    int test_option;
    if (argc != 2) test_option = tests_UI();
    else test_option = atoi(argv[1]);
    STATS stats = create_STATS();
    clock_t start, end;
    start = clock();
    USERS users = read_users(stats);
    REPOS repos_valid = validate_fields_repos(stats);
    COMMITS commits = read_commits(stats,users,repos_valid);
    REPOS repos = read_repos(stats,repos_valid,users,commits);
    end = clock();
    double load_time = (double) (end - start) / CLOCKS_PER_SEC;
    create_expected_files(stats,users,commits,repos,test_option);
    tests_controller(test_option, load_time,stats,users,commits,repos);
    destroy_USERS(users);
    destroy_COMMITS(commits);
    destroy_REPOS(repos);
    destroy_STATS(stats);
    return 0;
}


