[![Build Status](https://travis-ci.org/OpenSRP/opensrp-client-immunization.svg?branch=master)](https://travis-ci.org/OpenSRP/opensrp-client-immunization) [![Coverage Status](https://coveralls.io/repos/github/OpenSRP/opensrp-client-immunization/badge.svg?branch=master)](https://coveralls.io/github/OpenSRP/opensrp-client-immunization?branch=master) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/4a58cd4e1748432780ac66a9fbee0394)](https://www.codacy.com/app/OpenSRP/opensrp-client-immunization?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=OpenSRP/opensrp-client-immunization&amp;utm_campaign=Badge_Grade)

[![Dristhi](https://raw.githubusercontent.com/OpenSRP/opensrp-client/master/opensrp-app/res/drawable-mdpi/login_logo.png)](https://smartregister.atlassian.net/wiki/dashboard.action)


# Table of Contents

* [Introduction](#introduction)
* [Features](#features)
* [App Walkthrough](#app-walkthrough)
* [Developer Documentation](#developer-documentation)
   * [Pre-requisites](#pre-requisites)
   * [Installation Devices](#installation-devices)
   * [How to install](#how-to-install)

# Introduction

OpenSRP Client Immunization Module/App provides access to patients' immunization records which can be either be updated or retrieved. 


# Features

1. It enables the provider to update a patient's immunization records
2. It enables the provider to view a patient's immunization records
3. It enables the provider to undo an update on a patient's immunization record within 12 hours of updating the record
4. It enables the provider to easily update multiple immunization records at the same time

# App Walkthrough

1. On openning the app, the patient's vaccine card is displayed.

![Main Page](https://user-images.githubusercontent.com/31766075/30473281-86b68958-9a08-11e7-849f-d4859777f715.png)
![Main Page 2](https://user-images.githubusercontent.com/31766075/30470553-e5c6e812-99fd-11e7-87be-18b8599918ab.png)


The vaccine card has:

 * The Patient's basic details
    * Name
    * Picture
    * ID
    * Age
    * Birthdate
   
   ![Patient's Basic Details](https://user-images.githubusercontent.com/31766075/30473412-0e1e0722-9a09-11e7-80ee-614c0dcead04.jpg)


 * Recurring immunization Services
 
    ![Recurring Services Screenshot](https://user-images.githubusercontent.com/31766075/30471709-6a578e34-9a02-11e7-8706-8d147f3bc30f.png)


 * Periodic immunization Services eg. At **Birth**, **6 weeks**, **10 weeks**

    ![Periodic Immunization Services Screenshot](https://user-images.githubusercontent.com/31766075/30471706-6a5084ae-9a02-11e7-81e3-9761e71929fa.png)


Each of the immunization services are color-coded indicating their status

###### Service Status

Color | Meaning
----- | ---------
Green | Administered/Given recently
Red | Overdue
Blue | Due soon (Due today OR Within 10 days after due-date )
Light Blue | Upcoming
White | Upcoming but not anytime soon


2. Updating service status


   2.1 Click on any of the service buttons to update the immunization service status

   ![Updating Immunization Status Screenshot](https://user-images.githubusercontent.com/31766075/30438002-c8450a24-9978-11e7-90ee-5f80384076da.png)
   ![Updating Immunization Status Screenshot 2](https://user-images.githubusercontent.com/31766075/30472411-e8f580d2-9a04-11e7-8296-3d67cf0913dc.png)
   ![Updating Immunization Status Screenshot 3](https://user-images.githubusercontent.com/31766075/30437999-c759299c-9978-11e7-9d57-80b6e11e0dcf.png)

   Depending on the service being updated, different options will be available.


   2.2 In case, you need to update several service statuses at once, click on the **Record all** button in the time-respective services box.

   The following dialog will be shown:

   ![Updating multiple services statuses at once](https://user-images.githubusercontent.com/31766075/30437998-c754ee2c-9978-11e7-9414-d1e277bcc50f.png)


3. Once a service status has been updated, you can **undo** the action within 12 hours.

An **Undo button** as shown below appears beside the service button.

![Undo button Screenshot](https://user-images.githubusercontent.com/31766075/30471707-6a5481a8-9a02-11e7-8710-7fd5a6bfd05b.png)


4. On clicking the **Undo button**, the following dialog box will be shown:

![Undo Dialog Screenshot](https://user-images.githubusercontent.com/31766075/30437997-c754c15e-9978-11e7-8cc5-7d70ac07e3ec.png)


5. To view the patient's immunization history between birth and 5 years, click on the Patient's Details box at the top of the page:

![Patient's Details box Screenshot](https://user-images.githubusercontent.com/31766075/30473412-0e1e0722-9a09-11e7-80ee-614c0dcead04.jpg)

The following page will be shown:

![Patient's Under Five Immunization history](https://user-images.githubusercontent.com/31766075/30473476-464d1e6c-9a09-11e7-8d38-925995cb1970.png)
![Patient's Under Five Immunization history](https://user-images.githubusercontent.com/31766075/30438001-c7853f32-9978-11e7-8161-8ef3a5ec1af4.png)

This page shows all the patient's scheduled immunization services from birth to 5 years of age.

For each immunization service, the following is shown:
   
   * Vaccination abbreviation eg. `OPV 0`, `BCG`, `Penta 1`
   * [`status color`](#service-status)
   * The date 
      * The service is scheduled to be provided
      * The service was provided
   * An **Edit** button in case the service was recorded within the last 12 hours

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
