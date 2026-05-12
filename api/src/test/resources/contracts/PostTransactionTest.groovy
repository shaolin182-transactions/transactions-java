package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        urlPath( '/transactions')
        headers {
            contentType('application/json')
        }
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

