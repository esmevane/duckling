# Duckling

A petite Java http server, with the major goals of being enjoyable to read and well tested.  So named because the original author's son loves ducks (and also pigeons); it's not a Hans Christian Andersen reference, promise.

## Installation

Duckling is released with a `duckling.jar`, which is runnable through Java:

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

## Known bugs

* Deep traversal of directories is currently not supported.  This is getting punted on as of release 0.0.1 in the interests of getting a more thoroughly tested `FolderContents` and usable responder in play, vs. upgrading the current one further (which is moot, because it's serving its purpose as-is).
