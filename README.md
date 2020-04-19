# Piano Bench #

## Development ##

To launch the app with hot-reloading enabled, run:

```
clj -A:figwheel
```

You can also open
`http://localhost:9500/figwheel-extra-main/auto-testing` in a separate
tab, to launch an automatic test-runner of the unit tests.

## Unit Tests ##

To run the unit tests one-off, run:

```
clj -A:ci-tests
```

The `chrome` will need to be in your path.
