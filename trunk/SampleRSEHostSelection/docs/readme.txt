This is sample skeleton code on how to create an Eclipse Plugin.
This plugin contributes to the popup menu of an IBM i RSE connection.
When an IBM i system is selected with a right click, the option is available.
When executed it will display a message with the selected system.

This sample was put together using the examples found in the RSE Developer Guide on Eclipse.org.
In particular there is a section that describes Pluggin in a Popup Menu.
Another good section is Relevant Eclipse Extension Points.
I highly recommend reading this guide as it explains it all.

An overview:
Within Eclipse you'll first create a new Plugin Project.
The 'Bundle-Activator' in the manifest determines what class is run.
In this case I extend the SystemBasePlugin (so I can get the workbench shell) and implement IObjectActionDelegate.
My constructor sets up an array that'll hold the selected hosts.
I override the selectionChanged() method of IObjectActionDelegate to populate my array with the selections.
I then use run() to process the list and send a message.

 


