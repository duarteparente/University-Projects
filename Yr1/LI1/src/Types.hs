module Types where

import Data.List

data Manager = Manager 
    {   
        state   :: State
    ,    pid    :: Int
    ,    step   :: Int
    ,    before :: Integer
    ,    delta  :: Integer
    ,    delay  :: Integer
    } 


data State = State 
    {
        maze :: Maze
    ,   playersState :: [Player]
    ,   level :: Int
    }

type Maze = [Corridor]
type Corridor = [Piece]
data Piece =  Food FoodType | PacPlayer Player| Empty | Wall deriving (Eq)
data Player =  Pacman PacState | Ghost GhoState deriving (Eq)

data Orientation = L | R | U | D | Null deriving (Eq,Show)
data PacState= PacState 
    {   
        pacState :: PlayerState
    ,   timeMega :: Double
    ,   openClosed :: Mouth
    ,   pacmanMode :: PacMode
    
    } deriving Eq

data GhoState= GhoState 
    {
        ghostState :: PlayerState
    ,   ghostMode :: GhostMode
    } deriving Eq

type Coords = (Int,Int)
type PlayerState = (Int, Coords, Double , Orientation, Int, Int)
--                 (ID,  (x,y), velocity, orientation, points, lives) 
data Mouth = Open | Closed deriving (Eq,Show)
data PacMode = Dying | Mega | Normal deriving (Eq,Show)
data GhostMode = Dead  | Alive deriving (Eq,Show)
data FoodType = Big | Little deriving (Eq)
data Color = Blue | Green | Purple | Red | Yellow | None deriving Eq 

data Play = Move Int Orientation deriving (Eq,Show)

type Instructions = [Instruction]

data Instruction = Instruct [(Int, Piece)]
                 | Repeat Int deriving (Show, Eq)



instance Show State where
  show (State m ps p) = printMaze mz ++ "Level: " ++ show p ++ "\nPlayers: \n" ++ (foldr (++) "\n" (map (\y-> printPlayerStats y) ps))
                          where mz = placePlayersOnMap ps m

instance Show PacState where
   show ( PacState s o m Dying  ) =  "X"
   show ( PacState (a,b,c,R,i,l) _ Open m  ) =  "{"
   show ( PacState (a,b,c,R,i,l) _ Closed m  ) =  "<"
   show ( PacState (a,b,c,L,i,l) _ Open m  ) =  "}"
   show ( PacState (a,b,c,L,i,l) _ Closed m  ) =  ">"
   show ( PacState (a,b,c,U,i,l) _ Open m  ) =  "V"
   show ( PacState (a,b,c,U,i,l) _ Closed m  ) =  "v"
   show ( PacState (a,b,c,D,i,l) _ Open m  ) =  "^"
   show ( PacState (a,b,c,D,i,l) _ Closed m  ) =  "|"
   show ( PacState (a,b,c,Null,i,l) _ Closed m  ) =  "<"
   show ( PacState (a,b,c,Null,i,l) _ Open m  ) =  "{"

instance Show Player where
   show (Pacman x ) =  show x
   show ( Ghost x ) =   show x

instance Show GhoState where
   show (GhoState x Dead ) =  "?"
   show (GhoState x Alive ) =  "M"

instance Show FoodType where
   show ( Big ) =  "o"
   show ( Little ) =  "."

instance Show Piece where
   show (  Wall ) = coloredString "#" None
   show (  Empty ) = coloredString " " None
   show (  Food z ) = coloredString (show z )   Green
   show ( PacPlayer ( Pacman ( PacState (i, c, x, y,z,l) o m Normal ) ) ) = coloredString (show ( PacState (i, c, x, y,z,l) o m Normal)  ) Yellow
   show ( PacPlayer ( Pacman ( PacState (i, c, x, y,z,l) o m Mega   ) ) ) = coloredString (show ( PacState (i, c, x, y,z,l) o m Mega)  ) Blue
   show ( PacPlayer ( Pacman ( PacState (i, c, x, y,z,l) o m Dying   ) ) ) = coloredString (show ( PacState (i, c, x, y,z,l) o m Dying)  ) Red
   show ( PacPlayer (Ghost z) ) = coloredString (show z)  Purple


coloredString :: String -> Color -> String
coloredString x y
    | y == Blue = x
    | y == Red = x
    | y == Green = x 
    | y == Purple = x
    | y == Yellow = x 
    | otherwise = x 

-- | Escolhe a jogada a aplicar a determinado Player
choosePlay :: Int -> [Play] -> Play
choosePlay n [] = Move n R
choosePlay n ((Move i o):xs) | n==i = (Move i o)
                             | otherwise = choosePlay n xs

-- | Fecha ou abre a boca do Player
mouth :: Player -> Player 
mouth (Pacman (PacState (x,(a,b),z,t,h,li) q Open d )) = (Pacman (PacState (x,(a,b),z,t,h,li) q Closed d )) 
mouth (Pacman (PacState (x,(a,b),z,t,h,li) q Closed d )) = (Pacman (PacState (x,(a,b),z,t,h,li) q Open d ))
mouth x = x 

-- | Devolve as coordenadas para onde o Player se vai movimentar
coordenadas :: Player -> Orientation -> Coords
coordenadas p o = let (x,y) = getPlayerCoords p 
                  in case o of R -> (x,y+1)
                               L -> (x,y-1)
                               U -> (x-1,y)
                               otherwise -> (x+1,y)


-- | Insere o Player numa lista de Player ordenada por ordem crescente
insereElem :: Player -> [Player] -> [Player]
insereElem p [] = [p]
insereElem p (n:ns) = p:n:ns

