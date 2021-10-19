package repository

import (
	"context"
	"customer-api/internal/dao"
	"customer-api/internal/domain"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

type CustomerRepository struct {
	db *dao.CustomerDatabase
}

func (repository CustomerRepository) Update() {}
func (repository CustomerRepository) Delete() {}

func (repository CustomerRepository) FindById(ctx context.Context, id string) (error, *domain.Customer) {
	objId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return err, nil
	}
	var customer *domain.Customer

	filter := bson.M{"_id": bson.M{"$eq": objId}}

	if err := repository.db.Collection.FindOne(ctx, filter).Decode(&customer); err != nil {
		return err, nil
	}
	return nil, customer
}

func (repository CustomerRepository) Create(ctx context.Context, customer domain.Customer) (error, interface{}) {
	result, err := repository.db.Collection.InsertOne(ctx, customer)
	if err != nil {
		return err, nil
	}

	return nil, result.InsertedID
}

func NewCustomerService(ctx context.Context) (error, *CustomerRepository) {
	err, db := dao.CustomerDao(ctx)
	if err != nil {
		return err, nil
	}

	return nil, &CustomerRepository{
		db: db,
	}
}
