
# README

## Project Overview

This project is a multiplayer client-server game where two artificial intelligence (AI) clients compete to complete a series of tasks on a randomly generated game map. The primary goal for each AI is to find its hidden treasure and the opponent’s castle. The AI that completes the task first wins the game. The project is structured using a classic client-server architecture, where the server manages game coordination, communication, and validation of rules, while the clients control their respective AI players.

---

## Table of Contents

1. [Game Description](#game-description)
2. [Game Architecture](#game-architecture)
3. [Technology Stack](#technology-stack)
4. [Running the Game](#running-the-game)

---

## Game Description

The game involves two AI-driven clients, each tasked with exploring the map, finding a treasure hidden on their half of the map, and ultimately locating and taking over the opponent's castle. The map consists of two halves, which are created randomly by each client and then combined by the server. Key features include:
- **Game Map**: A grid-based map consisting of three terrain types: water, grass, and mountains.
- **AI Objectives**: Each AI needs to find its treasure (on its half of the map) and the opponent’s castle.
- **Turn-based System**: The game runs in turns, where each AI performs a single action (e.g., move or map exchange) per turn.
- **Limited Time**: Each game is capped at 320 turns, and each AI has a 5-second time limit per action.
- **CLI Visualization**: The game is visualized through a command-line interface (CLI) which shows the map, players’ positions, treasures, castles, and more.

---

## Game Architecture

The game follows a client-server architecture:
- **Server**: Manages the overall game state, validates moves, handles communication between clients, and ensures adherence to game rules.
- **Client**: Controls the AI’s actions, interacts with the server to send and receive game data, and provides visual feedback via the CLI.

The game map is dynamically generated, and each AI can only see its half of the map initially. Clients communicate with the server to update the map and game states.

### Main Components
- **Client**: Implements AI logic, handles movement, and processes game state updates.
- **Server**: Acts as the referee, combining map halves, managing game turns, and validating rule adherence.

---

## Technology Stack

The following technology stack is used in this project:

- **Language**: Java 17
- **Development Environment**: Eclipse 2023-09 for Java Developers
- **UI Framework**: Command Line Interface (CLI); additional GUI (e.g., JavaFX or Swing) is optional
- **Networking**: Spring Boot 2.7.8, Spring Webflux 2.7.8, JAXB, iStack
- **Build Tools**: Gradle (recommended for managing dependencies)
- **Database**: SQLite (via Hibernate and Spring Data JPA)
- **Testing**: JUnit 5.10.0, Mockito 5.6.0
- **Version Control**: Git (using the University’s GitLab server)

---

## Running the Game
### Client
Generate a new game ID via https://swe1.wst.univie.ac.at/games
Run the game with start parameters: java -jar <DateiNameClient.jar> <Modus> <BasisUrlServer> <SpielID>
  Example: java -jar client.jar TR http://swe1.wst.univie.ac.at:18235 6aDj2

**Alternative:** Use Test-Server https://swe1.wst.univie.ac.at/ to evaluate the Client AI by uploading Executables/ExampleClient.jar file.  

### Server
First, start your server and then run the test client using:
java -jar Server_Evaluation_Client.jar
