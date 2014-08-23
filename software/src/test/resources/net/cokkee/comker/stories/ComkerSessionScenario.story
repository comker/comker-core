Narrative:
In order to view the information of my session
As a user
I would like to request the session information, includes login username,
permissions, checkpoint..

Scenario: Anonymous Session Information
When I request the session information
Then a session information object should be return

Scenario: Logged-in Session Information
Given <username> and <password>
When I request the session information
Then a session information object should be return
