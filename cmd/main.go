package main

import (
	"context"
	"customer-api/internal/controller"
	"fmt"
	"github.com/gin-gonic/gin"
)

func main() {

	router := gin.Default()

	customerController := controller.NewCustomerController(context.Background())
	loginController := controller.Oauth2(customerController.CustomerRepository)

	router.GET("/", handleMain)
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
