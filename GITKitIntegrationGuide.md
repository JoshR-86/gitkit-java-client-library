# Overview #
1. GITkit is a set of tools that help 3rd party sites to support federated login using industry standard (OpenID and OAuth)<br />
2. The toolkit intends to minimize the work required for a site to become a relying party for major Identity Providers (IDP)<br />
3. Major components
  * Two Google APIs (Apiary APIs) that implement authUrl discovery, and IDP response verification
  * A Javascript widget for account chooser
  * Client side libraries that help with the new login logic and handles the callback

See GITKit documentation at: http://code.google.com/apis/identitytoolkit/

# Summary of RP work required #

  1. Register a Google account and login to API Console to activate GIT and configure Javascript widget
> 2. UI Changes
    * Modify the login page in your site to embed Javascript widget
    * Modify account creation page to remove password field for federated account
> 3. Add a field to account table to indicate if the account is federated
> 4. Download GIT client library for supported platforms, and implement a few changes
    * Add API key to the callback servlet/page
    * Implement code to handle account creation and new login logic
    * Deploy new callback page and login page

Optional RP Work

  1. UI changes
    * Modify account management page that allows a user to change their email address
    * Modify account management page to remove the option for a user to modify password if it is a federated account
    * Add account creation wizard which imports attributes from IDPs for federated accounts
> 2. Additional IDP support
    * Hotmail support: need to register client key and secretes on MSN web site and input into devconsole
    * Upload Yadis file to root directory to get rid of AOL RP discovery failure warning


