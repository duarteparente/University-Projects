import sys
from AStar import AStar
from DFS import DFS
from Graph import Graph
from BFS import BFS
from Greedy import Greedy
from IDS import IDS
from UCS import UCS
from UI import Menu
from multiplayer import multiplayer

gameMap = []
track = []
obstacle = []
start = []
finish = []

weigth = {
    'X': 25,
    '-': 1,
    'P': 1,
    'F': 1,
}


def readMap(filepath):
    del gameMap[:]
    del track[:]
    del obstacle[:]
    del start[:]
    del finish[:]
    file = open(filepath).read().split('\n')
    for line in file:
        gameMap.append(list(line))


def label():
    posType = {
        'X': obstacle,
        '-': track,
        'P': start,
        'F': finish,
    }
    for i in range(len(gameMap)):
        for j in range(len(gameMap[i])):
            posType.get(gameMap[i][j]).append((i + 1, j + 1))
            if gameMap[i][j] == 'F' or gameMap[i][j] == 'P':
                track.append((i + 1, j + 1))


def ops_frente(i, j, g):
    g.add_node((i, j), (i, j + 1), weigth.get(gameMap[i - 1][j]))
    if weigth.get(gameMap[i - 1][j]) != 25:
        g.add_node((i, j + 1), (i, j), weigth.get(gameMap[i - 1][j - 1]))


def ops_tras(i, j, g):
    g.add_node((i, j), (i, j - 1), weigth.get(gameMap[i - 1][j - 2]))
    if weigth.get(gameMap[i - 1][j - 2]) != 25:
        g.add_node((i, j - 1), (i, j), weigth.get(gameMap[i - 1][j - 1]))


def ops_baixo(i, j, g):
    g.add_node((i, j), (i + 1, j), weigth.get(gameMap[i][j - 1]))
    if weigth.get(gameMap[i][j - 1]) != 25:
        g.add_node((i + 1, j), (i, j), weigth.get(gameMap[i - 1][j - 1]))


def ops_cima(i, j, g):
    g.add_node((i, j), (i - 1, j), weigth.get(gameMap[i - 2][j - 1]))
    if weigth.get(gameMap[i - 2][j - 1]) != 25:
        g.add_node((i - 1, j), (i, j), weigth.get(gameMap[i - 1][j - 1]))


def ops_diagonal_frente_baixo(i, j, g):
    g.add_node((i, j), (i + 1, j + 1), weigth.get(gameMap[i][j]))
    if weigth.get(gameMap[i][j]) != 25:
        g.add_node((i + 1, j + 1), (i, j), weigth.get(gameMap[i - 1][j - 1]))


def ops_diagonal_tras_baixo(i, j, g):
    g.add_node((i, j), (i + 1, j - 1), weigth.get(gameMap[i][j - 2]))
    if weigth.get(gameMap[i][j - 2]) != 25:
        g.add_node((i + 1, j - 1), (i, j), weigth.get(gameMap[i - 1][j - 1]))


def ops_diagonal_frente_cima(i, j, g):
    g.add_node((i, j), (i - 1, j + 1), weigth.get(gameMap[i - 2][j]))
    if weigth.get(gameMap[i - 2][j]) != 25:
        g.add_node((i - 1, j + 1), (i, j), weigth.get(gameMap[i - 1][j - 1]))


def ops_diagonal_tras_cima(i, j, g):
    g.add_node((i, j), (i - 1, j - 1), weigth.get(gameMap[i - 2][j - 2]))
    if weigth.get(gameMap[i - 2][j - 2]) != 25:
        g.add_node((i - 1, j - 1), (i, j), weigth.get(gameMap[i - 1][j - 1]))


