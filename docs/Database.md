# Freeflow Firebase Setup

- Real-time Database (RTDB) 
  - Data is stored in Firebase Real-time Database, main advantage is that all users can listen to changes in the real-time database and update instantly.
- Cloud Firestore (Firestore)
  - Data is stored in Firebase Cloud Firestore, main advantage is that data is stored in the form of documents.

```mermaid
erDiagram 
    User-Firestore {
        string name
        string profilePictureURI
        string emailAddress
    }
    User-Firestore }o--|{ Workspace-Firestore: joins
    User-Firestore ||--}o Workspace-Firestore: administers
    
    Workspace-Firestore {
        string name
        int accentColor
        string inviteCode
    }
    
    Workspace-Firestore ||--|{ Category-RTDB: manages
    
    Category-RTDB {
        string name
    }
   
   
    Task-RTDB ||--}o User-Firestore: assignedTo
    Task-RTDB ||--}o Task-RTDB: contains
    
    Category-RTDB ||--}o Task-RTDB: contains
    Task-RTDB {
        string name
        DateTime deadline
        string links
    }
```
