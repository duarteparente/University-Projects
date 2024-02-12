
##############################################################################
#                            Greedy Search                                   #
##############################################################################

class Greedy:

    def __init__(self, graph, start_node, goal):
        self.graph = graph
        self.start = start_node
        self.goal = goal
        self.open_list = set()
        self.open_list.add(self.start)
        self.closed_list = set()
        self.v = {}
        self.v[self.start] = (0,0)
        self.parents = {}
        self.parents[self.start] = self.start
        self.heuristics = {}
        self.heuristics[self.start] = self.graph.heuristic(self.start, self.start, self.goal)
        self.visited = []


    def construct_path(self, current_node):
        path = []
        while self.parents[current_node] != current_node:
            path.append(current_node)
            current_node = self.parents[current_node]
        path.append(current_node)
        return path[::-1]    


    def search(self):
        
        while len(self.open_list) > 0:
            current_node = None

            for n in self.open_list:
                if current_node == None or self.heuristics[n] < self.heuristics[current_node]:
                    current_node = n
            
            if current_node == None:
                return None, self.visited
            
            self.visited.append(current_node)

            if not self.graph.is_possible(self.parents[current_node], current_node):
                self.open_list.remove(current_node)
                self.closed_list.add(current_node)
                current_node = self.parents[current_node]
                new_list = self.open_list.copy()
                for i in new_list:
                    if self.parents[i] == current_node:
                        self.open_list.remove(i)
                self.open_list.add(current_node)
                self.v[current_node] = (0,0)
    

            if current_node in self.goal:
                path = self.construct_path(current_node)
                cost = len(path)
                return path, cost, self.visited
            
        
            
            possible_moves = self.graph.get_possible_moves(current_node, self.v[current_node], self.v)

            for p in possible_moves:
                self.heuristics[p] = self.graph.heuristic(current_node, p, self.goal)

                if p not in self.open_list and p not in self.closed_list:
                    self.open_list.add(p)
                    self.parents[p] = current_node
            
            self.open_list.remove(current_node)
            self.closed_list.add(current_node)
        
        
        return None, self.visited
    

    def play(self, blocked_nodes):
        
        current_node = None

        for n in self.open_list:
            if (current_node == None or self.heuristics[n] < self.heuristics[current_node]) and n not in blocked_nodes:
                current_node = n
            
        self.visited.append(current_node)

        if not self.graph.is_possible(self.parents[current_node], current_node):
            self.open_list.remove(current_node)
            self.closed_list.add(current_node)
            current_node = self.parents[current_node]
            new_list = self.open_list.copy()
            for i in new_list:
                if self.parents[i] == current_node:
                    self.open_list.remove(i)
            self.open_list.add(current_node)
            self.v[current_node] = (0,0)
        
        if current_node not in self.goal:
            possible_moves = self.graph.get_possible_moves(current_node, self.v[current_node], self.v)

            for p in possible_moves:
                self.heuristics[p] = self.graph.heuristic(current_node, p, self.goal)

                if p not in self.open_list and p not in self.closed_list:
                    self.open_list.add(p)
                    self.parents[p] = current_node
            
            self.open_list.remove(current_node)
            self.closed_list.add(current_node)
        
        return current_node
    

    def get_results(self, current_node):
        path = self.construct_path(current_node)
        cost = len(path)
        return list((path, cost, self.visited))




        


        
        

        


            

                

