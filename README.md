# Customer
Customer API service<br>

### Porque uma API de customers ?

Quase todo projeto precisa de um servico seja de cliente/user/customer ... Então estou deixando um "pronto" pra quando houver necessidade só adaptar pro escopo do projeto ;)

### Dependências

	github.com/caarlos0/env 
	github.com/gin-gonic/gin 
	go.mongodb.org/mongo-driver

### Variáveis de Ambiente

	Uri                string        `env:"DATABASE_URI,required"`
	DatabaseName       string        `env:"DATABASE_NAME,required"`
	DatabaseCollection string        `env:"DATABASE_COLLECTION,required"`
	ConnectionTimeout  time.Duration `env:"DATABASE_TIMEOUT,required"`


### To Run !
- docker-compose up
- Configurar as variáveis de ambiente ;)
- go run main.go

### To Test !

#### Create Customer

    curl --location --request POST 'localhost:8080/customer' \
        --data-raw '{
            "name": "Seu Nome",
            "email": "seuemail@gmail.com",
            "cpf": "99999999999"
        }'
    
#### GET Customer By Id

    curl --location --request GET 'localhost:8080/customer/mongo_object_id'

#### DELETE Customer By Id

    curl --location --request DELETE 'localhost:8080/customer/mongo_object_id'

#### UPDATE Customer By CPF

    curl --location --request PUT 'localhost:8080/customer/customer-cpf' \
        --data-raw '{
            "email": "customer-email@gmail.com"
        }
    '

