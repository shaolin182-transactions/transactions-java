package contracts

[
    org.springframework.cloud.contract.spec.Contract.make {
        name("POST transactions - Bad Request - Invalid Date Format")
        request {
            method 'POST'
            urlPath( '/transactions')
            headers {
                contentType('application/json')
            }
            body(
                date: $(consumer(notMatching(iso8601WithOffset())), producer("invalid-date-format")),
                transactions: [
                    [
                        income : $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(0)),
                        outcome: $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(1276.87)),
                        category: [
                                id: $(consumer(anInteger()), producer(11)),
                                category: $(consumer(alphaNumeric()), producer("Maison")),
                                label: $(consumer(alphaNumeric()), producer("Assurances")),
                                type: "FIXE"
                        ],
                        bankAccount: [
                                id : $(consumer(anInteger()), producer(14)),
                                category: $(consumer(alphaNumeric()), producer("Commun")),
                                label: $(consumer(alphaNumeric()), producer("PEE")),
                        ],
                    ]
                ]
            )
        }
        response {
            status BAD_REQUEST()
        }
    },
    org.springframework.cloud.contract.spec.Contract.make {
        name("POST transactions - Bad Request - Missing Required Field date")
        request {
            method 'POST'
            urlPath( '/transactions')
            headers {
                contentType('application/json')
            }
            body(
                transactions: [
                    [
                        income : $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(0)),
                        outcome: $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(1276.87)),
                        category: [
                                id: $(consumer(anInteger()), producer(11)),
                                category: $(consumer(alphaNumeric()), producer("Maison")),
                                label: $(consumer(alphaNumeric()), producer("Assurances")),
                                type: "FIXE"
                        ],
                        bankAccount: [
                                id : $(consumer(anInteger()), producer(14)),
                                category: $(consumer(alphaNumeric()), producer("Commun")),
                                label: $(consumer(alphaNumeric()), producer("PEE")),
                        ],
                    ]
                ]
            )
        }
        response {
            status BAD_REQUEST()
        }
    },
    org.springframework.cloud.contract.spec.Contract.make {
        name("POST transactions - Bad Request - Missing Required Field transactions")
        request {
            method 'POST'
            urlPath( '/transactions')
            headers {
                contentType('application/json')
            }
            body(
                date: $(iso8601WithOffset()),
            )
        }
        response {
            status BAD_REQUEST()
        }
    },
    org.springframework.cloud.contract.spec.Contract.make {
        name("POST transactions - Bad Request - Invalid category Type")
        request {
            method 'POST'
            urlPath( '/transactions')
            headers {
                contentType('application/json')
            }
            body(
                    date: $(iso8601WithOffset()),
                    transactions: [
                            [
                                    income : $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(0)),
                                    outcome: $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(1276.87)),
                                    category: [
                                            id: $(consumer(anInteger()), producer(11)),
                                            category: $(consumer(alphaNumeric()), producer("Maison")),
                                            label: $(consumer(alphaNumeric()), producer("Assurances")),
                                            type: $(consumer(notMatching(regex('FIXE|COURANTE|EXTRA'))), producer("invalid-type"))
                                    ],
                                    bankAccount: [
                                            id : $(consumer(anInteger()), producer(14)),
                                            category: $(consumer(alphaNumeric()), producer("Commun")),
                                            label: $(consumer(alphaNumeric()), producer("PEE")),
                                    ],
                            ]
                    ]
            )
        }
        response {
            status BAD_REQUEST()
        }
    }
]



