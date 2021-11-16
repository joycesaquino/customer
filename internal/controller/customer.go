package controller

import (
	"customer-api/internal/domain"
	"customer-api/internal/repository"
	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
	"net/http"
)

type CustomerController struct {
	CustomerRepository *repository.CustomerRepository
	validator          *validator.Validate
}

func NewCustomerController(customerRepository *repository.CustomerRepository) *CustomerController {
	return &CustomerController{CustomerRepository: customerRepository, validator: validator.New()}
}

func (cc CustomerController) Update(c *gin.Context) {
	email := c.Param("email")
	var customer interface{}
	if err := c.ShouldBindJSON(&customer); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	objectID := cc.CustomerRepository.Update(c.Request.Context(), email, customer)
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

	if err := cc.validator.Struct(customer); err != nil {
		c.JSON(http.StatusBadRequest, err.(validator.ValidationErrors).Error())
		return
	}

	response, err := cc.CustomerRepository.Create(c.Request.Context(), customer)
	if err != nil {
		c.JSON(http.StatusInternalServerError, err)
		return
	}

	c.JSON(http.StatusCreated, response)
}
