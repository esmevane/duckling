[![Build Status][build-status-img]][build-status-link]

# Duckling

A petite Java http server, with the major goals of being enjoyable to read and well tested.  So named because the original author's son loves ducks (and also pigeons); it's not a Hans Christian Andersen reference, promise.

## Installation

Duckling is run with `duckling.jar` releases, found on our [releases page][releases] - you can get the latest `duckling.jar` there.  Or, if you like, you can get the latest `duckling.jar` [by clicking right here][latest-jar].

If you like your command line, and you don't want the whole quacking project, here's a good one-liner (you might need to grab `wget` first):

```bash
wget https://github.com/esmevane/duckling/releases/download/v0.0.3/duckling.jar
```

## Building

If building the current master (or a given release) directly is more your speed, you'll need to have Gradle installed.  Once you do, here's a snippet to get you going:

```bash
git clone git@github.com:esmevane/duckling.git
cd duckling
make
```

That snippet will grab this repository, install it in your `$(pwd)/duckling`, hop into that directory, and then use either `which gradle` or the `./gradlew` build scripts to get a build together.

The final result of the `make` process is a jar in the root of the duckling directory, named `duckling.jar`.

### Do you need to clean and restart?

If so, just run `make` again.  It will clean up the gradle build artifacts, rerun the tests and then rebuild.

## Usage

Once you've run `make` once, you've created your `duckling.jar`.  Now, you can run it with Java:

```bash
java -jar duckling.jar
```

Alternately, if you want to keep using `make`, that's fine, too.  Maybe even more than fine, it might be _better_.  That's because running `make` will perform a full clean, test, and rebuild before it does anything.

The command is easy:

```bash
make start
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
| GET, POST, PUT, DELETE | /form | 200 OK | Just a happy little route, look at this route |
| GET | /redirect | 302 FOUND | Sends you to the root path |
| GET | /parameters | 200 OK | Echoes any query parameters you give it |
| GET | /logs | 200 OK | Check out those logs.  Wait, do you have the password? |
| GET | /cookie | 200 OK | Give us a `type` query param with your favorite flavor |
| GET | /eat_cookie | 200 OK | You did tell us know what flavor you like, right? |
| GET | /method_options2 | 200 OK | Just a nice route |
| GET, PUT, POST | /method_options | 200 OK | This one, too! |

You can send any of these routes `HEAD` or `OPTIONS` requests as well!

## License

Licensed 2017, MIT, Joseph McCormick.  [More details here](LICENSE.md)

## Contributing

Submit any issues [here][issues] - please take note of the [code of conduct][CODE_OF_CONDUCT.md] before submitting any issues or pull requests.

To create a pull request:

* Fork the repository
* Create a local feature branch of your changes
* Please provide tests
* Submit your local feature branch vs. this repository as a pull request


[build-status-img]: https://travis-ci.org/esmevane/duckling.svg?branch=master
[build-status-link]: https://travis-ci.org/esmevane/duckling
[releases]: https://github.com/esmevane/duckling/releases
[issues]: https://github.com/esmevane/storyteller-sheet/issues
[latest-jar]: https://github.com/esmevane/duckling/releases/download/v0.0.3/duckling.jar
