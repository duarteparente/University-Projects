from DLS import DLS

#####################################################################################
#                            Iteractive Deepening Search                            #
#####################################################################################

class IDS:

    def __init__(self, graph, start_node, goal):
        self.graph = graph
        self.start = start_node
        self.goal = goal
        self.limit = 0
        self.total_nodes_visited = 0
    

    def search(self):
       
        while True:
            dls = DLS(self.graph, self.start, self.goal, self.limit)
            result = dls.search()
            if result[0] != None:
                result = list(result)
                result.append(self.limit)
                result.append(self.total_nodes_visited)
                return result 
            self.limit += 1
            self.total_nodes_visited += result[1]