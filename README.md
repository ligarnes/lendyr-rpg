# Lendyr

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX
logo.

## Platforms

- `model`: The internal representation of the models
- `grpc-client`: The gRPC client to communicate with the backend
- `shared-ui`: Contains all the UI element, component, view and screen that are shared by the application
- `encounter`: Contains the screen to manage an encounter.
- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the
ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

# Naming convention

## UI

This section defines the terminology used to name the UI concepts.

### Element

The element is a small flexible building block that can be assembled in components or be used directly in layer.

### Component

The component is a composition of one or more element that represents a feature complete ui entity

### Layer

The layer is a full screen view that allows to draw anything on it

### Screen

The screen is composed of one or multiple views and represents what is currently displayed.
