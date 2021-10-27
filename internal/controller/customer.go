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

func (cc CustomerController) FindAll(c *gin.Context) {
	all := cc.customerRepository.FindAll(c.Request.Context())
	c.IndentedJSON(http.StatusOK, all)
}

func (cc CustomerController) DeleteById(c *gin.Context) {
	id := c.Param("id")

	err := cc.customerRepository.DeleteById(c.Request.Context(), id)
	if err != nil {
		return
	}
	c.IndentedJSON(http.StatusOK, err)
}

func (cc CustomerController) FindById(c *gin.Context) {
	id := c.Param("id")

	err, response := cc.customerRepository.FindById(c.Request.Context(), id)
	if err != nil {
		return
	}
	c.IndentedJSON(http.StatusOK, response)
}

func (cc CustomerController) Create(c *gin.Context) {
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
