import random
from DFS import DFS
from BFS import BFS
from Greedy import Greedy
from UCS import UCS
from AStar import AStar

class multiplayer:

    def __init__(self, graph, start, goal):
        self.graph = graph
        self.start = start
        self.goal = goal
        self.nr_players = self.get_nrplayers()
    

    def print_rankings(self, rankings):
        alg = {
            '4': "BFS",
            '5': "DFS",
            '7': "UCS",
            '8': "Greedy",
            '9': "AStar"
        }
        j = 0
        print("\n=======================  RANKING  =======================\n")
        for i in range(0,len(rankings),2):
            j += 1
            print("   %dº - Player %d (%s)" % (j, rankings[i+1], alg[rankings[i]]))



    
    
    def get_nrplayers(self):
        res = int(input("    » Select number of players [2-4]: "))
        while res < 2 or res > 4:
            print(" Watch the limits!")
            res = int(input("    » Select number of players [2-4]: "))
        return res
    
    

    def select_algorithm(self):
        print("\n---------------------------------------------------------------------------------")
        print("  Select the algorithm using the main menu indexes\n  (IDS and BS can't be selected and options can't be repeated)\n")
        options = []
        for i in range(self.nr_players):
            s = "    > Player " +  str(i+1) + ": "
            op = input(s)
            while op in options or op == '6':
                print(" Select other algorithm!")
                op = input(s)
            options.append(op)
        return options
    

    def play(self, options):
        players = options[:]
        bfs = BFS(self.graph, self.start, self.goal)
        dfs = DFS(self.graph, self.start, self.goal)
        ucs = UCS(self.graph, self.start, self.goal)
        greedy = Greedy(self.graph, self.start, self.goal)
        astar = AStar(self.graph, self.start, self.goal)
        ranking = []
        res = None
        while len(options) != 0:
            random.shuffle(options)
            blocked_nodes = []
            for i in options:
                if i == '4':
                    result = bfs.play(blocked_nodes)
                    if result in self.goal:
                        ranking.append(i)
                        ranking.append((players.index(i)+1))
                        options.remove(i)
                        if res == None:
                            res = bfs.get_results(result)
                       
                    if result != self.start:   
                        blocked_nodes.append(result)

                elif i == '5':
                    result = dfs.play(blocked_nodes)
                    if result[0] in self.goal:
                        ranking.append(i)
                        ranking.append((players.index(i)+1))
                        options.remove(i)
                        if res == None:
                            res = dfs.get_results(result[1])
                       
                    if result[0] != self.start:
                        blocked_nodes.append(result[0])
                
                elif i == '7':
                    result = ucs.play(blocked_nodes)
                    if result in self.goal:
                        ranking.append(i)
                        ranking.append((players.index(i)+1))
                        options.remove(i)
                        if res == None:
                            res = ucs.get_results(result)
                        
                    if result != self.start:   
                        blocked_nodes.append(result)
                
                elif i == '8':
                    result = greedy.play(blocked_nodes)
                    if result in self.goal:
                        ranking.append(i)
                        ranking.append((players.index(i)+1))
                        options.remove(i)
                        if res == None:
                            res = greedy.get_results(result)
                        
                    if result != self.start:
                        blocked_nodes.append(result)
                
                elif i == '9':
                    result = astar.play(blocked_nodes)
                    if result in self.goal:
                        ranking.append(i)
                        ranking.append((players.index(i)+1))
                        options.remove(i)
                        if res == None:
                            res = astar.get_results(result)

                    if result != self.start:
                        blocked_nodes.append(result)

        winner = ranking[1]
        self.print_rankings(ranking)
        res.append(ranking[0])
        return res, winner
