from queue import Queue
import numpy as np
import sys

from Node import Node


class Graph:

    def __init__(self, oriented, obstacles):
        self.n_nodes = []           # List of nodes
        self.oriented = oriented    # Orientation flag
        self.obstacles = obstacles
        self.n_graph = {}           # node -> { (node_adj1, weigth), (node_adj2, weigth)}
        self.n_h = {}               # heuristic value for each node

    def __str__(self):
        out = ""
        for k in self.n_graph.keys():
            out += str(k) + " -> " + str(self.n_graph[k]) + "\n"
        return out.rstrip(out[-1])


    def add_node(self, node1, node2, weight):
        n1 = Node(node1)
        n2 = Node(node2)
        if n1 not in self.n_nodes:
            self.n_nodes.append(n1)
            self.n_graph[node1] = set()
        if n2 not in self.n_nodes:
            self.n_nodes.append(n2)
            self.n_graph[node2] = set()
        self.n_graph[node1].add((node2, weight))
        if not self.oriented:
            self.n_graph[node2].add((node1, weight))


    def get_node_by_name(self, name):
        search_node = Node(name)
        for node in self.n_nodes:
            if node == search_node:
                return node
            else:
                return None


    def print_graph(self):
        print(self)


    def get_arc_cost(self, node1, node2):
        cost = 0
        a = self.n_graph[node1]
        for (node, weight) in a:
            if node == node2:
                cost = weight
        return cost


    # Can only be used when path consists of nodes with direct link
    def path_cost(self, path):
        cost = 0
        i = 0
        while i + 1 < len(path):
            cost += self.get_arc_cost(path[i], path[i + 1])
            i += 1
        return cost


    
    def get_neighbours(self, node):
        return self.n_graph[node]


    # Euristic function, f(node) = euclidian_distance(node, end) * (penalty if path to get to node has Wall)
    # Chooses the smallest heuristic value from the goal node's list
    def heuristic(self, current, next, goal):
        min_dist = sys.maxsize
        for i in goal:
            dist = ((next[0] - i[0]) ** 2 + (next[1] - i[1]) ** 2) ** 0.5
            if not self.is_possible(current, next):
                dist *= 25
            if dist < min_dist:
                min_dist = dist
        return min_dist


    # Check if path pos -> nextp has Wall
    def is_possible(self, pos, nextp):
        aux_pos = list(pos)
        
        while(nextp != tuple(aux_pos)):
            if aux_pos[0] > nextp[0]:
                aux_pos[0] -= 1
            elif aux_pos[0] < nextp[0]:
                aux_pos[0] += 1
            if aux_pos[1] > nextp[1]:
                aux_pos[1] -= 1
            elif aux_pos[1] < nextp[1]:
                aux_pos[1] += 1
            if tuple(aux_pos) in self.obstacles:
                return False
        if tuple(aux_pos) in self.obstacles:
                return False
        return True
        

    # Get possible next moves based on state's velocity
    def get_possible_moves(self, n, vel, v):
        a = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 0), (0, 1), (1, -1), (1, 0), (1, 1)]
        res = []
        for i in a:
            nextv = tuple(np.add(i, vel))
            nextn = tuple(np.add(nextv, n))
            if Node(nextn) in self.n_nodes:
                res.append(nextn)
                v[nextn] = nextv
        return res
    
    
