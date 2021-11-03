package controller

import (
	"context"
	"customer-api/internal/domain"
	"customer-api/internal/repository"
	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
	"log"
	"net/http"
)

type CustomerController struct {
	CustomerRepository *repository.CustomerRepository
	validator          *validator.Validate
}

func NewCustomerController(ctx context.Context) *CustomerController {
	repo, err := repository.NewCustomerRepository(ctx)
	if err != nil {
		log.Fatalf("Fatal error: %s", err)
	}
	return &CustomerController{CustomerRepository: repo, validator: validator.New()}
}

func (cc CustomerController) Update(c *gin.Context) {
	cpf := c.Param("cpf")
	var customer interface{}
	if err := c.ShouldBindJSON(&customer); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	objectID := cc.CustomerRepository.Update(c.Request.Context(), cpf, customer)
	c.IndentedJSON(http.StatusOK, objectID)
}

func (cc CustomerController) DeleteById(c *gin.Context) {
	id := c.Param("id")
	err := cc.CustomerRepository.DeleteById(c.Request.Context(), id)
	if err != nil {
		return
	}
	c.IndentedJSON(http.StatusOK, err)
}

func (cc CustomerController) FindAll(c *gin.Context) {
	all := cc.CustomerRepository.FindAll(c.Request.Context())
	c.IndentedJSON(http.StatusOK, all)
}

func (cc CustomerController) FindById(c *gin.Context) {
	id := c.Param("id")

	response, err := cc.CustomerRepository.FindById(c.Request.Context(), id)
	if err != nil {
		c.IndentedJSON(http.StatusInternalServerError, err)
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

	err := cc.validator.Struct(customer)
	if err != nil {
		c.JSON(http.StatusBadRequest, err.(validator.ValidationErrors).Error())
		return
	}

	response, err := cc.CustomerRepository.Create(c.Request.Context(), customer)
	if err != nil {
		c.JSON(http.StatusBadRequest, err)
		return
	}

	c.JSON(http.StatusCreated, response)
}
