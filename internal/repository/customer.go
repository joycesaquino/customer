package repository

import (
	"context"
	"customer-api/internal/dao"
	"customer-api/internal/domain"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"log"
	"time"
)

type CustomerRepository struct {
	db *dao.CustomerDatabase
}

func (repository CustomerRepository) Update() {}

func (repository CustomerRepository) FindAll(ctx context.Context) []domain.Customer {
	find, err := repository.db.Collection.Find(ctx, bson.D{})
	if err != nil {
		return nil
	}

	var customers []domain.Customer

	defer find.Close(ctx)
	for find.Next(ctx) {
		var customer domain.Customer
		if err := find.Decode(&customer); err != nil {
			return nil
		}
		customers = append(customers, customer)
	}

	if err := find.Err(); err != nil {
		log.Fatal(err)
	}

	return customers
}

func (repository CustomerRepository) DeleteById(ctx context.Context, id string) error {
	objId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return err
	}
	filter := bson.M{"_id": bson.M{"$eq": objId}}

	if _, err := repository.db.Collection.DeleteOne(ctx, filter); err != nil {
		return err
	}
	return nil
}

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

func (repository CustomerRepository) Create(ctx context.Context, customer *domain.Customer) (error, *domain.Customer) {
	customer.Id = primitive.NewObjectID()
	customer.CreatedAt = time.Now()

	_, err := repository.db.Collection.InsertOne(ctx, customer)
	if err != nil {
		return err, nil
	}

	return nil, customer
}

func NewCustomerRepository(ctx context.Context) (error, *CustomerRepository) {
	err, db := dao.CustomerDao(ctx)
	if err != nil {
		return err, nil
	}

	return nil, &CustomerRepository{
		db: db,
	}
}
