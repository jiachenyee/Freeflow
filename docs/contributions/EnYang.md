# Contributions made by Koh En Yang (S10222282K)


## Leaving Workspace
- Allows all users to leave the workspace via the settings page
- Feature workflow:
1) Retrieve the current user's UID
2) Retrieve the selected workspace's ID from the workspace's settings
3) Send a command to Firestore which deletes the specific workspace from the user's array of workspaces
4) Send another command to Firestore to delete the current user from the workspace's list of users

## Admin Operations
- Enables admins to view and utilise admin-level actions such as modifying users and deleting the workspace
- Hides these actions from non-admins


## Elevating Users to Admins + Removing Users from workspace
- Allows admins to assign admin privileges to other users 
- Allows admins to also remove users from the workspace

- Feature workflow (User -> Admin):
1) Admin user inputs the user's email into the input textbox
2) Send a query to Firestore to retrieve the specific user document from email supplied by checking in the email field
3) Selected user's UID is then retrieved by Firebase
4) Retrieve the selected workspace's ID from the workspace's settings
5) Another command is then sent to Firestore to add that specific UID into the 'admin' array of that specific workspace

- Feature workflow (Removing Users):
1) Admin user inputs the user's email into the input textbox
2) Send a query to Firestore to retrieve the specific user document from email supplied by checking in the email field
3) Selected user's UID is then retrieved by Firebase
4) Retrieve the selected workspace's ID from the workspace's settings
5) Send a command to Firestore to delete the specified user from the workspace's list of users
6) Send anothercommand to delete the specific workspace from the user's array of workspaces
