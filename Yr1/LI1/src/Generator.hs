-- | Generator 
module Generator where    
import Types
import System.Random



-- | Maze example

sampleMaze :: Maze
sampleMaze = [
                [Wall, Wall, Wall, Wall, Wall, Wall, Wall, Wall],
                [Empty, Food Little, Food Little, Food Big, Food Little, Food Big, Food Little, Empty],
                [Wall, Wall, Wall, Wall, Wall, Wall, Wall, Wall]
             ]


-- | Given a seed returns a list of n integer randomly generated
--
geraAleatorios :: Int -> Int -> [Int]
geraAleatorios n seed = let gen = mkStdGen seed -- creates a random generator
                        in take n $ randomRs (0,99) gen -- takes the first n elements from an infinite series of random numbers between 0-99


-- | Given a seed returns an integer randomly generated
--
nrAleatorio :: Int -> Int
nrAleatorio seed = head $ geraAleatorios 1 seed


-- | Converssta list into a list of list of size n
--
subLista :: Int -> [a] -> [[a]]
subLista _ [] = []
subLista n l = take n l: subLista n (drop n l)


-- | Converts an integer number into a Piece,
-- 3 <=> Food Big
-- 0 <= n < 70 <=> Food Little
-- 70 < n <= 99 <=> Wall
--
convertePiece :: Int -> Piece
convertePiece n = if n==3
                 then Food Big
                 else if n>=0 && n<70
                      then Food Little
                      else Wall


-- | Converts a Corridor to a string
--
printCorridor :: Corridor -> String
printCorridor [] = "\n"
printCorridor (n:ns) = show n ++ printCorridor ns 


-- | Converts a list of integers into a Corridor
--
converteCorridor :: [Int] -> Corridor
converteCorridor [] = []
converteCorridor (n:ns) = convertePiece n : converteCorridor ns



-- | Converts a list of lists of integers into a Maze
--
converteMaze :: [[Int]] -> Maze
converteMaze [] = []
converteMaze (n:ns) = converteCorridor n : converteMaze ns 

-- | Generates a Maze using a random seed
geraMaze :: Int -> Int -> Int -> Maze
geraMaze x y s =
                 let random_nrs = geraAleatorios (x*y) s
                 in converteMaze $ subLista x random_nrs

