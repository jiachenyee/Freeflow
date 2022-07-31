#Contributions made by Koh En Yang (S10222282K)

## Leaving Workspace
- Allows all users to leave the workspace via the settings page
- Feature workflow:
1) Retrieve the current user's UID
2) Retrieve the selected workspace's ID from the workspace's settings
3) Send a command to Firestore which deletes the specific workspace from the user's array of workspaces
4) Send another command to Firestore to delete the current user from the workspace's list of users
