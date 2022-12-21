# Factorio Package Manager

A Factorio package manger used to greatly aid the creation of factorio mods.

## Usage

Command | Description                                                                            | Example        |
--- |----------------------------------------------------------------------------------------|----------------|
`fpm init` | Initializes a new FPM project.                                                         | `fpm init`     |
`fpm add` | Adds a dependency to a project.                                                        | `fpm add flib` |
`fpm login` | Logs into the factorio api servers.                                                    | `fpm login`    |
`fpm start` | Starts a game instance of factorio with your working directory's mods and dependencies. | `fpm start`    |
`fpm package` | Packages your local game mods into `output.zip`                                        | `fpm package`  |