def matToGraph(g):
    for i in range(len(gameMap)):
        i += 1
        for j in range(len(gameMap[i - 1])):
            if(gameMap[i-1][j]) == 'X':
                continue
            j += 1
            if i == len(gameMap):
                ops_cima(i, j, g)
                if j == len(gameMap[i - 1]):
                    ops_diagonal_tras_cima(i, j, g)
                    ops_tras(i, j, g)
                elif j == 1:
                    ops_diagonal_frente_cima(i, j, g)
                    ops_frente(i, j, g)
                else:
                    ops_diagonal_frente_cima(i, j, g)
                    ops_frente(i, j, g)
                    ops_diagonal_tras_cima(i, j, g)
                    ops_tras(i, j, g)
            elif i == 1:
                ops_baixo(i, j, g)
                if j == len(gameMap[i - 1]):
                    ops_tras(i, j, g)
                    ops_diagonal_tras_baixo(i, j, g)
                elif j == 1:
                    ops_diagonal_frente_baixo(i, j, g)
                    ops_frente(i, j, g)
                else:
                    ops_tras(i, j, g)
                    ops_frente(i, j, g)
                    ops_diagonal_tras_baixo(i, j, g)
                    ops_diagonal_frente_baixo(i, j, g)
            else:
                ops_cima(i, j, g)
                ops_baixo(i, j, g)
                if j == 1:
                    ops_frente(i, j, g)
                    ops_diagonal_frente_baixo(i, j, g)
                    ops_diagonal_frente_cima(i, j, g)
                elif j == len(gameMap[i - 1]):
                    ops_tras(i, j, g)
                    ops_diagonal_tras_baixo(i, j, g)
                    ops_diagonal_tras_cima(i, j, g)
                else:
                    ops_frente(i, j, g)
                    ops_diagonal_frente_baixo(i, j, g)
                    ops_diagonal_frente_cima(i, j, g)
                    ops_tras(i, j, g)
                    ops_diagonal_tras_baixo(i, j, g)
                    ops_diagonal_tras_cima(i, j, g)



def option_handler(graph, menu, option):
    if option == '3':
        menu.clear_screen()
        graph.print_graph()
    elif option == '6':
        menu.clear_screen()
        ids = IDS(graph,start[0],finish)
        result = ids.search()
        if result[0] != None:
            menu.IDS_output(start[0], finish, result[0], result[1], result[2], result[3], result[4], gameMap)
    elif option == '10':
        mult = multiplayer(graph,start[0],finish)
        options = mult.select_algorithm()
        menu.clear_screen()
        result, player = mult.play(options)
        menu.player_won(player)
        menu.output(result[3], start[0], finish, result[0], result[1], result[2], gameMap)

    elif option == '11':
        print("\n  Leaving...")
        sys.exit(0)
    else:
        menu.clear_screen()
        result = None
        if option == '4':
            bfs = BFS(graph,start[0],finish)
            result = bfs.search()
        elif option == '5':
            dfs = DFS(graph, start[0], finish)
            result = dfs.search()
        elif option == '7':
            ucs = UCS(graph, start[0], finish)
            result = ucs.search()
        elif option == '8':
            greedy = Greedy(graph, start[0], finish)
            result = greedy.search()
        elif option == '9':
            astart = AStar(graph, start[0], finish)
            result = astart.search()
        if result[0] != None:
                menu.output(option, start[0], finish, result[0], result[1], result[2], gameMap)
    



def build_graph(filepath):
    readMap(filepath)
    label()
    g = Graph(True, obstacle)
    matToGraph(g)
    return g


def main():
    filepath = "tracks/track1.txt"
    graph = build_graph(filepath)
    option = '1'
    menu = Menu(filepath)
    while True:
        menu.show_menu()
        option = menu.get_option()
        while (1 > int(option) or int(option) > 12):
            print(" Invalid option! ")
            option = menu.get_option()
        if option == '1':
            filepath = menu.maps_catalogue()
            menu.set_filepath(filepath)
            graph = build_graph(filepath)
        elif option == '2':
            filepath = menu.get_newFilepath()
            graph = build_graph(filepath) 
        else:
            
            option_handler(graph,menu,option)
            menu.show_sndMenu()
            option = menu.get_option()
            while option != 'S' and option != 'M':
                print(" Invalid option! ")
                option = menu.get_option()
            if option == 'S':
                print("\n  Leaving...")
                return 0


if __name__ == "__main__":
    main()
