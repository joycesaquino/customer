package main

import (
	"context"
	"customer-api/internal/controller"
	"github.com/gin-gonic/gin"
)

func main() {
	customerController := controller.NewCustomerController(context.Background())

	router := gin.Default()
	router.GET("/customer", controller.GetCustomers)
	router.GET("/customer/:id", controller.GetCustomerById)
	router.POST("/customer", customerController.CreateCustomer)
	router.Run("localhost:8080")

}
