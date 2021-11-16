package repository

import (
	"context"
	"customer-api/internal/domain"
	"github.com/stretchr/testify/mock"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

type CustomerRepositoryMock struct {
	mock.Mock
}

func getCustomer(id primitive.ObjectID, created time.Time) *domain.Customer {
	cpf := "99999999999"
	return &domain.Customer{
		Id:        id,
		Name:      "Joyce Aquino",
		Email:     "joycesaquino@gmail.com",
		CPF:       &cpf,
		Image:     "image/profile.jpg",
		CreatedAt: created,
	}
}

func (r *CustomerRepositoryMock) Create(ctx context.Context, customer *domain.Customer) (*domain.Customer, error) {
	r.Called(ctx, customer)
	return getCustomer(primitive.NewObjectID(), time.Now()), nil
}

func (r *CustomerRepositoryMock) FindById(id primitive.ObjectID) (*domain.Customer, error) {
	r.Called(id)
	return getCustomer(id, time.Now()), nil
}
