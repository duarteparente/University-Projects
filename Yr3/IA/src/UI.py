import copy
from subprocess import call
import os

class Menu:

    def __init__(self, default):
        self.filepath = default
    
    def set_filepath(self, filepath):
        self.filepath = filepath
    
    def get_newFilepath(self):
        self.filepath = input("\n  »» Filepath: ")
        while not os.path.isfile(self.filepath):
            print(" File does not exist!")
            self.filepath = input("\n  »» Filepath: ")
        return self.filepath
    
    def show_gameMap(self):
        file = open(self.filepath).read().split('\n')
        for line in file:
            print("       " + line)


    def show_menu(self):
        self.clear_screen()
        print("\n\n             Problem Solving - Search Algorithms  ::  LEI-IA-17\n\n\n   Track:\n")
        self.show_gameMap()
        s  = str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 1  |  Select another Track                                            |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 2  |  Import your Racetrack                                           |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 3  |  Print Graph                                                     |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   |                     Uninformed Search Algorithms                      |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 4  |  Breadth-First Search (BFS)                                      |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 5  |  Depth-Fisrt Search (DFS)                                        |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 6  |  Iteractive Deepening Search (IDS)                               |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 7  |  Uniform Cost Search (UCS)                                       |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   |                     Informed Search Algorithms                        |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 8  |  Greedy Search (GS)                                              |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 9  |  A* Search (A*S)                                                 |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 10 |  Multiplayer                                                     |")
        s += str("\n   +-----------------------------------------------------------------------+")
        s += str("\n   | 11 |  Leave                                                           |")
        s += str("\n   +-----------------------------------------------------------------------+")
        print(s)


    def show_sndMenu(self):
        sm  = str("\n   +-----------------------------------------------------------------------+")
        sm += str("\n   | M  |  Go back to main menu                                            |")
        sm += str("\n   +-----------------------------------------------------------------------+")
        sm += str("\n   | S  |  Leave                                                           |")
        sm += str("\n   +-----------------------------------------------------------------------+")
        print(sm)


    def player_won(self, id):
        s =  "\n             +----------+"
        s += "\n  > WINNER:  | Player " + str(id) + " |"
        s += "\n             +----------+"
        print(s)

    
    def output(self, option, start, finish, path, cost, visited, gameMap):
        if   option == '4':
            res = "\n\n   ========== Breadth-First Search ==========\n\n"
        elif option == '5':
            res = "\n\n   ==========  Depth-First Search  ==========\n\n"
        elif option == '7':
            res = "\n\n    ========== Uniform Cost Search ==========\n\n"
        elif option == '8':
            res = "\n\n   ==========   Greedy Search   ==========\n\n"
        elif option == '9':
            res = "\n\n   ==========   A* Search   ==========\n\n"
        res += self.draw_path(gameMap, path)
        res += "\n\n   Start: " + str(start) + "      Finish: " + str(finish)
        res += "\n\n      » Final path: "
        for i in range(len(path)-1):
            res += ( str(path[i]) + " -> ")
        res += (str(path[-1]) + "\n      » Cost: " + str(cost))
        res += "\n      » Number of nodes visited: " + str(len(visited))
        res += "\n      » Full path taken: "
        for i in range(len(visited)-1):
            res += ( str(visited[i]) + " -> ")
        res += str(visited[-1])
        print(res + "\n")


    def IDS_output(self, start, finish, path, cost,  visited_nodes, iterations, total_visited, gameMap):
        res = "\n\n   ========== Iteractive Deepening Search ==========\n\n"
        res += self.draw_path(gameMap, path)
        res += "\n\n   Start: " + str(start) + "      Finish: " + str(finish)
        res += "\n\n      » Final path: "
        for i in range(len(path)-1):
            res += ( str(path[i]) + " -> ")
        res += (str(path[-1]) + "\n      » Cost: " + str(cost) + "\n      » Number of iterations: " + str(iterations))
        res += "\n      » Total number of nodes visited: " + str(total_visited) + "\n      » Number of nodes visited in last iteration: " + str(len(visited_nodes)) + "\n      » Full path taken in last iteration: "
        for i in range(len(visited_nodes)-1):
            res += ( str(visited_nodes[i]) + " -> ")
        res += str(visited_nodes[-1])
        print(res + "\n")



    def get_option(self): 
        return input("\n  »» Option: ")


    def clear_screen(self):
        call('clear' if os.name == 'posix' else 'cls')

    
    def draw_path(self, gameMap, path):
        aux = copy.deepcopy(gameMap)
        for p in path[1:]:
            aux[p[0]-1][p[1]-1] = '*'
        s = ""
        for i in range(len(aux)):
            s += "\n      "
            for j in range(len(aux[i])):
                s += aux[i][j]
        return s


    def maps_catalogue(self):
        self.clear_screen()
        for i in range(len(os.listdir("tracks/"))):
            if os.path.isfile("tracks/track" + str(i+1) + ".txt"):
                print("\n     ==========   Track %d  ==========\n" % (i+1))
                file = [l.rstrip() for l in open("tracks/track" + str(i+1) + ".txt")]
                for line in file:
                    print("       " + line)
        choice = self.get_option()
        return "tracks/track" + choice + ".txt" 
