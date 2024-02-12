
############################################################################
#                            Depth-First Search                            #
############################################################################

class DFS:

    def __init__(self, graph, start_node, goal):
        self.graph = graph
        self.start = start_node
        self.goal = goal
        self.visited = []
        self.stack = [(self.start, [self.start])]  # stack holds node and current path
    

    def search(self):

        while self.stack:
            current_node, path = self.stack.pop()

            if current_node not in self.visited:
                self.visited.append(current_node)

                if current_node in self.goal:
                    cost = self.graph.path_cost(path)
                    return path, cost, self.visited
                for adj, _ in self.graph.get_neighbours(current_node):
                    self.stack.append((adj, path + [adj]))
        
        # Path not found
        return None, self.visited
    

    def play(self, blocked_nodes):

        current_node, path = self.stack.pop()
        while current_node in blocked_nodes or current_node in self.visited:
            current_node, path = self.stack.pop()
        
        self.visited.append(current_node)
        if current_node not in self.goal:
            for adj, _ in self.graph.get_neighbours(current_node):
                self.stack.append((adj, path + [adj]))
        
        return current_node, path
    

    def get_results(self, path):
        cost = self.graph.path_cost(path)
        return list((path, cost, self.visited))


        


    
