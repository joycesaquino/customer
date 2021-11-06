package domain

import (
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

type Customer struct {
	Id        primitive.ObjectID `bson:"_id" json:"id"`
	Name      string             `bson:"name" json:"name" validate:"required"`
	Email     string             `bson:"email" json:"email" validate:"required"`
	CPF       *string            `bson:"cpf" json:"cpf"`
	Image     string             `bson:"image" json:"image"`
	CreatedAt time.Time          `bson:"created_at" json:"created_at"`
}