-- | Remove um PLayer de uma lista de Player
removePLayer :: Int -> [Player] -> [Player]
removePLayer _ [] = []
removePLayer n (x:xs) | n == getPlayerID x = xs
                      | otherwise = x:removePLayer n xs

-- | Devolve a lista de jogadores de um State
getPlayersList :: State -> [Player]
getPlayersList (State _ j _) = j

-- | Dada uma lista de Player devolve uma lista apenas com fantasmas
onlyfantasmas :: [Player] -> [Player]
onlyfantasmas [] = []
onlyfantasmas (x:xs) = if isGhost x then x : onlyfantasmas xs else onlyfantasmas xs

-- | Testa se o fantasma está em modo Dead
isGhostDead :: Player -> Bool
isGhostDead (Ghost (GhoState (x,(a,b),z,t,h,li) Dead )) = True
isGhostDead _ = False

-- | Testa se o Pacman está em modo Mega
isPacmanMega :: Player -> Bool
isPacmanMega (Pacman (PacState (x,y,z,t,h,l) q c Mega )) = True
isPacmanMega _ = False

-- | Testa se o Pacman perdeu as vidas todas
gameFinished :: Player -> Bool
gameFinished (Pacman (PacState (x,y,z,t,h,l) q c Dying )) = True
gameFinished _ = False

-- | Devolve o estado de um Ghost
getGhostMode :: Player -> GhostMode
getGhostMode (Ghost (GhoState a q )) = q                       

-- | Atualiza o estado de um Manager                      
updatePLayersState :: Manager -> Player -> State
updatePLayersState m@(Manager (State l ls le) pid step bf delt del) jg = State l (insereElem jg (removePLayer pid ls)) le

placePlayersOnMap :: [Player] -> Maze -> Maze
placePlayersOnMap [] x = x
placePlayersOnMap (x:xs) m = placePlayersOnMap xs ( replaceElemInMaze (getPlayerCoords x) (PacPlayer x) m )

-- | Testa se um Player é ou não um fantasma
isGhost :: Player -> Bool
isGhost (Pacman (PacState (x,(a,b),z,t,h,li) q c d )) = False
isGhost (Ghost (GhoState (x,(a,b),z,t,h,li) q )) = True  

-- | Devolve uma lista de jogadores apenas do tipo Pacman
onlyPacman :: [Player] -> [Player]
onlyPacman [] = []
onlyPacman (x:xs) = if isGhost x then onlyPacman xs else x:onlyPacman xs


-- | Devolve a peça de determinadas coordenadas de um Maze sem jogadores
getpeca :: Coords -> Maze -> Piece
getpeca (x,y) (n:ns) = if x==0 then (!!) n y else getpeca (x-1,y) ns
 
-- | Permite a vizualização de um Maze
imprimeMaze :: Maze -> IO ()
imprimeMaze l = do putStrLn ( printMaze ( l ))  

printMaze :: Maze -> String
printMaze []  =  ""
printMaze (x:xs) = foldr (++) "" ( map (\y -> show y) x )  ++ "\n" ++ printMaze ( xs )

printPlayerStats :: Player -> String
printPlayerStats p = let (a,b,c,d,e,l) = getPlayerState p
                     in "ID:" ++ show a ++  " Points:" ++ show e ++ " Lives:" ++ show l ++"\n"

getPlayerID :: Player -> Int
getPlayerID (Pacman (PacState (x,y,z,t,h,l) q c d )) = x
getPlayerID  (Ghost (GhoState (x,y,z,t,h,l) q )) = x
 
getPlayerPoints :: Player -> Int
getPlayerPoints (Pacman (PacState (x,y,z,t,h,l) q c d )) = h
getPlayerPoints (Ghost (GhoState (x,y,z,t,h,l) q )) = h

setPlayerCoords :: Player -> Coords -> Player
setPlayerCoords (Pacman (PacState (x,y,z,t,h,l) q c d )) (a,b) = Pacman (PacState (x,(a,b),z,t,h,l) q c d )
setPlayerCoords (Ghost (GhoState (x,y,z,t,h,l) q )) (a,b) = Ghost (GhoState (x,(a,b),z,t,h,l) q )


getPieceOrientation :: Piece -> Orientation
getPieceOrientation (PacPlayer p) =  getPlayerOrientation p
getPieceOrientation _ = Null

getPacmanMode :: Player -> PacMode
getPacmanMode (Pacman (PacState a b c d)) = d
  
getPlayerState :: Player -> PlayerState
getPlayerState (Pacman (PacState a b c d )) = a
getPlayerState (Ghost (GhoState a b )) = a

getPlayerOrientation :: Player -> Orientation
getPlayerOrientation (Pacman (PacState (x,y,z,t,h,l) q c d )) = t
getPlayerOrientation  (Ghost (GhoState (x,y,z,t,h,l) q )) = t

replaceElemInMaze :: Coords -> Piece -> Maze -> Maze
replaceElemInMaze (a,b) _ [] = []
replaceElemInMaze (a,b) p (x:xs) 
  | a == 0 = replaceNElem b p x : xs 
  | otherwise = x : replaceElemInMaze (a-1,b) p xs


replaceNElem :: Int -> a -> [a] -> [a]
replaceNElem i _ [] = [] 
replaceNElem i el (x:xs)
  |  i == 0 = el : xs 
  | otherwise =  x : replaceNElem (i-1) el xs

getPlayerCoords :: Player -> Coords
getPlayerCoords (Pacman (PacState (x,y,z,t,h,l) b c d )) = y
getPlayerCoords (Ghost (GhoState (x,y,z,t,h,l) b )) = y

