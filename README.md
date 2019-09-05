# Retro Game: Snake
This project is a contribution for the first round of [devathlon.eu](https://devathlon.eu).
The topic is retro game - with no more restrictions.

We decided to create a basic snake game with additional features and choices.

## Installation
You can either download the latest release or build it yourself.

### Download
You can download the latest version [here](https://www.duden.de/rechtschreibung/To_do).

### Build (with Maven)
1. Clone the repository `git clone https://github.com/devathlon-hnl/retro-game.git`.
2. Navigate into `retro-game`.
3. Run `mvn clean build`.
4. Navigate into `target`.

Just run the jar `snake.jar` by double-click or `java -jar snake-jar`.

## Game information
### Features
- retro look
- simple to extend by using [Overlay](https://www.duden.de/rechtschreibung/To_do), [EngineUpdate](https://www.duden.de/rechtschreibung/To_do) and core module
- custom map system
- scary death screen

### Control
| Key           | Description              |
|---------------|--------------------------|
| W/Arrow Up    | Move up                  |
| A/Arrow left  | Move left                |
| S/Arrow down  | Move down                |
| D/Arrow right | Move right               |
| Space         | Start and pause the game |
| ESC           | Open the settings menu   |

### Goal
The goal is to survive as long as you can!

### Special items
You can collect special items by eating them.
Every effect takes about 10 seconds before expiring.

| Color      | Effect                                    |
|------------|-------------------------------------------|
| Bisque     | Removes the half of your body             |
| Pink       | Double coins for few seconds              |
| Red        | Invincibility against border and yourself |
| Blue       | Slowness                                  |
| Aquamarine | Speed                                     |

## Devathlon
This project is a contribution for a devathlon by [Devathlon.eu](https://devathlon.eu).
It's split up in 3 rounds.
This one is the first.

The task is to create a retro game - with no more restrictions.

## Screenshots
Coming soon.

## Development
As we are a team of two persons, we split up the project into 2 main modules:
`engine` and `game`.

Engine is all about the graphical rendering and interaction with user input.
`GameEngine` is the interface for the game module to use.
It provides starting and pausing the engine, but also to interact with user input und their decisions (e.g. map selection).

Game uses the engine to implement the logical code base for the game.
It holds information like entities and map management.

There is also a third module, `core`, that allows to use common classes that are used by both modules.
The `game` can sent engine updates, that are processed by the engine.
It follows the Observer pattern with specified update parameters.