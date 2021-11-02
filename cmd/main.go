package main

import (
	"context"
	"customer-api/internal/controller"
	"fmt"
	"github.com/gin-gonic/gin"
)

func main() {
	customerController := controller.NewCustomerController(context.Background())
	loginController := controller.Oauth2()

	router := gin.Default()

	router.GET("/", handleMain)
	router.GET("/logged", handleLogged)
	router.GET("/login", loginController.Login)
	router.GET("/callback", loginController.GoogleCallback)
	router.GET("/customer", customerController.FindAll)
	router.GET("/customer/:id", customerController.FindById)
	router.PUT("/customer/:cpf", customerController.Update)
	router.POST("/customer", customerController.Create)
	router.DELETE("/customer/:id", customerController.DeleteById)
	err := router.Run("localhost:8080")
	if err != nil {
		return
	}

}

func handleMain(c *gin.Context) {
	var htmlIndex = `<html>
<body>
	<a href="/login">Google Log In</a>
</body>
</html>`
	fmt.Fprintf(c.Writer, htmlIndex)
}

func handleLogged(c *gin.Context) {
	var htmlIndex = `<html>
		<body>
			<a href="/login">Google Log In</a>
		</body>
		</html>
`
	fmt.Fprintf(c.Writer, htmlIndex)
}
