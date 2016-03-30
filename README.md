# SONOS Controller

This is a small Toy-project, aiming to add some situation aware control to my collection of SONOS speakers.

## Architecture

The server does all the actual interaction with the SONOS speakers. It is written in Python using [Flask](http://flask.pocoo.org/) as the web-framework and the [SoCo](https://github.com/SoCo/SoCo)-library to interact with the speakers. 

The second Part is the Android application, which runs a service on the device that listens to a bunch of events (e.g. phone calls) and notifies the Server on the configured URL about what's happening.

## License

    Copyright 2016 Lukas Knuth

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
