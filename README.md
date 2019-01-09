# Ex4 - Final OOP project

In this project we've created a simulation of a **Pacman Game**. 
The game can be played on 9 different maps with different scenarios and different methods such as: Running a game via clicking a point
for every step, Running a game via clicking and creating a movement in the direction of the desired location, Running a simulation of
the game automatically with an Algorithm. The game has different objects like the Player, Fruits, other Pacmans, Ghosts and blocks.

The game has a set timer for the game length, the player can gather points by eating fruits and other pacmans, the game ends either when 
there are no more fruits or when the timer is over. The player can also lose points if there is a collision with a block, or the game
borders, or the ghosts. The points gathered by the player are shown on top of the game board.

In the project we've implemented the use of the *Observer* design pattern, and the *MVC* design pattern.

## Project Hierarchy

 The Hierarchy of this project is as follows:
  1. One of the 9 maps is loaded to the board.
  2. The user decides whether to add manually a point on the board for the player starting point\run an Algorithm automatically without 
     adding a point manually.
  3. If the option to add a point manually was chosen, the user has 3 different options:
     - **Step By Step:** clicking on the board and moving the player in the direction of the click. For every movement a click is needed.
     - **Run Game:** clicking on the board and moving the player in the desired direction of the click. the player will move in the direction
       of the click until another click.
     - **Run Algorithm:** The player will move automatically to the closest fruit, while considering the blocks and the fruits that are left.
  4. If the user didn't add a point an automatic mode of the Algorithm mode can be played, the player point will be set close to the     first
     fruit in the game.
  5. After the game is done, the player can watch the statistics of the scores from all the games that were played.
  
 ## Short explanation about the packages
 Algorithms - This package is in charge of the Automatic mode played.
 
 Controller - This package is in charge of the communication between the UI and the logic's of the project.
 
 Converters - This package is in charge of the conversion between GPS points and Pixels points.
 
 Elements - This package is in charge of the objects needed for the game. Such objects are the Pacmans, Fruits, Blocks and Ghosts.
 
 Graph - This package is in charge of creating the graph and calculations of the graph. This package is needed for the Algorithm
         calculations.
 
 GUI - This package is in charge of the UI of the game.
 
 Tests - This package is in charge of the tests needed for the calculations.
 
 Utils - This package is in charge of the classes needed in order to perform some of the methods.
 
 ## Short explanation about the Algorithm
 Calculations - This class is the main part for the Auto run mode, The class calculates a path from the player to each fruit while
                considering the blocks in the way. Each path is calculated with the use of the [dijkstra Algorithm]       (https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)
                for graphs and with the [BFS algorithm](https://en.wikipedia.org/wiki/Breadth-first_search). After all the paths were
                calculated the Algorithm finds the shortest path and returns it as the path for the player movement.
 
 ### Other Projects
 For different version of this project you can go into:
     [Ex3](https://github.com/itaytu/Ex3). [Ex2](https://github.com/itaytu/Ex2).
 
