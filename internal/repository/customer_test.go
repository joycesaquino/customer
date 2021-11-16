package repository

import (
	"context"
	"customer-api/internal/domain"
	"github.com/go-playground/validator/v10"
	"github.com/stretchr/testify/mock"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"os"
	"testing"
	"time"
)

func init() {
	_ = os.Setenv("DATABASE_URI", "mongodb://localhost:27017")
	_ = os.Setenv("DATABASE_NAME", "customers")
	_ = os.Setenv("DATABASE_COLLECTION", "customers")
	_ = os.Setenv("DATABASE_TIMEOUT", "1s")
}

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

func TestCustomerController_Create(t *testing.T) {
	created := time.Now()

	type fields struct {
		CustomerRepository *CustomerRepositoryMock
		validator          *validator.Validate
	}
	type args struct {
		ctx context.Context
	}
	tests := []struct {
		name   string
		fields fields
		args   args
	}{
		{name: "Create new Customer", fields: fields{
			CustomerRepository: new(CustomerRepositoryMock),
			validator:          validator.New(),
		}, args: args{
			ctx: context.Background(),
		}},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			mocked := tt.fields.CustomerRepository
			id := primitive.NewObjectID()
			tt.fields.CustomerRepository.On("Create", tt.args.ctx, getCustomer(id, created)).Return(getCustomer(id, created), nil)
			_, err := mocked.Create(tt.args.ctx, getCustomer(id, created))
			if err != nil {
				return
			}
			mocked.AssertNumberOfCalls(t, "Create", 1)
		})
	}
}
func TestCustomerController_FindById(t *testing.T) {
	created := time.Now()

	type fields struct {
		CustomerRepository *CustomerRepositoryMock
		validator          *validator.Validate
	}
	type args struct {
		ctx context.Context
	}
	tests := []struct {
		name   string
		fields fields
		args   args
	}{
		{name: "Find customer by Id", fields: fields{
			CustomerRepository: new(CustomerRepositoryMock),
			validator:          validator.New(),
		}, args: args{
			ctx: context.Background(),
		}},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			mocked := tt.fields.CustomerRepository
			id := primitive.NewObjectID()
			tt.fields.CustomerRepository.On("FindById", id).Return(getCustomer(id, created), nil)
			_, err := mocked.FindById(id)
			if err != nil {
				return
			}
			mocked.AssertNumberOfCalls(t, "FindById", 1)
		})
	}
}
