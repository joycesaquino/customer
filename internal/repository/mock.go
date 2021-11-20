package repository

import (
	"context"
	"customer-api/internal/types"
	"github.com/stretchr/testify/mock"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

type CustomerRepositoryMock struct {
	mock.Mock
}

func getCustomer(id primitive.ObjectID, created time.Time) *types.Customer {
	cpf := "99999999999"
	return &types.Customer{
		Id:        id,
		Name:      "Joyce Aquino",
		Email:     "joycesaquino@gmail.com",
		CPF:       &cpf,
		Image:     "image/profile.jpg",
		CreatedAt: created,
	}
}

func (r *CustomerRepositoryMock) Create(ctx context.Context, customer *types.Customer) (*types.Customer, error) {
	r.Called(ctx, customer)
	return getCustomer(primitive.NewObjectID(), time.Now()), nil
}

func (r *CustomerRepositoryMock) FindById(id primitive.ObjectID) (*types.Customer, error) {
	r.Called(id)
	return getCustomer(id, time.Now()), nil
}
