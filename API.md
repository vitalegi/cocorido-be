# API 2.0

## Services

### User

| API                  | Description       | Response |
| -------------------- | ----------------- | -------- |
| POST /users/register | firebase? Cookie? | User     |
| POST /users/login    | firebase? Cookie? | User     |
| GET  /users/{id}     | Get user info     | User     |

### Board

| API                 | Description            | Response |
| ------------------- | ---------------------- | -------- |
| GET /boards         | list all boards        | Board    |
| POST /boards        | create new board       | Board    |
| DELETE /boards/{id} | delete board           | Board    |
| PATCH /boards/{id}  | update config of board | Board    |

### BoardTurn

| API                                        | Description                         | Response  |
| ------------------------------------------ | ----------------------------------- | --------- |
| POST /boards/{id}/init                     | Init new turn (called by MASTER)    | BoardTurn |
| GET /boards/{id}/turn/{id}/blackcard       | Gets blackcard                      | void      |
| POST /boards/{id}/turn/{id}/whitecard/play | Play whitecard                      | void      |
| GET /boards/{id}/turn/{id}                 | Get current board turn              | BoardTurn |
| GET /boards/{id}/turn/{id}/results         | Get all played whitecards           | [{"playerId": "UUID", "whitecards": ["UUID"]}] |
| POST /boards/{id}/turn/{id}/winner         | Select winner                       | void      |
| GET /boards/{id}/turn/{id}/winner          | Get winner                          | void      |

### BoardRole

| API                         | Description              | Response |
| --------------------------- | ------------------------ | -------- |
| GET /boards/{id}/owner      | Get board's owner        | text |
| GET /boards/{id}/users/{id} | Get user's roles         | BoardRole |
| POST /boards/{id}/join      | Join board as a PLAYER, with MAX number of WHITECARDS | BoardRole |

### WhiteCard

| API                     | Description     | Response  |
| ----------------------- | --------------- | --------- |
| GET /whitecards         | list all cards  | WhiteCard |
| POST /whitecards        | create new card | WhiteCard |
| DELETE /whitecards/{id} | delete card     | WhiteCard |

### BlackCard

| API                     | Description     | Response  |
| ----------------------- | --------------- | --------- |
| GET /blackcards         | list all cards  | BlackCard |
| POST /blackcards        | create new card | BlackCard |
| DELETE /blackcards/{id} | delete card     | BlackCard |

## Models

### User

```json
{
    "userId": "<UUID>",
    "username": "text"
}
```

### Board

```json
{
    "boardId": "<UUID>",
    "name": "text",
    "lastUpdate": "date",
    "privacy": "PUBLIC / RESTRICTED / PRIVATE"
}
```

### BoardTurn

```json
{
    "boardId": "<UUID>",
    "boardTurnId": "<UUID>",
    "status": "text"
}
```

| Status          | Description                   |
| --------------- | ----------------------------- |
| START_TURN      | All players draw cards        |
| READ_BLACK_CARD | Master reads black card       |
| PLAY_WHITE_CARD | All players play whitecard(s) |
| SELECT_WINNER   | Master selects winner         |
| END_TURN        | Scoreboard is shown           |

### BoardRole

```json
{
    "userId": "<UUID>",
    "boardId": "<UUID>",
    "roles": ["text"]
}
```

### WhiteCard

```json
{
    "whitecardId": "<UUID>",
    "value": "text",
    "deck": "text" // name of the deck, in order to group the cards
}
```

### BlackCard

```json
{
    "blackcardId": "<UUID>",
    "value": "text",
    "deck": "text", // name of the deck, in order to group the cards
    "requiredCards": 1
}
```