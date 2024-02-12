
#####################################################################################
#                            Uniformed Cost Search                                  #
#####################################################################################

class UCS:

    def __init__(self, graph, start_node, goal):
        self.graph = graph
        self.start = start_node
        self.goal = goal
        self.unvisited = set()
        self.unvisited.add(self.start)
        self.visited = []
        self.costs = {}
        self.costs[self.start] = 0
        self.parents = {}
        self.parents[self.start] = None

    
    def construct_path(self, current_node):
        path = [current_node]
        while self.parents[current_node] is not None:
                path.append(self.parents[current_node])
                current_node = self.parents[current_node]
        return path[::-1]


    def search(self):
        
        while self.unvisited:

            current_node = None
            for node in self.unvisited:
                if current_node is None or self.costs[node] < self.costs[current_node]:
                    current_node = node
            
            self.visited.append(current_node)
            self.unvisited.remove(current_node)

            if current_node in self.goal:
                path = self.construct_path(current_node)
                path_cost = self.graph.path_cost(path)
                return path, path_cost, self.visited
            
            for adj, weigth in self.graph.get_neighbours(current_node):
                if adj not in self.visited:
                    self.unvisited.add(adj)
                    cost = weigth + self.costs[current_node]
                    if adj not in self.costs.keys() or cost < self.costs[adj]:
                        self.parents[adj] = current_node
                        self.costs[adj] = cost
    
        return None, self.visited



    def play(self, blocked_nodes):

        current_node = None
        for node in self.unvisited:
            if (current_node is None or self.costs[node] < self.costs[current_node]) and node not in blocked_nodes:
                current_node = node

        self.visited.append(current_node)
        self.unvisited.remove(current_node)

        if current_node not in self.goal:
            for adj, weigth in self.graph.get_neighbours(current_node):
                if adj not in self.visited:
                    self.unvisited.add(adj)
                    cost = weigth + self.costs[current_node]
                    if adj not in self.costs.keys() or cost < self.costs[adj]:
                        self.parents[adj] = current_node
                        self.costs[adj] = cost
        
        return current_node
    

    def get_results(self, current_node):
        path = self.construct_path(current_node)
        cost = self.graph.path_cost(path)
        return list((path, cost, self.visited))

        



