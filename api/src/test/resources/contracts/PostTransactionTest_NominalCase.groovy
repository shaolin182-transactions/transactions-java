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
            date: $(consumer(iso8601WithOffset()), producer("2026-06-01T12:00:00Z")),
            transactions: [
                [
                    income : $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(0)),
                    outcome: $(consumer(regex('-?\\d+(\\.\\d+)?')), producer(1276.87)),
                    category: [
                        id: $(consumer(anInteger()), producer(11)),
                        category: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Maison")),
                        label: $(consumer(regex('[A-Za-zÀ-ÿ \'\\-\\_\\,]+')), producer("Assurances")),
                        type: $(consumer(regex('FIXE|COURANTE|EXTRA')), producer("FIXE")),
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
        status CREATED()
        body (
                id: $(consumer(anyUuid()), producer("someId")),
                date: 1778508796.000000000,
                description: "Some description",
                cost: $(anyInteger()),
                costAbs: $(anyInteger()),
                transactions: [
                    [
                        cost: $(anyInteger()),
                        costAbs: $(anyInteger()),
                        income : $(consumer(fromRequest().body('$.transactions[0].income')), producer(0)),
                        outcome: $(consumer(fromRequest().body('$.transactions[0].outcome')), producer(1276.87)),
                        category: [
                            id: $(consumer(fromRequest().body('$.transactions[0].category.id')), producer(11)),
                            category: $(consumer(fromRequest().body('$.transactions[0].category.category')), producer("Maison")),
                            label: $(consumer(fromRequest().body('$.transactions[0].category.label')), producer("Assurances")),
                            type: $(consumer(fromRequest().body('$.transactions[0].category.type')), producer("FIXE")),
                        ],
                        bankAccount: [
                            id : $(consumer(fromRequest().body('$.transactions[0].bankAccount.id')), producer(14)),
                            category: $(consumer(fromRequest().body('$.transactions[0].bankAccount.category')), producer("Commun")),
                            label: $(consumer(fromRequest().body('$.transactions[0].bankAccount.label')), producer("PEE")),
                        ],
                        description: $(consumer(fromRequest().body('$.transactions[0].description')), producer("description")),
                    ]
                ]
        )
        headers {
            contentType('application/json')
        }
    }
}

