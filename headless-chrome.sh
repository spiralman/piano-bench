#!/usr/bin/env bash

if [ -d '/Applications/Google Chrome.app' ]
then
    CHROME='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'
else
    CHROME='/opt/google/chrome/chrome'
fi

"$CHROME" --headless --disable-gpu --repl "$@"
