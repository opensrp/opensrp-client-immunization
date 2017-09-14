[![Build Status](https://travis-ci.org/OpenSRP/opensrp-client-immunization.svg?branch=master)](https://travis-ci.org/OpenSRP/opensrp-client-immunization) [![Coverage Status](https://coveralls.io/repos/github/OpenSRP/opensrp-client-immunization/badge.svg?branch=master)](https://coveralls.io/github/OpenSRP/opensrp-client-immunization?branch=master) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/4a58cd4e1748432780ac66a9fbee0394)](https://www.codacy.com/app/OpenSRP/opensrp-client-immunization?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=OpenSRP/opensrp-client-immunization&amp;utm_campaign=Badge_Grade)

[![Dristhi](https://raw.githubusercontent.com/OpenSRP/opensrp-client/master/opensrp-app/res/drawable-mdpi/login_logo.png)](https://smartregister.atlassian.net/wiki/dashboard.action)


# Table of Contents

* [Introduction](#introduction)
* [Features](#features)
* [App Walkthrough](#app-walkthrough)
   * [Sample Android Form](#2-sample-form)
   * [Sample JSON Form](#sample-json-form)
   * [Json Form Attributes](#json-form-attributes)
* [Usage](#usage)
* [Developer Documentation](#developer-documentation)
   * [Pre-requisites](#pre-requisites)
   * [Installation Devices](#installation-devices)
   * [How to install](#how-to-install)
   * [Customisations From Android Native JSON Form](#customisations-from-android-native-json-form)

# Introduction

OpenSRP Client Immunization Module/App provides access to immunisation records which can be updated and retrieved. 

# Developer Documentation

This section will provide a brief description how to build and install the application from the repository source code.

## Pre-requisites

1. Make sure you have Java 1.7 to 1.8 installed
2. Make sure you have Android Studio installed or [download it from here](https://developer.android.com/studio/index.html)


## Installation Devices

1. Use a physical Android device to run the app
2. Use the Android Emulator that comes with the Android Studio installation (Slow & not advisable)
3. Use Genymotion Android Emulator
    * Go [here](https://www.genymotion.com/) and register for genymotion account if none. Free accounts have limitations which are not counter-productive
    * Download your OS Version of VirtualBox at [here](https://www.virtualbox.org/wiki/Downloads)
    * Install VirtualBox
    * Download Genymotion & Install it
    * Sign in to the genymotion app
    * Create a new Genymotion Virtual Device 
        * **Preferrable & Stable Choice** - API 22(Android 5.1.0), Screen size of around 800 X 1280, 1024 MB Memory --> eg. Google Nexus 7, Google Nexus 5

## How to install

1. Import the project into Android Studio by: **Import a gradle project** option
   _All the plugins required are explicitly stated, therefore it can work with any Android Studio version - Just enable it to download any packages not available offline_
1. Open Genymotion and Run the Virtual Device created previously.
1. Run the app on Android Studio and chose the Genymotion Emulator as the ` Deployment Target`
