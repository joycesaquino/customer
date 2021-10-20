package controller

import (
	"context"
	"customer-api/internal/domain"
	"customer-api/internal/repository"
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
)

type CustomerController struct {
	customerRepository *repository.CustomerRepository
}

func NewCustomerController(ctx context.Context) *CustomerController {
	err, repo := repository.NewCustomerRepository(ctx)
	if err != nil {
		log.Fatalf("Fatal error: %s", err)
	}
	return &CustomerController{customerRepository: repo}
}

func GetCustomers(c *gin.Context) {
	var customers []domain.Customer
	c.IndentedJSON(http.StatusOK, customers)
}

func GetCustomerById(c *gin.Context) {
	var customer domain.Customer
	c.IndentedJSON(http.StatusOK, customer)
}

func (cc CustomerController) CreateCustomer(c *gin.Context) {
	var customer *domain.Customer
	if err := c.ShouldBindJSON(&customer); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	err, response := cc.customerRepository.Create(c.Request.Context(), customer)
	if err != nil {
		return
	}

	c.JSON(http.StatusCreated, response)
}
