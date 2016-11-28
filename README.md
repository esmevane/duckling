[![Build Status](https://travis-ci.org/esmevane/duckling.svg?branch=master)](https://travis-ci.org/esmevane/duckling)

# Duckling

A petite Java http server, with the major goals of being enjoyable to read and well tested.  So named because the original author's son loves ducks (and also pigeons); it's not a Hans Christian Andersen reference, promise.

## Installation

Duckling is run with `duckling.jar` releases, found on our [releases page](https://github.com/esmevane/duckling/releases) - you can get the latest `duckling.jar` there.  Or, if you like, you can get the latest `duckling.jar` [by clicking right here](https://github.com/esmevane/duckling/releases/download/v0.0.1/duckling.jar).

If you like your command line, here's a good one-liner (you might need to grab `wget` first):

```bash
wget https://github.com/esmevane/duckling/releases/download/v0.0.1/duckling.jar
```

## Building

If building the current master (or a given release) directly is more your speed, you'll need to have Gradle installed.  Once you do, here's a snippet to get you going:

```bash
git clone git@github.com:esmevane/duckling.git
cd duckling
./gradlew test && ./gradlew jar
```

That snippet will grab this repository, install it in your `$(pwd)/duckling`, hop into that directory, and then use the `./gradlew` build scripts to get a jar together.  It will deposit the jar in the root of the duckling directory, under `duckling.jar`.

Enjoy!

## Usage

Once you've either downloaded or created your `duckling.jar`, you can run it with Java:

```bash
java -jar duckling.jar
```

### Configuring the Duckling Jar

Duckling accepts configuration of its port, root directory.

| **Option** | **What it does** | **Default** |
| --- | --- | --- |
| `-p` | Assign a custom port | `5000` |
| `-d` | Assign a public directory | `.` (Current directory) |

For example

```bash
java -jar duckling.jar -p $(YOUR_FAVORITE_PORT) -d $(YOUR_FAVORITE_DIR)
```

For example, if you wanted to kick it off in its current directory, on port 8000:

```bash
java -jar duckling.jar -p 8000 -d .
```

## Installation Mode

Duckling has been built with a preconfigured set of routes, which are there purely for testing purposes.  In the future there may be a configuration layer of some sort, but for now Duckling is considered to be in default mode at all times.

### The Default Mode Routes

| **Method** | **Path** | **Response Code** | **What is it?** |
| --- | --- | --- | --- |
| GET | / | 200 OK | The public directory |
| GET | /coffee | 418 TEAPOT | A quirky teapot code |
| GET | /tea | 200 OK | You probably meant to go here, not to /coffee |
