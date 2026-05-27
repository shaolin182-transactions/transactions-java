package contracts

org.springframework.cloud.contract.spec.Contract.make {
    name("POST transactions - Nominal Case")
    request {
        method 'POST'
        urlPath( '/transactions')
        headers {
            contentType('application/json')
        }
        // On attend un corps JSON contenant un champ 'date' au format ISO-8601 (OffsetDateTime)
        body(
            // consumer: attente côté test (regex), producer: valeur utilisée pour générer le stub
            date: $(iso8601WithOffset()),
            transactions: [
                [
                    income : $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(0)),
                    outcome: $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(1276.87)),
                    category: [
                        id: $(consumer(anInteger()), producer(11)),
                        category: $(consumer(alphaNumeric()), producer("Maison")),
                        label: $(consumer(alphaNumeric()), producer("Assurances")),
                        type: $(consumer(regex('FIXE|COURANTE|EXTRA')), producer("FIXE")),
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
        status CREATED()
        body (
                id: "someId",
                date: 1778508796.000000000,
                description: "Some description",
                transactions: [
                    [
                        income : 0,
                        outcome: 1276.87,
                        category: [
                            id: 11,
                            category: "Maison",
                            label: "Assurances",
                            type: "FIXE"
                        ],
                        bankAccount: [
                            id : 14,
                            category: "Commun",
                            label: "PEE"
                        ],
                        description: "description"
                    ]
                ]
        )
        headers {
            contentType('application/json')
        }
    }
}

