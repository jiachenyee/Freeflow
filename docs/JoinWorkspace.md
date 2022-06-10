# Join Workspace
Requirements:
- If user inputs a URL as the inviteCode, strip the URL to get the _actual_ code
- Check that the inviteCode corresponds to a workspace in Cloud Firestore
- If successful, append the `workspaceID` to the users/`userID`'s workspaces property and append the `userID` to the workspaces/`workspaceID`'s users property in Cloud Firestore.

```mermaid
flowchart
  start([START]) --> A[/INPUT inviteCode/]
  A --> B{Is inviteCode a URL?}
  B -- Yes --> C[Set inviteCode to the last path component of the input inviteCode]
  C --> D["Get workspaces from Cloud Firestore workspaces/"]
  B -- No --> D
  
  D --> E[Set workspaceIndex to 0]
  E --> F[Set workspace to the item in workspaces at workspaceIndex]
  F --> G{Is workspace's inviteCode equal to inviteCode?}
  G -- Yes --> H[Set workspaceID to the workspace's ID]
  I -- Yes --> J[Increment workspaceIndex by 1]
  G -- No --> I{Is workspaceIndex < number of workspaces - 1?}
  J --> F
  H --> K{Is workspaceID null?}
  I -- No --> K
  
  K -- Yes --> L[Present toast saying 'Workspace not found']
  K -- No --> M[INPUT userID]
  M --> N["Create workspaceReference, a Firestore reference to workspaces/workspaceID"]
  N --> O[Append userID to users array in Firestore]
  O --> P{Is the database write successful?}
  P -- Yes --> Q["Create userReference, a Firestore reference to users/userID"]
  P -- No --> U
  
  Q --> R[Append workspaceID to user's workspaces array in Firestore]
  R --> S{Is the database write successful?}
  S -- Yes --> T[Reload workspaces list]
  S -- No --> U[Present alert to user saying workspace creation failed with the option to retry]
  
  U --> V{Did the user select retry?}
  V -- Yes --> C
  
  L --> T
  V -- No --> T
  
  T --> stop([STOP])
```