# Step-by-step guide for integrating a sample Java app with GITkit #
## Step 1: Setup Apache Maven (http://maven.apache.org/) ##
  1. Download Apache Maven 3.0.3 (http://maven.apache.org/download.html)
> 2. Follow the instructions to setup Apache Maven (http://maven.apache.org/download.html)

## Step 2: Download the sample application and GITKit Java client library. ##
  1. Download this project by svn, or download gitkit-java-client-library-1.0.zip then unzip it to a directory;
> 2. Go to the "samples\tutorial" dir, run "`mvn jetty:run`" or ("`mvn jetty:run [-Djetty.port port_num]`"). This starts the sample application.
> 3. Go to http://localhost:8080 and you should see the home page of the sample app. You can sign up a new account and sign in with username/password;
> 4. Play with the sample RP
    * Sign up an account.
    * Sign out.
    * Sign in.
    * Delete the account.

This is the starting status for this sample app. Next let's integrate this sample app with GITKit.

## Step 3: API Console config (https://code.google.com/apis/console/b/0/) ##
  1. Login with a google account
> 2. You need to "Add Project" for the first time
> 3. Go to "services" and activate "Identity Toolkit API"
> 4. Refresh API console, and then click on "Identity Toolkit" in the lower left panel
> 5. Click on "Configure Login Widget", input params
> 6. Select IDPs you'd support
> 7. Copy the JS widget code, also take a note of the API key

> Please visit here for more detailed instruction: https://code.google.com/apis/identitytoolkit/v1/devconsole.html
> For the sample application, you can use below settings:
    * Callback Url: "`http://localhost:8080/gitkit`"
    * Login Url: "`/gitkit`"
    * userStatusUrl: "`/gitkit`"
    * Signup Url: "`/signup.jsp`"
    * Logout Url: "`/logout`"
    * Home Url: "`/home.jsp`"

## Step 4: Home page UI changes. ##
  1. Open home.jsp in /src/main/webapp dir
> 2. Copy & paste the JS widget code from API console to home.jsp. (you can put them before the "`</head>`" tag in home.jsp)
> 3. Remove navbar and old login box
    * Replace after "`<% if (user != null) { %>`" to "`<% } %>`"  (about line 72 - 90 after you paste widget code) with:
```html

<div id="navbar" style="padding: 0px 10px 5px 0px; float: right;">

Unknown end tag for &lt;/div&gt;


<div style="clear: both; width: 100%; border-top: 1px solid #E6E6E6;">

Unknown end tag for &lt;/div&gt;


```
    * Replace after "`<% if (user == null) {%>`" to "`<<% } else {%>`" (line 145-157) with:
> > > `<% if (user != null) {%>`

> 4. Save the file and refresh your home page of the browser, you should see the new account chooser login widget
> 5. You can now try a federated login in the 'add account' page, a popup should show and redirect user to IDP.

## Step 5: Database changes: add federated attribute that indicates if an account is federated ##
> This sample app use JavaDB and we write a simple database management page for it. Go and play it at http://localhost:8080/dbms.jsp.
> Also, we provide a simple ORM tool so that if you add/remove fields in User.java, the database will be changed accordingly after you restart the app.
> (These tools are only for the demo and tutoral purpose.)
  1. Open User.java under src/main/java/com/google/apps/easyconnect/demos/easyrpbasic/web/data
> 2. Add "federated" attribute -- You can just remove the comment for
> > @ORM<br />
> > private String federated = "false";

> 3. Add data access method for this field -- You can remove comment for
> > public boolean isFederated() {
> > > return "true".equalsIgnoreCase(this.federated);

> > }<br />
> > public void setFederated(boolean federated) {
> > > this.federated = Boolean.toString(federated);

> > }

> 4. Restart the jetty, go to http://localhost:8080/dbms.jsp, and you should see the new field.

## Step 6: Integrating with GITkit client library ##
### Step 6.1: Add dependency to GITkit client library ###
  1. Open tutorial/pom.xml, add dependency to gitkit library (just remove comment):
```xml

<dependency>
<groupId>com.google.gitkit

Unknown end tag for &lt;/groupId&gt;


<artifactId>client-library

Unknown end tag for &lt;/artifactId&gt;


<version>1.0

Unknown end tag for &lt;/version&gt;


<scope>compile

Unknown end tag for &lt;/scope&gt;




Unknown end tag for &lt;/dependency&gt;


```


### Step 6.2: integrate with GIT client library -- implement Account interface ###
  1. Add Account interface to User class (User.java):
> > public class User implements Account

> 2. Add import statement and method (just remove comment)<br />
> 3. Implement AccountService (just rename AccountServiceImpl.txt in src\main\java\com\google\apps\easyconnect\demos\easyrpbasic\web\data to AccountServiceImpl.java)<br />
> 4. Add ContextLoader servlet listener (just rename ContextLoader.txt in src\main\java\com\google\apps\easyconnect\demos\easyrpbasic\web\servlet to ContextLoader .java, make sure you are using your own API key

===Step 6.3: Change servlets config on src\main\webapp\WEB-INF\web.xml.=== (You can delete web.xml, then rename web.ac.xml to web.xml)
  1. Delete Login servlet and servlet-mapping
> 2. Add GitKit servlet
```xml

<servlet>
<servlet-name>GitKit

Unknown end tag for &lt;/servlet-name&gt;


<servlet-class>com.google.apps.easyconnect.easyrp.client.basic.servlet.GitServlet


Unknown end tag for &lt;/servlet-class&gt;




Unknown end tag for &lt;/servlet&gt;


```
> 3. Add servlet mapping for the new GitkitServlet
```xml

<servlet-mapping>
<servlet-name>GitKit

Unknown end tag for &lt;/servlet-name&gt;


<url-pattern>/gitkit

Unknown end tag for &lt;/url-pattern&gt;




Unknown end tag for &lt;/servlet-mapping&gt;


```
> 4. Add ContextLoader listener
```xml
<listener>
<listener-class>
com.google.apps.easyconnect.demos.easyrpbasic.web.servlet.ContextLoader


Unknown end tag for &lt;/listener-class&gt;




Unknown end tag for &lt;/listener&gt;


```
> > Restart jetty by "mvn jetty:run", federated login should now work.
### Step 6.4: Show logged-in user in Account Chooser ###

> If you want to show the logged-in user in AC widget, copy & paste following code after "`$("#navbar").accountChooser();`" (line 65)
<pre><% if (user != null) { %><br>
var userData = {<br>
email: '<%= user.getEmail() %>',<br>
displayName: '<%= user.getDisplayName() %>',<br>
photoUrl: '<%= user.getPhotoUrl() %>',<br>
};<br>
window.google.identitytoolkit.updateSavedAccount(userData);<br>
window.google.identitytoolkit.showSavedAccount('<%= user.getEmail() %>');<br>
<% } %></pre>

We are now done with changes for RP integration. You can sign-in/sign-out with sample application now.


# Add support for hotmail and AOL #
# Hotmail suport

  1. Register your domain, callback url in MSN site

> login at: manage.dev.live.com

> Make sure the callback URL's domain matches the one you input into devconsole's callback URL

> 2. Input the "Client ID" and "Secrete key" value in API Console

> 3. Add hotmail to JS widget code

#AOL support: just need to do some settings to get rid of the warning for "site verification could not be completed"
  1. Add a yadis file to your root directory (a sample yadis file can be downloaded here)
> 2. Add an additional field in http header in the response (login.jsp file):
> > `response.setHeader("X-XRDS-Location", "/yadis.xml");`