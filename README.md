# com-money-transfer
Simple money transfer RESTful api application using Micronaut framework.

Application provides several endpoints to create accounts, top up balance, transfer money between existing accounts and retrieval of transactions associated to an account (including failures).

The storage of the accounts is in memory.

# Application

Created using micronaut framework 1.1.3. Uses lombok 1.18.4.

Configured to run on port 8080, using the url:

```
http://localhost:8080/money-transfer
```
## Endpoints

The available endpoints are the following (I have tried to use swagger but was unable to configure it properly. Will try again later):

* Create account:
    * ``POST http://localhost:8080/money-transfer/accounts``.
    * Request: No input.
    * Response: *accountId*.
        * Example: ``d0a4ada1-bba5-40b7-99eb-8c7b390a38d5``

* Get account balance:
    * ``GET http://localhost:8080/money-transfer/accounts/{accountId}``.
    * Request: No input.
    * Response: *Amount object*.
        * Example: ``Amount(currency=GBP, units=3)``
        * Example of error: ``{"message": "Internal Server Error: 'INVALID_ACCOUNT_ID' is an invalid account. Please provide a valid account."}``

* Top up account balance: (_could have used an object request instead of input parameters_):
    * ``POST http://localhost:8080/money-transfer/transfers/accounts/{accountId}/amount/{amount}``.
    * Request: No input.
    * Response: *Transaction object*.
       * Example:

```
    {
    "sucess": true,
    "id": "d05a239f-1833-4404-b639-26eb27e13083",
    "created": [
        2019,
        6,
        23,
        23,
        19,
        46,
        365000000
    ],
    "amount": {
        "currency": "GBP",
        "units": 5
    },
    "originAccountId": "d0a4ada1-bba5-40b7-99eb-8c7b390a38d5",
    "recipientAccountId": "d0a4ada1-bba5-40b7-99eb-8c7b390a38d5"}
```
        * Example of error: ``{"message": "Internal Server Error: 'INVALID_ACCOUNT_ID' is an invalid account. Please provide a valid account."}``

* Transfer between accounts:
    * ``POST http://localhost:8080/money-transfer/transfers``.
    * Request: *Transfer request*.
        * Example:

```
    {
	"originAccountId": "d0a4ada1-bba5-40b7-99eb-8c7b390a38d5",
	"recipientAccountId": "f9261b65-d957-4a78-a4f9-08e6e91f3158",
	"amount": "2"
	}
```


    * Response: *Transaction object*.
        * Example:

```
    {
    "sucess": true,
    "id": "f60cb050-8e81-49e4-b9cc-a8e9426aa558",
    "created": [
        2019,
        6,
        23,
        23,
        21,
        0,
        372000000
    ],
    "amount": {
        "currency": "GBP",
        "units": 2
    },
    "originAccountId": "d0a4ada1-bba5-40b7-99eb-8c7b390a38d5",
    "recipientAccountId": "f9261b65-d957-4a78-a4f9-08e6e91f3158"}
```
        * Example of error: ``{"message": "Internal Server Error: 'INVALID_ACCOUNT_ID' is an invalid account. Please provide a valid account."}``
        * Example of error: ``{"message": "Internal Server Error: Account 54a8263a-b71d-422b-b1a4-9d6b006bb845 has insufficient funds to complete the transfer."}``


* Get transactions by account:
    * `GET http://localhost:8080/money-transfer/transfers/accounts/{accountId}`.
    * Request: No input.
    * Response: *List of Transactions *.
        * Example:

```
[
    {
        "sucess": false,
        "errors": [
            {
                "message": "'b3a4e12b-4e0f-4306-b5f2-974429c765fc' is an invalid account. Please provide a valid account."
            }
        ],
        "id": "965a80fe-a211-418f-b81a-0b1bc9fec1b3",
        "created": [
            2019,
            6,
            23,
            23,
            18,
            54,
            365000000
        ],
        "amount": {
            "currency": "GBP",
            "units": 2
        },
        "originAccountId": "d0a4ada1-bba5-40b7-99eb-8c7b390a38d5",
        "recipientAccountId": "b3a4e12b-4e0f-4306-b5f2-974429c765fc"
    },
    {
        "sucess": false,
        "errors": [
            {
                "message": "Account d0a4ada1-bba5-40b7-99eb-8c7b390a38d5 has insufficient funds to complete the transfer."
            }
        ],
        "id": "b45a2a06-c372-4ce2-8477-8cb30d6d28e7",
        "created": [
            2019,
            6,
            23,
            23,
            19,
            11,
            619000000
        ],
        "amount": {
            "currency": "GBP",
            "units": 2
        },
        "originAccountId": "d0a4ada1-bba5-40b7-99eb-8c7b390a38d5",
        "recipientAccountId": "f9261b65-d957-4a78-a4f9-08e6e91f3158"
    },
    {
        "sucess": true,
        "id": "d05a239f-1833-4404-b639-26eb27e13083",
        "created": [
            2019,
            6,
            23,
            23,
            19,
            46,
            365000000
        ],
        "amount": {
            "currency": "GBP",
            "units": 5
        },
        "originAccountId": "d0a4ada1-bba5-40b7-99eb-8c7b390a38d5",
        "recipientAccountId": "d0a4ada1-bba5-40b7-99eb-8c7b390a38d5"
    },
    {
        "sucess": true,
        "id": "f60cb050-8e81-49e4-b9cc-a8e9426aa558",
        "created": [
            2019,
            6,
            23,
            23,
            21,
            0,
            372000000
        ],
        "amount": {
            "currency": "GBP",
            "units": 2
        },
        "originAccountId": "d0a4ada1-bba5-40b7-99eb-8c7b390a38d5",
        "recipientAccountId": "f9261b65-d957-4a78-a4f9-08e6e91f3158"
    }
]
```

        * Example of error: ``{"message": "Internal Server Error: 'INVALID_ACCOUNT_ID' is an invalid account. Please provide a valid account."}``

# Run Tests

Tests written using Junit 5 and Micronaut tests. To run the tests, run:

```
./mvnw install
```

# Run Application

* Using mvn

```
./mvnw exec:exec
```

* Or using the standard jar:

```
./mvnw package #generates the jar file
java -jar target/com-money-transfer-server-0.1.jar
```

To check if the application is running, go to:

```
http://localhost:8080/health
```
