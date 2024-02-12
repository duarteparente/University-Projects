from queue import Queue

##############################################################################
#                            Breadth-First Search                            #
##############################################################################

class BFS:

    def __init__(self, graph, start_node, goal):
        self.graph = graph
        self.start = start_node
        self.goal = goal
        self.expanded = [self.start]
        self.visited = []
        self.parents = {}
        self.parents[self.start] = None
        self.queue = Queue()
        self.queue.put(self.start)

    
    def construct_path(self, current_node):
        path = [current_node]
        while self.parents[current_node] is not None:
                path.append(self.parents[current_node])
                current_node = self.parents[current_node]
        return path[::-1]
    

    def search(self):

        while not self.queue.empty():
            current_node = self.queue.get()
            self.visited.append(current_node)

            if current_node in self.goal:
                path = self.construct_path(current_node)
                cost = self.graph.path_cost(path)
                return path, cost, self.visited
            
            for adj, _ in self.graph.get_neighbours(current_node):
                if adj not in self.expanded:
                    self.queue.put(adj)
                    self.parents[adj] = current_node
                    self.expanded.append(adj)
        
        # Path not found
        return None, self.visited
    

    def play(self, blocked_nodes):

        current_node = self.queue.get()
        
        while current_node in blocked_nodes:
            self.queue.put(current_node)
            current_node = self.queue.get()

        self.visited.append(current_node)

        if current_node not in self.goal:
            for adj, _ in self.graph.get_neighbours(current_node):
                if adj not in self.expanded:
                    self.queue.put(adj)
                    self.parents[adj] = current_node
                    self.expanded.append(adj)
        
        return current_node
    

    def get_results(self, current_node):
        path = self.construct_path(current_node)
        cost = self.graph.path_cost(path)
        return list((path, cost, self.visited))
        




