
##############################################################################
#                            Depth Limited Search                            #
##############################################################################

class DLS:

    def __init__(self, graph, start_node, goal, limit):
        self.graph = graph
        self.start = start_node
        self.goal = goal
        self.limit = limit
        self.visited = []
        self.stack = []  # stack holds node and current path

    
    def search(self):
        self.stack.append((self.start, [self.start]))
        iteration = 0

        while self.stack:
            current_node, path = self.stack.pop()

            if current_node not in self.visited:
                self.visited.append(current_node)

                if current_node in self.goal:
                    cost = self.graph.path_cost(path)
                    return path, cost, self.visited
                if iteration != self.limit:
                    iteration += 1
                    for adj, _ in self.graph.get_neighbours(current_node):
                        self.stack.append((adj, path + [adj]))
        
        # Path not found
        return None, len(self.visited)

    
