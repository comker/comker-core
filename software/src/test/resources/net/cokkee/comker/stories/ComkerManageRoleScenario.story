Narrative:
In order to create new role in system
As a administrator
I would like to use POST method to insert a role object.

Scenario: Role Object Creation
Given a role object with code:'<code>', name:'<name>', description:'<description>'
When I insert role object to database
Then role object should be insert successful
Examples:
|code   |name       |description|
|ROLE_A |Role A     |200        |
|ROLE_B |Role B     |303        |
