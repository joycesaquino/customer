package repository

import (
	"context"
	"customer-api/internal/dao"
	"customer-api/internal/types"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

type CustomerRepository struct {
	db *dao.CustomerDatabase
}

type CustomerRepositoryInterface interface {
	Create(ctx context.Context, customer *types.Customer) (*types.Customer, error)
	DeleteById(ctx context.Context, id string) error
	FindAll(ctx context.Context) []types.Customer
	FindById(ctx context.Context, id string) (*types.Customer, error)
	Update(ctx context.Context, cpf string, customer interface{}) *string
}

func (repository CustomerRepository) Create(ctx context.Context, customer *types.Customer) (*types.Customer, error) {
	customer.Id = primitive.NewObjectID()
	customer.CreatedAt = time.Now()

	_, err := repository.db.Collection.InsertOne(ctx, customer)
	if err != nil {
		return nil, err
	}

	return customer, nil
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

func (repository CustomerRepository) FindById(ctx context.Context, id string) (*types.Customer, error) {
	objId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return nil, err
	}
	var customer *types.Customer

	filter := bson.M{"_id": bson.M{"$eq": objId}}

	if err := repository.db.Collection.FindOne(ctx, filter).Decode(&customer); err != nil {
		return nil, err
	}

	return customer, nil
}

func (repository CustomerRepository) Update(ctx context.Context, email string, customer interface{}) *string {
	pByte, err := bson.Marshal(customer)
	if err != nil {
		return nil
	}

	var update bson.M
	err = bson.Unmarshal(pByte, &update)
	if err != nil {
		return nil
	}

	filter := bson.M{"email": bson.M{"$eq": email}}
	result := repository.db.Collection.FindOneAndUpdate(ctx, filter, bson.D{{Key: "$set", Value: update}})

	var updatedCustomer *types.Customer
	err = result.Decode(&updatedCustomer)
	if err != nil {
		return nil
	}

	id := updatedCustomer.Id.Hex()
	return &id
}

func NewCustomerRepository(ctx context.Context) (*CustomerRepository, error) {
	err, db := dao.CustomerDao(ctx)
	if err != nil {
		return nil, err
	}

	return &CustomerRepository{
		db: db,
	}, nil
}
