package service

import (
	"context"
	"customer-api/internal/dao"
	"customer-api/internal/domain"
)

type CustomerService struct {
	db *dao.CustomerDatabase
}

func (service CustomerService) Delete()   {}
func (service CustomerService) Update()   {}
func (service CustomerService) FindById() {}

func (service CustomerService) Create(ctx context.Context, customer domain.Customer) (error, interface{}) {
	result, err := service.db.Collection.InsertOne(ctx, customer)
	if err != nil {
		return err, nil
	}

	return nil, result.InsertedID
}

func NewCustomerService(ctx context.Context) (error, *CustomerService) {
	err, db := dao.CustomerDao(ctx)
	if err != nil {
		return err, nil
	}

	return nil, &CustomerService{
		db: db,
	}
}
