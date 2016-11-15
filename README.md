# Duckling

A petite Java http server, with the major goals of being enjoyable to read and well tested.  So named because the original author's son loves ducks (and also pigeons); it's not a Hans Christian Andersen reference, promise.

## Installation

Duckling is run with `duckling.jar` releases, found on our [releases page](https://github.com/esmevane/duckling/releases) - you can get the latest `duckling.jar` there.  Or, if you like, you can get the latest `duckling.jar` [by clicking right here](https://github.com/esmevane/duckling/releases/download/v0.0.1/duckling.jar).

If you like your command line, here's a good one-liner (you might need to grab `wget` first):

```bash
wget https://github.com/esmevane/duckling/releases/download/v0.0.1/duckling.jar
```

## Building

If building the current master (or a given release) directly is more your speed, you'll need to have `gradle` installed.  Once you do, here's a snippet to get you going:

```bash
git clone git@github.com:esmevane/duckling.git
cd duckling
gradle jar
```

That snippet will grab this repository, install it in your `${pwd}/duckling`, hop into that directory, and then use the `gradle` build scripts to get a jar together.  It will deposit the jar in the root of the duckling directory, under `duckling.jar`.

Enjoy!

## Usage

Once you have your `duckling.jar`, you can run it with Java:

```bash
java ./duckling.jar
```

You can also configure the port (with `-p`) and root directory (with
`-d`):

```bash
java duckling.jar -p $(YOUR_FAVORITE_PORT) -d $(YOUR_FAVORITE_DIR)
```

## A small overview

Right now Duckling doesn't support routing at all.  It will display some HTML as expected, provide a small navigable directory listing, and respond with a 404 to missing content.

