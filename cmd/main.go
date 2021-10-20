package main

import (
	"context"
	"customer-api/internal/controller"
	"github.com/gin-gonic/gin"
)

func main() {
	customerController := controller.NewCustomerController(context.Background())

	router := gin.Default()
	router.GET("/customer/:id", customerController.FindById)
	router.POST("/customer", customerController.Create)
	err := router.Run("Customer API Running on localhost:8080")
	if err != nil {
		return
	}

}
