package repository

import (
	"context"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"testing"
	"time"
)

func TestCustomerController_Create(t *testing.T) {
	created := time.Now()

	type fields struct {
		CustomerRepository *CustomerRepositoryMock
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
