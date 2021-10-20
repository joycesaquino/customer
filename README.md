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
- Setar as variáveis de ambiente ;)
- go run main.go
