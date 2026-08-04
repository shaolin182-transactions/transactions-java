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
                date: $(consumer(regex('^(?!([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.\\d+)?(Z|[+-][01]\\d:[0-5]\\d)).+$')), producer("invalid-date-format")),
                transactions: [
                    [
                        income : $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(0)),
                        outcome: $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(1276.87)),
                        category: [
                                id: $(consumer(anInteger()), producer(11)),
                                category: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Maison")),
                                label: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Assurances")),
                                type: "FIXE"
                        ],
                        bankAccount: [
                                id : $(consumer(anInteger()), producer(14)),
                                category: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Commun")),
                                label: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("PEE")),
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
                                category: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Maison")),
                                label: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Assurances")),
                                type: "FIXE"
                        ],
                        bankAccount: [
                                id : $(consumer(anInteger()), producer(14)),
                                category: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Commun")),
                                label: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("PEE")),
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
                transactions: null
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
                    date: $(consumer(iso8601WithOffset()), producer("2026-06-01T12:00:00Z")),
                    transactions: [
                            [
                                    income : $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(0)),
                                    outcome: $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(1276.87)),
                                    category: [
                                            id: $(consumer(anInteger()), producer(11)),
                                            category: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Maison")),
                                            label: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Assurances")),
                                            type: $(consumer(regex('^(?!FIXE$|COURANTE$|EXTRA$).+$')), producer("invalid-type"))
                                    ],
                                    bankAccount: [
                                            id : $(consumer(anInteger()), producer(14)),
                                            category: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Commun")),
                                            label: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("PEE")),
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



