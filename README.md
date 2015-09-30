# AppEdit
## Overview

AppEdit is a web application that allows users to edit a configurable set of one or more custom attribute fields on an application.

## Where can I get the latest release?

You can download the latest source from GitHub: https://github.com/blackducksoftware/appedit.

You can pull the binary from BinTray via Maven: https://bintray.com/bds/tools/appedit/view

## Documentation

Please see wiki for more information: https://github.com/blackducksoftware/appedit/wiki

## License

GNU General Public License v2.0 only.

## Setup

1. In Code Center: If they do not already exist: Create the custom attributes that end users will edit with AppEdit.
1. In Code Center: If it does not already exist: Create AppEditURL app custom attribute (type=URL).
1. In the ccimporter config file:
    ``numprefixed.app.attribute.appediturl=<the name of the AppEditURL attribute>
    numprefixed.app.value.appediturl=http://<host:port>/AppEdit/editappdetails
    numprefixed.update.appediturl.on.old.apps=true``
1. Run ccimporter to populate AddEdit URL field (or edit some by hand)
1. Install the AppEdit application:
  1. If AppEdit was previously installed: Remove the AppEdit directory (and all of its contents) and the AppEdit.war file from the CodeCenter/tomcat/webapps directory.
  1. Copy the new AppEdit .war file into CodeCenter/tomcat/webapps, naming it AppEdit.war
  1. Restart tomcat to expand the AppEdit.war file.
1. Edit the appedit.properties. AppEdit reads the appedit.properties file from the tomcat process owner's (bds_codecenter's) home directory.
  1. If you are installing AppEdit for the first time, create this file by copying it from
    		CodeCenter/tomcat/webapps/AppEdit/WEB-INF/classes/appedit.properties. If this is not the
    		first installation, check CodeCenter/tomcat/webapps/AppEdit/WEB-INF/classes/appedit.properties
    		for any new properties. If there are any, merge them into the appedit.properties file
    		in the tomcat process owner's home directory. 
  1. appedit.properties should be configured as follows:
    1. Set Code Center server, user, password
    1. For each custom attribute to be included in the AppEdit UI: specify the AppEdit UI label (attr.N.label), the Code Center custom attribute name (attr.N.ccname), and the regex to use to validate input (attr.N.regex).
    1. Input field validation for username and password is performed using regular expressions. If you want to adjust those regular expressions to allow/disallow different characters (this can help make the application more secure), adjust the values of the following properties:
        ``field.input.validation.regex.username=\[A-Za-z0-9@_.-\]+
        field.input.validation.regex.psw=.+``
1. Launch from Code Center: In Code Center, go to an app, and click on the AppEdit URL View link

## Testing

To launch by hand (for testing):

1. Make sure in appedit.properties the app.version property is set to the version string for the Code Center app you will use for testing.
1. To avoid having to URL-escape any characters, choose for testing a Code Center application whose name consists only of alphanumeric characters.
1. Launch the following URL in your browser:
    ``http://<host>/AppEdit/editappdetails?appName=<Code Center application name>``
    
## Change History
* v1.0.0 Initial version
* v1.0.1 Mar 5, 2015: Read config file from user's (process owner's) home directory
* v1.1.0 April 16, 2015: Migrated to Code Center 7 SDK.
* v1.2.0 May 1, 2015: Support any arbitrary (configurable) set of custom attributes
* v1.3.0 Sept, 2015: Open Sourced
