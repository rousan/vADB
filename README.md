# vADB

An utility app that helps to debug `android` application on real devices over `Wi-Fi`.

## Installation

It consists two module,

   * A `Java` application(a `.jar` file) for PC,
   
   * A `.apk` file for target device on which the app will be tested.
   
So just download all project files and install them.

## Usage

After installing `.apk` file on target device, open it and also run `.jar` file on PC, then
connect them with a common Wi-Fi network. Enter target device's `IP` and `Port` on PC, and then choose
target `.apk` file that will be tested and finally click on `Run` button. A app installer 
`Activity` will be prompted in target device, just tap on `Install` button then after installing tap on `Open` button.

Voila! your target app runs on real device over Wi-Fi.

There is a built-in tool named `adb`(a command line application, part of android `SDK`) that helps to debug app,
execute `shell` command directly from PC to phone etc. But sometimes it makes problems for some devices. So use this `vADB` app, only when built-in `abd` does not work.

## Contributors

* [Ariyan Khan](https://github.com/ariyankhan)

## License

MIT License

Copyright (c) 2017 Ariyan Khan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

